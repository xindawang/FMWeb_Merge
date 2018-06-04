package com.tqh.demo;

import com.tqh.demo.mapper.KMeansMapper;
import com.tqh.demo.model.KMeansEntity;
import com.tqh.demo.model.PointLocation;
import com.tqh.demo.model.RpEntity;
import com.tqh.demo.service.*;
import com.tqh.demo.util.FileTool;
import com.tqh.demo.util.RssiTool;
import org.apache.poi.hslf.blip.Bitmap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
	KMeansService kMeansService;

	@Autowired
	DatasourceService datasourceService;

	@Autowired
	PointLocationService pointLocationService;

	@Autowired
	KnnService knnService;

	@Autowired
	BayesService bayesService;

	@Autowired
	KMeansMapper kMeansMapper;

	@Test
	public void createNewTable(){
		String diviceId = "2";
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
		String recordDate = sDateFormat.format(new java.util.Date());
		datasourceService.createTable(diviceId+"_"+recordDate);
		datasourceService.createTable(diviceId+"_"+recordDate+"_minus_rel");
		datasourceService.createTable(diviceId+"_"+recordDate+"_divide_rel");
		kMeansService.createCoreTable(diviceId+"_"+recordDate+"_core");
		kMeansService.createTypeTable(diviceId+"_"+recordDate+"_type");
	}

	@Test
	public void insertData(){
		String filename = "E:\\tablet_mi\\mi";
		datasourceService.insertDataFromTxt("2_2018-05-15-09:28:11",filename);
//		String coreFilename = "E:\\tablet_mi\\tabletCore.txt";
//		kMeansService.insertCoreFromTxt("1_2018-04-27-17:11:51",coreFilename);
//		String typeFilename = "E:\\tablet_mi\\tabletType.txt";
//		kMeansService.insertTypeFromTxt("1_2018-04-27-17:11:51",typeFilename);
	}

	@Test
	public void insertRelData(){
//		String filename = "E:\\tablet_mi\\mi";
//		datasourceService.printAbsData(filename);

		String filename = "D:\\IotSrc\\mi\\minusData.txt";
		datasourceService.insertRelDataFromTxt("2_2018-05-15-09:28:11_minus_rel",filename);

	}

	public RpEntity getTestRpEntity(){
		String rssi = "abc2 -53;TP-LINK_3E5D -33;abc4 -67;TP-LINK_1646 -80;abc8 -71;abc3 -54;Xiaomi_31CB_CE34 -81;abc7 -56;TP-LINK_0236 -71;Xiaomi_3525_CADA -81;abc6 -71;MERCURY_BD09 -20;MERCURY_CFF9 -41;TP-LINK_115D -75;";
//		String rssi = "abc6 -32;abc7 -34;abc4 -47;Xiaomi_31CB_CE34 -61;MERCURY_B932 -59;Xiaomi_3525_CADA -47;abc2 -68;abc3 -55;abc8 -43;";
		RpEntity rpEntity = new RpEntity();
		HashMap<String,Double> apentities = new HashMap<>();
		String[] eachRpSet = rssi.split(";");
		for (int i = 0; i < eachRpSet.length; i++) {
			String[] eachAp = eachRpSet[i].split(" ");
			if (RssiTool.getNameChangeMap().containsKey(eachAp[0]))
				apentities.put(RssiTool.getNewName(eachAp[0]),Double.valueOf(eachAp[1]));
		}
		rpEntity.setPoints(apentities);
		RssiTool.changeAbsEntityToDivideRel(rpEntity);
		return rpEntity;
	}

	@Test
	public void getLoc(){
		RpEntity rpEntity = getTestRpEntity();
		bayesService.getLocByBayes(rpEntity,"1_2018-04-27-17:11:51_divide_rel",1);
		System.out.println(rpEntity.getLocString());
	}

	@Test
	public void getKMeansResult(){
		RpEntity rpEntity = getTestRpEntity();
		kMeansService.getRpKmeansGroupNum(rpEntity);
	}

	@Test
	public void getGroupPrecision(){
		String tabletName = "E:\\tablet_mi_test\\tablet";
		String miName = "E:\\tablet_mi_test\\mi";
		String absTable ="1_2018-04-27-17:11:51";
		String divideTable ="1_2018-04-27-17:11:51_divide_rel";
		String minusTable ="1_2018-04-27-17:11:51_minus_rel";

//		getPrecision(tabletName,absTable,1,0,3);
//		getPrecision(tabletName,absTable,2,0,5);

		System.out.println("tablet abs bayes");
		for (int i = 1; i < 10; i++) {
			getPrecision(tabletName,absTable,2,0,i);
		}
//
//		System.out.println("mi abs bayes");
//		for (int i = 1; i < 10; i++) {
//			getPrecision(miName,absTable,2,0,i);
//		}
//
//		System.out.println("tablet divide knn");
//		for (int i = 1; i < 10; i++) {
//			getPrecision(tabletName,divideTable,1,2,i);
//		}
//
//		System.out.println("tablet divide bayes");
//		for (int i = 1; i < 10; i++) {
//			getPrecision(tabletName,divideTable,2,2,i);
//		}
//
//		System.out.println("mi divide knn");
//		for (int i = 1; i < 10; i++) {
//			getPrecision(miName,divideTable,1,2,i);
//		}
//
//		System.out.println("mi divide bayes");
//		for (int i = 1; i < 10; i++) {
//			getPrecision(miName,divideTable,2,2,i);
//		}
	}


	public void getPrecision(String filename, String tablename, int algo, int dataSrc,int candidateNum){
//		long startTime = System.currentTimeMillis();    //获取开始时间

		List<String> fileList = FileTool.traverseFolder(filename);
		List<String> locStrings = getCNNPointLoc();
		int rpCurCount = 1;
		double dif_x = 0;
		double dif_y = 0;
		double difSum_x = 0;
		double difSum_y = 0;
		int count = 1;
		for (int j = 0; j < fileList.size(); j++){
			List<String> eachPointData = datasourceService.getRssiFromTxt(fileList.get(rpCurCount-1),1,20);
//			System.out.println(fileList.get(rpCurCount-1));
			int curFileCount=1;
			for(String str : eachPointData){
				RpEntity rpEntity = new RpEntity();
				HashMap<String,Double> apentities = new HashMap<>();
				String[] eachRpSet = str.split(";");
				for (int i = 0; i < eachRpSet.length; i++) {
					String[] eachAp = eachRpSet[i].split(" ");
					if (RssiTool.getNameChangeMap().containsKey(eachAp[0]))
					apentities.put(RssiTool.getNewName(eachAp[0]),Double.valueOf(eachAp[1]));
				}
				rpEntity.setPoints(apentities);

//				kMeansService.getRpKmeansGroupNum(rpEntity);
				if (dataSrc ==1)RssiTool.changeAbsEntityToMinusRel(rpEntity);
				if (dataSrc ==2)RssiTool.changeAbsEntityToDivideRel(rpEntity);
				if (algo ==1)knnService.getLocByKnn(rpEntity,tablename,candidateNum);
				if (algo ==2)bayesService.getLocByBayes(rpEntity,tablename,candidateNum);

				String [] locxy = locStrings.get(j).split(" ");
				dif_x = Math.abs((rpEntity.getX() - 12735839)*Math.pow(10,6)-Integer.valueOf(locxy[0]));
				dif_y = Math.abs((rpEntity.getY()-3569534)*Math.pow(10,6)-Integer.valueOf(locxy[1]));
//				System.out.println(count +" " +curFileCount + " " +dif_x/Math.pow(10,6)+" "+dif_y/Math.pow(10,6));
				count++;
				curFileCount++;
				difSum_x+=dif_x;
				difSum_y+=dif_y;
			}
			rpCurCount++;
		}
		System.out.println((int)(difSum_x/20/34)/Math.pow(10,6));
		System.out.println((int)(difSum_y/20/34)/Math.pow(10,6));

//		long endTime = System.currentTimeMillis();    //获取结束时间
		System.out.println(candidateNum);
//		System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间

	}

	@Test
	public void setPointLoc(){
		//x-12735839,y-3569534
		//5.3/5.58m*1000000

		pointLocationService.setPointLoc();
	}

	@Test
	public void insertPointLocation(){
		String filename = "E:\\JavaWebProject\\FMMap_web\\src\\main\\resources\\static\\data\\projectSrc\\Point_Location.txt";
		pointLocationService.insertPointLocation(filename);
	}

	@Test
	public void setCNNPointLoc() {

		for (int i = 1; i <=5; i++) {
			System.out.print(i+ " ");
			System.out.println(22312057 + (i-1)*922116 + " " +8976347);
		}
		for (int i = 6; i <=10; i++) {
			System.out.print(i+ " ");
			System.out.println(26000521 - (i-6)*922116 + " " +11544702);
		}

		System.out.println(11 +" " + 8484650 + " " +9722775);
		System.out.println(12 +" " + 8484650 + " " +10994549);
		System.out.println(13 +" " + 9293809 + " " +9722775);
		System.out.println(14 +" " + 9293809 + " " +10994549);

		System.out.println(15 +" " + 9946986 + " " +9722775);
		System.out.println(16 +" " + 9946986 + " " +10570625);
		System.out.println(17 +" " + 10756145 + " " +9722775);
		System.out.println(18 +" " + 10756145 + " " +10570625);

		System.out.println(19 +" " + 11565305 + " " +10146700);
		System.out.println(20 +" " + 11565305 + " " +10994549);
		System.out.println(21 +" " + 12374464 + " " +10146700);
		System.out.println(22 +" " + 13037390 + " " +10146700);

		int flag =0;
		for (int i = 23; i <= 34; i++) {
			System.out.print(i+ " ");
			flag = i%2;
			if (flag == 1) System.out.println(1620822 + (i-23)*1365237 + " " +6631515);
			else System.out.println(2986059 + (i-24)*1365237 + " " +7246899);
		}
	}

	public List<String> getCNNPointLoc(){
		String filename = "E:\\tablet_mi_test\\Test_Location.txt";
		List<String> locStrings = new ArrayList<>();
		try {
			FileReader reader = new FileReader(filename);
			BufferedReader br = new BufferedReader(reader);
			String str = br.readLine();
			while (str != null) {
				String[] eachRpSet = str.split(" ");
				locStrings.add(eachRpSet[1]+" "+eachRpSet[2]);
				str = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return locStrings;
	}

	@Test
	public void getCnnSrc(){
		String filename = "E:\\tablet_mi_test\\mi";
		List<String> locStrings = getCNNPointLoc();
		List<String> fileList = FileTool.traverseFolder(filename);
		HashMap<String,String> newName =  RssiTool.getCNNNameChangeMap();

		int curFile = 0;
		for(String file : fileList){
			HashMap<String,Integer> rss =  new HashMap<>();
			try {
				FileReader reader = new FileReader(file);
				BufferedReader br = new BufferedReader(reader);
				br.readLine();
				String str = br.readLine();
				while (str != null) {
					String[] eachRpSet = str.split(";");
					for (int i = 0; i < eachRpSet.length; i++) {
						String[] eachAp = eachRpSet[i].split(" ");
						rss.put(RssiTool.getNewName(eachAp[0]),Integer.valueOf(eachAp[1]));
					}
					for (String name: newName.values()) {
						if (rss.containsKey(name)) System.out.print(rss.get(name)+" ");
						else System.out.print(0+" ");
					}
					System.out.println(locStrings.get(curFile));
//					System.out.println(curFile);
					br.readLine();
					br.readLine();
					str = br.readLine();
				}
				br.close();
				curFile++;
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

	@Test
	public void startClustering(){
		kMeansService.startClustering("1_2018-04-27-17:11:51");
	}

	@Test
	public void testKmeans(){
		List<String> allPointNames;
		allPointNames= kMeansMapper.getPointNameByCoreNum(RssiTool.tableName+"_type",0);
		System.out.println(allPointNames);
	}

	@Test
	public void getBitmap(){
		String filename = "D:\\11.txt";
		try {
			FileReader reader = new FileReader(filename);
			BufferedReader br = new BufferedReader(reader);
			String str = br.readLine();
//			Bitmap bitmap = FileTool.convertStringToIcon(str);
//			System.out.println(bitmap);
			br.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
