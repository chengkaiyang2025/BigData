package wc

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark01_WordCount {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local").setAppName("WorldCount")
    val sc = new SparkContext(sparkConf)

    val lines:RDD[String] = sc.textFile("src/main/resources/datas.txt")
    val strings = lines.collect()
    strings.foreach(println)
    sc.stop()

  }
}
