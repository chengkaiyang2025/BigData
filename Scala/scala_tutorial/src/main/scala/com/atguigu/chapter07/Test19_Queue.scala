package com.atguigu.chapter07

import scala.collection.mutable

object Test19_Queue {
  def main(args: Array[String]): Unit = {
    val queue:mutable.Queue[String] = new mutable.Queue[String]()
    queue.enqueue("ab")
    queue.enqueue("a")
    println(queue)

    queue.dequeue()
    queue.enqueue("azz")
    println(queue)
  }
}
