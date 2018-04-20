package com.tqh.demo.controller;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tqh.demo.model.User;
import com.tqh.demo.model.UserLocation;
import com.tqh.demo.service.UserLocationService;
import com.tqh.demo.util.JsonTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/")
public class IotMapController {
    @Autowired
    UserLocationService userLocationService;





    @RequestMapping("/openMap")
    public String openLoginPage(HttpSession session,Model model){
        User user= (User) session.getAttribute("userNow");
        model.addAttribute("user",user);
        if(user==null){
            return "login";
        }else {
            return "mapPage";
        }
    }

    @RequestMapping(value = "/deviceTable",method = RequestMethod.GET)
    public String openDeviceTable(HttpSession session,Model model){
        User user=(User) session.getAttribute("userNow");
        model.addAttribute("user",user);
        if(user==null){
            return "userNotFound";
        }
        if("manager".equals(user.getRole())){
            return "deviceTable";
        }else {
            return "pageNotFound";
        }
    }

    @RequestMapping(value = "/datasourceTable",method = RequestMethod.GET)
    public String openDatasourceTable(HttpSession session, Model model){
        User user=(User) session.getAttribute("userNow");
        model.addAttribute("user",user);
        if(user==null){
            return "userNotFound";
        }
        if("manager".equals(user.getRole())){
            return "datasourceTable";
        }else {
            return "pageNotFound";
        }
    }

    @RequestMapping(value = "/fingerTable",method = RequestMethod.GET)
    public String openFingerTable(HttpSession session,Model model){
        User user=(User) session.getAttribute("userNow");
        model.addAttribute("user",user);
        if(user==null){
            return "userNotFound";
        }
        if("manager".equals(user.getRole())){
            return "fingerTable";
        }else {
            return "pageNotFound";
        }
    }



}
