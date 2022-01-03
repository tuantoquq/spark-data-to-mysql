package com.admicro

import com.admicro.base.BaseProcess
import com.admicro.process.CpaAnalyticProcess
import com.admicro.utils.config.{ConfigDate, ConfigInfo, CustomLog}
import org.apache.spark.sql.SparkSession

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Calendar

object Main {
  val dfsql = new SimpleDateFormat("yyyy-MM-dd")
  val configDate = new ConfigDate
  var beginDate: Calendar = null
  var endDate: Calendar = null
  def main(args: Array[String]): Unit = {
    var spark: SparkSession = null
    try {
//      initDateRun()

//      CustomLog.printLog("Date run:\t" + dfsql.format(beginDate.getTime) + " -> " + dfsql.format(endDate.getTime))
        CustomLog.printLog("Date run:\t" + configDate.getCurrentDate())
      spark = SparkSession.builder
//        .appName(ConfigInfo.appName + " from " + dfsql.format(beginDate.getTime) + " to " + dfsql.format(endDate.getTime))
        .appName(ConfigInfo.appName + " on "+ configDate.getCurrentDate())
        .getOrCreate()

      val process: BaseProcess = new CpaAnalyticProcess(spark)
      val currentHour = Calendar.getInstance().getTime.getHours
      if(currentHour >= 1){
        process.run(configDate.getCurrentDate())
      }else{
        process.run(LocalDate.now().minusDays(1).toString)
      }

//      val daysBetween = ChronoUnit.DAYS.between(beginDate.toInstant, endDate.toInstant).toInt
//
//      for (step <- 0 to daysBetween) {
//        val temp = Calendar.getInstance()
//        temp.setTime(beginDate.getTime)
//        temp.add(Calendar.DATE, step)
//
//        val dateRun = dfsql.format(temp.getTime)
//        process.run(dateRun)
//      }

    } catch {
      case e: Exception => {
        var appName: String = ConfigInfo.appName + " "
        if (spark != null) {
          appName = spark.conf.get("spark.app.name")
        }
        e.printStackTrace()
      }
    } finally {
      if (spark != null) {
        spark.close
      }
    }
  }

  def initDateRun() {
    // neu truyen tu ngay den ngay
    // thi chay theo for
    // neu khong, k chay for
    // 1h:00 // time config
    // chay ngay cu

    val strBeginDate = ConfigInfo.beginDate
    val strEndDate = ConfigInfo.endDate

    endDate = configDate.getDate(strEndDate)

    if (!strBeginDate.isEmpty()) {
      beginDate = configDate.getDate(strBeginDate)
    } else {
      beginDate = endDate
    }


  }
}