package com.tqh.demo.service;

import com.tqh.demo.mapper.NormalizedMapper;
import com.tqh.demo.model.Normalized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NormalizedService{
    @Autowired
    NormalizedMapper normalizedMapper;

    public Normalized selectNormalized(String samplePoint) {
        return normalizedMapper.selectNormalized(samplePoint);
    }

    public void InsertNormalized(Normalized normalized) {
        normalizedMapper.InsertNormalized(normalized);
    }

    public void DeleteNormalized(String samplePoint) {
        normalizedMapper.DeleteNormalized(samplePoint);
    }
}
