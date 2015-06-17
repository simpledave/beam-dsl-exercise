package com.beam.exercise.dsl.rule

case class TotalRewardBudgetLimit(budget: BigDecimal) extends Limit {

  override def toString: String = s"total reward budget is ${budget.toLong}"

}
