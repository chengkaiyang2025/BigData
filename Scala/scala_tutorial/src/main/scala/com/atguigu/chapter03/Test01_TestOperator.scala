package com.atguigu.chapter03

object Test01_TestOperator {
  def main(args: Array[String]): Unit = {
    // 1、算数运算符
    val result1: Int = 10 / 3
    println(s"result1: ${result1}")
    val result2: Float = 10 / 3
    println(s"result2: ${result2}")
    val result3: Double = 10.0 / 3
    println(s"result3: ${result3}")
    val result3f: String = result3.formatted("%5.2f")
    println(s"result3: ${result3f}")
    val result4: Int = 10 % 3
    println(s"result4: ${result4}")
    // 2、比较运算符
    val s1: String = "hello"
    val s2: String = new String("hello")

    println(s1==s2)
    println(s1.equals(s2))
    println(s1.eq(s2))
    println("===================")
    // 3、逻辑运算符
    def m(n: Int) : Int = {
      println("m被调用")
      return n
    }

    val n = 1
    println((4 > 5) || m(n) > 0)
    // 判断一个字符串是否为空
    def isEmpty(s: String): Boolean = {
      return s != null && !("".equals(s.trim))
    }
    println(isEmpty("s"))
    println(isEmpty(null))
    println(isEmpty(""))
    // 4、赋值运算符

    // 5、位运算符
  }
}
