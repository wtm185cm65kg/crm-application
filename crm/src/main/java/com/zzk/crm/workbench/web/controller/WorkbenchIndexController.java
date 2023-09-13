package com.zzk.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WorkbenchIndexController {

    @RequestMapping("/workbench/index.do")
    public String index(){
        //最后拼接为 /WEB-INF/pages/workbench/index.jsp
        return "workbench/index";
    }

}
