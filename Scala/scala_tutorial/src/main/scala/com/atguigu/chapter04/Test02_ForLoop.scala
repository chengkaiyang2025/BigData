package com.atguigu.chapter04

object Test02_ForLoop {
  def main(args: Array[String]): Unit = {
    for (i <- 1 to 10){
      println(s"${i}+hello world")
    }
    println("============")
    for (i: Int <- 1.to(10)){
      println(s"${i}+hello world")
    }
    println("============")
    for (i <- 1 until 10){
      println(s"${i} + hello world")
    }
    // 2 collection
    for ( i <- Array(1,3,4)){
      println(i)
    }
    println("---")
    for (i <- List(1,3,5)){
      println(i)
    }
    println("----")
    for (i <- Set(1,3,5)){
      println(i)
    }
    println("----")
  }
}
