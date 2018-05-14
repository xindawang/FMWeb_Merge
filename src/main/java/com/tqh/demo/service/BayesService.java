package com.tqh.demo.service;

import com.tqh.demo.mapper.DatasourceMapper;
import com.tqh.demo.mapper.KMeansMapper;
import com.tqh.demo.mapper.PointLocationMapper;
import com.tqh.demo.model.BayesArgsEntity;
import com.tqh.demo.model.PointLocation;
import com.tqh.demo.model.RpEntity;
import com.tqh.demo.util.RssiTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.lang.Math.E;
@Service
public class BayesService implements IPositioningAlgorithm {
    @Autowired
    PointLocationMapper pointLocationMapper;

    @Autowired
    DatasourceMapper datasourceMapper;

    @Autowired
    KMeansMapper kMeansMapper;

    private int k;
    public void getLocByBayes(RpEntity rpEntity, String  tableName,int kAmount){

        k = kAmount;
        //initialize the input data
//        HashMap<String, Double> rpInfoSrc = RssiTool.getNBiggestMap(rpEntity.getPoints(),7);
        HashMap<String, Double> rpInfoSrc = rpEntity.getPoints();

        List<String> allPointNames;
        if (rpEntity.getKmeansGroupNum()!=null) allPointNames= kMeansMapper.getPointNameByCoreNum(RssiTool.tableName+"_type",rpEntity.getKmeansGroupNum());
        else allPointNames = datasourceMapper.getAllPointName(tableName);

        //get ratio and location info of each point and add up for the result
        PointLocation[] maxKEntities = getKPointsWithHighestProb(tableName,rpInfoSrc,allPointNames);


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
        double result_x = x/Math.pow(10,6) + 12735839;
        double result_y = y/Math.pow(10,6) + 3569534;
        rpEntity.setX(result_x);
        rpEntity.setY(result_y);
    }

    private PointLocation[] getKPointsWithHighestProb(String tableName, HashMap<String, Double> rpInfoSrc,List<String> allPointNames){

        //use the way of priority queue, which could be compared to the knn
        Comparator<PointLocation> cmp = new Comparator<PointLocation>() {
            @Override
            public int compare(PointLocation o1, PointLocation o2) {
                return o2.getBayesResult()>o1.getBayesResult()?1:-1;
            }
        };

        Queue<PointLocation> maxKPoints = new PriorityQueue<>(cmp);
        PointLocation[] pointLocEntities = new PointLocation[k];

        //calculate the probability of each candidate point, pick the max k
        for (String pointName : allPointNames) {
            PointLocation candidateLocEntity = new PointLocation();
            double eachPointProb = 1.0;

            //put the online data into Gauss formula with Gauss index of each ap
            for (int i = 1; i <= RssiTool.apAmount; i++) {
                String apName = "ap" + i;
                String avgName = "ap" + i + "_average";
                String varName = "ap" + i + "_variance";
                BayesArgsEntity eachAp = datasourceMapper.getEachApArgs(tableName,avgName, varName, pointName);
                if (eachAp == null) continue;
                if (eachAp.getApNameVar() == 0){
                    eachAp.setApNameVar(0.000001);
                }
                //handle the situation when apX is not found
                double thisApProb = 1;
                if (rpInfoSrc.get(apName)!=null){
                    thisApProb = 1 / Math.sqrt(2 * Math.PI * eachAp.getApNameVar()) * Math.pow(E, -Math.pow(rpInfoSrc.get(apName) - eachAp.getApNameAvg(), 2) / (2 * eachAp.getApNameVar()));
                    if (thisApProb<0.000001) thisApProb = 0.000001;
                }
                eachPointProb *= thisApProb;
            }
            candidateLocEntity.setPoint_name(pointName);
            candidateLocEntity.setBayesResult(eachPointProb);
            maxKPoints.offer(candidateLocEntity);
        }
        for (int i = 0; i < k; i++){
            pointLocEntities[i] = maxKPoints.poll();
        }
        return pointLocEntities;
    }

    @Override
    public void getLoc(RpEntity rpEntity) {
        getLocByBayes(rpEntity, RssiTool.tableName, 5);
    }

    @Override
    public void getLocByMinusRel(RpEntity rpEntity) {
        RssiTool.changeAbsEntityToMinusRel(rpEntity);
        getLocByBayes(rpEntity, RssiTool.tableName+"_minus_rel", 5);
    }

    @Override
    public void getLocByDivideRel(RpEntity rpEntity) {
        RssiTool.changeAbsEntityToDivideRel(rpEntity);
        getLocByBayes(rpEntity, RssiTool.tableName+"_divide_rel", 5);
    }
}
