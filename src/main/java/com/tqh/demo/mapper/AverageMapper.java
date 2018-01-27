package com.tqh.demo.mapper;

import com.tqh.demo.model.Average;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface AverageMapper {
    @Select("SELECT * FROM average WHERE samplePoint = #{samplePoint}")
    Average selectAverage(String samplePoint);

    @Insert("INSERT INTO average values(#{samplePoint},#{value1}," +
            "#{value2}," +
            "#{value3}," +
            "#{value4}," +
            "#{value5}," +
            "#{value6}," +
            "#{value7}," +
            "#{value8}," +
            "#{value9})")
    void InsertAverage(Average average);

    @Delete(" DELETE FROM average WHERE samplePoint= #{samplePoint}")
    void DeleteAverage(String samplePoint);
    @Select("SELECT * FROM average ")
    List<Average> selectAllAverage();
}
