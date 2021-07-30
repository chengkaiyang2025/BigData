package com.atguigu.bigdata.spark.core.rdd.operator.transfomer

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark01_RDD_Operator_Transform {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("transformer")
    val sc = new SparkContext(conf)
    val value:RDD[Int] = sc.makeRDD(Seq(1, 2, 3, 4))
    val value1 = value.map(_ * 2)
    val result:Array[Int] = value1.collect()
    result.foreach(println)
    sc.stop()
  }

}
