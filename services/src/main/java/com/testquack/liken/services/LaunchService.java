package com.testquack.liken.services;

import com.testquack.liken.beans.Launch;
import com.testquack.liken.beans.LaunchStatus;
import com.testquack.liken.beans.Testcase;
import com.testquack.liken.dal.LaunchRepository;
import com.testquack.liken.error.EntityNotFoundException;
import com.testquack.liken.external.LikenExternalServicePlugin;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ILock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.testquack.liken.beans.LaunchStatus.RUNNABLE;
import static com.testquack.liken.beans.LaunchStatus.RUNNING;

@Service
public class LaunchService {

    @Value("${launch.lock.timeout.ms}")
    private long launchLockTimeout;

    @Autowired
    private LikenExternalServicePlugin externalPlugin;

    @Autowired
    private LaunchRepository repository;

    @Autowired
    private HazelcastInstance hazelcastInstance;

    public Launch create(Launch launch){
        launch.getTestcases().forEach(testcase -> {
            testcase.setStatus(testcase.getStatus() == null ? RUNNABLE : testcase.getStatus());
            testcase.setUuid(testcase.getUuid() == null ? UUID.randomUUID().toString() : testcase.getUuid());
        });
        repository.save(launch);
        return launch;
    }

    public Launch update(Launch launch){
        ILock lock = hazelcastInstance.getLock(launch.getId());
        try {
            lock.lock(launchLockTimeout, TimeUnit.MILLISECONDS);
            launch.setLastModifiedTime(System.currentTimeMillis());
            repository.save(launch);
            return launch;
        } finally {
            lock.unlock();
        }
    }

    public void delete(HttpServletRequest request, String launchId){
        Launch launch = repository.findById(launchId).orElseThrow(EntityNotFoundException::new);
        launch.setDeleted(true);
        deleteExternal(request, launchId);
        update(launch);
    }

    public Launch getLaunch(String id){
        return repository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Page<Launch> getLaunches(int page, int size, Sort.Direction directrion, String...  sortByField){
        return repository.findAll(PageRequest.of(page, size, directrion, sortByField));
    }

    public Testcase updateTestcaseStatus(HttpServletRequest request, String launchId, String testcaseUUID, LaunchStatus status){
        ILock lock = hazelcastInstance.getLock(launchId);
        try{
            lock.lock(launchLockTimeout, TimeUnit.MILLISECONDS);
            Launch launch = repository.findById(launchId).orElseThrow(EntityNotFoundException::new);
            Testcase testcase = getLaunchTestcase(launch, testcaseUUID);
            //Do not make finished testcase runnable on testcase switch
            if (isTescaseFinished(testcase) && status == RUNNABLE){
                return testcase;
            }
            testcase.setStatus(status);
            launch.setLastModifiedTime(System.currentTimeMillis());
            updateExternalTestcaseStatus(request, launch, testcase.getExternalUuid(), status);
            repository.save(launch);
            return testcase;
        } finally {
            lock.unlock();
        }

    }

    private boolean isTescaseFinished(Testcase testcase) {
        return testcase.getStatus() != RUNNABLE && testcase.getStatus() != RUNNING;
    }

    private void updateExternalTestcaseStatus(HttpServletRequest request, Launch launch, String testcaseUUID, LaunchStatus status) {
        externalPlugin.updateExternalTestcaseStatus(request, launch, testcaseUUID, status);
    }

    private void deleteExternal(HttpServletRequest request, String launchId) {
        externalPlugin.deleteExternal(request, launchId);
    }

    public Testcase getLaunchTestcase(String launchId, String testcaseUUID){
        Launch launch = repository.findById(launchId).orElseThrow(EntityNotFoundException::new);
        return getLaunchTestcase(launch, testcaseUUID);
    }

    public Testcase getLaunchTestcase(Launch launch, String testcaseUUID){
        return launch.getTestcases().stream().
                filter(testcase -> testcase.getUuid().equals(testcaseUUID)).
                findFirst().orElseThrow(EntityNotFoundException::new);
    }

    public Testcase startNextRunnableTestcase(String launchId){
        ILock lock = hazelcastInstance.getLock(launchId);
        try {
            lock.lock(launchLockTimeout, TimeUnit.MILLISECONDS);
            Launch launch = repository.findById(launchId).orElseThrow(EntityNotFoundException::new);
            Testcase testcase = launch.getTestcases().stream().filter(this::isRunnable).findFirst().orElse(null);
            if (testcase != null){
                testcase.setStatus(RUNNING);
                update(launch);
            }
            return testcase;
        } finally {
            lock.unlock();
        }

    }

    private boolean isRunnable(Testcase testcase) {
        return testcase.getStatus() == RUNNABLE;
    }
}
