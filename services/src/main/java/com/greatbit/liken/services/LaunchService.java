package com.greatbit.liken.services;

import com.greatbit.liken.beans.Launch;
import com.greatbit.liken.beans.LaunchStatus;
import com.greatbit.liken.beans.Testcase;
import com.greatbit.liken.dal.LaunchRepository;
import com.greatbit.liken.error.EntityNotFoundException;
import com.greatbit.liken.external.LikenExternalServicePlugin;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ILock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.greatbit.plow.PluginsContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.greatbit.liken.beans.LaunchStatus.RUNNABLE;
import static com.greatbit.liken.beans.LaunchStatus.RUNNING;

@Service
public class LaunchService {

    @Value("${launch.lock.timeout.ms}")
    private long launchLockTimeout;

    @Autowired
    private LaunchRepository repository;

    @Autowired
    private PluginsContainer pluginsContainer;

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
        pluginsContainer.getPlugins(LikenExternalServicePlugin.class).values().
                forEach(plugin -> plugin.updateExternalTestcaseStatus(request, launch, testcaseUUID, status));
    }

    private void deleteExternal(HttpServletRequest request, String launchId) {
        pluginsContainer.getPlugins(LikenExternalServicePlugin.class).values().
                forEach(plugin -> plugin.deleteExternal(request, launchId));
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
