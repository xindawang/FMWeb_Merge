package com.tqh.demo.mapper;

import com.tqh.demo.model.BayesArgsEntity;
import com.tqh.demo.model.Datasource;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatasourceMapper {
    @Select("select * from datasource")
    List<Datasource> selectAll();


    @Update("CREATE TABLE `${upload_time}`\n" +
            "(\n" +
            "  id int(11)NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" +
            "  point_name VARCHAR(5),\n" +
            "  ap1_average DECIMAL(10,7),\n" +
            "  ap2_average DECIMAL(10,7),\n" +
            "  ap3_average DECIMAL(10,7),\n" +
            "  ap4_average DECIMAL(10,7),\n" +
            "  ap5_average DECIMAL(10,7),\n" +
            "  ap1_variance DECIMAL(10,7),\n" +
            "  ap2_variance DECIMAL(10,7),\n" +
            "  ap3_variance DECIMAL(10,7),\n" +
            "  ap4_variance DECIMAL(10,7),\n" +
            "  ap5_variance DECIMAL(10,7)\n" +
            ")")
    boolean createTable(@Param(value="upload_time") String upload_time);

    @Select("select count(id) from `${tableName}` where point_name = #{pointName}")
    boolean hasPoint(@Param("tableName") String tableName,@Param("pointName")String pointName);

    @Select("select distinct point_name from `${tableName}`")
    List<String> getAllPointName(@Param("tableName") String tableName);

    @Select("select ${apNameAvg} as apNameAvg,${apNameVar} as apNameVar from `${tableName}` where point_name = #{pointName}")
    BayesArgsEntity getEachApArgs(@Param("tableName") String tableName,@Param("apNameAvg") String apNameAvg, @Param("apNameVar") String apNameVar,
                                  @Param("pointName") String pointName);
    @Select("select * from datasource where id = #{id}")
    Datasource selectDatasource(int id);

    @Update("DROP TABLE `${table_name}`")
    boolean removeTable(@Param(value="table_name") String table_name);

    @Select("select ${apNameAvg} as apNameAvg from `${tableName}` where point_name = #{pointName}")
    BayesArgsEntity getEachApAvg(@Param("tableName") String tableName, @Param("apNameAvg") String apNameAvg, @Param("pointName") String pointName);

    @Insert("insert into `${tableName}` (point_name,${apNameAvg},${apNameVar}) "
            +"values (#{pointName},#{apAvg},#{apVar})")
    boolean insertAvrAndVar(@Param("tableName") String tableName,@Param("apNameAvg") String apNameAvg, @Param("apNameVar") String apNameVar,
                         @Param("pointName") String pointName, @Param("apAvg") double apAvg, @Param("apVar") double apVar);

    @Update("update `${tableName}` set ${apNameAvg}=#{apAvg},${apNameVar}=#{apVar}" +
            "where point_name = #{pointName}")
    boolean updateAvrAndVar(@Param("tableName") String tableName,@Param("apNameAvg") String apNameAvg,@Param("apNameVar") String apNameVar,
                            @Param("pointName") String pointName,@Param("apAvg") double apAvg,@Param("apVar") double apVar);
}
