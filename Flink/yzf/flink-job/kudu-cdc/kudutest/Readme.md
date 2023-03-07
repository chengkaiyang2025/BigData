无论是宽表还是窄表，在三副本的情况下，在一个小时后吞吐量都从万下降到千。
而使用单副本性能会高很多。

TODO
1、评估当前每个tablet的数据量，做好单帐期的tablet数量。
2、了解kudu的[range分区](https://mp.weixin.qq.com/s?__biz=MzU3MzgwNTU2Mg==&mid=2247485307&idx=1&sn=05aaae1f19058e8feae1eddcdef3c2fc&chksm=fd3d41eeca4ac8f83eaa0077ba206dcba24b3a4e144d146f95ede559265178aa99f1ef65ed77&mpshare=1&scene=1&srcid=0709VLLbUlZvDHiJd4Ux8TJ3&sharer_sharetime=1625807427380&sharer_shareid=a5fcf34b2f397ddd7d2cb65a28dfa2d6&exportkey=AQp8mRu6Hx334gpXGFDTpRQ%3D&pass_ticket=2QkSzjCgNy7JzCla9DHGj2KZ4ChuLkudrPAo0G%2BrsBZPDdCExS%2F%2BA%2FgaZ1qzzA52&wx_header=0#rd)
官方说明可以按照时间分区，是比较好的分区方式，而且可以支持动态增加与删除分区

3、kudu单tablet最大建议不要超过10g，一个table最多60个tablets
目前56个tablet一个表，记录数58375122，8g，也就是说一天数据量约为16g，一个月16g*22 = 352g,

线上最大的表rpt_zbz202012，数据量为40亿左右，线上容量为359G，一共35个分区，每个分区数据量为35G 按照最多来算一年4.3T，


    Recommended maximum number of masters is 3.

    Recommended maximum number of tablet servers is 100.

    Recommended maximum amount of stored data, post-replication and post-compression, per tablet server is 8 TiB.

    Recommended number of tablets per tablet server is 1000 (post-replication) with 2000 being the maximum number of tablets allowed per tablet server.
    Maximum number of tablets per table is 60, per tablet server, at table-creation time.

    Maximum number of tablets per table for each tablet server is 60, post-replication (assuming the default replication factor of 3), at table-creation time.

    Recommended maximum amount of data per tablet is 50 GiB. Going beyond this can cause issues such a reduced performance, compaction issues, and slow tablet startup times.

    The recommended target size for tablets is under 10 GiB.
