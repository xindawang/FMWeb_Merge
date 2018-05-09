package com.tqh.demo.mapper;

import com.tqh.demo.model.BayesArgsEntity;
import com.tqh.demo.model.PointLocation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KMeansMapper {

    @Update("CREATE TABLE `${upload_time}`\n" +
            "(\n" +
            "  fingerprint_id int(11),\n" +
            "  type int(11)\n" +
            ")")
    boolean createTypeTable(@Param(value="upload_time") String upload_time);

    @Update("CREATE TABLE `${upload_time}`\n" +
            "(\n" +
            "  id int(11),\n" +
            "${apString}\n"+
            ")")
    boolean createCoreTable(@Param(value="upload_time") String upload_time,
                        @Param(value="apString") String apString);

    @Insert("  INSERT INTO `${table_name}` (fingerprint_id,type) VALUES (#{fingerprint_id},#{type})")
    void insertType(@Param(value="table_name") String table_name,
                    @Param(value = "fingerprint_id") int fingerprint_id,
                             @Param(value = "type") double type);

    @Insert("update `${tableName}` set ${apNameAvg}=#{apAvg} where id = #{id}" )
    boolean insertCore(@Param("tableName") String tableName,@Param("id") int id,
                       @Param("apNameAvg") String apNameAvg,
                             @Param("apAvg") double apAvg);

    @Insert("insert into `${tableName}` (id) values (#{id})" )
    boolean insertCoreIndex(@Param("tableName") String tableName,@Param("id") int id);

    @Select("select distinct id from `${tableName}`")
    List<Integer> getAllCoreNum(@Param("tableName") String tableName);

    @Select("select ${apNameAvg} as apNameAvg from `${tableName}` where id = #{id}")
    BayesArgsEntity getEachApArgs(@Param("tableName") String tableName, @Param("apNameAvg") String apNameAvg,
                                  @Param("id") int id);
}
