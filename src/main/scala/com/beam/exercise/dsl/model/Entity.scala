package com.beam.exercise.dsl.model

import org.joda.time.DateTime

trait Entity {

  def id: Long

  def created: DateTime

}
