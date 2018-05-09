package com.tqh.demo.service;


import com.tqh.demo.mapper.DatasourceMapper;
import com.tqh.demo.mapper.KMeansMapper;
import com.tqh.demo.model.BayesArgsEntity;
import com.tqh.demo.model.KMeansEntity;
import com.tqh.demo.model.PointLocation;
import com.tqh.demo.model.RpEntity;
import com.tqh.demo.util.RssiTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by ACER on 2018/4/12.
 */
@Service
public class KMeansService {

    @Autowired
    DatasourceMapper datasourceMapper;

    @Autowired
    KMeansMapper kMeansMapper;

    private int apNum = 15;
    private int pointNum = 130;
    private int k = 6;
    private double threshold = 0;

    private List<RpEntity> rpEntities = new ArrayList<>();
    private List<KMeansEntity> kMeansEntities = new ArrayList<>();
    List<KMeansEntity> newKMeansEntities = new ArrayList<>();

    private String tableName = "1_2018-04-27-17:11:51";

    public void startClustering(String tableName){
        this.tableName = tableName;
        init();
        moveCore();
    }

    private void init(){
        List<String> allPointNames = datasourceMapper.getAllPointName(tableName);

        //calculate the probability of each candidate point, pick the max k
        for (String pointName : allPointNames) {
            RpEntity rpEntity = new RpEntity();
            HashMap<String,Double> apMap  = new HashMap<>();
            //put the online data into Gauss formula with Gauss index of each ap
            for (int i = 1; i <= apNum; i++) {
                String apName = "ap" + i;
                String avgName = "ap" + i + "_average";
                String varName = "ap" + i + "_variance";
                BayesArgsEntity eachAp = datasourceMapper.getEachApArgs(tableName, avgName, varName, pointName);
                if (eachAp!=null) apMap.put(apName,eachAp.getApNameAvg());
            }
            rpEntity.setPoints(apMap);
            rpEntities.add(rpEntity);
        }

        //指定聚类中心
        List<Integer> coreNumList = new ArrayList<>(Arrays.asList(10,40,64,95,111,123));
        for (int i = 0;i<coreNumList.size();i++){
            KMeansEntity kMeansEntity = new KMeansEntity();
            kMeansEntity.setCoreNumer(i);
            kMeansEntity.setApEntities(rpEntities.get(coreNumList.get(i)).getPoints());
            kMeansEntities.add(kMeansEntity);
        }

        //剩下的聚类中心随机选择
        Set<Integer> coreIndexSet = new HashSet<>();
        for (int i = 0; i <k-coreNumList.size() ; i++) {
            int randomCoreIndex = (int) (Math.random()*pointNum);
            if (!coreIndexSet.contains(randomCoreIndex)){
                KMeansEntity kMeansEntity = new KMeansEntity();
                kMeansEntity.setCoreNumer(i);
                kMeansEntity.setApEntities(rpEntities.get(randomCoreIndex).getPoints());
                kMeansEntities.add(kMeansEntity);
            }else{
                i--;
            }
        }


    }

    private void moveCore(){
        setGroupIndex();
        calculateAvgOfEachGroups();
        if (!lookLikeCorrect()){
            kMeansEntities = newKMeansEntities;
            newKMeansEntities = new ArrayList<>();
            moveCore();
        }else{
            for (KMeansEntity kMeansEntity : kMeansEntities){
                for (String apName : kMeansEntity.getApEntities().keySet()){
                    System.out.print(apName+" " + kMeansEntity.getApEntities().get(apName)+";");
                }
                System.out.println();
            }
            System.out.println();
            for (RpEntity rpEntity : rpEntities){
                System.out.println(rpEntity.getKmeansGroupNum());
            }
        }
    }

    private boolean lookLikeCorrect() {
        for (int i = 0; i < k; i++) {
            for (String apName : kMeansEntities.get(i).getApEntities().keySet()){
                if (kMeansEntities.get(i).getApEntities().get(apName) == null) continue;
                if (newKMeansEntities.get(i).getApEntities().get(apName) == null) continue;
                if (Math.abs(kMeansEntities.get(i).getApEntities().get(apName)-
                        newKMeansEntities.get(i).getApEntities().get(apName))> threshold)
                    return false;
            }
        }
        return true;
    }

    public void calculateAvgOfEachGroups(){
        for (int i = 0; i < k; i++) {
            KMeansEntity kMeansEntity = new KMeansEntity();
            HashMap<String,Double> apMap = new HashMap<>();
            for (int j = 0; j < apNum; j++){
                int entityEachApCount =0;
                double entityEachApValue = 0.0;
                for (RpEntity rpEntity : rpEntities) {
                    if (rpEntity.getKmeansGroupNum() == i) {
                        if (rpEntity.getPoints().containsKey("ap"+(j+1))){
                            entityEachApCount++;
                            entityEachApValue += rpEntity.getPoints().get("ap"+(j+1));
                        }
                    }
                }
                if (entityEachApCount!=0)
                apMap.put("ap"+(j+1),entityEachApValue/entityEachApCount);
            }
            kMeansEntity.setApEntities(apMap);
            newKMeansEntities.add(kMeansEntity);
        }
    }

    public void setGroupIndex(){
        for (RpEntity rpEntity : rpEntities){
            int groupCount = 0;
            double minDistance = Double.MAX_VALUE;
            for (int i = 0; i < k; i++) {
                double distance =0.0;
                for (String apName: kMeansEntities.get(i).getApEntities().keySet()){
                    if (rpEntity.getPoints().containsKey(apName)){
                        distance +=Math.sqrt(Math.abs(kMeansEntities.get(i).getApEntities().get(apName) - rpEntity.getPoints().get(apName)));
                    }
                }
                if (distance<minDistance){
                    minDistance = distance;
                    groupCount = i;
                }
            }
            rpEntity.setKmeansGroupNum(groupCount);
        }
    }

    public boolean createCoreTable(String upload_time) {
        String apString = "";
        for (int i = 1; i <= apNum; i++) {
            apString = apString+"ap"+i+" DECIMAL(10,7)";
            if (i<apNum) apString += ",";
        }
        return kMeansMapper.createCoreTable(upload_time,apString);
    }

    public boolean createTypeTable(String upload_time) {
        return kMeansMapper.createTypeTable(upload_time);
    }

    public boolean insertCoreFromTxt(String tableName,String filename) {
        try {
            FileReader reader = new FileReader(filename);
            BufferedReader br = new BufferedReader(reader);
            String str = br.readLine();
            int count =0;
            while (str != null) {
                String[] eachAp = str.split(";");
                kMeansMapper.insertCoreIndex(tableName+"_core",count);
                for (String apInfo : eachAp){
                    String[] eachApRss = apInfo.split(" ");
                    kMeansMapper.insertCore(tableName+"_core",count,eachApRss[0],Double.parseDouble(eachApRss[1]));
                }
                count++;
                str = br.readLine();
            }
            br.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public boolean insertTypeFromTxt(String tableName,String filename) {
        try {
            FileReader reader = new FileReader(filename);
            BufferedReader br = new BufferedReader(reader);
            String str = br.readLine();
            int count =1;
            while (str != null) {
                String fingerprint_name = datasourceMapper.getPointNameById(tableName,count);
                kMeansMapper.insertType(tableName+"_type",count,fingerprint_name,Double.parseDouble(str));
                count++;
                str = br.readLine();
            }
            br.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public void getRpKmeansGroupNum(RpEntity rpEntity){
        List<KMeansEntity> kMeansEntities = getCoreEntityFromDatabase("1_2018-04-27-17:11:51_core");
        int core =-1;
        double leastResult = Double.MAX_VALUE;
        for (KMeansEntity singleCore : kMeansEntities) {
            double result = 0;
            int commonCount = 0;
            //result is sum of定位点的AP1-5与rp的AP1-5的偏差绝对值的开方
            for (String apName : rpEntity.getPoints().keySet()) {
                if (singleCore.getApEntities().containsKey(apName)) {
                    result += Math.sqrt(Math.abs(singleCore.getApEntities().get(apName) - rpEntity.getPoints().get(apName)));
                    commonCount++;
                }
            }
            //AP1-5平均偏差，越小匹配度越高
            if (commonCount > 0) {
                result /= commonCount;
            }
            if (result<leastResult){
                core = singleCore.getCoreNumer();
                leastResult = result;
            }
        }
        rpEntity.setKmeansGroupNum(core);
    }

    private List<KMeansEntity> getCoreEntityFromDatabase(String tableName) {
        List<KMeansEntity> kMeansEntities = new ArrayList<>();
        List<Integer> allCoreNum = kMeansMapper.getAllCoreNum(tableName);
        for (int pointNum : allCoreNum) {
            KMeansEntity kMeansEntity = new KMeansEntity();
            HashMap<String, Double> apEntities = new HashMap<>();
            for (int i = 1; i <= apNum; i++) {
                String apName =  "ap" + i;
                BayesArgsEntity eachAp = kMeansMapper.getEachApArgs(tableName,apName, pointNum);
                if (eachAp!=null)
                    apEntities.put(apName,eachAp.getApNameAvg());
            }
            kMeansEntity.setApEntities(apEntities);
            kMeansEntity.setCoreNumer(pointNum);
            kMeansEntities.add(kMeansEntity);
        }
        return kMeansEntities;
    }
}