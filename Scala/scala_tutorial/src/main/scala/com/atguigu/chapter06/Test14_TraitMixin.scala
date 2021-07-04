package com.atguigu.chapter06

/**
 *实现多个特质时候的掺入问题
 */
object Test14_TraitMixin {
  def main(args: Array[String]): Unit = {
    val student1 = new Student14 with Talent {
      override def dancing(): Unit = println("dancing")

      override def singing(): Unit = println("singing")
    }
    student1.dancing()
    student1.dating()
    student1.singing()
  }
}
trait Knowledge{
  var amount: Int = 0
  def increase(): Unit
}

trait Talent{
  def singing(): Unit
  def dancing(): Unit
}

class Student14 extends Person13 with Young with Knowledge{
  override val name: String = "student"
  override def dating(): Unit = {
    println(s"student ${name} is dating.")
  }

  override def sayHello(): Unit = {
    println(s"hello from student: ${name}")
  }

  override def play(): Unit = {

  }
}

