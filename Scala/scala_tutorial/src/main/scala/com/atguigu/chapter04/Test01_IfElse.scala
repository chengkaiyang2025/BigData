package com.atguigu.chapter04

import scala.io.StdIn

object Test01_IfElse {
  def main(args: Array[String]): Unit = {
    println("请输入年龄：")
    val age: Int = StdIn.readInt()
    // 1、单分支
    if(age>18){
      println("成年")
    }
    // 2、双分支
    if(age>18){
      println("成年")
    }else{
      println("未成年")
    }
    // 3、多分支
    if(age<6){
      println("儿童")
    }else if(age<18){
      println("少年")
    }else if (age<30){
      println("青年")
    }else if(age< 50){
      println("中年")
    }else{
      println("老年")
    }
    val s:String = if (age > 18) "成年" else "未成年"
    // 4、分支语句的返回值
  }
}
