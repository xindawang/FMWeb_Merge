package com.tqh.demo.model;

public class PointLocation {
    private String point_name;
    private Integer x;
    private Integer y;

    public Double getBayesResult() {
        return bayesResult;
    }

    public void setBayesResult(Double bayesResult) {
        this.bayesResult = bayesResult;
    }

    private Double bayesResult;

    public String getPoint_name() {
        return point_name;
    }

    public void setPoint_name(String point_name) {
        this.point_name = point_name;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }
}
