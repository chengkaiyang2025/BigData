package com.atguigu.bigdata.spark.core.rdd.operator.transfomer

import org.apache.spark.{SparkConf, SparkContext}

/**
 *切割日志，取出链接打印，并保存
 */
object Spark01_RDD_Operator_Transform_Test {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("Spark01_RDD_Operator_Transform_Test")
    val sc = new SparkContext(conf)
    val lines = sc.textFile("data/apache.log",10)
    val re = lines.map(
      t => {
        val s: Array[String] = t.split(" ")
        s(6)
      }
    )
    re.saveAsTextFile("output/Spark01_RDD_Operator_Transform_Test")
//    val re2 = re.collect()
//    re2.foreach(println)
    sc.stop()
  }
}
