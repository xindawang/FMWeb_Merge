package com.tqh.demo;

import com.tqh.demo.model.PointLocation;
import com.tqh.demo.model.RpEntity;
import com.tqh.demo.service.BayesService;
import com.tqh.demo.service.DatasourceService;
import com.tqh.demo.service.KnnService;
import com.tqh.demo.service.PointLocationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Autowired
	DatasourceService datasourceService;

	@Autowired
	PointLocationService pointLocationService;

	@Autowired
	KnnService knnService;

	@Autowired
	BayesService bayesService;

	@Test
	public void getTestLoc(){
		String diviceId = "1";
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
		String recordDate = sDateFormat.format(new java.util.Date());
		datasourceService.createTable(diviceId+"_"+recordDate);
	}

	@Test
	public void insertData(){
		String filename = "E:\\tablet_mi\\tablet";
		datasourceService.getArgsFromDir("1_2018-04-27-17:11:51",filename);
	}

	@Test
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

	@Test
	public void insertPointLocation(){
		String filename = "E:\\JavaWebProject\\FMMap_web\\src\\main\\resources\\static\\data\\projectSrc\\Point_Location.txt";
		pointLocationService.insertPointLocation(filename);
	}

}
