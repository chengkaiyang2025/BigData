package com.atguigu.bigdata.spark.core.rdd.operator.transfomer

import org.apache.spark.Partitioner

class MyPartitioner(partitioner: Int) extends Partitioner {
  override def numPartitions: Int = if(partitioner < 3) 3 else partitioner
  override def getPartition(key: Any): Int = {
    key match {
      case null=> 0
      case i:Int =>
        i % partitioner
      case s:String => if (s.contains(",")) 0 else if (s.contains(" ")) 1 else partitioner - 1
      }
    }

}
