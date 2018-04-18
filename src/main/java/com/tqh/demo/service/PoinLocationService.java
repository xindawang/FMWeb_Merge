package com.tqh.demo.service;

import com.tqh.demo.mapper.PointLocationMapper;
import com.tqh.demo.model.PointLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PoinLocationService {
    @Autowired
    PointLocationMapper pointLocationMapper;
    public void insertPointLocation(String point_name,int x,int y){
        pointLocationMapper.insertPointLocation(point_name,x,y);
    }

    public PointLocation getPointLocation(String pointName){
        return pointLocationMapper.getPointLocation(pointName);
    }
}
