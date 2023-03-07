1、创建hadoop用户，因为远程写腾讯云hive时候，会有ranger的权限校验。因此不能使用flink。
```shell
# 在三台机器上新建用户
adduser hadoop
passwd hadoop
```
```shell

yum install -y epel-release 
yum install -y net-tools
yum install -y nc
yum install -y vim
```
2、配置免密
```shell
# source一下/etc/profile文件，让新的环境变量PATH生效
source /etc/profile
172.16.190.27 vm-190-27-centos flink1
172.16.0.12 vm-6-42-centos flink2
172.16.190.38 vm-190-38-centos flink3
```
同时一定要保证 hostname 为flink1、flink2、flink3。否则使用./bin/sql-client.sql embedded的时候会报错。
3、配置flink用户免密码
切换到flink用户
```shell
## 生成公钥和私钥
mkdir ~/.ssh
cd ~/.ssh
ssh-keygen -t rsa
## 拷贝
ssh-copy-id flink1
ssh-copy-id flink2
ssh-copy-id flink3
```
如果遇到免密问题，参考下面链接
https://blog.csdn.net/lisongjia123/article/details/78513244
chmod 700 /home/hadoop/.ssh
chmod 600 /home/hadoop/.ssh/authorized_keys
安装jdk rpm -i /tmp/jdk-8u301-linux-x64.rpm
3、配置环境变量。
```shell
# 新建/etc/profile.d/my_env.sh
sudo vim /etc/profile.d/my_env.sh
## 添加如下内容
# JAVA_HOME
export JAVA_HOME=/usr/java/jdk1.8.0_301-amd64
export PATH=$PATH:$JAVA_HOME/bin

## 验证jdk安装成功
java -version
echo $JAVA_HOME
```

4、创建目录
/home/hadoop/flink-1.12.4
创建目录/data/flink2用于保存保存点、jar包等，创建/data/flink/emr_hive_conf/conf,并将线上的hive-site.xml放入此目录下，用于与集群的hive关联。
scp /home/hadoop/flink-1.12.4 flink1:/home/hadoop/flink-1.12.4

5、同步配置目录
scp flink-conf.yaml flink2:/home/hadoop/flink-1.12.4/conf/flink-conf.yaml
scp masters flink2:/home/hadoop/flink-1.12.4/conf/masters
scp workers flink2:/home/hadoop/flink-1.12.4/conf/workers
scp sql-client-defaults.yaml flink2:/home/hadoop/flink-1.12.4/conf/sql-client-defaults.yaml

scp flink-conf.yaml flink3:/home/hadoop/flink-1.12.4/conf/flink-conf.yaml
scp masters flink3:/home/hadoop/flink-1.12.4/conf/masters
scp workers flink3:/home/hadoop/flink-1.12.4/conf/workers
scp sql-client-defaults.yaml flink3:/home/hadoop/flink-1.12.4/conf/sql-client-defaults.yaml

6、前台启动调试
./bin/jobmanager.sh start-foreground
./bin/taskmanager.sh start-foreground

启动sql gateway
nohup ./bin/sql-gateway.sh 2>&1 &
7、直接启动
./bin/start-cluster.sh
备注：
以线上Flink配置为准，如果遇到问题，请先翻阅[gitlab issues](http://gitlab.yzf.net/group_di/bigdata/flink-job/issues?scope=all&utf8=%E2%9C%93&state=all)

https://blog.csdn.net/qq_27327261/article/details/109100219
https://www.geek-share.com/detail/2796479990.html


