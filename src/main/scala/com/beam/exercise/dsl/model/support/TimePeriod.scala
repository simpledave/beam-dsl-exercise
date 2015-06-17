package com.beam.exercise.dsl.model.support

sealed trait TimePeriod {

  def name: String

}

case class Day(name: String = "day") extends TimePeriod

case class Week(name: String = "week") extends TimePeriod
