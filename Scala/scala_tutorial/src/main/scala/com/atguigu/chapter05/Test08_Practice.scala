package com.atguigu.chapter05

object Test08_Practice {
  /**柯里化：将接受多个参数的函数，转为接受单一参数的函数，
   * @param args
   */
  def main(args: Array[String]): Unit = {
    // 1、多参列表
    val fun = (i: Int, s: String, c: Char) => {
      if(i == 0 && s == "" && c == '0')  true else false
    }
    println(fun(0, "", '0'))
    println(fun(1, "", '0'))
    // 2、改为高阶函数
    def fun2(i: Int): String =>( Char => Boolean) = {
      def f1(s:String): (Char => Boolean) = {
        def f2(c:Char): Boolean = {
        if(i == 0 && s == "" && c == '0') true else false
        }
        f2
      }
      f1
    }
    println("高阶----------")
    println(fun2(0)("")('0'))
    println(fun2(1)("sdf")('0'))
    // 3、通过lambda表达式 简写 高阶函数
    def fun3(i:Int): String => (Char => Boolean) = {
      s => c=> if(i == 0 && s == "" && c == '0') true else false
    }
    // 4、单一参数的嵌套函数
  }
}
