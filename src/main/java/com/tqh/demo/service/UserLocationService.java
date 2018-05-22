package com.tqh.demo.service;

import com.tqh.demo.mapper.UserLocationMapper;
import com.tqh.demo.mapper.UserMapper;
import com.tqh.demo.model.RpEntity;
import com.tqh.demo.model.User;
import com.tqh.demo.model.UserLocation;
import com.tqh.demo.util.RssiTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class UserLocationService {
    @Autowired
    UserLocationMapper userLocationMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    KnnService knnService;

    @Autowired
    BayesService bayesService;

    @Autowired
    KMeansService kMeansService;

    public List<UserLocation> selectAll() {
        List<UserLocation> userLocations = userLocationMapper.selectAll();
        for(UserLocation userLocation :userLocations){
            userLocation.setX(userLocation.getX()/Math.pow(10,6)+12735839);
            userLocation.setY(userLocation.getY()/Math.pow(10,6)+3569534);

            if (userLocation.getUserid()!=null){
                User user = userMapper.selectUserById(userLocation.getUserid());
                userLocation.setUser(user);
            }
        }
        return userLocations;
    }
    public UserLocation selectUser(String id){
        UserLocation userLocation = userLocationMapper.selectUser(id);

        //add with the offset
        userLocation.setX(userLocation.getX()/Math.pow(10,6)+12735839);
        userLocation.setY(userLocation.getY()/Math.pow(10,6)+3569534);

        if (userLocation.getUserid()!=null){
            User user = userMapper.selectUserById(userLocation.getUserid());
            userLocation.setUser(user);
        }
        return userLocation;
    }

    public void getMBUserLocation(RpEntity rpEntity,String algorithm){
        kMeansService.getRpKmeansGroupNum(rpEntity);
        IPositioningAlgorithm positioningAlgorithm;

        //choose algorithm
        if (algorithm.contains("knn")) positioningAlgorithm = knnService;
        else positioningAlgorithm = bayesService;

        //use relative RSSI value or initial value
        if (algorithm.contains("DivideRel")) positioningAlgorithm.getLocByDivideRel(rpEntity);
        else if (algorithm.contains("MinusRel")) positioningAlgorithm.getLocByMinusRel(rpEntity);
        else positioningAlgorithm.getLoc(rpEntity);
    }

    public void saveMBUserLocation(RpEntity rpEntity,String device){
        int result_x = (int)((rpEntity.getX() - 12735839)*Math.pow(10,6));
        int result_y = (int)((rpEntity.getY() - 3569534)*Math.pow(10,6));
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        String recordDate = sDateFormat.format(new java.util.Date());
        if (userLocationMapper.userExists(device)!=null)
            userLocationMapper.updateUserLocation(device,result_x,result_y,recordDate);
        else userLocationMapper.insertUserLocation(device,result_x,result_y,recordDate);
    }
}
