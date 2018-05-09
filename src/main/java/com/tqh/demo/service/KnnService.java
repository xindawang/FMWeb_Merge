package com.tqh.demo.service;

import com.tqh.demo.mapper.DatasourceMapper;
import com.tqh.demo.model.BayesArgsEntity;
import com.tqh.demo.model.PointLocation;
import com.tqh.demo.model.RpEntity;
import com.tqh.demo.util.RssiTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class KnnService {
    @Autowired
    DatasourceMapper datasourceMapper;
    @Autowired
    PointLocationService pointLocationService;
    private int apAmount=15;
    private int k = 3;
    public void getLocByKnn(RpEntity rpEntity, String tableName, int kAmount){

        k = kAmount;

        //appoint the number of chosen AP point
//        rpEntity.setPoints(RssiTool.getNBiggestMap(rpEntity.getPoints(),10));

        //get from database
        List<RpEntity> rpList = getRssiEntityFromDatabase(tableName);
        //得到匹配度最高的k个rp
        RpEntity[] rpEntitiesH = getMinK(rpEntity,rpList);
        RpEntity[] rpEntities;
        rpEntities = rpEntitiesH;

        //把k个rp点一一对应到地图上的点坐标
        PointLocation[] pointLocEntities = getRelPointInfo(rpEntities);
        double sum = 0, x=0, y=0;

        //calculate the sum of k rp
        for (int i = 0; i <rpEntities.length ; i++) {
            if (rpEntities[i].getKnnResult()!= 0){
                sum += 1/rpEntities[i].getKnnResult();
            }
        }

        //get ratio and location info of each point and add up for the result
        for (int i = 0; i <rpEntities.length ; i++) {
            if (rpEntities[i].getKnnResult()!= 0) {
                double ratio = 1 / rpEntities[i].getKnnResult() / sum;
                int point_x = pointLocEntities[i].getX();
                int point_y = pointLocEntities[i].getY();
                x += ratio * point_x;
                y += ratio * point_y;
            }else {
                x = pointLocEntities[i].getX();
                y = pointLocEntities[i].getY();
                break;
            }
        }
        //convert the format of location info according to how it store into database
        double result_x = x/Math.pow(10,6) + 12735839;
        double result_y = y/Math.pow(10,6) + 3569534;
        rpEntity.setX(result_x);
        rpEntity.setY(result_y);
//        rpEntity.setLeftpx((int)Math.round(x));
//        rpEntity.setToppx((int)Math.round(y));
    }

    /**
     * 从数据库取出每一个采样点的AP均值、方差。返回指纹实体的集合
     */
    private List<RpEntity> getRssiEntityFromDatabase(String tableName) {
        List<RpEntity> rpEntities = new ArrayList<>();
        List<String> allPointNames = datasourceMapper.getAllPointName(tableName);
        for (String pointName : allPointNames) {
            RpEntity rpEntity = new RpEntity();
            HashMap<String, Double> apEntities = new HashMap<>();
            for (int i = 1; i <= apAmount; i++) {
                String apName =  "ap" + i;
                String avgName = "ap" + i + "_average";
                BayesArgsEntity eachAp = datasourceMapper.getEachApAvg(tableName,avgName, pointName);
                if (eachAp!=null)
                apEntities.put(apName,eachAp.getApNameAvg());
            }
            rpEntity.setPoints(apEntities);
            rpEntity.setPoint_name(pointName);
            rpEntities.add(rpEntity);
        }
        return rpEntities;
    }

    public RpEntity[] getMinK(RpEntity rpEntity, List<RpEntity> rpEntityList){
        //get rp info from database according to the given device_id

        //initialize big top heap
        int curNum = 0;
        RpEntity[] minK = new RpEntity[k];

        for (RpEntity singleRp : rpEntityList){
            double result = 0;
            int commonCount = 0;
            //result is sum of定位点的AP1-5与rp的AP1-5的偏差绝对值的开方
            for (String apName: rpEntity.getPoints().keySet()) {
                if (singleRp.getPoints().containsKey(apName)) {
                    result += Math.sqrt(Math.abs(singleRp.getPoints().get(apName) - rpEntity.getPoints().get(apName)));
                    commonCount++;
                }
            }
            //AP1-5平均偏差，越小匹配度越高
            if (commonCount>0){
                result /= commonCount;
            }
            singleRp.setKnnResult(result);
            //维护一个大小为k的大顶堆
            if(curNum < k){
                minK[curNum++] = singleRp;
                if (curNum == k) {
                    minK =maxHeapify(minK, 0, k-1);
                }
            }
            else if (singleRp.getKnnResult() < minK[0].getKnnResult()){
                minK[0] = singleRp;

                //sort the array by big top heap(using method of array)
                minK =maxHeapify(minK, 0, k-1);
            }
//            System.out.println(result);
        }
        return minK;
    }
    //similar to heap rank
    private RpEntity[] maxHeapify(RpEntity[] rpEntities, int index, int len){
        // 左子节点索引
        int li = (index << 1) + 1;
        // 右子节点索引
        int ri = li + 1;
        // 子节点值最大索引，默认左子节点。
        int cMax = li;

        // 左子节点索引超出计算范围，直接返回。
        if(li > len){
            return rpEntities;
        }
        // 先判断左右子节点，哪个较大。
        if(ri <= len && rpEntities[ri].getKnnResult() > rpEntities[li].getKnnResult()){
            cMax = ri;
        }
        if(rpEntities[cMax].getKnnResult() > rpEntities[index].getKnnResult()){
            RpEntity tmpRpEntity = rpEntities[index];
            rpEntities[index] = rpEntities[cMax];
            rpEntities[cMax] = tmpRpEntity;
            // 则需要继续判断换下后的父节点是否符合堆的特性。
            rpEntities = maxHeapify(rpEntities,cMax, len);
        }
        return rpEntities;
    }
    private PointLocation[] getRelPointInfo(RpEntity[] rpEntities) {
        PointLocation[] pointLocEntity = new PointLocation[k];
        int i = 0 ;
        for (RpEntity rpEntity :rpEntities) {
            pointLocEntity[i++]= pointLocationService.getPointLocation(rpEntity.getPoint_name());
        }
        return pointLocEntity;
    }
}
