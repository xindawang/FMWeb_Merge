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
