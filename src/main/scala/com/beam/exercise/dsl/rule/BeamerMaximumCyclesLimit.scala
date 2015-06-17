package com.beam.exercise.dsl.rule

import com.beam.exercise.dsl.model.support.TimePeriod

/**
 * The maximum number of times that an individual beamer can earn a reward in a time period.
 *
 * @param timePeriod  The applicable time period
 * @param value       The number of times they can earn the reward in the time period
 */
case class BeamerMaximumCyclesLimit(timePeriod: TimePeriod, value: Int) extends Limit {

  override def toString = s"per beamer maximum cycles per ${timePeriod.name} is $value"

}
