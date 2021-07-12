package com.atguigu.chapter07

object Test11_CommonOp {
  def main(args: Array[String]): Unit = {
    val list = List(1, 2, 3, 4, 5, 80)
    val set = Set(1,2,2,2,2,21,2, 80)
  // 集合长度
    println(list.length)
    println(list.size)

    // 集合大小
    println(set.size)
    //循环遍历
    list.foreach(println)
    set.foreach(println)
    //迭代器
    for(i <- list.iterator) println(i)
    //生成字符串
    println(list.mkString("-,"))
    println(set.mkString("-,"))
    //是否包含
    println(set.contains(111))
    println(list.contains(2))
  }
}
