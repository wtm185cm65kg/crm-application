package com.zzk.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ActivityController {

    @RequestMapping("/workbench/activity/index.do")
    public String index(){
        return "workbench/activity/index";
    }
}
