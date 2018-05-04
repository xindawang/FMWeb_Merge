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
        List<UserLocation> userLocations = userLocationMapper.selectAll();
        for(UserLocation userLocation :userLocations){
            userLocation.setX(userLocation.getX()/Math.pow(10,6)+12735839);
            userLocation.setY(userLocation.getY()/Math.pow(10,6)+3569534);
        }
        return userLocations;
    }
    public UserLocation selectUser(String id){
        UserLocation userLocation = userLocationMapper.selectUser(id);
        userLocation.setX(userLocation.getX()/Math.pow(10,6)+12735839);
        userLocation.setY(userLocation.getY()/Math.pow(10,6)+3569534);
        return userLocation;
    }
}
