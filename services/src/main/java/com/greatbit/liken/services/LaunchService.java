package com.greatbit.liken.services;

import com.greatbit.liken.beans.Launch;
import com.greatbit.liken.beans.LaunchStatus;
import com.greatbit.liken.dal.LaunchRepository;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ILock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Service
public class LaunchService {

    @Value("${launch.lock.timeout.ms}")
    private long launchLockTimeout;

    @Autowired
    LaunchRepository repository;

    @Autowired
    private HazelcastInstance hazelcastInstance;

    public Launch create(Launch launch){
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

    public void delete(String launchId){
        Launch launch = repository.findById(launchId).orElseThrow(EntityNotFoundException::new);
        launch.setDeleted(true);
        deleteExternal(launchId);
        update(launch);
    }

    public Launch getLaunch(String id){
        return repository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Page<Launch> getLaunches(int page, int size, Sort.Direction directrion, String...  sortByField){
        return repository.findAll(PageRequest.of(page, size, directrion, sortByField));
    }

    public Launch updateTestcaseStatus(String launchId, String testcaseUUID, LaunchStatus status){
        ILock lock = hazelcastInstance.getLock(launchId);
        try{
            lock.lock(launchLockTimeout, TimeUnit.MILLISECONDS);
            Launch launch = repository.findById(launchId).orElseThrow(EntityNotFoundException::new);
            launch.getTestcases().stream().filter(testcase -> testcase.getUuid().equals(testcaseUUID)).
                    forEach(testcase -> {
                        updateExternalTestcaseStatus(launch, testcaseUUID, status);
                        testcase.setStatus(status);
                    });
            launch.setLastModifiedTime(System.currentTimeMillis());
            return repository.save(launch);
        } finally {
            lock.unlock();
        }

    }

    private void updateExternalTestcaseStatus(Launch launch, String testcaseUUID, LaunchStatus status) {
        //ToDO: safely call plugins
    }

    private void deleteExternal(String launchId) {
        //ToDO: safely remove launch
    }
}
