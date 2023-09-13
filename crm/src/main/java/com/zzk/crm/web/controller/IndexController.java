package com.zzk.crm.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    /**
     * 理论上来说应该写全，为了简便和统一写法，一律省略前缀
     */
    //@RequestMapping("http://127.0.0.1:8080/crm/")
    @RequestMapping("/")
    public String index(){
        //return "forward:/WEB-INF/pages/index.jsp";
        //视图解析器已经拼接好了
        return "index";
    }

}
