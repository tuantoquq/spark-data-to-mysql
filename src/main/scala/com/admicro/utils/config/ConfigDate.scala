package com.admicro.utils.config

import org.apache.log4j.Logger
import org.joda.time.LocalDate

import java.util.Calendar

class ConfigDate {
  @transient lazy val logger = Logger.getLogger(getClass())

  /**
   * get Date from file config
   * ex : DATE = 2021-04-29 then calendar is seted.
   *
   * @param DATE String date
   * @return
   */
  def getDate(DATE: String): Calendar = {
    val calendar: Calendar = Calendar.getInstance()

    if (DATE != null && !DATE.isEmpty) {
      logger.info("DATE " + DATE)


      val year = DATE.split("-")(0).toInt
      val month = DATE.split("-")(1).toInt - 1 // month count from 0
      val day = DATE.split("-")(2).toInt

      calendar.set(year, month, day)

    } else {
      /**
       * OFFSET = 0, 1, 2, 3 ...... 0 is today
       */
      val OFFSET = ConfigInfo.offset
      calendar.add(Calendar.DATE, -OFFSET.toInt)
    }

    calendar
  }
  def getCurrentDate(): String = {
    LocalDate.now().toString()
  }

}
