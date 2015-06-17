package com.beam.exercise.dsl

import com.beam.exercise.dsl.rule.Limit

class LimitParser {

  /**
   * Parses a string into a Limit.
   *
   * @param rules The raw limits dsl string to parse
   * @return either the parse failure message or a Limit
   */
  def parse(rules: String): Either[String, Limit] = {
    ??? // TODO: implement this
  }

}
