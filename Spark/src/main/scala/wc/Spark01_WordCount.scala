package wc

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark01_WordCount {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local").setAppName("WorldCount")
    val sc = new SparkContext(sparkConf)

    val lines:RDD[String] = sc.textFile("src/main/resources/datas.txt")

    val value = lines.flatMap(l => {
      l.split(",")
    }).groupBy(t=>t)
    val value1 = value.map {
      case (w, l) => {
        (w, l.size)
      }
    }
    val tuples = value1.collect()
    tuples.foreach(println)
    sc.stop()

  }
}
