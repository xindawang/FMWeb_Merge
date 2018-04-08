package com.tqh.demo.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tqh.demo.mapper.UserLocationMapper;
import com.tqh.demo.model.UserLocation;
import com.tqh.demo.service.UserLocationService;
import com.tqh.demo.util.JsonTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    @ResponseBody
    @RequestMapping(value = "/getDeviceInfo",method = RequestMethod.GET)
    public String getDevicesInfo(int pageSize, int page) {//两个参数是传入的data{}里写好的，不能改
        //这个操作将生成一个Page加入map里
        PageHelper.startPage(page, pageSize);
        List<UserLocation> users= userLocationService.selectAll();
        long total = ((Page<UserLocation>) users).getTotal();
        Map<String, Object> map = new HashMap<>();
        map.put("list", users);
        map.put("total", total);
        return JsonTool.objectToJson(map);
    }
}
