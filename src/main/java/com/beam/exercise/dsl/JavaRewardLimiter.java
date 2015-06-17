package com.beam.exercise.dsl;

import com.beam.exercise.dsl.model.RewardParticipation;
import com.beam.exercise.dsl.repository.RewardParticipationRepository;
import com.beam.exercise.dsl.service.TimeService;

public class JavaRewardLimiter {
    private final TimeService timeService;
    private final RewardParticipationRepository rewardParticipationRepository;

    public JavaRewardLimiter(TimeService timeService, RewardParticipationRepository rewardParticipationRepository) {
        this.timeService = timeService;
        this.rewardParticipationRepository = rewardParticipationRepository;
    }

    /**
     * Evaluates if this participation is limited or not.
     * <p/>
     * Participations with a state not set to Started are invalid input.
     * A participation under the limits should have it's status set to Completed.
     * A participation that hits the limits should have it's status set to Limited.
     *
     * @param participation The participation to evaluate
     * @return the participation with the state set according to the outcome of the limits
     */
    public RewardParticipation evaluate(RewardParticipation participation) {
        throw new UnsupportedOperationException(); // TODO: impl
    }

}
