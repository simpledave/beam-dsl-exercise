package com.beam.exercise.dsl.service

import org.joda.time.{DateTime, DateTimeZone}

class TimeService {

  def now = new DateTime(DateTimeZone.UTC)

}
