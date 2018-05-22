package com.tqh.demo.mapper;

import com.tqh.demo.model.UserLocation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
@Repository
public interface UserLocationMapper {
    @Select("SELECT * FROM userlocation")
    List<UserLocation> selectAll();

    @Select("SELECT * FROM userlocation where no=#{id}")
    UserLocation selectUser(String id);

    @Insert("insert into userlocation (device,x,y,saveTime) " +
            "values (#{device},#{x},#{y},#{saveTime})")
    boolean insertUserLocation(@Param("device") String device, @Param("x") int x,
                             @Param("y") int y,@Param("saveTime")String saveTime);

    @Update("update userlocation set x=#{x},y=#{y},saveTime=#{saveTime}" +
            "where device = #{device}")
    boolean updateUserLocation(@Param("device") String device, @Param("x") int x,
                             @Param("y") int y,@Param("saveTime")String saveTime);

    @Select("select * from userlocation where device = #{device} limit 1")
    Object userExists(@Param("device") String device);

    @Select("select userid from device where device = #{device}")
    Integer getUserId(@Param("device") String device);
}
