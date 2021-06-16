package com.atguigu.chapter02

object Test04_String {
  def main(args: Array[String]):Unit = {
    // 字符串，通过+链接
    val name: String = "alice"
    val age: Int = 19
    println(age + "岁的"+ name)

    // * 将一个字符串复制n次，类似于python
    println(name * 3)

    // printf，字数串拼接类似于python
    println(s"我叫${name}，今年${age}")
    val num: Double = 2.3456
    println(f"The number is ${num}%2.2f")

    // 三引号表示字符串
    val sql = {
      s"""
        |select
        |*
        |from student
        |where age > ${age}
        |and name = ${name}
        |""".stripMargin
    }
    println(sql)

  }

}
