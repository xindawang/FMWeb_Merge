package com.tqh.demo.service;

import com.tqh.demo.mapper.AverageMapper;
import com.tqh.demo.model.Average;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AverageService{

    @Autowired
    AverageMapper averageMapper;
    public Average selectAverage(String samplePoint) {
        return averageMapper.selectAverage(samplePoint);
    }


    public void InsertAverage(Average average) {
        averageMapper.InsertAverage(average);
    }

    public void DeleteAverage(String samplePoint){
        averageMapper.DeleteAverage(samplePoint);
    }

    public List<Average> selectAllAverage() {
        return averageMapper.selectAllAverage();
    }

    ;
}
