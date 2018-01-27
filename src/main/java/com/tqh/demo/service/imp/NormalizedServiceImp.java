package com.tqh.demo.service.imp;

import com.tqh.demo.mapper.NormalizedMapper;
import com.tqh.demo.model.Average;
import com.tqh.demo.model.Normalized;
import com.tqh.demo.service.NormalizedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NormalizedServiceImp implements NormalizedService{
    @Autowired
    NormalizedMapper normalizedMapper;

    @Override
    public Normalized selectNormalized(String samplePoint) {
        return normalizedMapper.selectNormalized(samplePoint);
    }

    @Override
    public void InsertNormalized(Normalized normalized) {
        normalizedMapper.InsertNormalized(normalized);
    }

    @Override
    public void DeleteNormalized(String samplePoint) {
        normalizedMapper.DeleteNormalized(samplePoint);
    }
}
