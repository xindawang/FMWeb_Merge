package com.tqh.demo;

import com.tqh.demo.service.FingerPrintService;
import com.tqh.demo.service.PoinLocationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TansanctionalTest {

    @Autowired
    PoinLocationService poinLocationService;
    @Test
    public void test(){
        File file=new File("F:/材料/实验室/数据 - 副本.txt");
        File output=new File("F:/材料/实验室/hhhhh.txt");
        try {
            FileReader fileReader=new FileReader(file);
            BufferedReader bufferedReader=new BufferedReader(fileReader);
            String line=bufferedReader.readLine();
            //true在末尾继续写，false覆盖
            FileWriter fileWriter=new FileWriter(output,false);
            BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
            while (line!=null){

                if (line.length()!=0){
                    String[] strings=line.split(",");
                    String point_name=strings[0].trim();
                int x=Integer.parseInt(strings[1].trim().substring(6,15).replace(".",""));
                int y=Integer.parseInt(strings[2].trim().substring(6,15).replace(".",""));

                     poinLocationService.insertPointLocation(point_name,x,y);
                    line=bufferedReader.readLine();
                }

            }
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
