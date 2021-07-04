package com.atguigu.chapter04

object Test05_WhileLoop {
  def main(args: Array[String]): Unit = {
    var a: Int = 10
    while (a >= 1){
      println(s"this is ${a}")
      a -= 1
    }
    do {
      println(s"this is ${a}")
      a += 1
    } while(a < 10)
  }
}
