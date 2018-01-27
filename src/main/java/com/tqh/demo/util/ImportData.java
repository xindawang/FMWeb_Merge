package com.tqh.demo.util;

import com.tqh.demo.model.Average;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class ImportData {
    public static void main(String[] args){
        String path="src/main/resources/笔记本电脑_均值.txt";
        File fileName=new File(path);
        Average average=null;
        try{

            InputStreamReader reader=new InputStreamReader(new FileInputStream(fileName));
            BufferedReader bufferedReader=new BufferedReader(reader);
            String line=bufferedReader.readLine();
            while(line!=null) {
                //跳过空行
                if(line.length()!=0) {
                    char fristChar = line.charAt(0);
                    if(average==null)
                        average=new Average();
                    if(fristChar=='采'){
                        //截取‘采样点：’后面的值
                        average.setSamplePoint(line.substring(6));
                    }else if(fristChar=='a'){
                        //找到数据行了，以';'为标志分离
                        String[] datas=line.split(";");
                        average.setValue1(datas[0]);
                        average.setValue2(datas[1]);
                        average.setValue3(datas[2]);
                        average.setValue4(datas[3]);
                        average.setValue5(datas[4]);
                        average.setValue6(datas[5]);
                        average.setValue7(datas[6]);
                        average.setValue8(datas[7]);
                        average.setValue9(datas[8]);
                        System.out.println(average);
                        average=null;//这个Average对象装配完成了，准备生成下一个
                    }

                }
                    line=bufferedReader.readLine();
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
