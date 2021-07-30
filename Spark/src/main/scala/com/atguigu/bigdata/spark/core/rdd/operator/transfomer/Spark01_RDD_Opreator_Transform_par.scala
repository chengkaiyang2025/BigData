package com.atguigu.bigdata.spark.core.rdd.operator.transfomer

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark01_RDD_Opreator_Transform_par {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("map")
    val sc = new SparkContext(conf)
    // 1. rdd的计算一个分区内的数据是一个一个执行逻辑
    //    只有前面一个数据全部的逻辑执行完毕后，才会执行下一个数据。
    //    分区内数据的执行是有序的。
    // 2. 不同分区数据计算是无序的。
    val input:RDD[Int] = sc.makeRDD(Seq(1, 2, 3, 4,5,6,7,8,9), 2)
    val mapRDD = input.map(
      t => {
        println(s"mapRDD======>${t}")
        t
      }
    )
    val mapRDD1 = mapRDD.map(
      t => {
        println(s"mapRDD1======>${t}")
        t
      }
    )
    val ints = mapRDD1.collect()
    sc.stop()
  }
}
