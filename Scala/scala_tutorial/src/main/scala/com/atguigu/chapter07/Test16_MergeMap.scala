package com.atguigu.chapter07

object Test16_MergeMap {
  def main(args: Array[String]): Unit = {
    val stringList: List[String] = List(
      "hello",
      "hello world",
      "hello scala",
      "hello spark from scala",
      "hello flink from scala"
    )
    println(stringList.flatMap(_.split(" "))
      .groupBy(a => a)
      .map((a) => (a._1, a._2.length))
      .toList
      .sortBy(_._2).reverse.take(10))
  }
}
