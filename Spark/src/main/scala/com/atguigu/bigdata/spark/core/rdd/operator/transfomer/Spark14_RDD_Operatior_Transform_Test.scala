package com.atguigu.bigdata.spark.core.rdd.operator.transfomer

import org.apache.spark.{SparkConf, SparkContext}

/**
 *交集并集补集
 */
object Spark14_RDD_Operatior_Transform_Test {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(conf)
    val rdd1 = sc.makeRDD(List(1,2,3,4,5,6,7),2)
    val rdd2 = rdd1.map(t => (t, 1))

    val value = rdd2.partitionBy(new MyPartitioner(2))
    value.saveAsTextFile("output/Spark14_RDD_Operatior_Transform_Test/number")

    val rddText = sc.makeRDD(List("this is", "this is yck", "hello,yck", "hello,ss","god"))
    rddText.map(t => (t,1)).partitionBy(new MyPartitioner(3)).saveAsTextFile("output/Spark14_RDD_Operatior_Transform_Test/text")
    sc.stop()
  }

}


