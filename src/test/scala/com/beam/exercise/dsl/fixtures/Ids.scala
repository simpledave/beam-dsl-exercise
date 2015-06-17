package com.beam.exercise.dsl.fixtures

import java.util.concurrent.atomic.AtomicLong

object Ids {
  private val next = new AtomicLong

  def nextId = next.incrementAndGet()

}
