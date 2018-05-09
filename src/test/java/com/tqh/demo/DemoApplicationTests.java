package com.tqh.demo;

import com.tqh.demo.model.KMeansEntity;
import com.tqh.demo.model.PointLocation;
import com.tqh.demo.model.RpEntity;
import com.tqh.demo.service.*;
import com.tqh.demo.util.FileTool;
import com.tqh.demo.util.RssiTool;
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

	@Test
	public void createNewTable(){
		String diviceId = "2";
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
		String recordDate = sDateFormat.format(new java.util.Date());
		datasourceService.createTable(diviceId+"_"+recordDate);
		kMeansService.createCoreTable(diviceId+"_"+recordDate+"_core");
		kMeansService.createTypeTable(diviceId+"_"+recordDate+"_type");
	}

	@Test
	public void insertData(){
		String filename = "E:\\tablet_mi\\tablet";
//		datasourceService.insertDataFromTxt("1_2018-04-27-17:11:51",filename);
		String coreFilename = "E:\\tablet_mi\\tabletCore.txt";
		kMeansService.insertCoreFromTxt("1_2018-04-27-17:11:51",coreFilename);
		String typeFilename = "E:\\tablet_mi\\tabletType.txt";
		kMeansService.insertTypeFromTxt("1_2018-04-27-17:11:51",typeFilename);
	}

	public RpEntity getTestRpEntity(){
		String rssi = "TP-LINK_115D -58;Xiaomi_3525_CADA -55;abc3 -61;MERCURY_BD09 -66;abc2 -62;abc7 -45;MERCURY_CFF9 -67;Xiaomi_31CB_CE34 -67;abc6 -38;abc4 -36;MERCURY_B932 -71;TP-LINK_1646 -61;TP-LINK_0236 -35;TP-LINK_3E5D -65;abc8 -43;";
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
		return rpEntity;
	}

	@Test
	public void getLoc(){
		RpEntity rpEntity = getTestRpEntity();
		bayesService.getLocByBayes(rpEntity,"1_2018-04-27-17:11:51",3);
		System.out.println(rpEntity.getLocString());
	}

	@Test
	public void getKMeansResult(){
		RpEntity rpEntity = getTestRpEntity();
		kMeansService.getRpKmeansGroupNum(rpEntity);
	}

	@Test
	public void getPrecision(){
		long startTime = System.currentTimeMillis();    //获取开始时间

		String filename = "E:\\tablet_mi_test\\tablet";
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
			System.out.println(fileList.get(rpCurCount-1));
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
//				knnService.getLocByKnn(rpEntity,"1_2018-05-03-14:37:33",5);
//				kMeansService.getRpKmeansGroupNum(rpEntity);
				knnService.getLocByKnn(rpEntity,"1_2018-04-27-17:11:51",4);
				String [] locxy = locStrings.get(j).split(" ");
				dif_x = Math.abs((rpEntity.getX() - 12735839)*Math.pow(10,6)-Integer.valueOf(locxy[0]));
				dif_y = Math.abs((rpEntity.getY()-3569534)*Math.pow(10,6)-Integer.valueOf(locxy[1]));
				System.out.println(count +" " +curFileCount + " " +dif_x/Math.pow(10,6)+" "+dif_y/Math.pow(10,6));
				count++;
				curFileCount++;
				difSum_x+=dif_x;
				difSum_y+=dif_y;
			}
			rpCurCount++;
		}
		System.out.println((int)(difSum_x/20/34)/Math.pow(10,6));
		System.out.println((int)(difSum_y/20/34)/Math.pow(10,6));

		long endTime = System.currentTimeMillis();    //获取结束时间
		System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间

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
		String filename = "E:\\tablet_mi_test\\tablet";
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

}
