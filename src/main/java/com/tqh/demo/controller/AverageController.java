package com.tqh.demo.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tqh.demo.model.Average;
import com.tqh.demo.model.Users;
import com.tqh.demo.service.AverageService;
import com.tqh.demo.util.JsonTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
 public class AverageController {
     @Autowired
     private AverageService averageService;

//     @RequestMapping("/show/{samplePoint}")
//     public String selectAverage (@PathVariable String samplePoint){
//                 return averageService.selectAverage(samplePoint).toString();
//
//             }

    @RequestMapping(value = "/test",method = RequestMethod.POST)
    @ResponseBody
    public Average test(HttpServletRequest request)  {
         System.out.println("get ajax request!");
         String samplePoint=request.getParameter("samplePoint");
         System.out.println(request.getParameter("name"));
         System.out.println(request.getParameter("age"));
       return averageService.selectAverage(samplePoint);
    }

    @ResponseBody
    @RequestMapping(value = "/getAverage",method = RequestMethod.GET)
    public String getAverageInfo(int pageSize, int page) {//两个参数是传入的data{}里写好的，不能改
        System.out.println("get ajax request");
        //这个操作将生成一个Page加入map里
        PageHelper.startPage(page, pageSize);
        List<Average> list= averageService.selectAllAverage();
        long total = ((Page<Average>) list).getTotal();
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("total", total);
        return JsonTool.objectToJson(map);
    }
 }
