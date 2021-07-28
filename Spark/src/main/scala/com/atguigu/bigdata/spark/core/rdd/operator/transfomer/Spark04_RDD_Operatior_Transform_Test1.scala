package com.atguigu.bigdata.spark.core.rdd.operator.transfomer

import org.apache.spark.{SparkConf, SparkContext}

object Spark04_RDD_Operatior_Transform_Test1 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("test")
    val sc = new SparkContext(conf)
    val rdd = sc.makeRDD(List("this is a", "this is b", "this is c"))
    val re = rdd.flatMap(_.split(" ")).map(t => {
      (t, s"word's length is ${t.length}")
    })
    re.collect().foreach(println)
    sc.stop()
  }
}
