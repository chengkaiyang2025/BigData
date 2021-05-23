# 使用virtualbox安装hadoop集群

## 前言

本文件夹主要保存在Ubuntu环境下，使用virtualbox配置虚拟机、安装hadoop集群的过程。用于后续yarn学习中调试参数，配置资源用。

## 第一部分 配置标准虚拟机

物理机环境：Linux Mint 19.3 Tricia、Virtual Box 6.1、16g内存、110g固态硬盘，796g机械硬盘。

虚拟机环境：CentOS-7-x86_64_Minimal_1908、2g内存 4核、硬盘50g。

### 第一步：新建虚拟机

首先分配内存与磁盘分别为2g和50g。

然后将CentOS-7-x86_64_Minimal_1908的镜像导入到虚拟机中进行安装。

### 第二步：配置用户、网络、jdk

```shell
yum install -y epel-release 
yum install -y net-tools
yum install -y vim

```

