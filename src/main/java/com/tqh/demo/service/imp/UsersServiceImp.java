package com.tqh.demo.service.imp;

import com.tqh.demo.mapper.UsersMapper;
import com.tqh.demo.model.Users;
import com.tqh.demo.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersServiceImp implements UsersService {
    @Autowired
    UsersMapper usersMapper;
    @Override
    public List<Users> selectAll() {

        return usersMapper.selectAll();
    }
}
