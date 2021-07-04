package com.atguigu.chapter05

/**
 * 方法与函数的区别
 */
object Test01_FunctionAndMethod {
  def main(args: Array[String]): Unit = {
    // 1、定义函数 方法里的def叫函数，对象里面的def叫方法
    def sayHi(name: String): Unit = {
      println("hi," + name)
    }
    // 调用函数
    sayHi("函数")
    // 调用对象方法
    Test01_FunctionAndMethod.sayHi("方法")
    // 获取方法返回值
    val re = Test01_FunctionAndMethod.sayHello("返回值")
    println(re)
  }
  // 定义对象方法
  def sayHi(name : String): Unit = {
    println("Hi," + name)
  }

  def sayHello(name : String): Unit = {
    println("Hello, " + name)
    return "Hello"
  }
}
