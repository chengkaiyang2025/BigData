package com.atguigu.chapter05

object Test05_Lambda {
  def main(args: Array[String]): Unit = {

    val fun = (name: String) => {
      println(name)
    }
    fun("rick")

    // 0、定义一个函数，以函数作为参数输入
    def f(func: String => Unit): Unit = {
      func("rick and morty")
    }
    f(fun)
    f((name: String) => println(name + "is on adult swim"))

    // 匿名函数的简化原则
    // 1、参数的类型可以省略，会根据形参进行自动的推导
    f( (name) => println(name + "is on adult swim"))
    // 2、如果参数只有一个，可以省略（）
    f( name => println(name + "is on adult swim"))
    // 3、如果函数中代码只有一行，大括号也可以省略
    f( name => println(name + "is on adult swim"))
    // 4、如果函数参数只出现一次，则参数省略而且后面参数用_代替
    f( println(_))
    // 5、如果可以推断出传入的pl是一个函数体，可以直接省略下划线
    f( println )
    //

    // 实例定义二元操作运算，但是如果操作需要参数传入
    def dualFunctionOneAndTwo(fun: (Int, Int) => Int): Int ={
      return fun(1, 2)
    }
    val add = (a: Int, b: Int) => a + b
    val minus = (a: Int, b: Int) => a - b

    println(dualFunctionOneAndTwo(add))
    println(dualFunctionOneAndTwo(minus))

    println(dualFunctionOneAndTwo((a:Int,b:Int) => (a * b)))
    println(dualFunctionOneAndTwo( _ * _))
    println(dualFunctionOneAndTwo( _ + _))
    println(dualFunctionOneAndTwo( - _ + _))
  }
}
