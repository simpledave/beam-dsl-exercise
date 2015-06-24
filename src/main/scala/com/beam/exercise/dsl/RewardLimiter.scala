package com.beam.exercise.dsl

import com.beam.exercise.dsl.model.RewardParticipation
import com.beam.exercise.dsl.model.support.{Completed, Limited}
import com.beam.exercise.dsl.repository.RewardParticipationRepository
import com.beam.exercise.dsl.rule.{Limit, ConjunctionLimit, BeamerTotalRewardLimit, TotalRewardBudgetLimit}
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
    if(passedConjunctionLimit(participation, participation.reward.limit))
      participation.copy(status = Completed())
    else
      participation.copy(status = Limited())



   /* participation.reward.limit match {
      //case BeamerMaximumCyclesLimit(time, count) =>
      case l: BeamerTotalRewardLimit if l.amount > totalAwardPerBeamer => participation.copy(status = Limited())

      case c: ConjunctionLimit if passedConjunctionLimit (c) => participation.copy(status = Limited())

      case TotalRewardBudgetLimit(totalAward) => participation.copy(status = Limited())
      case _ => throw new IllegalStateException("it is not")
    }*/
  }

  private def passedConjunctionLimit(participation: RewardParticipation, conjunctionLimit: Limit): Boolean = {
    conjunctionLimit match {
      //case BeamerMaximumCyclesLimit(time, count) =>
      case l: BeamerTotalRewardLimit => {
        val totalAwardPerBeamer = rewardParticipationRepository.findByRewardAndBeamerAndStatus(participation.reward, participation.beamer, Completed())
          .foldLeft(BigDecimal(0))((acc, rewardParticipation) => acc + rewardParticipation.reward.award)

        l.amount < totalAwardPerBeamer
      }
      case c: ConjunctionLimit => passedConjunctionLimit(participation,c.left) && passedConjunctionLimit(participation,c.right)
      case TotalRewardBudgetLimit(totalRewardLimit) => {
        val totalAward = rewardParticipationRepository.findByRewardAndStatus(participation.reward, Completed())
          .foldLeft(BigDecimal(0))((acc, rewardParticipation) => acc + rewardParticipation.reward.award)

        totalAward < totalRewardLimit
      }
      case _ => throw new IllegalStateException("it is not")
    }

  }


}
