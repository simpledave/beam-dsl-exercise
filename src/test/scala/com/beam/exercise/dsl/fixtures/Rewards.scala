package com.beam.exercise.dsl.fixtures

import com.beam.exercise.dsl.model.Reward
import com.beam.exercise.dsl.rule.Limit
import org.joda.time.{DateTimeZone, DateTime}

object Rewards {

  def reward100(limit: Limit) = Reward(Ids.nextId, new DateTime(DateTimeZone.UTC), limit, 100)

}
