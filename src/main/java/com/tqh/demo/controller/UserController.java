package com.tqh.demo.controller;

import com.tqh.demo.model.User;
import com.tqh.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class UserController {

    @Autowired
    UserService userService;
    @RequestMapping("/iotMap")
    public String openIotMap(HttpServletRequest request, HttpSession session,Model model){
        String userName=request.getParameter("userName");
        String passWord=request.getParameter("password");
        User user=userService.selectUser(userName);
        if(user==null){
            return "redirect:/login";
        }
        if(userService.checkPassWord(user.getUserName(),passWord)){
            session.setAttribute(userName,user);
            model.addAttribute("user",user);
            return "openLayers";
        }else {
            return "redirect:/login";
        }
    }

    @RequestMapping("/logout/{userName}")
    public String logOut(HttpSession session,@PathVariable("userName") String userName){
        session.removeAttribute(userName);
        return "redirect:/login";
    }

    @ResponseBody
    @RequestMapping("/checkUserName")
    private Map userNameValidate(@RequestParam(defaultValue = "0") String userName) {
        Map map = new HashMap();
        //1. 校验身份证是否合法
        map.put("valid", true);
        User user=userService.selectUser(userName);
        if (user==null){
            map.put("valid", false);
        }
        return map;
    }

    @ResponseBody
    @RequestMapping("/checkPassWord")
    public Map passWordValidate(@RequestParam(defaultValue = "null") String userName,@RequestParam(defaultValue = "null") String password){
        Map map = new HashMap();
        map.put("valid", false);

        if(userService.checkPassWord(userName,password)){
            map.put("valid", true);
        }
        return map;
    }


}
