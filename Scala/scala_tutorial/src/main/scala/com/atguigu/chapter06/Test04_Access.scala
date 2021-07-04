package com.atguigu.chapter06

import javafx.concurrent.Worker

/**
 *成员属性
 */
object Test04_Access {
  def main(args: Array[String]): Unit = {
    val person = new Person()
    println(person.sex)
    println(person.printInfo())
    println("----------")
    val worker = new Worker()
    println(worker.sex)
    println(worker.age)
    println(worker)
    worker.printInfo()
    def c = (a:Int) => ( a + 1)

    println(c(1))
  }
}

// 定义子类
class Worker extends Person{
  sex = "male"
  age = 55
  name = "Bob"

  override def toString: String = s"This is a ${sex} man,who is ${age} old"

  override def printInfo(): Unit = println("sdf")

}

