package com.beam.exercise.dsl.model.support

sealed trait RewardParticipationStatus

case class Started() extends RewardParticipationStatus

case class Completed() extends RewardParticipationStatus

case class Limited() extends RewardParticipationStatus
