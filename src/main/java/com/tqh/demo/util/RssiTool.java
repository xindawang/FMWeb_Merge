package com.tqh.demo.util;


import com.tqh.demo.mapper.DatasourceMapper;
import com.tqh.demo.mapper.KMeansMapper;
import com.tqh.demo.model.RpEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by ACER on 2018/3/8.
 */
public class RssiTool {

    @Autowired
    static DatasourceMapper datasourceMapper;

    @Autowired
    static KMeansMapper kMeansMapper;

    public static int apAmount = 15;
    public static String tableName = "1_2018-04-27-17:11:51";

    public static void changeAbsEntityToMinusRel(RpEntity rpEntity){
        HashMap<String,Double> apEntities = rpEntity.getPoints();
        double minAbsRssi = -200;
        for (Double v : apEntities.values()){
            if (v > minAbsRssi){
                minAbsRssi = v;
            }
        }
        for (String s : apEntities.keySet()){
            apEntities.put(s,apEntities.get(s)-minAbsRssi);
        }
        rpEntity.setPoints(apEntities);
    }

    public static void changeAbsEntityToDivideRel(RpEntity rpEntity){
        HashMap<String,Double> apEntities = rpEntity.getPoints();
        double minAbsRssi = -200;
        for (Double v : apEntities.values()){
            if (v > minAbsRssi){
                minAbsRssi = v;
            }
        }
        for (String s : apEntities.keySet()){
            apEntities.put(s,apEntities.get(s)/minAbsRssi);
        }
        rpEntity.setPoints(apEntities);
    }

    public static void setRssiInRpEntity(RpEntity rpEntity,String str){
        HashMap<String, Double> apEntities = new HashMap<>();
        String[] eachRpSet = str.split(";");
        for (int i=0;i< eachRpSet.length;i++) {
            String[] eachAp = eachRpSet[i].split(" ");
            apEntities.put(RssiTool.getNewName(eachAp[0]),Double.valueOf(eachAp[1]));
        }
        rpEntity.setPoints(apEntities);
    }

    public static String getNewName(String oldName){
        HashMap<String, String> changeName = new HashMap<>();
        changeName.put("abc2", "ap1");
        changeName.put("abc3", "ap2");
        changeName.put("abc4", "ap3");
        changeName.put("abc6", "ap4");
        changeName.put("abc7", "ap5");
        changeName.put("abc8", "ap6");
//        changeName.put("TP-LINK_0236", "ap7");
//        changeName.put("TP-LINK_3E5D", "ap8");
//        changeName.put("TP-LINK_115D", "ap9");
//        changeName.put("TP-LINK_1646", "ap10");
        changeName.put("Xiaomi_3525_CADA", "ap11");
        changeName.put("Xiaomi_31CB_CE34", "ap12");
        changeName.put("MERCURY_CFF9", "ap13");
        changeName.put("MERCURY_BD09", "ap14");
        changeName.put("MERCURY_B932", "ap15");
        return changeName.get(oldName);
    }

    public static HashMap<String, String> getNameChangeMap(){
        HashMap<String, String> changeName = new HashMap<>();
        changeName.put("abc2", "ap1");
        changeName.put("abc3", "ap2");
        changeName.put("abc4", "ap3");
        changeName.put("abc6", "ap4");
        changeName.put("abc7", "ap5");
        changeName.put("abc8", "ap6");
//        changeName.put("TP-LINK_0236", "ap7");
//        changeName.put("TP-LINK_3E5D", "ap8");
//        changeName.put("TP-LINK_115D", "ap9");
//        changeName.put("TP-LINK_1646", "ap10");
        changeName.put("Xiaomi_3525_CADA", "ap11");
        changeName.put("Xiaomi_31CB_CE34", "ap12");
        changeName.put("MERCURY_CFF9", "ap13");
        changeName.put("MERCURY_BD09", "ap14");
        changeName.put("MERCURY_B932", "ap15");
        return changeName;
    }

    public static HashMap<String, String> getCNNNameChangeMap(){
        HashMap<String, String> changeName = new LinkedHashMap<>();
        changeName.put("abc2", "ap1");
        changeName.put("abc3", "ap2");
        changeName.put("abc4", "ap3");
        changeName.put("abc6", "ap4");
        changeName.put("abc7", "ap5");
        changeName.put("abc8", "ap6");
//        changeName.put("TP-LINK_0236", "ap7");
//        changeName.put("TP-LINK_3E5D", "ap8");
//        changeName.put("TP-LINK_115D", "ap9");
//        changeName.put("TP-LINK_1646", "ap10");
        changeName.put("Xiaomi_3525_CADA", "ap11");
        changeName.put("Xiaomi_31CB_CE34", "ap12");
        changeName.put("MERCURY_CFF9", "ap13");
        changeName.put("MERCURY_BD09", "ap14");
        changeName.put("MERCURY_B932", "ap15");
        return changeName;
    }

    public static HashMap<String, Double> getNBiggestMap(HashMap<String, Double> map , int n){
        if (n>map.size())return map;
        HashMap<String, Double> resultMap = new HashMap<>();
        List<Map.Entry<String,Double>> list = new ArrayList<Map.Entry<String,Double>>(map.entrySet());
        Collections.sort(list,new Comparator<Map.Entry<String,Double>>() {
            //升序排序
            public int compare(Map.Entry<String, Double> o1,
                               Map.Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }

        });
        for (int i = 0; i < n; i++) {
            resultMap.put(list.get(i).getKey(),list.get(i).getValue());
        }
        return resultMap;
    }
}
