package com.admicro.dao.hdfs

import com.admicro.model.CpaAnalyticModel.{CpaAnalyticInput, LogCpa, LogFull}
import org.apache.spark.sql.{Dataset, SparkSession}

import java.sql.Date

class CpaLogDao(spark: SparkSession) extends BaseHdfsDao [LogFull](spark){

 override def getData(input: String): Dataset[LogFull] = {
    import spark.implicits._
    val data = spark.read.parquet(input)
      .filter("isSuccess==1")
      .select(
        $"dt".as("timeLog"),
        $"clickId".as("transId"),
        $"isSuccess".as("success"),
        $"domain",
        $"path",
        $"cpa_value",
        $"sourceType".as("srcId"),
        $"cpa_id".as("reqId"),
        $"f9".as("step"),
        $"guid",
        $"f11".as("dmGuid"),
        $"f12".as("sourceDomain"),
        $"f13".as("ip"),
        $"f14".as("ipLong")
      )
      .as[LogCpa]
      .map(x =>CpaAnalyticInput(
        dt = Date.valueOf(x.timeLog.substring(0,10)),
        timeLog = x.timeLog,
        transId = x.transId,
        success = x.success,
        domain = x.domain,
        path = x.path,
        cpaId = getSplit(x.cpa_value)(0),
        eventId = getSplit(x.cpa_value)(1),
        cpaValue = getSplit(x.cpa_value)(2),
        cpaOrderId = getSplit(x.cpa_value)(3),
        srcId = x.srcId,
        reqId = x.reqId,
        step = x.step,
        guid = x.guid,
        dmGuid = x.dmGuid,
        sourceDomain = x.sourceDomain,
        ip = x.ip,
        ipLong = IPv4ToLong(x.ipLong)
      ))
      .where("cpaOrderId != ''")
    data
  }
  def getSplit(col: String): Array[String]={
    val splits = col.split("(@@@|###)")
    val rs: Array[String] = new Array(4)
    rs(0) = splits(0)
    if(splits.length == 3) {
      if(splits(1).length > 6){
        rs(1) = splits(1).substring(0,6)
        rs(2) = splits(1).substring(6)
      }else{
        rs(2) = ""
        rs(1) = splits(1)
      }
      rs(3) = splits(2)
    }
    else if(splits.length == 2){
      if(splits(1).length > 6){
        rs(1) = splits(1).substring(0,6)
        rs(2) = splits(1).substring(6)
      }else{
        rs(1) = splits(1)
        rs(2) = ""
      }
      rs(3) = ""
    }else{
      rs(1) = ""
      rs(2) = ""
      rs(3) = ""
    }
    rs
  }
  def IPv4ToLong(dottedIP: String): Long = {
    val addrArray: Array[String] = dottedIP.split("\\.")
    var num: Long = 0
    var i: Int = 0
    while (i < addrArray.length) {
      val power: Int = 3 - i
      num = num + ((addrArray(i).toInt % 256) * Math.pow(256, power)).toLong
      i += 1
    }
    num
  }

}
