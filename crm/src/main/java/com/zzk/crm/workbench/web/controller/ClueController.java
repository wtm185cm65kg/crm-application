package com.zzk.crm.workbench.web.controller;

import com.zzk.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ClueController {
    @Autowired
    private UserService userService;


    @RequestMapping("")
    public Object s(){

    }
}
