package com.tqh.demo.service;

import com.tqh.demo.mapper.UserLocationMapper;
import com.tqh.demo.model.UserLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserLocationService {
    @Autowired
    UserLocationMapper userLocationMapper;

    public List<UserLocation> selectAll() {

        return userLocationMapper.selectAll();
    }
}
