package com.beam.exercise.dsl;

import com.beam.exercise.dsl.model.support.*;

/**
 * Convenience java references to the scala types.
 */
public interface Constants {

    TimePeriod DAY = new Day("day");

    TimePeriod WEEK = new Week("week");


    RewardParticipationStatus STARTED = new Started();

    RewardParticipationStatus COMPLETED = new Completed();

    RewardParticipationStatus LIMITED = new Limited();

}
