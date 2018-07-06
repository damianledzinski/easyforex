package easyforex.util;

import com.dukascopy.api.IContext;
import com.dukascopy.api.IEngine;
import com.dukascopy.api.IOrder;
import com.dukascopy.api.Instrument;
import com.dukascopy.api.JFException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Utility class for strategies
 */
public final class StrategyUtils {

    /**
     * Subscribe for given instruments
     *
     * @param context     strategy context
     * @param instruments instrumensts to subscribe
     */
    public static void subscribeInstruments(IContext context, Instrument... instruments) {
        subscribeInstruments(context, Arrays.asList(instruments));
    }

    /**
     * Subscribe for given instruments
     *
     * @param context     strategy context
     * @param instruments instrumensts to subscribe
     */
    public static void subscribeInstruments(IContext context, Collection<Instrument> instruments) {
        Set<Instrument> subscribed = new HashSet<>(context.getSubscribedInstruments());
        subscribed.addAll(instruments);
        context.setSubscribedInstruments(subscribed, true);
    }

    /**
     * Submits order, using market place and slippage=5
     *
     * @param context
     * @param label           order label, must be unique
     * @param instrument
     * @param command         order command
     * @param amount          trade amount in millions
     * @param stopLossPrice
     * @param takeProfitPrice
     * @return
     * @throws JFException
     */
    public static IOrder submitOrder(IContext context, String label, Instrument instrument, IEngine.OrderCommand command, double amount, double stopLossPrice, double takeProfitPrice) throws JFException {
        if (command != IEngine.OrderCommand.BUY && command != IEngine.OrderCommand.SELL) {
            throw new RuntimeException("Using this method with " + command + " command has no sense");
        }
        return context.getEngine().submitOrder(label, instrument, command, amount, 0, 5, stopLossPrice, takeProfitPrice);
    }

    public static String getUniqueNameSufix() {
        return UUID.randomUUID().toString().substring(0, 6);
    }
}
