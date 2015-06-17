package com.beam.exercise.dsl.fixtures

import com.beam.exercise.dsl.model.support.{RewardParticipationStatus, Started}
import com.beam.exercise.dsl.model.{Beamer, Reward, RewardParticipation}
import org.joda.time.{DateTime, DateTimeZone}

object RewardParticipations {

  def participation(reward: Reward, beamer: Beamer, status: RewardParticipationStatus = Started()) =
    RewardParticipation(Ids.nextId, new DateTime(DateTimeZone.UTC), reward, beamer, status)

}
