package com.tgl.springboot01cache;

import com.tgl.springboot01cache.bean.Employee;
import com.tgl.springboot01cache.mapper.EmployeeMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class Springboot01CacheApplicationTests {

    @Autowired
    EmployeeMapper employeeMapper;

    //RedisAutoConfiguration 中如果没有什么类，就会自动创建这2个template
    //jdbc 也有jdbctemplate 原理都是相同的
    @Autowired
    StringRedisTemplate stringRedisTemplate;//k-v 都是字符串的 操作字符串的

    @Autowired
    RedisTemplate redisTemplate;//k-v 都是对象的

    @Autowired
    RedisTemplate<Object,Employee> empredisTemplate;

    @Test
    void contextLoads() {
        Employee empById = employeeMapper.getEmpById(1);
        System.out.println(empById);
    }

    /**
     * redis 的基本操作
     * redis 常见的五大数据类型
     *  string(字符创) list(列表) set(集合) hash(散列) zset(有序集合)
     *  stringRedisTemplate.opsForValue() 操作字符串的
     *  stringRedisTemplate.opsForHash() 操作hash的
     *  ....
     */
    @Test
    public void testredis(){
        //给redis保存数据
        //stringRedisTemplate.opsForValue().append("msg","hello");
//        String msg = stringRedisTemplate.opsForValue().get("msg");
//        System.out.println(msg);
        stringRedisTemplate.opsForList().leftPush("mylist","1");
        stringRedisTemplate.opsForList().leftPush("mylist","w");
    }

    //测试保存对象
    @Test
    public void testredis2(){
        Employee empById = employeeMapper.getEmpById(1);
        //默认如果保存对象，使用JDK序列化机制，序列化后的数据保存到redis,在源码中可以看到redisTemplate的默认的序列化器
        //是 JDKSerializationRedisSerializer
        //redisTemplate.opsForValue().set("emp-01",empById);//报错，说需要序列化，将Employeee对象序列化
        // implements Serializable
        //会发现，一串字母，都是序列化的结果
        //1.可以将数据以json的方式保存
            //（1）自己将对象转为json
        //改变redis 序列化规则
        empredisTemplate.opsForValue().set("emp-02",empById);
    }
}
