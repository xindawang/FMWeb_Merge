package com.tqh.demo.mapper;

import com.tqh.demo.model.FingerPrint;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository
public interface FingerPrintMapper {
    @Insert("insert into fingerprint(table_name) values(#{table_name})")
    void insertFinger(String table_name);

    @Select("select * from fingerprint")
    List<FingerPrint> selectAll();

    @Delete("delete  from fingerprint where id=#{id}")
    void deleteFinger(int id);

    @Select("select * from fingerprint where id=#{id}")
    FingerPrint selectFinger(int id);
}
