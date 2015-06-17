package com.beam.exercise.dsl

import com.beam.exercise.dsl.model.RewardParticipation
import com.beam.exercise.dsl.repository.RewardParticipationRepository
import com.beam.exercise.dsl.service.TimeService

class RewardLimiter(timeService: TimeService, rewardParticipationRepository: RewardParticipationRepository) {

  /**
   * Evaluates if this participation is limited or not.
   *
   * Participations with a state not set to Started are invalid input.
   * A participation under the limits should have it's status set to Completed.
   * A participation that hits the limits should have it's status set to Limited.
   *
   * @param participation The participation to evaluate
   * @return the participation with the state set according to the outcome of the limits
   */
  def evaluate(participation: RewardParticipation): RewardParticipation = {
    ??? // TODO: implement this
  }

}
