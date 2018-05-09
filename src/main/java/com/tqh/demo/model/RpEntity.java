package com.tqh.demo.model;

import java.util.HashMap;

public class RpEntity {
    private Integer id;
    private Integer device_id;
    private HashMap<String, Double> points;
    private String point_name;
    private Double knnResult;
    private Double x;
    private int kmeansGroupNum;

    public int getKmeansGroupNum() {
        return kmeansGroupNum;
    }

    public void setKmeansGroupNum(int kmeansGroupNum) {
        this.kmeansGroupNum = kmeansGroupNum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDevice_id() {
        return device_id;
    }

    public void setDevice_id(Integer device_id) {
        this.device_id = device_id;
    }

    public HashMap<String, Double> getPoints() {
        return points;
    }

    public void setPoints(HashMap<String, Double> points) {
        this.points = points;
    }

    public String getPoint_name() {
        return point_name;
    }

    public void setPoint_name(String point_name) {
        this.point_name = point_name;
    }

    public Double getKnnResult() {
        return knnResult;
    }

    public void setKnnResult(Double knnResult) {
        this.knnResult = knnResult;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    private Double y;

    public String getLocString(){
        return this.getX()+","+this.getY();
    }
}
