package com.atguigu.bigdata.spark.core.rdd.operator.transfomer

import org.apache.spark.{SparkConf, SparkContext}

/**
 *求每个班级最高身高的和与平均值
 */
object Spark17_RDD_Operatior_Transform_Test {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(conf)

    val rdd = sc.makeRDD(List(
      ("class1",190),("class2",187),("class1",178),("class2",160)
    ))
    rdd.aggregateByKey(0)(
      (x, y) => math.max(x ,y),
      (x, y) => x + y
    ).collect().foreach(println)
    sc.stop()

  }

}


