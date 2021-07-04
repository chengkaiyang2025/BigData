package com.atguigu.chapter05

object Test09_ClosureAndCurrying {
  def main(args: Array[String]): Unit = {
    def add(a: Int, b: Int) = {
      a + b
    }
   def addByFour(b: Int) = {
     4 + b
   }
    def addByFive(b: Int) = {
    5 + b
    }
    def addByFour1(): Int=>Int = {
      val a = 4
      def addB(b: Int) = {
        a + b
      }
      addB
    }
    def addByA(a: Int): Int=>Int = {
      def addByB(b: Int):Int = {
        a + b
      }
      addByB
    }

    println(addByA(1)(2))
    val addByFour2 = addByA(4)
    val addByFive2 = addByA(5)

    println(addByFour2(13))
    println(addByFive2(25))

    //  4 lambda简写
    def addByA1(a: Int): Int => Int = {
      (b: Int) => {
        a + b
      }
    }

    def addByA2(a: Int): Int => Int = {
      b => a + b
    }

    def addByA3(a: Int): Int => Int = a + _
    val addByFour3 = addByA3(4)
    val addByFive3 = addByA3(5)

    println(addByFour3(13))
    println(addByFive3(25))

    def addCurring(a: Int)(b: Int) = {
      a + b
    }
    println(addCurring(35)(24))
  }
}
