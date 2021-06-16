package com.atguigu.chapter02

import com.atguigu.chapter01.Student

object Test02_Variable {
  def main(args: Array[String]): Unit = {
    // 声明一个变量的通用语法
    val a: Int = 10
    //(1) 声明变量时候，类型可以忽略，由编译器推导
    var a1 = 10
    a1 = 20
    val a2 = 20
    // var声明的为变量，val为常量
//    a
//    a2 = 12
    // (2) 类型确定后，就不能修改说明scala为强类型语言
//    a2 = "sdf"
    //(3) 类型声明的时候，必须要有初始值
//    var a3: Int
    // （4）

    var alice = new Student("alice", 22)
    alice = new Student("alice",23)
//    alice = null
    alice.printInfo()
    val bob = new Student("bob", 22)
    bob.age = 11
    bob.printInfo()
  }
}
