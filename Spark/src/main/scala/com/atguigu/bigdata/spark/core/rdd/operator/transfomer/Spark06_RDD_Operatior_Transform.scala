package com.atguigu.bigdata.spark.core.rdd.operator.transfomer

import org.apache.spark.{SparkConf, SparkContext}

object Spark06_RDD_Operatior_Transform {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(conf)

    val rdd = sc.makeRDD(List(1, 2, 3, 4, 5, 6))
    val re = rdd.groupBy(t => t % 2).map(
      t => {
        t
      }
    )
    re.collect().foreach(println)

    sc.stop()
  }
}
