package com.beam.exercise.dsl.rule

case class ConjunctionLimit(left: Limit, right: Limit) extends Limit {

  override def toString = s"($left and $right)"

}
