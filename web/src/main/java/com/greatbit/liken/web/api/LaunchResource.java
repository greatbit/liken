package com.greatbit.liken.web.api;

import com.greatbit.liken.beans.Launch;
import com.greatbit.liken.beans.LaunchStatus;
import com.greatbit.liken.beans.Testcase;
import com.greatbit.liken.services.LaunchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/launch")
public class LaunchResource {

    @Autowired
    private LaunchService launchService;

    @PostMapping(produces = "application/json", consumes = "application/json")
    public Launch create(@RequestBody Launch launch){
        return launchService.create(launch);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    public Launch update(@RequestBody Launch launch){
        return launchService.update(launch);
    }

    @GetMapping(produces = "application/json")
    public Page<Launch> getLaunches(@RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                    @RequestParam(name = "size", required = false, defaultValue = "20") int size,
                                    @RequestParam(name = "direction", required = false, defaultValue = "DESC") String direction,
                                    @RequestParam(name = "sortByField", required = false, defaultValue = "id") String sortByField){
        return launchService.getLaunches(page, size, Sort.Direction.fromString(direction), sortByField);
    }

    @GetMapping(value = "/{launchId}",  produces = "application/json")
    public Launch getLaunch(@PathVariable("launchId") String launchId){
        return launchService.getLaunch(launchId);
    }

    @PostMapping(value = "/{launchId}/{testcaseUUID}",  produces = "application/json")
    public Testcase updateTestcaseStatus(@PathVariable("launchId") String launchId,
                                       @PathVariable("testcaseUUID") String testcaseUUIDe,
                                       @RequestParam("status") LaunchStatus status,
                                       HttpServletRequest request){
        return launchService.updateTestcaseStatus(request, launchId, testcaseUUIDe, status);
    }

    @DeleteMapping(value = "/{launchId}/{testcaseUUID}")
    public void deleteLaunch(@PathVariable("launchId") String launchId,
                             HttpServletRequest request){
        launchService.delete(request, launchId);
    }

    @GetMapping(value = "/{launchId}/{testcaseUUID}",  produces = "application/json")
    public Testcase getTestcase(@PathVariable("launchId") String launchId,
                                @PathVariable("testcaseUUID") String testcaseUUIDe){
        return launchService.getLaunchTestcase(launchId, testcaseUUIDe);
    }

    @GetMapping(value = "/{launchId}/next",  produces = "application/json")
    public Testcase startNextTestcase(@PathVariable("launchId") String launchId){
        return launchService.startNextRunnableTestcase(launchId);
    }

}
