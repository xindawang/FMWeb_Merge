package com.tqh.demo.service.imp;

import com.tqh.demo.mapper.AverageMapper;
import com.tqh.demo.model.Average;
import com.tqh.demo.service.AverageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AverageServiceImp implements AverageService{

    @Autowired
    AverageMapper averageMapper;
    @Override
    public Average selectAverage(String samplePoint) {
        return averageMapper.selectAverage(samplePoint);
    }

    @Override
    public void InsertAverage(Average average) {
        averageMapper.InsertAverage(average);
    }

    @Override
    public void DeleteAverage(String samplePoint){
        averageMapper.DeleteAverage(samplePoint);
    }

    @Override
    public List<Average> selectAllAverage() {
        return averageMapper.selectAllAverage();
    }

    ;
}
