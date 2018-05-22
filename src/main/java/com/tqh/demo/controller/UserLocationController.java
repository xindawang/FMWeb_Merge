package com.tqh.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tqh.demo.mapper.UserLocationMapper;
import com.tqh.demo.model.ApEntity;
import com.tqh.demo.model.RpEntity;
import com.tqh.demo.model.UserLocation;
import com.tqh.demo.service.BayesService;
import com.tqh.demo.service.KnnService;
import com.tqh.demo.service.UserLocationService;
import com.tqh.demo.util.JsonTool;
import com.tqh.demo.util.RssiTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class UserLocationController {
    @Autowired
    UserLocationService userLocationService;

    @Autowired
    private SimpMessagingTemplate template;

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

    @ResponseBody
    @RequestMapping(value = "/loc", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String getMbLoc(@RequestBody JSONObject jsonParam) {
        RpEntity rpEntity = new RpEntity();
        HashMap<String,Double> apentities = new HashMap<>();
        String algorithm = jsonParam.getString("algorithm");
        String device = jsonParam.getString("device");
         for (String key : jsonParam.keySet()){
            if (RssiTool.getNameChangeMap().containsKey(key)) apentities.put(RssiTool.getNewName(key),Double.parseDouble(jsonParam.getString(key)));
        }
        rpEntity.setPoints(apentities);
        userLocationService.getMBUserLocation(rpEntity,algorithm);
        if (device!=null) userLocationService.saveMBUserLocation(rpEntity,device);
        return rpEntity.getLocString();
    }

    @MessageMapping("/app_wifiMessage")
    public void getMbLocWS(@RequestBody JSONObject jsonParam) {
        RpEntity rpEntity = new RpEntity();
        HashMap<String,Double> apentities = new HashMap<>();
        String algorithm = jsonParam.getString("algorithm");
        String device = jsonParam.getString("device");
        String username = jsonParam.getString("username");
        for (String key : jsonParam.keySet()){
            if (RssiTool.getNameChangeMap().containsKey(key)) apentities.put(RssiTool.getNewName(key),Double.parseDouble(jsonParam.getString(key)));
        }
        rpEntity.setPoints(apentities);
        rpEntity.setUsername(username);
        userLocationService.getMBUserLocation(rpEntity,algorithm);
        if (device!=null) userLocationService.saveMBUserLocation(rpEntity,device);
        if (username!=null) template.convertAndSend("/iotMap/loc/"+username, rpEntity.getLocString());
        template.convertAndSend("/iotMap/loc", rpEntity.getLocString());
    }
}
