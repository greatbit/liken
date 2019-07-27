package com.greatbit.liken.web.api;

import com.greatbit.liken.beans.Launch;
import com.greatbit.liken.beans.LaunchStatus;
import com.greatbit.liken.services.LaunchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@RestController
@RequestMapping("/launch")
public class LaunchResource extends BaseResource{

    @Autowired
    private LaunchService launchService;

    @PostMapping(value = "/", produces = "application/json", consumes = "application/json")
    public Launch create(@RequestBody Launch launch){
        return launchService.create(launch);
    }

    @PutMapping(value = "/", produces = "application/json", consumes = "application/json")
    public Launch update(@RequestBody Launch launch){
        return launchService.update(launch);
    }

    @GetMapping(value = "/", produces = "application/json")
    public Page<Launch> getLaunches(@RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                    @RequestParam(name = "size", required = false, defaultValue = "20") int size,
                                    @RequestParam(name = "direction", required = false, defaultValue = "DESC") String direction,
                                    @RequestParam(name = "sortByField", required = false, defaultValue = "id") String sortByField){
        return launchService.getLaunches(page, size, Sort.Direction.fromString(direction), sortByField);
    }

    @PostMapping(value = "/{launchId}/{testcaseUUID}",  produces = "application/json")
    public Launch updateTestcaseStatus(@PathParam("launchId") String launchId, @PathParam("testcaseUUID") String testcaseUUIDe,
                                             @RequestParam("status") LaunchStatus status){
        return launchService.updateTestcaseStatus(launchId, testcaseUUIDe, status);
    }

    @DeleteMapping(value = "/{launchId}/{testcaseUUID}")
    public void deleteLaunch(@PathParam("launchId") String launchId){
        launchService.delete(launchId);
    }

}
