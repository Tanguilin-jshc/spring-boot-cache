package com.tgl.springboot01cache.mapper;

import com.tgl.springboot01cache.bean.Employee;
import org.apache.ibatis.annotations.*;

@Mapper
public interface EmployeeMapper {

    @Select("select * from employee where id=#{id}")
    public Employee getEmpById(Integer id);

    @Update("update employee set lastName=#{lastName},email=#{email},gender=#{gender}," +
            "d_id=#{dId} where id=#{id}")
    public void updateEmp(Employee employee);

    @Delete("delete from employee where id=#{id}")
    public void deleteEmpById(Integer id);

    @Insert("isnert into employee(lastName,emaile,gender,d_id)values(#{lastName},#{emaile}," +
            "#{gender},#{dId})")
    public void insertEmp(Employee employee);

    @Select("select * from employee where lastName=#{lastName}")
    Employee getEmpByLastName(String lastName);
}
