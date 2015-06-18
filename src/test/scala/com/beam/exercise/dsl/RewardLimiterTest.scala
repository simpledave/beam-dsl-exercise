package com.beam.exercise.dsl

import com.beam.exercise.dsl.fixtures.{Beamers, RewardParticipations, Rewards}
import com.beam.exercise.dsl.model.support.{Completed, Day, Limited, Week}
import com.beam.exercise.dsl.repository.RewardParticipationRepository
import com.beam.exercise.dsl.rule.{BeamerMaximumCyclesLimit, BeamerTotalRewardLimit, ConjunctionLimit, TotalRewardBudgetLimit}
import com.beam.exercise.dsl.service.TimeService
import com.beam.exercise.dsl.test.EvenMoreSugar
import org.joda.time.{DateTime, DateTimeZone}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FunSpec, Matchers}

@RunWith(classOf[JUnitRunner])
class RewardLimiterTest extends FunSpec with Matchers with EvenMoreSugar {

  trait Fixtures {
    val timeService = mock[TimeService]
    val rewardParticipationRepository = mock[RewardParticipationRepository]

    val limiter = new JavaRewardLimiter(timeService, rewardParticipationRepository)

    val now = new DateTime(DateTimeZone.UTC)
    val serdar = Beamers.serdar
  }

  describe("evaluate ConjunctionLimit") {
    val reward = Rewards.reward100(ConjunctionLimit(BeamerMaximumCyclesLimit(Day(), 2), BeamerMaximumCyclesLimit(Week(), 3)))

    it("completes on true true") {
      new Fixtures {
        val participation = RewardParticipations.participation(reward, serdar)

        timeService.now returns now
        rewardParticipationRepository.countByRewardAndBeamerAndStatusBetween(participation.reward, participation.beamer, Completed(), now.minusDays(1), now) returns 0
        rewardParticipationRepository.countByRewardAndBeamerAndStatusBetween(participation.reward, participation.beamer, Completed(), now.minusWeeks(1), now) returns 1

        val actual = limiter.evaluate(participation)

        actual.status shouldEqual Completed()
      }
    }

    it("limits on true false") {
      new Fixtures {
        val participation = RewardParticipations.participation(reward, serdar)

        timeService.now returns now
        rewardParticipationRepository.countByRewardAndBeamerAndStatusBetween(participation.reward, participation.beamer, Completed(), now.minusDays(1), now) returns 0
        rewardParticipationRepository.countByRewardAndBeamerAndStatusBetween(participation.reward, participation.beamer, Completed(), now.minusWeeks(1), now) returns 10

        val actual = limiter.evaluate(participation)

        actual.status shouldEqual Limited()
      }
    }

    it("limits on false true") {
      new Fixtures {
        val participation = RewardParticipations.participation(reward, serdar)

        timeService.now returns now
        rewardParticipationRepository.countByRewardAndBeamerAndStatusBetween(participation.reward, participation.beamer, Completed(), now.minusDays(1), now) returns 10
        rewardParticipationRepository.countByRewardAndBeamerAndStatusBetween(participation.reward, participation.beamer, Completed(), now.minusWeeks(1), now) returns 0

        val actual = limiter.evaluate(participation)

        actual.status shouldEqual Limited()
      }
    }

    it("limits on false false") {
      new Fixtures {
        val participation = RewardParticipations.participation(reward, serdar)

        timeService.now returns now
        rewardParticipationRepository.countByRewardAndBeamerAndStatusBetween(participation.reward, participation.beamer, Completed(), now.minusDays(1), now) returns 10
        rewardParticipationRepository.countByRewardAndBeamerAndStatusBetween(participation.reward, participation.beamer, Completed(), now.minusWeeks(1), now) returns 10

        val actual = limiter.evaluate(participation)

        actual.status shouldEqual Limited()
      }
    }

  }

  describe("evaluate BeamerMaximumCyclesLimit") {
    val twiceADay = Rewards.reward100(BeamerMaximumCyclesLimit(Day(), 2))
    val thriceAWeek = Rewards.reward100(BeamerMaximumCyclesLimit(Week(), 3))

    it("completes when under day limit") {
      new Fixtures {
        val participation = RewardParticipations.participation(twiceADay, serdar)

        timeService.now returns now
        rewardParticipationRepository.countByRewardAndBeamerAndStatusBetween(participation.reward, participation.beamer, Completed(), now.minusDays(1), now) returns 0

        val actual = limiter.evaluate(participation)

        actual.status shouldEqual Completed()
      }
    }

    it("completes when under week limit") {
      new Fixtures {
        val participation = RewardParticipations.participation(thriceAWeek, serdar)

        timeService.now returns now
        rewardParticipationRepository.countByRewardAndBeamerAndStatusBetween(participation.reward, participation.beamer, Completed(), now.minusWeeks(1), now) returns 1

        val actual = limiter.evaluate(participation)

        actual.status shouldEqual Completed()
      }
    }

    it("limits when on limit") {
      new Fixtures {
        val participation = RewardParticipations.participation(thriceAWeek, serdar)

        timeService.now returns now
        rewardParticipationRepository.countByRewardAndBeamerAndStatusBetween(participation.reward, participation.beamer, Completed(), now.minusWeeks(1), now) returns 3

        val actual = limiter.evaluate(participation)

        actual.status shouldEqual Limited()
      }
    }

    it("limits when over limit") {
      new Fixtures {
        val participation = RewardParticipations.participation(twiceADay, serdar)

        timeService.now returns now
        rewardParticipationRepository.countByRewardAndBeamerAndStatusBetween(participation.reward, participation.beamer, Completed(), now.minusDays(1), now) returns 5

        val actual = limiter.evaluate(participation)

        actual.status shouldEqual Limited()
      }
    }

  }

  describe("evaluate BeamerTotalRewardLimit") {
    val reward = Rewards.reward100(BeamerTotalRewardLimit(250))

    it("completes when under limit") {
      new Fixtures {
        val participation = RewardParticipations.participation(reward, serdar)

        rewardParticipationRepository.findByRewardAndBeamerAndStatus(participation.reward, participation.beamer, Completed()) returns Nil

        val actual = limiter.evaluate(participation)

        actual.status shouldEqual Completed()
      }
    }

    it("limits when over limit") {
      new Fixtures {
        val participation = RewardParticipations.participation(reward, serdar)

        timeService.now returns now
        rewardParticipationRepository.findByRewardAndBeamerAndStatus(participation.reward, participation.beamer, Completed()) returns
          RewardParticipations.participation(reward, serdar) :: RewardParticipations.participation(reward, serdar) :: RewardParticipations.participation(reward, serdar) :: Nil

        val actual = limiter.evaluate(participation)

        actual.status shouldEqual Limited()
      }
    }

  }

  describe("evaluate TotalRewardBudgetLimit") {
    val dave = Beamers.dave
    val reward = Rewards.reward100(TotalRewardBudgetLimit(250))

    it("completes when under limit") {
      new Fixtures {
        val participation = RewardParticipations.participation(reward, serdar)

        rewardParticipationRepository.findByRewardAndStatus(participation.reward, Completed()) returns Nil

        val actual = limiter.evaluate(participation)

        actual.status shouldEqual Completed()
      }
    }

    it("limits when over limit") {
      new Fixtures {
        val participation = RewardParticipations.participation(reward, serdar)

        timeService.now returns now
        rewardParticipationRepository.findByRewardAndStatus(participation.reward, Completed()) returns
          RewardParticipations.participation(reward, dave) :: RewardParticipations.participation(reward, serdar) :: RewardParticipations.participation(reward, dave) :: Nil

        val actual = limiter.evaluate(participation)

        actual.status shouldEqual Limited()
      }
    }

  }

}
