package easyforex.util;

import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author chazoshtare
 */
public class EasyOperationsTest {

    @Test
    public void valuesCrossed_CrossedUpwardsRecent_Returns1() {
        List<Double> fast = Arrays.asList(0.5, 0.5, 2.0);
        List<Double> slow = Arrays.asList(1.0, 1.0, 1.0);

        Integer expected = 1;
        Integer result = EasyOperations.valuesCrossed(fast, slow);

        assertEquals(expected, result);
    }

    @Test
    public void valuesCrossed_CrossedDownwardsRecent_ReturnsMinus1() {
        List<Double> fast = Arrays.asList(1.5, 1.5, 0.5);
        List<Double> slow = Arrays.asList(1.0, 1.0, 1.0);

        Integer expected = -1;
        Integer result = EasyOperations.valuesCrossed(fast, slow);

        assertEquals(expected, result);
    }

    @Test
    public void valuesCrossed_NoCross_Returns0() {
        List<Double> fast = Arrays.asList(2.0, 2.0, 2.0);
        List<Double> slow = Arrays.asList(1.0, 1.0, 1.0);

        Integer expected = 0;
        Integer result = EasyOperations.valuesCrossed(fast, slow);

        assertEquals(expected, result);
    }

    @Test
    public void valuesCrossed_CrossedUpwardsEarly_Returns0() {
        List<Double> fast = Arrays.asList(1.0, 3.0, 3.0, 3.0, 3.0, 3.0);
        List<Double> slow = Arrays.asList(2.0, 2.0, 2.0, 2.0, 2.0, 2.0);

        Integer expected = 5;
        Integer result = EasyOperations.valuesCrossed(fast, slow);

        assertEquals(expected, result);
    }

    @Test
    public void valuesCrossed_CrossedDownwardsEarly_Returns0() {
        List<Double> fast = Arrays.asList(3.0, 1.0, 1.0, 1.0, 1.0, 1.0);
        List<Double> slow = Arrays.asList(2.0, 2.0, 2.0, 2.0, 2.0, 2.0);

        Integer expected = -5;
        Integer result = EasyOperations.valuesCrossed(fast, slow);

        assertEquals(expected, result);
    }
}
