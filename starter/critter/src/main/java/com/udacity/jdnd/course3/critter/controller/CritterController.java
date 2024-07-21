package com.udacity.jdnd.course3.critter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class CritterController {

    @GetMapping()
    public String checkCritterAppStart(){
        return "Critter Starter installed successfully";
    }

}
