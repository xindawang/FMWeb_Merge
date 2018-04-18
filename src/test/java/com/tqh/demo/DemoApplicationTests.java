package com.tqh.demo;

import com.tqh.demo.model.RpEntity;
import com.tqh.demo.service.BayesService;
import com.tqh.demo.service.DatasourceService;
import com.tqh.demo.service.KnnService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.NumberFormat;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Autowired
	DatasourceService datasourceService;
	@Autowired
	KnnService knnService;
	@Autowired
	BayesService bayesService;
	@Test
	public void getTestLoc(){
		RpEntity rpEntity = new RpEntity();
		HashMap<String,Double> apentities = new HashMap<>();
		apentities.put("ap1",Double.valueOf(-50));
		apentities.put("ap2",Double.valueOf(-44));
		apentities.put("ap3",Double.valueOf(-44));
		apentities.put("ap4",Double.valueOf(-43));
		apentities.put("ap5",Double.valueOf(-47));
		rpEntity.setPoints(apentities);
		knnService.getLocByKnn(rpEntity,"12017-11-13 13:43:11.0");
//		bayesService.getLocByBayes(rpEntity,"12017-11-13 13:43:11.0");
		NumberFormat numberFormat=NumberFormat.getInstance();
		numberFormat.setGroupingUsed(false);
		System.out.println(numberFormat.format(rpEntity.getX()));
		System.out.println(numberFormat.format(rpEntity.getY()));
	}

	@Test
	public void txt() {
		datasourceService.InsertDataFromTxt("ss","F:\\材料\\实验室\\Tablet.txt");
	}

}
