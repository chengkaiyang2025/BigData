package com.atguigu.chapter06

/**
 * 特质，就是java里的interface
 */
object Test13_Trait {
  def main(args: Array[String]): Unit = {
    val student1 = new Student13
    student1.study()
    student1.dating()
  }
}
class Person13{
  val name: String = "person"
  var age: Int = 28
  def sayHello(): Unit = {
    println("hello,every one")
  }
  def increase(): Unit = {
    println("age is increasing")
  }
}

trait Young{
  val name: String = "young"
  var age: Int
  def play():Unit = {
    println("young people like play")
  }
  def dating():Unit
}

class Student13 extends Person13 with Young {
  def study(): Unit = {
    println("student like study")
  }

  override val name: String = "student"
  override def play(): Unit = {
    println("student like play")
  }
  override def dating(): Unit = {
    println("student go to party and dating")
  }
}
