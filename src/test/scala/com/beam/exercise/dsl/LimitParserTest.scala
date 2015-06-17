package com.beam.exercise.dsl

import com.beam.exercise.dsl.model.support.{Day, Week}
import com.beam.exercise.dsl.rule.{BeamerMaximumCyclesLimit, BeamerTotalRewardLimit, ConjunctionLimit, TotalRewardBudgetLimit}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FunSpec, Matchers}

@RunWith(classOf[JUnitRunner])
class LimitParserTest extends FunSpec with Matchers {

  describe("parse") {
    val parser = new LimitParser

    it("fails to parse invalid input") {
      parser.parse("blah") match {
        case Left(msg) => msg shouldEqual "..."
        case Right(_) => fail("expected a parse failure")
      }
    }

    it("parses ConjunctionLimit") {
      val one = BeamerMaximumCyclesLimit(Day(), 2)
      val two = BeamerMaximumCyclesLimit(Week(), 3)

      parser.parse(ConjunctionLimit(one, two).toString) match {
        case Right(l: ConjunctionLimit) =>
          l.left shouldEqual one
          l.right shouldEqual two
        case _ => fail("expected ConjunctionLimit")
      }
    }

    it("parses BeamerMaximumCyclesLimit per day") {
      val rule = BeamerMaximumCyclesLimit(Day(), 2)
      parser.parse(rule.toString) match {
        case Right(l: BeamerMaximumCyclesLimit) =>
          l.timePeriod shouldEqual rule.timePeriod
          l.value shouldEqual rule.value
        case _ => fail("expected BeamerMaximumCyclesLimit")
      }
    }

    it("parses BeamerMaximumCyclesLimit per week") {
      val rule = BeamerMaximumCyclesLimit(Week(), 3)
      parser.parse(rule.toString) match {
        case Right(l: BeamerMaximumCyclesLimit) =>
          l.timePeriod shouldEqual rule.timePeriod
          l.value shouldEqual rule.value
        case _ => fail("expected BeamerMaximumCyclesLimit")
      }
    }

    it("parses BeamerTotalRewardLimit per week") {
      val rule = BeamerTotalRewardLimit(100000)
      parser.parse(rule.toString) match {
        case Right(l: BeamerTotalRewardLimit) =>
          l.amount shouldEqual rule.amount
        case _ => fail("expected BeamerTotalRewardLimit")
      }
    }

    it("parses TotalRewardBudgetLimit per week") {
      val rule = TotalRewardBudgetLimit(100000)
      parser.parse(rule.toString) match {
        case Right(l: TotalRewardBudgetLimit) =>
          l.budget shouldEqual rule.budget
        case _ => fail("expected TotalRewardBudgetLimit")
      }
    }

  }

}
