package com.tqh.demo.mapper;

import com.tqh.demo.model.PointLocation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface PointLocationMapper {
    @Insert("  INSERT INTO pointLocation(point_name, x, y) VALUES (#{pointName},#{x},#{y}) ")
    void insertPointLocation(@Param(value = "pointName") String pointName,
      @Param(value = "x")int x, @Param(value = "y")int y);

    @Select("select * from pointlocation where point_name=#{pointName}")
    PointLocation getPointLocation(String pointName);
}
