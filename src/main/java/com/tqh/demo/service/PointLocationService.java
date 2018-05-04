package com.tqh.demo.service;

import com.tqh.demo.mapper.PointLocationMapper;
import com.tqh.demo.model.PointLocation;
import com.tqh.demo.util.RssiTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class PointLocationService {
    @Autowired
    PointLocationMapper pointLocationMapper;

    PointLocationService pointLocationService;

    private int pointCount =1;

    public void insertPointLocation(String point_name,int x,int y){
        pointLocationMapper.insertPointLocation(point_name,x,y);
    }

    public PointLocation getPointLocation(String pointName){
        return pointLocationMapper.getPointLocation(pointName);
    }

    public void insertPointLocation(String filename){
        try {
            FileReader reader = new FileReader(filename);
            BufferedReader br = new BufferedReader(reader);
            String str = br.readLine();
            while (str != null) {
                String[] eachPointSet = str.split(" ");
                insertPointLocation(eachPointSet[0],Integer.valueOf(eachPointSet[1]),Integer.valueOf(eachPointSet[2]));
                str = br.readLine();
            }
            br.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void setPointLoc(){
        //x-12735839,y-3569534
        //5.3/5.58m*1000000

        String areaName = "A";
        double width = 8.93;
        double height = 3.8;
        List<Integer> horizontal = new ArrayList<>((Arrays.asList(100, 83, 83, 67, 83, 83, 83, 68,83,83,100)));
        List<Integer> vertical = new ArrayList<>((Arrays.asList(20, 83, 83, 83, 83,20)));
        pointLocationService.setPointLoc(areaName, width, height, 6.7006,8.2467,horizontal, vertical);

        areaName = "B";
        width = 21.6;
        height = 1.6;
        horizontal = new ArrayList<>((Arrays.asList(21, 140, 140, 140, 140, 140, 140, 140, 140, 140, 140, 140, 140, 140, 140,234)));
        vertical = new ArrayList<>((Arrays.asList(21, 140, 21)));
        pointLocationService.setPointLoc(areaName, width, height, 0.0508,6.4469,horizontal, vertical);

        areaName = "C";
        width = 4.16;
        height = 3.8;
        horizontal = new ArrayList<>((Arrays.asList(44, 88, 88, 88,88,1)));
        vertical = new ArrayList<>((Arrays.asList(54, 88, 88, 88, 88,28)));
        pointLocationService.setPointLoc(areaName, width, height, 21.8510,8.2467,horizontal, vertical);

        areaName = "D";
        width = 3.52;
        height = 5.3;
        horizontal = new ArrayList<>((Arrays.asList(72, 107, 107, 78)));
        vertical = new ArrayList<>((Arrays.asList(120, 120, 120, 120, 78)));
        pointLocationService.setPointLoc(areaName, width, height, 6.5709,0.9463,horizontal, vertical);

        areaName = "E";
        width = 2.91;
        height = 5.3;
        horizontal = new ArrayList<>((Arrays.asList(78, 107, 107, 90)));
        vertical = new ArrayList<>((Arrays.asList(70, 120, 120, 120, 128)));
        pointLocationService.setPointLoc(areaName, width, height, 10.2902,0.9464,horizontal, vertical);

        int x = (int)(10.05083*1000000);
        int y = (int)(4.5803*1000000);
        System.out.println("E130 "+x+" "+y);
    }

    public void setPointLoc(String areaName, double width, double height,
                            double zeroX, double zeroY,
                            List<Integer> horizontal, List<Integer> vertical){

        int horizontalSum = 0;
        int verticalSum =0;
        for (int i = 0; i < horizontal.size() ; i++)  horizontalSum += horizontal.get(i);
        for (int i = 0; i < vertical.size() ; i++)  verticalSum += vertical.get(i);

        for (int i = 0; i < horizontal.size()-1; i++) {
            int horizontalCurCum =0;
            for (int a = 0; a <= i; a++) horizontalCurCum+= horizontal.get(a);
            for (int j = 0; j < vertical.size()-1; j++) {

                System.out.print(areaName + pointCount);
                System.out.print(" ");

                System.out.print((int)((zeroX + width*horizontalCurCum/horizontalSum)*1000000));
                System.out.print(" ");

                int verticalCurCum =0;
                for (int b = 0; b <= j; b++) verticalCurCum+= vertical.get(b);
                System.out.println((int)((zeroY + height*verticalCurCum/verticalSum)*1000000));
                pointCount++;
            }
        }
    }
}
