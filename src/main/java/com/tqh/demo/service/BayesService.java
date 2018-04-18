package com.tqh.demo.service;

import com.tqh.demo.mapper.DatasourceMapper;
import com.tqh.demo.mapper.PointLocationMapper;
import com.tqh.demo.model.BayesArgsEntity;
import com.tqh.demo.model.PointLocation;
import com.tqh.demo.model.RpEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.lang.Math.E;
@Service
public class BayesService {
    @Autowired
    PointLocationMapper pointLocationMapper;
    @Autowired
    DatasourceMapper datasourceMapper;
    private int apAmount = 5;

    private int k = 3;
    public void getLocByBayes(RpEntity rpEntity, String tableName){

        //initialize the input data
        HashMap<String, Double> rpInfoSrc = rpEntity.getPoints();

        //get ratio and location info of each point and add up for the result
        PointLocation[] maxKEntities = getKPointsWithHighestProb(tableName,rpInfoSrc,1,50);


        double sum = 0,x=0,y=0;
        for (int i = 0; i < maxKEntities.length; i++){
            sum += maxKEntities[i].getBayesResult();
        }
        for (int i = 0; i < maxKEntities.length; i++){
            double ratio = maxKEntities[i].getBayesResult()/sum;
            PointLocation locEntity = pointLocationMapper.getPointLocation(maxKEntities[i].getPoint_name());
            x += ratio * locEntity.getX();
            y += ratio * locEntity.getY();
        }

        //convert the format of location info according to how it store into database in knnService
        double result_x = x/Math.pow(10,6) + 12735800;
        double result_y = y/Math.pow(10,7) + 3569540;
        rpEntity.setX(result_x);
        rpEntity.setY(result_y);
    }

    private PointLocation[] getKPointsWithHighestProb(String tableName, HashMap<String, Double> rpInfoSrc,
                                                       int startCount, int endCount){

        //use the way of priority queue, which could be compared to the knn
        Comparator<PointLocation> cmp = new Comparator<PointLocation>() {
            @Override
            public int compare(PointLocation o1, PointLocation o2) {
                return o2.getBayesResult()>o1.getBayesResult()?1:-1;
            }
        };

        Queue<PointLocation> maxKPoints = new PriorityQueue<>(cmp);
        PointLocation[] pointLocEntities = new PointLocation[k];
        List<String> allPointNames = datasourceMapper.getAllPointName(tableName);

        int count =1;
        //calculate the probability of each candidate point, pick the max k
        for (String pointName : allPointNames) {
            if (count>=startCount&&count<=endCount) {
                PointLocation candidateLocEntity = new PointLocation();
                double eachPointProb = 1.0;

                //put the online data into Gauss formula with Gauss index of each ap
                for (int i = 1; i <= apAmount; i++) {
                    String apName = "ap" + i;
                    String avgName = "ap" + i + "_average";
                    String varName = "ap" + i + "_variance";
                    BayesArgsEntity eachAp = datasourceMapper.getEachApArgs(tableName,avgName, varName, pointName);
                    if (eachAp.getApNameVar() == 0){
                        eachAp.setApNameVar(0.000001);
                    }
                    //handle the situation when apX is not found
                    double thisApProb = 1;
                    if (rpInfoSrc.get(apName)!=null){
                        thisApProb = 1 / Math.sqrt(2 * Math.PI * eachAp.getApNameVar()) * Math.pow(E, -Math.pow(rpInfoSrc.get(apName) - eachAp.getApNameAvg(), 2) / (2 * eachAp.getApNameVar()));
                    }
                    eachPointProb *= thisApProb;
                }
                candidateLocEntity.setPoint_name(pointName);
                candidateLocEntity.setBayesResult(eachPointProb);
                maxKPoints.offer(candidateLocEntity);
            }
            count++;
        }
        for (int i = 0; i < k; i++){
            pointLocEntities[i] = maxKPoints.poll();
        }
        return pointLocEntities;
    }
}
