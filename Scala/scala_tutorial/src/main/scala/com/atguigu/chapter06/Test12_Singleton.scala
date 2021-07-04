package com.atguigu.chapter06

/**
 *scala中的单例
 */
object Test12_Singleton {
  def main(args: Array[String]): Unit = {
    val student1 = Student12.getInstance()
    student1.printInfo()
  }
}

class Student12 private(val name: String, val age: Int){
  def printInfo(): Unit ={
    println(s"student: name = ${name}, age = ${age}")
  }
}
// 饿汉模式
//object Student12{
//  private val student: Student12 = new Student12("alice", 18)
//  def getInstance(): Student12 = student
//}
object Student12{
  private var student: Student12 = _
  def getInstance(): Student12 = {
    if(student == null){
      student = new Student12("alice",19)
    }
    student
  }
}