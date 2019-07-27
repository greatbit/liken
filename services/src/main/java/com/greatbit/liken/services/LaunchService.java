package com.greatbit.liken.services;

import com.greatbit.liken.beans.Launch;
import com.greatbit.liken.beans.LaunchStatus;
import com.greatbit.liken.dal.LaunchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class LaunchService {

    @Autowired
    LaunchRepository repository;

    public Launch create(Launch launch){
        repository.save(launch);
        return launch;
    }

    public Launch update(Launch launch){
        //toDo: lock and check timestamps
        launch.setLastModifiedTime(System.currentTimeMillis());
        repository.save(launch);
        return launch;
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
        //ToDo: lock and check timestamp
        Launch launch = repository.findById(launchId).orElseThrow(EntityNotFoundException::new);
        launch.getTestcases().stream().filter(testcase -> testcase.getUuid().equals(testcaseUUID)).
                forEach(testcase -> {
                    updateExternalTestcaseStatus(launch, testcaseUUID, status);
                    testcase.setStatus(status);
                });
        return update(launch);
    }

    private void updateExternalTestcaseStatus(Launch launch, String testcaseUUID, LaunchStatus status) {
        //ToDO: safely call plugins
    }

    private void deleteExternal(String launchId) {
        //ToDO: safely remove launch
    }
}
