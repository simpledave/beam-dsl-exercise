package com.beam.exercise.dsl.rule

import com.beam.exercise.dsl.model.support.TimePeriod

case class BeamerMaximumCyclesLimit(timePeriod: TimePeriod, value: Int) extends Limit {

  override def toString = s"per beamer maximum cycles per ${timePeriod.name} is $value"

}
