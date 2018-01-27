package com.tqh.demo.service;

import com.tqh.demo.model.Average;
import com.tqh.demo.model.Normalized;

public interface NormalizedService {
    Normalized selectNormalized(String samplePoint);
    void InsertNormalized(Normalized normalized );
    void DeleteNormalized(String samplePoint);
}
