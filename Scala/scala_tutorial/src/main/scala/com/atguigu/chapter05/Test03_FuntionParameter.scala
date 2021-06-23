package com.atguigu.chapter05

/**
 * 函数中多参数输入
 */
object Test03_FuntionParameter {
  def main(args: Array[String]): Unit = {
    // 可变参数
    def f1(args: String*): Unit = {
      println(args)
    }
    f1("rick","morty","bob","lili")
    // 可变参数放在后面
    def f2(name: String, friends: String*): Unit ={
      println(s"${name} and ${friends}")
    }
    f2("cat","dog","mouse")
    // 参数默认值
    def f3(name: String = "rick and morty"): Unit = {
      println(s"I like TV show ${name}")
    }
    f3()
    f3("Big Bang Theory")
    // 带名参数
    def f4(name: String = "yck" ,age: Int = 50): Unit = {
      println(s"${name} age ${age}")
    }
    f4()
  }

}
