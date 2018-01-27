package com.tqh.demo.mapper;

import com.tqh.demo.model.Users;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UsersMapper {
    @Select("SELECT * FROM USERS")
    List<Users> selectAll();


}
