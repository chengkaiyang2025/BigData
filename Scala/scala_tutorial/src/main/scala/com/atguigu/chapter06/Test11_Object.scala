package com.atguigu.chapter06

/**
 *伴生对象
 */
object Test11_Object {
  def main(args: Array[String]): Unit = {
    val student11 = Student11.newStudent("alice", 18)
    student11.printInfo()
    val student2 = Student11.apply("alice", 22)
    student2.printInfo()
    val student3 = Student11("alice", 23)
    student3.printInfo()
  }
}
class Student11 private(val name: String, val age: Int){
  def printInfo(): Unit ={
    println(s"student: name ${name}, age: ${age}")
  }
}

object Student11{
  val school: String = "atguigu"
  def newStudent(name: String, age: Int): Student11 = new Student11(name, age)
  def apply(name: String, age: Int): Student11 = new Student11(name, age)
}