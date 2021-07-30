package com.atguigu.bigdata.spark.core.rdd.operator.transfomer

import org.apache.spark.{SparkConf, SparkContext}

object Spark12_RDD_Operatior_Transform_Test2 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(conf)
    val rdd = sc.makeRDD(List((1,"a"),(-1,"c"),(2,"a")),2)
    rdd.sortBy(t => t._1).collect().foreach(println)
    sc.stop()
  }

}
