package com.beam.exercise.dsl.model

import com.beam.exercise.dsl.rule.Limit
import org.joda.time.DateTime

case class Reward(id: Long, created: DateTime, limit: Limit, award: BigDecimal) extends Entity {

}
