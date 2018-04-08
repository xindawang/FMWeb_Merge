package com.tqh.demo.mapper;

import com.tqh.demo.model.Datasource;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatasourceMapper {
    @Select("select * from datasource")
    List<Datasource> selectAll();
}
