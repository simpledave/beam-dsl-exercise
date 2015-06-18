package com.beam.exercise.dsl;

import com.beam.exercise.dsl.model.RewardParticipation;
import com.beam.exercise.dsl.model.support.*;
import com.beam.exercise.dsl.repository.RewardParticipationRepository;
import com.beam.exercise.dsl.rule.*;
import com.beam.exercise.dsl.service.TimeService;
import org.joda.time.DateTime;
import scala.collection.JavaConversions;
import scala.math.BigDecimal;

import java.util.List;

public class JavaRewardLimiter {

    // I don't know of a better way to get access to these scala sealed traits / constants.
    // I believe these statics are okay as I understand that sealed traits are immutable.
    private static final TimePeriod Day = new Day("day");
    private static final TimePeriod Week = new Week("week");
    private static final RewardParticipationStatus Started = new Started();
    private static final RewardParticipationStatus Completed = new Completed();
    private static final RewardParticipationStatus Limited = new Limited();

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
    public RewardParticipation evaluate(final RewardParticipation participation) {

        if (!Started.equals(participation.status())) {
            // This throw should be replaced with however the application is designed to handle exceptional situations.
            throw new IllegalArgumentException();
        }

        return checkLimit(participation, participation.reward().limit());
    }

    private RewardParticipation checkLimit(RewardParticipation participation, final Limit limit) {

        if (limit instanceof ConjunctionLimit) {
            participation = checkLimit(participation, ((ConjunctionLimit) limit).left());
            if (!Limited.equals(participation.status())) {
                participation = checkLimit(participation, ((ConjunctionLimit) limit).right());
            }
        } else if (limit instanceof BeamerMaximumCyclesLimit) {
            participation = checkLimit(participation, (BeamerMaximumCyclesLimit) limit);
        } else if (limit instanceof BeamerTotalRewardLimit) {
            participation = checkLimit(participation, (BeamerTotalRewardLimit) limit);
        } else if (limit instanceof TotalRewardBudgetLimit) {
            participation = checkLimit(participation, (TotalRewardBudgetLimit) limit);
        } else {
            throw new IllegalArgumentException();
        }
        return participation;
    }

    private RewardParticipation checkLimit(final RewardParticipation participation, final BeamerMaximumCyclesLimit limit) {

        final TimePeriod timePeriod = limit.timePeriod();
        final DateTime now = timeService.now();
        final DateTime fromDate;

        // This if-then-else with equality checks seems cumbersome compared to constants or enums.
        // I guess that it is more succinct in scala.
        if (Day.equals(timePeriod)) {
            fromDate = now.minusDays(1);
        } else if (Week.equals(timePeriod)) {
            fromDate = now.minusWeeks(1);
        } else {
            // This throw should be replaced with however the application is designed to handle exceptional situations.
            throw new IllegalArgumentException();
        }

        final long beamerRewardCount =
                rewardParticipationRepository.countByRewardAndBeamerAndStatusBetween(
                        participation.reward(), participation.beamer(), new Completed(),
                        fromDate, now);

        // Test limits
        if (beamerRewardCount >= limit.value()) {
            return participation.setStatus(Limited);
        } else {
            return participation.setStatus(Completed);
        }
    }

    protected RewardParticipation checkLimit(final RewardParticipation participation, final BeamerTotalRewardLimit limit) {

        final List<RewardParticipation> rewardParticipations =
                JavaConversions.seqAsJavaList(
                        rewardParticipationRepository.findByRewardAndBeamerAndStatus(
                                participation.reward(), participation.beamer(), new Completed()));

        // I guess this would be neater in scala.
        BigDecimal total = new BigDecimal(new java.math.BigDecimal(0D));

        // With Java 8 we could use lambdas to sum big-decimal values. In any case this loop is not bad.
        for (RewardParticipation rewardParticipation : rewardParticipations) {
            total = total.$plus(rewardParticipation.reward().award());
        }

        // Test limits
        if (total.$greater(limit.amount())) {
            return participation.setStatus(Limited);
        } else {
            return participation.setStatus(Completed);
        }
    }

    protected RewardParticipation checkLimit(final RewardParticipation participation, final TotalRewardBudgetLimit limit) {

        final List<RewardParticipation> rewardParticipations =
                JavaConversions.seqAsJavaList(
                        rewardParticipationRepository.findByRewardAndStatus(
                                participation.reward(), new Completed()));

        // I guess this would be neater in scala.
        BigDecimal total = new BigDecimal(new java.math.BigDecimal(0D));

        // With Java 8 we could use lambdas to sum big-decimal values. In any case this loop is not bad.
        for (RewardParticipation rewardParticipation : rewardParticipations) {
            total = total.$plus(rewardParticipation.reward().award());
        }

        // Test limits
        if (total.$greater(limit.budget())) {
            return participation.setStatus(Limited);
        } else {
            return participation.setStatus(Completed);
        }
    }
}
