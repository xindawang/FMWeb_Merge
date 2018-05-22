package com.tqh.demo.service;

import com.tqh.demo.mapper.DatasourceMapper;
import com.tqh.demo.model.BayesArgsEntity;
import com.tqh.demo.model.Datasource;
import com.tqh.demo.model.RpEntity;
import com.tqh.demo.util.FileTool;
import com.tqh.demo.util.RssiTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
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

    public void printRelData(String filename){
        List<String> fileList = FileTool.traverseFolder(filename);
        for(String file : fileList){
            List<String> eachPointData = getRssiFromTxt(file,1,100);
            for (String eachTimeOfEachPoint : eachPointData){
                RpEntity rpEntity = new RpEntity();
                HashMap<String,Double> apentities = new HashMap<>();
                String[] eachRpSet = eachTimeOfEachPoint.split(";");
                for (int i = 0; i < eachRpSet.length; i++) {
                    String[] eachAp = eachRpSet[i].split(" ");
                    if (RssiTool.getNameChangeMap().containsKey(eachAp[0]))
                        apentities.put(eachAp[0],Double.valueOf(eachAp[1]));
                }
                rpEntity.setPoints(apentities);
//                RssiTool.changeAbsEntityToMinusRel(rpEntity);
                RssiTool.changeAbsEntityToDivideRel(rpEntity);
                printRssi(rpEntity);
            }

        }
    }

    public void printAbsData(String filename){
        List<String> fileList = FileTool.traverseFolder(filename);
        FileWriter writer;
        try {
            writer = new FileWriter("D:\\IotSrc\\mi\\data.txt",true);
            for(String file : fileList){
                List<String> eachPointData = getRssiFromTxt(file,1,100);
                for (String s:eachPointData){
                    writer.write(s+"\n");
                }
            }
            writer.write("\n");
            writer.flush();//刷新内存，将内存中的数据立刻写出。
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void printRssi(RpEntity rpEntity){
        FileWriter writer;
        try {
            writer = new FileWriter("D:\\IotSrc\\mi\\divideData",true);
//            writer = new FileWriter("D://divideData.txt",true);
            for (String apName :rpEntity.getPoints().keySet()) {
                writer.write(apName+" "+rpEntity.getPoints().get(apName)+";");
            }
            writer.write("\n");
            writer.flush();//刷新内存，将内存中的数据立刻写出。
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
                List<String> eachPointData = getRssiFromTxt(fileList.get(rpCurCount-1),1,100);
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

    public List<String> getRelRssiFromTxt(String filename, int stNum, int edNum){
        List<String> eachPointData= new ArrayList<>();
        try {
            FileReader reader = new FileReader(filename);
            BufferedReader br = new BufferedReader(reader);
            String str = br.readLine();
            int curNum = 1;
            while (curNum<stNum && str !=null){
                br.readLine();
                curNum++;
            }
            while (curNum <= edNum && str != null) {
                eachPointData.add(str);
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

    @Transactional(rollbackFor = Exception.class)
    public boolean insertRelDataFromTxt(String tableName, String filename) {
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
        int rpCurCount = 1;
        for (String str : allPoints) {
            List<String> eachPointData = getRelRssiFromTxt(filename,(rpCurCount-1)*100+1,100*rpCurCount);
            for (int i = 1; i <= apAmount; i++) {
                String apName = "ap"+i;
                List<Double> eachApData =  getApRssiOfRpFromTxt(eachPointData,apName);
                if (eachApData.isEmpty()) continue;
                if (!computeAndInsertGaussArgs(tableName,eachApData,str,apName)) return false;
//                System.out.println(eachApData);
            }
            rpCurCount++;
        }
        return true;
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
