package com.beam.exercise.dsl.rule

/**
 * The total amount that an individual beamer can be awarded for the given reward.
 *
 * @param amount The total amount
 */
case class BeamerTotalRewardLimit(amount: BigDecimal) extends Limit {

  override def toString: String = s"per beamer total reward is ${amount.toLong}"

}
