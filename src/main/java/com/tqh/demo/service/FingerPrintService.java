package com.tqh.demo.service;

import com.tqh.demo.mapper.FingerPrintMapper;
import com.tqh.demo.model.FingerPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FingerPrintService {
    @Autowired
    FingerPrintMapper fingerPrintMapper;
    public boolean insertFinger(String table_name){
        fingerPrintMapper.insertFinger(table_name);
        return true;
    }

    public List<FingerPrint> selectAll(){
        return fingerPrintMapper.selectAll();
    }

    public boolean deleteFinger(int id){
            fingerPrintMapper.deleteFinger(id);
            return true;
    }
    public FingerPrint selectFinger(int id){
       return fingerPrintMapper.selectFinger(id);
    }
}
