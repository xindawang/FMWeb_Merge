package com.tqh.demo.service;

import com.tqh.demo.mapper.DatasourceMapper;
import com.tqh.demo.model.Datasource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatasourceService {
    @Autowired
    DatasourceMapper datasourceMapper;
    public List<Datasource>selectAll(){
        return datasourceMapper.selectAll();
    }
}
