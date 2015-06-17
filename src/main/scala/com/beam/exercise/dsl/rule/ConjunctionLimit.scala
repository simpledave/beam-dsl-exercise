package com.beam.exercise.dsl.rule

case class ConjunctionLimit(terms: Seq[Limit]) extends Limit {

  override def toString = s"(${terms.mkString(" and ")})"

}
