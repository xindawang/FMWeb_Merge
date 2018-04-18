package com.tqh.demo.mapper;

import com.tqh.demo.model.UserLocation;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserLocationMapper {
    @Select("SELECT * FROM userlocation")
    List<UserLocation> selectAll();

    @Select("SELECT * FROM userlocation where no=#{id}")
    UserLocation selectUser(String id);


}
