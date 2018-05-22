package com.tqh.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.tqh.demo.model.User;
import com.tqh.demo.service.UserService;
import com.tqh.demo.util.FileTool;
import org.apache.poi.hslf.blip.Bitmap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
            return "redirect:/openMap";
        }
        if(userService.checkPassWord(user.getUserName(),passWord)){
            session.setAttribute("userNow",user);
            model.addAttribute("user",user);
            return "mapPage";
        }else {
            return "redirect:/openMap";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/mbUserLogin", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String getMbUser(@RequestBody JSONObject jsonParam,HttpSession session, Model model){
        String username=jsonParam.getString("userName");
        String password=jsonParam.getString("passWord");

        User user=userService.selectUser(username);
        JSONObject result = new JSONObject();
        if(user==null){
            return "username cannot find";
        }
        if(userService.checkPassWord(user.getUserName(),password)){
            session.setAttribute(username,user);
            model.addAttribute("user",user);
            result.put("data", user);
            System.out.println(JSONObject.toJSON(user).toString());
            return JSONObject.toJSON(user).toString();
        }else {
            return "password wrong";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/uploadPersonalInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String uploadPersonalInfo(@RequestBody JSONObject jsonParam,HttpSession session, Model model){
        String username=jsonParam.getString("username");
        String nickname=jsonParam.getString("nickname");
        String portrait=jsonParam.getString("portrait");
        String macAddress = jsonParam.getString("macAddress");
//        Bitmap bitmap = FileTool.convertStringToIcon(jsonParam.getString("portrait"));
        String portraitPath = "static/img/"+username+".png";
        return "success";
    }

    @RequestMapping("/logout")
    public String logOut(HttpSession session){
        session.removeAttribute("userNow");
        return "redirect:/openMap";
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
