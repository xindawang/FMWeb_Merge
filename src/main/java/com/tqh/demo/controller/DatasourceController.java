package com.tqh.demo.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tqh.demo.exception.DataException;
import com.tqh.demo.model.Datasource;
import com.tqh.demo.model.UserLocation;
import com.tqh.demo.service.DatasourceService;
import com.tqh.demo.service.FingerPrintService;
import com.tqh.demo.util.JsonTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class DatasourceController {

    @Autowired
    DatasourceService datasourceService;
    @Autowired
    FingerPrintService fingerPrintService;
    @ResponseBody
    @RequestMapping(value = "/getDatasourceInfo",method = RequestMethod.GET)
    public List<Datasource> getDataInfo() {

        List<Datasource> datasources= datasourceService.selectAll();

        return datasources;
    }

    @RequestMapping("/generate/{id}")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public boolean createTable(@PathVariable("id") int id){
            Datasource datasource=datasourceService.selectDatasource(id);
            String upload_time=id+datasource.getUpload_time();
            String data_path=datasource.getData_path();
            datasourceService.createTable(upload_time);
            datasourceService.InsertDataFromTxt(upload_time,data_path);
            fingerPrintService.insertFinger(upload_time);
            return  true;
    }

    @ResponseBody
    @RequestMapping("/tableExist")
    @Transactional(rollbackFor = Exception.class)
    public boolean tableExist(@RequestParam(defaultValue = "null") int id){
        Datasource datasource=datasourceService.selectDatasource(id);
        String upload_time=id+datasource.getUpload_time();
        try{
            datasourceService.createTable(upload_time);
        }catch(Exception e){
            e.printStackTrace();
             return true;
        }
        datasourceService.removeTable(upload_time);
        return false;
    }



}
