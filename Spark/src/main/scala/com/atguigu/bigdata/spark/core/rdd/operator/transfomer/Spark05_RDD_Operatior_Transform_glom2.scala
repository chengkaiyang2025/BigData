package com.atguigu.bigdata.spark.core.rdd.operator.transfomer

import org.apache.spark.{SparkConf, SparkContext}

object Spark05_RDD_Operatior_Transform_glom2 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("test")
    val sc = new SparkContext(conf)
    val rdd = sc.makeRDD(List(1, 2, 3, 4, 5), 2)
    println(rdd.glom().map(
      list => {
        println(list.max)
        list.max
      }
    ).collect().sum)
    sc.stop()
  }
}
