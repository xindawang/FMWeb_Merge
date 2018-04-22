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
import org.springframework.beans.factory.annotation.Autowired;
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
    private KnnService knnService;

    @Autowired
    private BayesService naiveBayesService;

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
        for (String key : jsonParam.keySet()){
            if (key.contains("ap")) apentities.put(key,Double.parseDouble(jsonParam.getString(key)));
        }
        rpEntity.setPoints(apentities);
        if (algorithm!=null) {
            if (algorithm.equals("knn")) {
                knnService.getLocByKnn(rpEntity,null);
            }else if (algorithm=="bayes") {
                naiveBayesService.getLocByBayes(rpEntity,null);
            }
        }else{
            return null;
        }
        return rpEntity.getLocString();
    }
}
