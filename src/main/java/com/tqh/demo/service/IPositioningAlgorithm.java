package com.tqh.demo.service;

import com.tqh.demo.model.RpEntity;

/**
 * Created by ACER on 2018/5/14.
 */
public interface IPositioningAlgorithm {

    void getLoc(RpEntity rpEntity);

    void getLocByMinusRel(RpEntity rpEntity);

    void getLocByDivideRel(RpEntity rpEntity);
}
