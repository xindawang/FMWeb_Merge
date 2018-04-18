package com.tqh.demo.controller;

import com.tqh.demo.exception.DataException;
import com.tqh.demo.model.Datasource;
import com.tqh.demo.model.FingerPrint;
import com.tqh.demo.model.User;
import com.tqh.demo.service.DatasourceService;
import com.tqh.demo.service.FingerPrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class FingerPrintController {

    @Autowired
    FingerPrintService fingerPrintService;
    @Autowired
    DatasourceService datasourceService;
    @ResponseBody
    @RequestMapping(value = "/getFingerInfo",method = RequestMethod.GET)
    public List<FingerPrint> getFingerInfo() {
        List<FingerPrint> fingerPrints= fingerPrintService.selectAll();
        return fingerPrints;
    }

    @RequestMapping("/deleteFinger/{id}")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteFinger(@PathVariable int id) {
        FingerPrint fingerPrint=fingerPrintService.selectFinger(id);
            fingerPrintService.deleteFinger(id);
            datasourceService.removeTable(fingerPrint.getTable_name());
            return true;

        }

}

