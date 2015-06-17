package com.beam.exercise.dsl.rule

/**
 * The absolute budget for the reward across all beamers.
 *
 * @param budget The total budget
 */
case class TotalRewardBudgetLimit(budget: BigDecimal) extends Limit {

  override def toString: String = s"total reward budget is ${budget.toLong}"

}
