package com.tgl.springboot01cache.service;

import com.tgl.springboot01cache.bean.Employee;
import com.tgl.springboot01cache.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

//@CacheConfig(cacheNames = "emps",cacheManager="empCacheManager")抽取缓存的公共配置
@Service
public class EmployeeService {

    @Autowired
    EmployeeMapper employeeMapper;

    /**
     * 将方法的运行结果进行缓存，以后要相同的数据，直接从缓存中获取，不用调用方法
     *
     * CacheManager管理多个cache组件的，对缓存的真正CRUD操作在Cache组建中，每一个缓存组件
     * 有自己惟一的一个名字，几个属性：
     *      cacheNames/value:指定缓存组建中的名字；将返回的结果放在哪个缓存中，是数组的方式，可以指定
     *      多个缓存
     *      key:缓存数据使用的key:可以用它来指定。默认是使用方法参数的值 1.方法的返回值
     *      编写SpEl: #id;参数id的值 等同于  #a0  #p0 #root.args[0]
     *      keyGenerator: key的生成器;可以自己指定key的生成器的组件id
     *          key/keyGennertator 二选一
     *      cacheManager: 指定缓存管理器：或者CacheResolver指定获取解析器(二选一)
     *      condition:指定符合条件的情况下才缓存;condition = "#a0>1" 第一个参数的值大于1才进行缓存
     *      unless:否定缓存，当unless指定的条件为true,方法的返回值就不会被缓存
     *          unless = "#result==null"，unless="#a0==2":如果第一个参数的值是2，结果不缓存
     *      sync:是否使用异步模式
     *原理：
     * 1.自动配置类：cacheAutoConfiguration
     * 2.缓存的配置类...有10条
     *   哪个配置类生效：SimpleCacheConfiguration
     * 4.给容器中注册一个CacheManager:ConcurrentCacheManager
     * 5.可以获取和创建ConcurrentMapCache类型的缓存组件；他的作用将数据保存在ConcurrentMap中
     *
     * 运行流程：
     * @cacheable:
     * 1.方法运行之前，先去查询cache(缓存组件），按照cacheNames指定的名字获取；
     *      （CacheManager先获取相应的缓存），第一次获取缓存如果没有Cache组件会自动创建
     * 2.取Cache中查找缓存的内容，使用一个key,默认就是方法的参数
     *    key是按照某种策略生成的，默认是使用keyGnerator生成的，默认使用SimpleKeyGenrator生成
     *    key;
     *    SimpleKeyGenrator生成key的默认策略：
     *      如果没有参数：key=new SimpleKey();
     *      如果有一个参数： key=new SimpleKey(params);
     * 3.没有查到缓存就调用目标方法
     * 4.将目标方法返回的结果，放进缓存中
     *
     * @Cacheable标注的方法执行之前先来检查缓存中有没有这个数据，默认按照参数的值作为key去
     * 查询缓存，如果没有就运行方法并将结果放入缓存，以后再来调用就可以直接使用换缓存中的数据
     *
     * 核心：
     *  1）使用CacheManager[ConcurrentMapCacheManager]按照名字得到Cache[ConcurrentMapCache]
     *  组件
     *  2）key使用keyGennerator生成的
     *
     * @param id
     * @return
     */
    @Cacheable(cacheNames = {"emps"}/*key="#root.methodName+'['+#id+']'",condition = "#a0>1"*/)
    public Employee getEmp(Integer id){
        System.out.println("查询"+id+"号员工");
        Employee empById = employeeMapper.getEmpById(id);
        return empById;
    }

    /**
     * @CachePut: 既调用方法，又更新缓存数据，同步更新缓存
     * 修改了数据库的某个数据，同时更新缓存
     *
     * 运行时机：
     * 1.先调用目标方法
     * 2.将目标方法的结果缓存起来
     *
     * 测试步骤
     * 1.查询1号员工,查到的结果会放到缓存中
     *      key: 1 value:lastName:小王
     * 2.以后的查询还是之前的结果
     * 3.更新1号员工：【lastName:zhangsan,gender:0]
     *      将方法的返回值也放进缓存了；
     *      key:传入的employee对象 值：返回的employee对象
     * 4.查询1号员工？
     *      应该是更新后的
     *          key="#employee.id":使用传入的参数的员工的id
     *          key="#resulet.id":使用返回后的id
     *              注意：@Cacheable的key是不能用#result的
     *      为什么是没更新前的？
     *
     */

    @CachePut(value="emps",key="#employee.id")
    public Employee updateEmp(Employee employee){
        System.out.println("updateEmp:"+employee);
        employeeMapper.updateEmp(employee);
        return employee;
    }

    /**
     * @CacheEvict : 缓存清除
     * key:指定要清除的数据
     * allEntries = true 指定清除当前这个缓存中所有数据
     * beforeInvocation=false : 缓存的清除是否在方法之前执行，默认是false
     *  缓存清除操作是在方法之后执行；如果出现异常，缓存就不会清除
     * beforeInvocation=true:
     *  代表清除缓存操作是在方法运行之前执行，无论方法是否出现异常，缓存都清除
     */

    @CacheEvict(value="emps",/*key="#id",*/allEntries = true,beforeInvocation = true)
    public void deleteEmp(Integer id){
        System.out.println("deleteEmp"+id);
       // employeeMapper.deleteEmpById(id);
        int i=10/0;
    }

    //定义复杂的缓存规则
    @Caching(
            cacheable = {
                    @Cacheable(value = "emps",key = "#lastName")
            },
            put = {
                    @CachePut(value = "emps",key="#result.id"),
                    @CachePut(value="emps",key="#result.email")
            }
    )
    public Employee getEmpByLastName(String lastName){
        return employeeMapper.getEmpByLastName(lastName);
    }
}
