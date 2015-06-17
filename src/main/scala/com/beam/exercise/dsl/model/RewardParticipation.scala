package com.beam.exercise.dsl.model

import com.beam.exercise.dsl.model.support.{RewardParticipationStatus, Started}
import org.joda.time.DateTime

case class RewardParticipation(id: Long, created: DateTime, reward: Reward, beamer: Beamer, status: RewardParticipationStatus = Started()) extends Entity {

}
