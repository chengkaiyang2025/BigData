# 使用virtualbox安装hadoop集群

## 前言

本文件夹主要保存在Ubuntu环境下，使用virtualbox配置虚拟机、安装hadoop集群的过程。用于后续yarn学习中调试参数，配置资源用。

## 第一部分 配置标准虚拟机

物理机环境：Linux Mint 19.3 Tricia、Virtual Box 6.1、16g内存、110g固态硬盘，796g机械硬盘。

虚拟机环境：CentOS-7-x86_64_Minimal_1908、2g内存 4核、硬盘50g。

### 第一步：新建虚拟机

首先分配内存与磁盘分别为2g和50g。

然后将CentOS-7-x86_64_Minimal_1908的镜像导入到虚拟机中进行安装。

注意在安装的时候必须配置好网络、root的密码等。

### 第二步：配置用户、网络、jdk

1、安装必要的软件包。

```shell
yum install -y epel-release 
yum install -y net-tools
yum install -y vim

```

2 、关闭防火墙。

```shell
systemctl stop firewalld
systemctl disable firewalld.service
```

3、将atguigu放入sudoers中

```shell
vim  /etc/sudoers
## Allow root to run any commands anywhere
root = ALL=(ALL) ALL
## Allows people in group wheel to run all commands
%whell ALL=(ALL) ALL
atguigu ALL=(ALL) NOPASSWD:ALL

## 注意:atguigu 这一行不要直接放到 root 行下面,因为所有用户都属于 wheel 组,你先
## 配置了 atguigu 具有免密功能,但是程序执行到%wheel 行时,该功能又被覆盖回需要
##密码。所以 atguigu 要放到%wheel 这行下面。

```

4、创建目录

```shell
mkdir /opt/module
mkdir /opt/software
 chown atguigu:atguigu /opt/module
 chown atguigu:atguigu /opt/software
```

5、关闭机器，**新建桥接网卡**，保证物理机能ping通虚拟机。

6、修改虚拟机主机名称

```shell
vim /etc/hosts
添加如下内容
172.37.4.198 hadoop100
172.37.4.201 hadoop101
172.37.4.202 hadoop102
```

修改物理机的配置文件

```shell
vim /etc/hosts
```

### 第三步、安装jdk

**以下开始使用atguigu用户操作。**

1、安装前保证卸载了jdk。然后在[Oracle官网](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)上下载jdk,如jdk-8u291-linux-64.tar.gz。

2、首先将jdk放入 /opt/software

```shell
[atguigu@hadoop102 ~]$ ll /opt/software
jdk-8u291-linux-x64.tar.gz
```

3、然后解压

```shell
tar -zxvf jdk-8u291-linux-x64.tar.gz -C /opt/module
```

4、配置jdk环境变量

```shell
# 新建/etc/profile.d/my_env.sh
sudo vim /etc/profile.d/my_env.sh
## 添加如下内容
# JAVA_HOME
export JAVA_HOME=/opt/module/jdk1.8.0_291
export PATH=$PATH:$JAVA_HOME/bin
## source一下/etc/profile文件，让新的环境变量PATH生效
source /etc/profile
## 验证jdk安装成功
java -version
```

### 第四步、安装hadoop

**使用atguigu的用户进行以下操作**

1、下载[hadoop安装包](https://archive.apache.org/dist/hadoop/common/hadoop-3.1.3/)

2、解压

```shell
## 进入到hadoop安装包下。
cd /opt/software
## 解压到module下
tar -zxvf hadoop-3.1.3.tar.gz -C /opt/module

```

3、将hadoop添加到环境变量中

```shell
## 获取hadoop的安装路径
pwd
## 打开/etc/profile.d/my_env.sh文件
# HADOOP_HOME
export HADOOP_HOME=/opt/module/hadoop-3.1.3
export PATH=$PATH:$HADOOP_HOME/bin
export PATH=$PATH:$HADOOP_HOME/sbin
## 让文件生效
source /etc/profile
## 验证是否安装成功
hadoop version
## 重启
reboot


```

2)重要目录
(1)bin 目录:存放对 Hadoop 相关服务(hdfs,yarn,mapred)进行操作的脚本
(2)etc 目录:Hadoop 的配置文件目录,存放 Hadoop 的配置文件
(3)lib 目录:存放 Hadoop 的本地库(对数据进行压缩解压缩功能)
(4)sbin 目录:存放启动或停止 Hadoop 相关服务的脚本
(5)share 目录:存放 Hadoop 的依赖 jar 包、文档、和官方案例

## 第二部分 Hadoop运行模式

### 第一步 本地运行模式

1、准备文件

```shell
# 进入工作目录
cd /opt/module/hadoop-3.1.3
# 创建目录
mkdir wcinput
cd wcinput
# 编辑文件
vim word.txt
hadoop yarn
hadoop mapreduce
atguigu
atguigu

```

2、运行wc程序

```shell
## 回到/opt/module/hadoop-3.1.3
cd /opt/module/hadoop-3.1.3
hadoop jar share/hadoop/mapreduce/hadoop-mapreduce-examples-3.1.3.jar wordcount wcinput wcoutput
cat wcoutput/part-r-00000 

```



### 第二步 分布式运行模式

1、了解scp、rsync等远程同步工具。

2、编写xsync集群脚本。

```shell
## 在/home/atguigu/bin目录下创建xsync文件。
cd /home/atguigu
mkdir bin
cd bin
vim xsync
```

在文件中编写如下代码：

```shell
#!/bin/bash
#1. 判断参数个数
if [ $# -lt 1 ]
then
    echo Not Enough Arguement!
    exit;
fi
#2. 遍历集群所有机器
for host in hadoop100 hadoop101 hadoop102
do
    echo ==================== $host ====================
    #3. 遍历所有目录,挨个发送
    for file in $@
    do
        #4. 判断文件是否存在
        if [ -e $file ]
            then
                #5. 获取父目录
                pdir=$(cd -P $(dirname $file); pwd)
                #6. 获取当前文件的名称
                fname=$(basename $file)
                ssh $host "mkdir -p $pdir"
                rsync -av $pdir/$fname $host:$pdir
            else
                echo $file does not exists!
        fi
    done
done
```

``` shell
## 修改脚本xsync具有执行权限
[atguigu@hadoop102 bin] $ chmod +x xsync
## 测试脚本
[atguigu@hadoop102 bin] $ xsync /home/atguigu/bin
## 将脚本复制到/bin中，便于全局调用
sudo cp xsync /bin/
[atguigu@hadoop102 ~]$ sudo ./bin/xsync /etc/profile.d/my_env.sh
[atguigu@hadoop103 bin]$ source /etc/profile
[atguigu@hadoop104 opt]$ source /etc/profile
```











```
hadoop100:172.37.4.198
```

