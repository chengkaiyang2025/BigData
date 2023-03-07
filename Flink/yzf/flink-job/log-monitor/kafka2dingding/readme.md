### 打包：

```
mvn clean package
```



### 部署flink集群地址

```
http://115.159.51.208:2081/#/submit
```



### 运行参数：

```
 --Kafka_host 172.16.7.109:9092 --Kafka_topic logstash_error_log  --alert_post_url http://172.16.7.109:8355/alert/ --window_time_minutes 60
```

