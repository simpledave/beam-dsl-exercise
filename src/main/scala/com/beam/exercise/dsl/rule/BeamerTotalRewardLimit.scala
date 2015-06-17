package com.beam.exercise.dsl.rule

case class BeamerTotalRewardLimit(amount: BigDecimal) extends Limit {

  override def toString: String = s"per beamer total reward is ${amount.toLong}"

}
