package com.atguigu.bigdata.spark.core.rdd.operator.transfomer

import org.apache.spark.{SparkConf, SparkContext}

import java.text.SimpleDateFormat

object Spark06_RDD_Operatior_Transform_Test3 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(conf)

    val rdd = sc.textFile("data/apache.log")
    val re = rdd.map(t => {
      val time = t.split(" ")(3)
      val sdf = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss")
      val date = sdf.parse(time)
      (date.getHours,1)
    }).groupBy(_._1).map(
      c => {
        s"时间 ${c._1} 访问 ${c._2.size}"
      }
    )
    re.collect().foreach(println)
    sc.stop()
  }
}
