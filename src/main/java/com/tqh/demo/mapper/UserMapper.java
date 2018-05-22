package com.tqh.demo.mapper;

import com.tqh.demo.model.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    @Select("select * from user where userName=#{userName}")
    User selectUser(String userName);

    @Select("select * from user where id=#{user_id}")
    User selectUserById(int user_id);
}
