package com.tqh.demo.service;

import com.tqh.demo.mapper.DatasourceMapper;
import com.tqh.demo.model.Datasource;
import com.tqh.demo.util.RssiTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DatasourceService {
    @Autowired
    DatasourceMapper datasourceMapper;
    public List<Datasource> selectAll(){
        return datasourceMapper.selectAll();
    }
    public Datasource selectDatasource(int id){
        return  datasourceMapper.selectDatasource(id);
    }
    public boolean createTable(String upload_time) {
            return datasourceMapper.createTable(upload_time);
    }
    public boolean removeTable(String table_name) {
           return datasourceMapper.removeTable(table_name);
    }

    private int apAmount=5;
    @Transactional(rollbackFor = Exception.class)
    public boolean InsertDataFromTxt(String tableName,String filename){

        //      List<String> allPoints = pointLocMapper.getHorizontalPointName();//50个点
        //A0-A49 50个点 每个采样19次,5个AP
        List<String> allPoints=new ArrayList<String>();
        for(int i=1;i<=50 ;i++){
            allPoints.add("A"+i);
        }
        int rpCurCount = 1;
        for (String str : allPoints) {
            for (int i = 1; i <= apAmount; i++) {
                String apName = "ap"+i;
                List<Double> eachApData = getApRssiOfRpFromTxt(filename,apName,rpCurCount,19);
                if (!computeAndInsertGaussArgs(tableName,eachApData,str,apName)) return false;
                System.out.println(eachApData);
            }
            rpCurCount++;
        }
        return true;
    }

    public List<Double> getApRssiOfRpFromTxt(String filename,String apName, int rpCurCount,int repeatTimes){

        List<Double> eachApData= new ArrayList<>();
        try {
            FileReader reader = new FileReader(filename);
            BufferedReader br = new BufferedReader(reader);
            String str = br.readLine();
            int count = 0;
            while (str != null) {
                int rpTextCount = count/repeatTimes;
                if (rpTextCount == rpCurCount -1) {
                    String[] eachRpSet = str.split(";");
                    for (int i = 0; i < eachRpSet.length; i++) {
                        String[] eachAp = eachRpSet[i].split(" ");
                        if (RssiTool.getNewName(eachAp[0]).equals(apName)){
                            eachApData.add(Double.valueOf(eachAp[1]));
                            break;
                        }

                    }
                }
                str = br.readLine();
                count++;
            }
            br.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return eachApData;
    }

    /**
    *计算这19个点的均值和方差并插入数据库
     */
    private boolean computeAndInsertGaussArgs(String tableName, List<Double> eachApData, String pointName, String apName) {
        double sum = 0.0, variance = 0.0;
        for (Double eachResult : eachApData)
        {
            sum += eachResult;
        }
        double average = sum/eachApData.size();

        for (Double eachResult : eachApData){
            variance += Math.pow((eachResult - average),2);
        }
        variance = variance/eachApData.size();

        String avgName = apName+"_average";
        String varName = apName+"_variance";

        //insert a point if store the args for the first time
        //update the point info if the point has existed
        if (datasourceMapper.hasPoint(tableName,pointName)){
            if(!datasourceMapper.updateAvrAndVar(tableName,avgName,varName,pointName,average,variance)) return false;
        }else{
            if (!datasourceMapper.insertAvrAndVar(tableName,avgName,varName,pointName,average,variance)) return false;
        }
        System.out.println(pointName +" "+apName+" " +average+" " +variance);
        return true;
    }
}
