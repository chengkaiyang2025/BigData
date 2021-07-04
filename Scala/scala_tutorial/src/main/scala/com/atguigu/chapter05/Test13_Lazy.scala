package com.atguigu.chapter05

object Test13_Lazy {
  def main(args: Array[String]): Unit = {
    lazy val result: Int = sum(13, 47)
    println("re: "+ result)
    println("re: "+ result)
  }
  def sum(a: Int,b: Int) = {
    println("sum run")
    a + b
  }
}
