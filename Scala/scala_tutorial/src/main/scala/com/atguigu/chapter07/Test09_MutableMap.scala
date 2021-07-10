package com.atguigu.chapter07

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object Test09_MutableMap {
  def main(args: Array[String]): Unit = {

    val map: mutable.Map[String, Int] = mutable.Map("a" -> 13, "b" -> 1)
    println(map)
    map.mkString(" 's key is ")
    map.put("abc", 12)
    println(map)

    map.foreach((kv:(String, Int)) => {
      if (kv._1.length == 1) println(kv)
    })
    map +=(("e", 12))
    println(map)

    println(map.remove("e"))
    println("---")
    println(map)

    val array = ArrayBuffer("sdf", "yck", "yck", "wsn", "wsn", "sdf", "avsd")
    array.append("yxp")
    array.append("yck")
    array.append("yck")
    println(array)
    var wordcount:mutable.Map[String, Int] = mutable.Map()
    array.foreach((w) => {
      if(wordcount.contains(w)) wordcount.update(w, wordcount.get(w).get + 1) else wordcount.put(w, 1)
    })
    println(wordcount)
    println(wordcount.getOrElse("niuniu", 1))

    val map2:mutable.Map[String, Int] = mutable.Map("yck" -> 1, "father" -> 2)
    println(" --------- ")
    println(s"map2 is ${map2}")
    println(s"wordcount is ${wordcount}")
    println(" ----------- ")
    println(wordcount ++ map2)
    println(map2 ++ wordcount)
  }
}
