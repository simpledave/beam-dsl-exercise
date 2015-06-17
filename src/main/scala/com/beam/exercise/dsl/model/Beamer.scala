package com.beam.exercise.dsl.model

import org.joda.time.DateTime

// What we call a customer
case class Beamer(id: Long, created: DateTime, name: String) extends Entity {

}
