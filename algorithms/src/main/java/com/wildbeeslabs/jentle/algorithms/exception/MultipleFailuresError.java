package com.wildbeeslabs.jentle.algorithms.exception;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Multiple failure {@link AssertionError} implementation
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MultipleFailuresError extends AssertionError {

    private static final long serialVersionUID = 191418478018791149L;

    private static final String EOL = System.getProperty("line.separator");

    private final String heading;
    private final List<Throwable> failures;

    public MultipleFailuresError(String heading, List<? extends Throwable> failures) {
        this.heading = isBlank(heading) ? "Multiple Failures" : heading.trim();

        this.failures = new ArrayList<>();
        for (Throwable failure : failures) {
            if (failure == null) {
                throw new NullPointerException("failures must not contain null elements");
            }
            this.failures.add(failure);
        }
    }

    @Override
    public String getMessage() {
        int failureCount = this.failures.size();

        if (failureCount == 0) {
            return this.heading;
        }

        // @formatter:off
        StringBuilder builder = new StringBuilder(this.heading)
            .append(" (")
            .append(failureCount).append(" ")
            .append(pluralize(failureCount, "failure", "failures"))
            .append(")")
            .append(EOL);
        // @formatter:on

        int lastIndex = failureCount - 1;
        for (Throwable failure : this.failures.subList(0, lastIndex)) {
            builder.append("\t").append(nullSafeMessage(failure)).append(EOL);
        }
        builder.append('\t').append(nullSafeMessage(this.failures.get(lastIndex)));

        return builder.toString();
    }

    public List<Throwable> getFailures() {
        return Collections.unmodifiableList(this.failures);
    }

    public boolean hasFailures() {
        return !this.failures.isEmpty();
    }

    private static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    private static String pluralize(int count, String singular, String plural) {
        return count == 1 ? singular : plural;
    }

    private static String nullSafeMessage(Throwable failure) {
        if (isBlank(failure.getMessage())) {
            return "<no message> in " + failure.getClass().getName();
        }
        return failure.getMessage();
    }

}
