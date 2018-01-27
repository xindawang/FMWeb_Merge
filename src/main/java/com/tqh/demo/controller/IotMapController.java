package com.tqh.demo.controller;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tqh.demo.model.Users;
import com.tqh.demo.service.UsersService;
import com.tqh.demo.util.JsonTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/")
public class IotMapController {
    @Autowired
    UsersService usersService;
    @RequestMapping("/iotMap")
    public String openIotMap(){

        return "hello_FengMap";
    }
    @RequestMapping("/table")
    public String openTable(){

        return "table_plug";
    }
    @RequestMapping("/ajax")
    public String test(){
        return "ajax";
    }

    @RequestMapping("/selectAllUsers")
    @ResponseBody
    public List<Users> selectAllUsers(){
        List<Users> users=usersService.selectAll();
        return users;
    }

    @ResponseBody
    @RequestMapping(value = "/getData",method = RequestMethod.GET)
    public String getDevicesInfo(int pageSize, int page) {//两个参数是传入的data{}里写好的，不能改
        System.out.println("get ajax request");
        //这个操作将生成一个Page加入map里
        PageHelper.startPage(page, pageSize);
        List<Users> users= usersService.selectAll();
        long total = ((Page<Users>) users).getTotal();
        Map<String, Object> map = new HashMap<>();
        map.put("list", users);
        map.put("total", total);
        return JsonTool.objectToJson(map);
    }

}
