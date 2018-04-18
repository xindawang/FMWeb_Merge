package com.tqh.demo.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tqh.demo.mapper.UserLocationMapper;
import com.tqh.demo.model.UserLocation;
import com.tqh.demo.service.UserLocationService;
import com.tqh.demo.util.JsonTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class UserLocationController {
    @Autowired
    UserLocationService userLocationService;

    @RequestMapping("/selectAllUserLocation")
    @ResponseBody
    public List<UserLocation> selectAllUsers(){
        List<UserLocation> users= userLocationService.selectAll();
        return users;
    }

    @RequestMapping( "/selectUserLocation/{id}")
    @ResponseBody
    public UserLocation selectUser(@PathVariable String id){
        UserLocation user= userLocationService.selectUser(id);
        return user;
    }

}
