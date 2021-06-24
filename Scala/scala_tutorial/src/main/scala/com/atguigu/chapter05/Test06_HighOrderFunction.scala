package com.atguigu.chapter05

object Test06_HighOrderFunction {
  def main(args: Array[String]): Unit = {
    def f(name: Int): Int = {
//      println(name)
      name + 10
    }
    def fun(): Int = {
      1
    }
    // 1、函数作为值传递
    val f2 = f _
    println(f(1))
    println(f2(1))
    val fun2 = fun _
    println(fun())
    println(fun2())

    // 2、函数作为参数进行传递
    def dualEval(op: (Int,Int) => Int,a: Int, b:Int) = op(a,b)
    println(dualEval(_ + _, 1, 2))
    println(dualEval(_ - _, 4, 10))
    println(dualEval( - _ + _, 6, 10))
    println("--------1")
    val f1  =(a: Int,b: Int) => {
      a + b
    }
    println(dualEval(f1,4,50))
    println("--------")
    // 3、函数作为函数的返回值传递
    def f5(): Int=> Unit = {
      def f6(a: Int): Unit = {
        println("调用"+a)
      }
      f6
    }

    println(f5()(24))
    // 4、
  }
}
