package com.beam.exercise.dsl.repository

import com.beam.exercise.dsl.model.support.RewardParticipationStatus
import com.beam.exercise.dsl.model.{Beamer, Reward, RewardParticipation}
import org.joda.time.DateTime

/**
 * Consider the implementation of this as provided and simply define the methods you require and mock accordingly in the tests.
 */
trait RewardParticipationRepository {

  def findByRewardAndStatus(reward: Reward, status: RewardParticipationStatus): Seq[RewardParticipation]

  def findByRewardAndBeamerAndStatus(reward: Reward, beamer: Beamer, status: RewardParticipationStatus): Seq[RewardParticipation]

  def countByRewardAndBeamerAndStatusBetween(reward: Reward, beamer: Beamer, status: RewardParticipationStatus, from: DateTime, till: DateTime): Long

}
