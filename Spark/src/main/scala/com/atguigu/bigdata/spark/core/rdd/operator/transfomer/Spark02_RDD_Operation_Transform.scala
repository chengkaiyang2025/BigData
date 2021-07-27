package com.atguigu.bigdata.spark.core.rdd.operator.transfomer

import org.apache.spark.{SparkConf, SparkContext}

object Spark02_RDD_Operation_Transform {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("Spark02_RDD_Operation_Transform")
    val sc = new SparkContext(conf)
    val inputs = sc.makeRDD(Seq(1, 2, 3, 4))
    val value = inputs.mapPartitions(
      t => {
        t.map(_ * 2)
      }
    )
    value.collect().foreach(println)
    sc.stop()
  }
}
