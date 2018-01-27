package com.tqh.demo.service;

import com.tqh.demo.model.Average;

import java.util.List;

public interface AverageService {
    Average selectAverage(String samplePoint);
    void InsertAverage(Average average);
    void DeleteAverage(String samplePoint);
    List<Average> selectAllAverage();
}
