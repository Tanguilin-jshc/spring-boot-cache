package com.tgl.springboot01cache.config;

import com.tgl.springboot01cache.bean.Department;
import com.tgl.springboot01cache.bean.Employee;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.net.UnknownHostException;
import java.util.List;

@Configuration //和RedisConfiguration 写法是一样的，根据它的运行机制，可以知道相当于如果重写
//这个Configuration ,那么启动器就会用我们的configuration进行场景配置,在RedisAutoConfiguration
//中参照RedisConfiguration
public class MyredisConfig {

    //把Object改成Employee 泛型变一下
    @Bean
    public RedisTemplate<Object, Employee> empredisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
        RedisTemplate<Object, Employee> template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        //设置序列化器
        Jackson2JsonRedisSerializer<Employee> serializer = new Jackson2JsonRedisSerializer<Employee>(Employee.class);
        template.setDefaultSerializer(serializer);//里面要传Redis
        //Serializer 这是一个接口，ctrl+h 这个RedisSerializer就可以看到它的一些实现类
        return template;
    }

    @Bean
    public RedisTemplate<Object, Department> deptredisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
        RedisTemplate<Object, Department> template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        //设置序列化器
        Jackson2JsonRedisSerializer<Department> serializer = new Jackson2JsonRedisSerializer<Department>(Department.class);
        template.setDefaultSerializer(serializer);//里面要传Redis
        //Serializer 这是一个接口，ctrl+h 这个RedisSerializer就可以看到它的一些实现类
        return template;
    }

    //CacheManagerCustomizers可以定制缓存的一些规则
    //参照RedisCacheConfiguration中生成的RedisCacheManager
    //@Primary 设置为默认cacheManager 不然会报错
//    @Bean
//    public RedisCacheManager employeeCacheManager(RedisTemplate<Object, Employee> empredisTemplate){//把上面的template传进去
//       // RedisCacheManager cacheManager=new RedisCacheManager(empredisTemplate);
        // key多了一个前缀
        //使用前缀，默认会将CacheName作为key的前缀
//        //cacheManager.setUserPrefix(true);
//       // return cacheManager; 视频中是这样的，之前版本的创建rediscacheManager 有这种构造方法
//        return null;
//    }

    //    @Bean
//    public RedisCacheManager deptCacheManager(RedisTemplate<Object, Department> deptredisTemplate){//把上面的template传进去
//       // RedisCacheManager cacheManager=new RedisCacheManager(deptredisTemplate);
    // key多了一个前缀
    //使用前缀，默认会将CacheName作为key的前缀
//        //cacheManager.setUserPrefix(true);
//       // return cacheManager; 视频中是这样的，之前版本的创建rediscacheManager 有这种构造方法
//        return null;
//    }
}
