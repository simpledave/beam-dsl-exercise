package com.beam.exercise.dsl.fixtures

import com.beam.exercise.dsl.model.Beamer
import org.joda.time.{DateTimeZone, DateTime}

object Beamers {

  def dave = Beamer(Ids.nextId, new DateTime(DateTimeZone.UTC), "Dave")

  def oeyvind = Beamer(Ids.nextId, new DateTime(DateTimeZone.UTC), "Oeyvind")

  def serdar = Beamer(Ids.nextId, new DateTime(DateTimeZone.UTC), "Serdar")

}
