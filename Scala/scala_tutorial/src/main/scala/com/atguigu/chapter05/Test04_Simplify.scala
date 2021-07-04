package com.atguigu.chapter05
// 函数至简原则
object Test04_Simplify {
  def main(args: Array[String]): Unit = {
    def f0(name: String): String = {
      return name
    }
    println(f0("rick"))
    //1、return可以省略
    def f1(name: String): String = {
      name
    }
    println(f1("rick 1"))
    //2、如果函数只有一行代码，省略花括号
    def f2(name: String): String = name
    println(f2("rick 2"))
    //3、如果返回类型能够推断，那么也可以省略
    def f3(name: String) = name
    println(f3("rick 3"))
    //4、如果有return，则必须指定返回类型

    //5、如果函数声明unit，那么即使使用return也不起作用
    def f5(name: String): Unit = {
      return name
    }
    println(f5("rick 5"))
    //6、如果无返回值类型，可以省略等号

    //7、如果函数无参数，但声明了参数列表，那么调用的时候，小阔和可以不加
    def f7():Unit = {
      println("rick 7")
    }
    f7()
    f7
    // 8、如果没有参数列表，可以不加小括号
    def f8:Unit = {
      println("f8")
    }
    f8
    // 9、def也可以省略
    (name: String) => { println(name) }
  }
}
