package com.atguigu.chapter06
·
import scala.beans.BeanProperty

/**
 *
 */
object Test03_Class {
  def main(args: Array[String]): Unit = {
    val student = new Student()

    println(student.age)
    println(student.sex)
    println(student.getAge)

    student.setAge(29)
    println(student.getAge)
  }
}


class Student{
  // 私有成员变量，用private关键字修饰
  private var name: String = "alice"

  // scala中如果 没有private关键字的就是public，外部可以直接调用，不用写set get方法。
  // 但外部直接调用就是 调用get方法。
  var sex: String = _
  // 加入BeanProperty 注解，外部可以通过set get方法获取。
  @BeanProperty
  var age: Int = _
}