package com.tqh.demo.service;

import com.tqh.demo.mapper.UserLocationMapper;
import com.tqh.demo.model.RpEntity;
import com.tqh.demo.model.UserLocation;
import com.tqh.demo.util.RssiTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserLocationService {
    @Autowired
    UserLocationMapper userLocationMapper;

    @Autowired
    KMeansService kMeansService;

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

        //add with the offset
        userLocation.setX(userLocation.getX()/Math.pow(10,6)+12735839);
        userLocation.setY(userLocation.getY()/Math.pow(10,6)+3569534);
        return userLocation;
    }

    public void getMBUserLocation(RpEntity rpEntity,String algorithm){
        kMeansService.getRpKmeansGroupNum(rpEntity);
        IPositioningAlgorithm positioningAlgorithm;

        //choose algorithm
        if (algorithm.contains("knn")) positioningAlgorithm = new KnnService();
        else positioningAlgorithm = new BayesService();

        //use relative RSSI value or initial value
        if (algorithm.contains("DivideRel")) positioningAlgorithm.getLocByDivideRel(rpEntity);
        else if (algorithm.contains("MinusRel")) positioningAlgorithm.getLocByMinusRel(rpEntity);
        else positioningAlgorithm.getLoc(rpEntity);
    }
}
