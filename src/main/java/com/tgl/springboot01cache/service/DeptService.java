package com.tgl.springboot01cache.service;

import com.tgl.springboot01cache.bean.Department;
import com.tgl.springboot01cache.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;

//@CacheConfig(cacheManager = "deptCacheManager")统一制定cacheManager
@Service
public class DeptService {

    @Autowired
    DepartmentMapper departmentMapper;

   // @Qualifier(value = "deptCacheManager") 因为我没有，所以用默认的
    @Autowired
    RedisCacheManager deptCacheManager; //但是有多个RedisCacheManager,所以用@Qulifier指定哪一个

    /**
     * 缓存的数据能存入redis
     * 第二次从缓存中查询就不能反序列化回来
     * 存的是dept的json数据，CacheManager默认使用RedisTemplate<Object,Employee>操作redis的，不过被我注释掉了，所以这只是视频中的
     *
     * @param id
     * @return
     */
//    @Cacheable(cacheNames = "dept" /*cacheManager = "deptCacheManager"*/)
//    public Department getDeptById(Integer id){
//        System.out.println("查询部门"+id);
//        Department department=departmentMapper.getDeptById(id);
//        return department;
//    }

    //使用缓存管理器得到缓存，进行api调用
    public Department getDeptById(Integer id){
        System.out.println("查询部门"+id);
        Department department=departmentMapper.getDeptById(id);
        //获取某个缓存
        Cache dept = deptCacheManager.getCache("dept");
        dept.put("dept:1",department);
        return department;
    }

}
