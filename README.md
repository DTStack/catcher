## 概述
java性能采集工具。可采集进程内的内存(堆、非堆、直接）、cpu、句柄数、gc、日志(logback日志，分级别采集)，也支持采集自定义指标，并提供http方式访问数据。

## http服务
- /runtime：运行时参数
- /metrics：prometheus采集数据
- /threads：线程堆栈
- /logs/recent：最近日志
- /logs/error：最近错误日志

## 接入方式
#### 引入包
```
<dependency>
  <groupId>com.dtstack</groupId>
  <artifactId>catcher</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

#### 启动服务
```
String address = "localhost:19222";//自定义端口ip
NetServer server = new NetServer(address);
server.start();
```

#### 关闭服务
```
server.stop();
```

#### 如果需要采集日志，logback.xml的appender中加入LogFilter
```
<appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
    <!-- 此处加入LogFilter -->
    <filter class="com.dtstack.catcher.monitor.log.LogFilter"/>
    <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
        <charset>${LOG_CHARSET}</charset>
        <pattern>${LOG_PATTERN}</pattern>
    </encoder>
</appender>
```

#### 如果需要采集自定义指标，目前支持prometheus的counter方式和gauge方式
（1）counter方式
```
CounterMonitor.increase(String name, String labelNames, String labelValues, long delta);
```
（2）gauge方式
```
GaugeMonitor.set(String name, String labelNames, String labelValues, long value);
```


