package com.tqh.demo.service;

import com.tqh.demo.mapper.UserMapper;
import com.tqh.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public User selectUser(String userName){
        return  userMapper.selectUser(userName);
    }

    public boolean checkPassWord(String userName,String passWord){
        User user=(User)selectUser(userName);
        if(user==null){
            return false;
        }
        return user.getPassWord().equals(passWord)?true:false;
    }

}
