package com.tqh.demo;


import com.tqh.demo.mapper.AverageMapper;
import com.tqh.demo.mapper.NormalizedMapper;
import com.tqh.demo.model.Average;
import com.tqh.demo.model.Normalized;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestImport {

    @Autowired
    NormalizedMapper normalizedMapper;//编译器抽风报错，不管...


    @Test
    public void insertTest(){
        String path="src/main/resources/笔记本电脑_归一化.txt";
        File fileName=new File(path);
        Normalized normalized=null;
        try{
            InputStreamReader reader=new InputStreamReader(new FileInputStream(fileName));
            BufferedReader bufferedReader=new BufferedReader(reader);
            String line=bufferedReader.readLine();
            while(line!=null) {
                //跳过空行
                if(line.length()!=0) {
                    char fristChar = line.charAt(0);
                    if(normalized==null)
                        normalized=new Normalized();
                    if(fristChar=='采'){
                        //截取‘采样点：’后面的值
                        normalized.setSamplePoint(line.substring(6));
                    }else if(fristChar=='a'){
                        //找到数据行了，以';'为标志分离
                        String[] datas=line.split(";");
                        normalized.setValue1(datas[0]);
                        normalized.setValue2(datas[1]);
                        normalized.setValue3(datas[2]);
                        normalized.setValue4(datas[3]);
                        normalized.setValue5(datas[4]);
                        normalized.setValue6(datas[5]);
                        normalized.setValue7(datas[6]);
                        normalized.setValue8(datas[7]);
                        normalized.setValue9(datas[8]);
                        System.out.println(normalized);
                        normalizedMapper.InsertNormalized(normalized);
                        normalized=null;//这个Average对象装配完成了，准备生成下一个
                    }

                }
                line=bufferedReader.readLine();
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
}

}