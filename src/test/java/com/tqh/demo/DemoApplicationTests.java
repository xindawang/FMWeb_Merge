package com.tqh.demo;

import com.tqh.demo.model.PointLocation;
import com.tqh.demo.model.RpEntity;
import com.tqh.demo.service.BayesService;
import com.tqh.demo.service.DatasourceService;
import com.tqh.demo.service.KnnService;
import com.tqh.demo.service.PointLocationService;
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
	}

	@Test
	public void insertData(){
		String filename = "E:\\tablet_mi\\mi";
		datasourceService.insertDataFromTxt("2_2018-05-03-18:58:35",filename);
	}

	@Test
	public void getLoc(){
//		String rssi = "MERCURY_BD09 -75;Xiaomi_31CB_CE34 -56;MERCURY_B932 -51;abc3 -83;abc6 -57;abc8 -62;abc7 -65;Xiaomi_3525_CADA -34;abc4 -61;";
		String rssi = "\n" +
				"abc8 -39;MERCURY_CFF9 -69;MERCURY_B932 -62;abc7 -35;MERCURY_BD09 -64;Xiaomi_3525_CADA -47;abc4 -45;abc3 -53;Xiaomi_31CB_CE34 -61;abc6 -33;abc2 -67;";
		RpEntity rpEntity = new RpEntity();
		HashMap<String,Double> apentities = new HashMap<>();
		String[] eachRpSet = rssi.split(";");
		for (int i = 0; i < eachRpSet.length; i++) {
			String[] eachAp = eachRpSet[i].split(" ");
			apentities.put(RssiTool.getNewName(eachAp[0]),Double.valueOf(eachAp[1]));
		}
		rpEntity.setPoints(apentities);
		knnService.getLocByKnn(rpEntity,"1_2018-05-02-20:49:06",1);
		System.out.println(rpEntity.getLocString());
	}


	@Test
	public void getPrecision(){
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
		String filename = "E:\\tablet_mi\\mi";
		List<String> fileList = FileTool.traverseFolder(filename);
		int rpCurCount = 1;
		double dif_x = 0;
		double dif_y = 0;
		double difSum_x = 0;
		double difSum_y = 0;
		int count = 1;
		for (int j = 0; j < 130; j++){
			List<String> eachPointData = datasourceService.getRssiFromTxt(fileList.get(rpCurCount-1),91,100);
			System.out.println(fileList.get(rpCurCount-1));
			int curFileCount=1;
			for(String str : eachPointData){
				RpEntity rpEntity = new RpEntity();
				HashMap<String,Double> apentities = new HashMap<>();
				String[] eachRpSet = str.split(";");
				for (int i = 0; i < eachRpSet.length; i++) {
					String[] eachAp = eachRpSet[i].split(" ");
					apentities.put(RssiTool.getNewName(eachAp[0]),Double.valueOf(eachAp[1]));
				}
				rpEntity.setPoints(apentities);
//				knnService.getLocByKnn(rpEntity,"1_2018-05-03-14:37:33",5);
				knnService.getLocByKnn(rpEntity,"2_2018-05-03-18:58:35",1);
				PointLocation pointLocation = pointLocationService.getPointLocation(allPoints.get(j));
				dif_x = Math.abs((rpEntity.getX() - 12735839)*Math.pow(10,6)-pointLocation.getX());
				dif_y = Math.abs((rpEntity.getY()-3569534)*Math.pow(10,6)-pointLocation.getY());
				System.out.println(count +" " +curFileCount + " " +dif_x+" "+dif_y);
				count++;
				curFileCount++;
				difSum_x+=dif_x;
				difSum_y+=dif_y;
			}
			rpCurCount++;
		}
		System.out.println((int)(difSum_x/1300)/Math.pow(10,6));
		System.out.println((int)(difSum_y/1300)/Math.pow(10,6));
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
	public void getCnnSrc(){
		String filename = "E:\\tablet_mi\\tablet";
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
					System.out.println(curFile);
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

}
