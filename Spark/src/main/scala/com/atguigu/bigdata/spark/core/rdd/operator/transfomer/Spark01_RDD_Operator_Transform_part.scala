package com.atguigu.bigdata.spark.core.rdd.operator.transfomer

import org.apache.spark.{SparkConf, SparkContext}

object Spark01_RDD_Operator_Transform_part {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("save")
    val sc = new SparkContext(conf)
    val input = sc.makeRDD(Seq(1, 2, 3, 4), 2)
    input.saveAsTextFile("output/save/output")
    val result = input.map(_ * 2)
    result.saveAsTextFile("output/save/output1")
    sc.stop()
  }
}
