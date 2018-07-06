package easyforex.base;

import com.dukascopy.api.IAccount;
import com.dukascopy.api.IBar;
import com.dukascopy.api.IContext;
import com.dukascopy.api.IEngine;
import com.dukascopy.api.IMessage;
import com.dukascopy.api.IOrder;
import com.dukascopy.api.IStrategy;
import com.dukascopy.api.ITick;
import com.dukascopy.api.Instrument;
import com.dukascopy.api.JFException;
import com.dukascopy.api.Period;
import easyforex.util.MessageUtils;
import easyforex.util.StopLossTakeProfitUtils;
import easyforex.util.StrategyUtils;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Base class with common tools for strategies
 */
public abstract class AbstractStrategy implements IStrategy {

    protected IContext context;
    // this strategy name
    protected final String strategyName;
    protected final AtomicInteger orderCounter = new AtomicInteger();

    public AbstractStrategy() {
        strategyName = this.getClass().getSimpleName() + "_" + StrategyUtils.getUniqueNameSufix();
    }

    public AbstractStrategy(String strategyName) {
        this.strategyName = strategyName;
    }

    /**
     * TODO use super.onStart(context)
     *
     * @param context
     * @throws JFException
     */
    @Override
    public void onStart(IContext context) throws JFException {
        this.context = context;
        println(strategyName + " started.");
    }

    @Override
    public void onTick(Instrument instrument, ITick tick) throws JFException {
    }

    @Override
    public void onBar(Instrument instrument, Period period, IBar askBar, IBar bidBar) throws JFException {
    }

    @Override
    public void onMessage(IMessage message) throws JFException {
        if (message.getOrder() != null && message.getOrder().getLabel().startsWith(strategyName)) {
            MessageUtils.printMessage(context, message);
        }
    }

    @Override
    public void onAccount(IAccount account) throws JFException {
    }

    @Override
    public void onStop() throws JFException {
        for (IOrder order : getOrders()) {
            order.close();
        }
    }

    /**
     * Submit order, automatically labeling it.
     *
     * @param instrument
     * @param command
     * @param amount
     * @param stopLossPrice
     * @param takeProfitPrice
     * @return
     * @throws JFException
     */
    protected IOrder submitOrder(Instrument instrument, IEngine.OrderCommand command, double amount, double stopLossPrice, double takeProfitPrice) throws JFException {
        final String label = strategyName + "_" + orderCounter.incrementAndGet();
        double sl = StopLossTakeProfitUtils.round(instrument, stopLossPrice);
        double tp = StopLossTakeProfitUtils.round(instrument, takeProfitPrice);
        return StrategyUtils.submitOrder(context, label, instrument, command, amount, sl, tp);
    }

    /**
     * Returns this strategy orders (submited by {@code submitOrder} method)
     *
     * @return
     * @throws JFException
     */
    protected List<IOrder> getOrders() throws JFException {
        return context.getEngine().getOrders()
                .stream()
                .filter(order -> order.getLabel().startsWith(strategyName))
                .collect(Collectors.toList());
    }

    /**
     * An "alias" for Ducascopy's print method.
     *
     * @param obj
     */
    protected void println(Object obj) {
        context.getConsole().getOut().println(obj);
    }

    /**
     * Subscribe to given instruments.
     *
     * @param instruments instruments to subscribe
     */
    protected void subscribeInstruments(Instrument... instruments) {
        StrategyUtils.subscribeInstruments(context, instruments);
    }

    /**
     * Subscribe to given instruments.
     *
     * @param instruments instruments to subscribe
     */
    protected void subscribeInstruments(List<Instrument> instruments) {
        StrategyUtils.subscribeInstruments(context, instruments);
    }
}
