package com.tgl.springboot01cache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 一、搭建基本环境
 * 1.导入数据库文件 创建出department和employee类
 * 2.创建javabean封装数据
 * 3.整合Mybatis操作数据库
 *      1.配置数据源信息
 *      2.使用注解版的Mybatis
 *          1).@MapperScan指定需要扫描的接口所在的包
 * 二、快速体验缓存
 *      步骤：
 *          1.开启基于注解的缓存@EnableCaching
 *          2.标注缓存注解即可
 * @Cacheable @CacheEvict @Cacheput
 *
 * 默认使用的是ConcurrentMapCacheManager,将数据保存到Concurrent
 *
 * 三.整合redis作为缓存
 * 1.redis是一个开源（BSD许可的），内存中的数据结构存储系统，他可以用作数据库，缓存和消息中间件
 *      1.安装redis，使用docker
 *      2.引入redis的stater(找springboot starter中的spring data redis)
 *      3.配置redis
 *      4.测试缓存
 *          原理：CacheManager(默认是ConcurrentMapManager)帮我们创建Cache 缓存组件给缓存中存取数据
 *          1）引入redis的starter，容器中保存的是RedisCacheManager
 *          2) RedisCacheManager 帮我们创建RedisCache来作为缓存组件，RedisCache就是通过redis来存取数据的
 *          3) 默认保存数据k-v 都是object ;利用序列化来保存的,如何保存为Json
 *              1.引入了redis的starter,cacheManager变为redisCacheManager
 *              2.默认创建的RedisCacheManager 在操作redis的时候使用的是 RedisTemplate<Object,Object>
 *                  redis<Object,Object>默认使用jdk的序列化机制
 *           4）自定义CacheManager
 */
@MapperScan("com.tgl.springboot01cache.mapper")
@SpringBootApplication
@EnableCaching
public class Springboot01CacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(Springboot01CacheApplication.class, args);
    }

}
