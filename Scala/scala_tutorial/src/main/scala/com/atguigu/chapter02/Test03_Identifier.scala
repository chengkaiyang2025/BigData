package com.atguigu.chapter02

object Test03_Identifier {
  def main(args: Array[String]) :Unit = {
    // 以字母或下划线开头、后面接字母、数字、下划线
    val hello:String = "sdf"
    val Hello123 = ""
    val __sdf = "sdf"
    println(__sdf)
    // 以操作符开头
    val + = "hello"
    println(+)
    // 以反引号``包含
    val `if` = "if"
    println(`if`)
  }
}
