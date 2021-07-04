package com.atguigu.chapter05

object Test11_ControlAbstraction {
  def main(args: Array[String]): Unit = {
    def f0(a: Int) = {
      println(s"a:${a}")
    }
    println(f0(23))
    def f1() = {
      println("f1()調用")
      12
    }
    f0(f1())
    println("=====================")
    f0({
      println("-----f0")
      23
    })
  }
}
