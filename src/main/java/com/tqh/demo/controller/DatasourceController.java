package com.tqh.demo.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tqh.demo.model.Datasource;
import com.tqh.demo.model.UserLocation;
import com.tqh.demo.service.DatasourceService;
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
public class DatasourceController {

    @Autowired
    DatasourceService datasourceService;
    @ResponseBody
    @RequestMapping(value = "/getDatasourceInfo",method = RequestMethod.GET)
    public String getDataInfo(int pageSize, int page) {
        PageHelper.startPage(page, pageSize);
        List<Datasource> datasources= datasourceService.selectAll();
        long total = ((Page<Datasource>) datasources).getTotal();
        Map<String, Object> map = new HashMap<>();
        map.put("list", datasources);
        map.put("total", total);
        return JsonTool.objectToJson(map);
    }
}
