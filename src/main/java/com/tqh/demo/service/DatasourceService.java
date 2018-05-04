package com.tqh.demo.service;

import com.tqh.demo.mapper.DatasourceMapper;
import com.tqh.demo.model.Datasource;
import com.tqh.demo.util.FileTool;
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

    private int apAmount=15;

    public List<Datasource> selectAll(){
        return datasourceMapper.selectAll();
    }

    public Datasource selectDatasource(int id){
        return  datasourceMapper.selectDatasource(id);
    }

    public boolean createTable(String upload_time) {
        String apString = "";
        for (int i = 1; i <= apAmount; i++) {
            apString = apString+"ap"+i+"_average DECIMAL(10,7),"
                    +"ap"+i+"_variance DECIMAL(10,7)";
            if (i<apAmount) apString += ",";
        }
            return datasourceMapper.createTable(upload_time,apString);
    }
    public boolean removeTable(String table_name) {
           return datasourceMapper.removeTable(table_name);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean insertDataFromTxt(String tableName, String filename) {
        List<String> allPoints=new ArrayList<String>();
        for(int i=1;i<=50 ;i++){
            allPoints.add("A"+i);
        }
        for(int i=51;i<=80 ;i++){
            allPoints.add("B"+i);
        }
        for(int i=81;i<=105 ;i++){
            allPoints.add("C"+i);
        }
        for(int i=106;i<=117 ;i++){
            allPoints.add("D"+i);
        }
        for(int i=118;i<=129 ;i++){
            allPoints.add("E"+i);
        }
        for(int i=130;i<=130 ;i++){
            allPoints.add("F"+i);
        }
        List<String> fileList = FileTool.traverseFolder(filename);
        int rpCurCount = 1;
        for (String str : allPoints) {
            for (int i = 1; i <= apAmount; i++) {
                String apName = "ap"+i;
                List<String> eachPointData = getRssiFromTxt(fileList.get(rpCurCount-1),1,80);
                List<Double> eachApData =  getApRssiOfRpFromTxt(eachPointData,apName);
                if (eachApData.isEmpty()) continue;
                if (!computeAndInsertGaussArgs(tableName,eachApData,str,apName)) return false;
//                System.out.println(eachApData);
            }
            rpCurCount++;
        }
        return true;
    }

    public List<String> getRssiFromTxt(String filename, int stNum, int edNum){
        List<String> eachPointData= new ArrayList<>();
        try {
            FileReader reader = new FileReader(filename);
            BufferedReader br = new BufferedReader(reader);
            br.readLine();
            String str = br.readLine();
            int curNum = 1;
            while (curNum<stNum && str !=null){
                br.readLine();
                br.readLine();
                br.readLine();
                curNum++;
            }
            while (curNum <= edNum && str != null) {
                eachPointData.add(str);
                br.readLine();
                br.readLine();
                str = br.readLine();
                curNum++;
            }
            br.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return eachPointData;
    }

    public List<Double> getApRssiOfRpFromTxt(List<String> eachPointData, String apName) {
        List<Double> eachApData= new ArrayList<>();
        for(String str : eachPointData) {
            String[] eachRpSet = str.split(";");
            for (int i = 0; i < eachRpSet.length; i++) {
                String[] eachAp = eachRpSet[i].split(" ");
                if (RssiTool.getNewName(eachAp[0]).equals(apName)) {
                    eachApData.add(Double.valueOf(eachAp[1]));
                    break;
                }

            }
        }
        return eachApData;
    }

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
