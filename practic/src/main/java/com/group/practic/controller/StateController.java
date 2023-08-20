package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.getResponse;
import com.group.practic.entity.StateEntity;
import com.group.practic.service.StateService;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/states")
public class StateController {

    @Autowired
    StateService stateService;


    @GetMapping
    public ResponseEntity<Collection<StateEntity>> get() {
        return getResponse(stateService.get());
    }

}
