package com.tgl.springboot01cache.mapper;

import com.tgl.springboot01cache.bean.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DepartmentMapper {

    @Select("select * from department where id= #{id}")
    Department getDeptById(Integer id);
}
