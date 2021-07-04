package com.atguigu.chapter05

/**
 * 不同情况下函数的定义方式，入参、出参等
 */
object Test02_FunctionDefine {
  def main(args: Array[String]): Unit = {
    // 函数1、无参、无返回值
    def f1(): Unit = {
      println("无参、无返回")
    }
    f1()
    // 函数2、无参、有返回值
    def f2(): Int = {
      println("无参、有返回")
      2
    }
    f2()
    // 函数3、有参、无返回值
    def f3(name: String): Unit = {
      println("Hello, " + name)
    }
    f3("omg")
    // 函数4、有参、有返回值
    def f4(name: String): String = {
      val ms = s"hello, ${name}"
      println(ms)
      ms
    }
    f4("rick and morty")
    // 函数5、多参数、无返回
    def f5(name1: String, name2: String): Unit = {
      val ms = s"${name1} and ${name2} !"
      println(ms)
    }
    f5("rick","morty")
    // 函数6、多参数、有返回值
    def f6(name1: String, name2: String): String = {
      val ms = s"${name1} + ${name2} season 5"
      println(ms)
      ms
    }
    println(f6("rick", "morty"))
    //
  }
}
