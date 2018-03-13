package easyforex.util;

import java.util.List;

/**
 * Utility class for frequently used operations in strategies.
 * 
 * @author chazoshtare
 */
public final class EasyOperations {

    /**
     * Detects most recent cross of passed values. Typically these values
     * represent chart lines, where one changes faster than other.
     * <p>
     * Returned integer represents distance of the cross from most recent values
     * (most recent come last). Returns positive value if "fast" line crossed
     * "slow" upwards, negative if downwards and 0 if values did not cross.
     *
     * @param fastValues
     * @param slowValues
     * @return Positive integer if crossed upwards, negative if downwards, 0 if no cross.
     */
    public static Integer valuesCrossed(List<Double> fastValues, List<Double> slowValues) {
        if (fastValues.size() < 1 || slowValues.size() < 1) {
            throw new IllegalArgumentException("Size of both lists has to be greater than 1.");
        } else if (fastValues.size() != slowValues.size()) {
            throw new IllegalArgumentException("Lists have to be equal in size.");
        }

        Integer index = fastValues.size() - 1;
        Integer crossPosition = 0;

        while (index > 0) {
            crossPosition++;

            if (slowValues.get(index - 1) > fastValues.get(index - 1)
                    && slowValues.get(index) < fastValues.get(index)) {
                return crossPosition;
            } else if (slowValues.get(index - 1) < fastValues.get(index - 1)
                    && slowValues.get(index) > fastValues.get(index)) {
                return -crossPosition;
            }

            index--;
        }
        return 0;
    }
}
