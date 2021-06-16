package com.atguigu.chapter02

import scala.io.StdIn

object Test05_StdIn {
  def main(args: Array[String]): Unit = {
    println("请输入名称")
    val name: String = StdIn.readLine()
    println("请输入年龄")
    val age: Int = StdIn.readInt()
    println("你的")
  }
}
