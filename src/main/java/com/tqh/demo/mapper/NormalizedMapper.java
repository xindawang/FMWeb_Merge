package com.tqh.demo.mapper;

import com.tqh.demo.model.Average;
import com.tqh.demo.model.Normalized;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;


@Repository
public interface NormalizedMapper {
    @Select("SELECT * FROM normalized WHERE samplePoint = #{samplePoint}")
    Normalized selectNormalized(String samplePoint);

    @Insert("INSERT INTO normalized values(#{samplePoint},#{value1}," +
            "#{value2}," +
            "#{value3}," +
            "#{value4}," +
            "#{value5}," +
            "#{value6}," +
            "#{value7}," +
            "#{value8}," +
            "#{value9})")
    void InsertNormalized(Normalized normalized);

    @Delete(" DELETE FROM normalized WHERE samplePoint= #{samplePoint}")
    void DeleteNormalized(String samplePoint);
}
