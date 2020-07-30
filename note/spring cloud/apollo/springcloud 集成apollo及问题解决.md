springboot 集成apollo及问题解决

## 1.springboot集成apollo

**代码改造**

1. 搭建本地apollo

   参见apollo的github
   https://github.com/ctripcorp/apollo/wiki/Quick-Start

   

2. springboot代码改造

   a.修改pom.xml文件,新增依赖

   ```xml
   <dependency>
       <groupId>com.ctrip.framework.apollo</groupId>
       <artifactId>apollo-client</artifactId>
       <version>1.6.2</version>
   </dependency>
   ```

   b.在启动类添加注解@EnableApolloConfig

   ```java
   @EnableApolloConfig
   @EnableSwagger2
   @EnableEurekaClient
   @MapperScan(basePackages = "com..stock.dao")
   @SpringBootApplication
   public class StockApplication {
   
       public static void main(String[] args) {
           SpringApplication.run(StockApplication.class, args);
       }
   
   }
   ```

   c.修改配置文件application.yml

   ```yml
   #apollo配置信息
   app:
     id: java-test #项目名称
   apollo:
     meta: http://localhost:8080 #这个是mata server的地址,client通过它获取config server地址
     bootstrap:
       namespace: application 
       enable: true # 设置在应用启动阶段就加载 Apollo 配置
       eagerLoad:
         enabled: true #是否将 Apollo 配置加载提到初始化日志系统之前，需要托管日志配置时开启
   ```

   d.系统环境的配置C:\opt\settings\server.properties，在不同环境中制定获取不同环境的配置信息

   对于Mac/Linux，文件位置为 /opt/settings/server.properties
   对于Windows，文件位置为 C:\opt\settings\server.properties

   `env=dev`

到此为止，本地配置完成。

**本地测试**

1. 在apollo-protral中添加自己的项目java-test

2. 添加配置信息,并发布

   `my.stockNum = 130`

3. 修改代码获取变量

```java
@Api
@RestController
@RequestMapping(value = "/stock")
public class StockController implements StockFeignService {
	
    //获取apollo配置信息
    @Value(value = "${my.stockNum}")
    private String stockNum;

    @Value(value = "${spring.profiles.active}")
    private String env;

    @ApiOperation(value = "测试stock接口")
    @GetMapping(value = "/getStock")
    public String getStock(){
        return env+"  "+stockNum;
    }

}
```

注意：代码本地的配置文件中也需要声明变量默认值，要不然springboot启动报错

4.测试调用接口获取配置信息



### 2.问题解决

1. apollo部署到阿里云之后连接失败
   以上配置信息在本地测试是没有问题的，但是在apollo上到阿里云的时候，就报错了，一直无法成功连接config-server。

日志如下：

```log
2020-07-29 18:58:15.601  INFO 3852 --- [           main] c.c.f.f.i.p.DefaultApplicationProvider   : App ID is set to java-test by app.id property from System Property
2020-07-29 18:58:15.604  INFO 3852 --- [           main] c.c.f.f.i.p.DefaultServerProvider        : Loading C:\opt\settings\server.properties
2020-07-29 18:58:15.607  INFO 3852 --- [           main] c.c.f.f.i.p.DefaultServerProvider        : Environment is set to [dev] by property 'env' in server.properties.
2020-07-29 18:58:15.774  INFO 3852 --- [           main] o.s.cloud.context.scope.GenericScope     : BeanFactory id=a94c1aec-610e-3a1d-96fc-91863f040dea
2020-07-29 18:58:15.814  INFO 3852 --- [           main] c.c.f.a.i.DefaultMetaServerProvider      : Located meta services from apollo.meta configuration: http://xxx.xxx.com/!
2020-07-29 18:58:15.818  INFO 3852 --- [           main] c.c.f.apollo.core.MetaDomainConsts       : Located meta server address http://xxx.xxx.com/ for env DEV from com.ctrip.framework.apollo.internals.DefaultMetaServerProvider
2020-07-29 18:58:17.180  WARN 3852 --- [           main] c.c.f.a.i.RemoteConfigRepository         : Load config failed, will retry in 1 SECONDS. appId: java-test, cluster: default, namespaces: application
2020-07-29 18:58:19.183  WARN 3852 --- [           main] c.c.f.a.i.RemoteConfigRepository         : Load config failed, will retry in 1 SECONDS. appId: java-test, cluster: default, namespaces: application
2020-07-29 18:58:21.185  WARN 3852 --- [           main] c.c.f.a.i.RemoteConfigRepository         : Load config failed, will retry in 1 SECONDS. appId: java-test, cluster: default, namespaces: application
2020-07-29 18:58:23.187  WARN 3852 --- [           main] c.c.f.a.i.RemoteConfigRepository         : Load config failed, will retry in 1 SECONDS. appId: java-test, cluster: default, namespaces: application

```

细心的同学都会发现，其实apollo配置app.id、env的配置信息mate server配置信息都起作用了，但是却无法连接config server，抓包发现请求config server地址使用的是阿里云的内网地址，config server地址并没有对外网开放，所以无法直接访问

后来看过apollo的源码之后，解决问题的方法很简单，只需要在server.properties中添加一个配置信息，明确制定config-server的url

`apollo.configService=http://xxx.xxxxx.com/`

源码分析：`ConfigServiceLocator.getCustomizedConfigService`

```java
//当前方法提供了两种方法获取config server地址的方式，
//并且这个方法运行在获取mate server地址方法之前，如果在这里获取到config server地址之后就不再主动获取mate server地址
//注意：区别于mate server的三种地址配置方式，mate server可以使用yml配置
private List<ServiceDTO> getCustomizedConfigService() {
    	//方式1：启动时添加启动参数
        String configServices = System.getProperty("apollo.configService");
        if (Strings.isNullOrEmpty(configServices)) {
            configServices = System.getenv("APOLLO_CONFIGSERVICE");
        }
		//方式2：通过server.properties配置
        if (Strings.isNullOrEmpty(configServices)) {
            configServices = Foundation.server().getProperty("apollo.configService", (String)null);
        }

        if (Strings.isNullOrEmpty(configServices)) {
            return null;
        } else {
            logger.warn("Located config services from apollo.configService configuration: {}, will not refresh config services from remote meta service!", configServices);
            String[] configServiceUrls = configServices.split(",");
            List<ServiceDTO> serviceDTOS = Lists.newArrayList();
            String[] arr$ = configServiceUrls;
            int len$ = configServiceUrls.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String configServiceUrl = arr$[i$];
                configServiceUrl = configServiceUrl.trim();
                ServiceDTO serviceDTO = new ServiceDTO();
                serviceDTO.setHomepageUrl(configServiceUrl);
                serviceDTO.setAppName("apollo-configservice");
                serviceDTO.setInstanceId(configServiceUrl);
                serviceDTOS.add(serviceDTO);
            }

            return serviceDTOS;
        }
```

下面是apollo的架构设计图

![image-20200729185248263](F:\ideaworkspace\study\note\picture\apollo架构.jpg)



