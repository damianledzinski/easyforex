package easyforex.base;

import com.dukascopy.api.Configurable;
import com.dukascopy.api.IEngine;
import com.dukascopy.api.IOrder;
import com.dukascopy.api.ITick;
import com.dukascopy.api.JFException;
import easyforex.util.RiskManagingUtils;
import easyforex.util.StopLossTakeProfitUtils;

public abstract class RiskManagingAbstractStrategy extends SingleInstrumentAbstarctStrategy {

    @Configurable(value = "Risk percent per trade", stepSize = 0.1)
    public double riskPercent = 3;

    /**
     * Submits order using risk percent
     *
     * @param command
     * @return
     * @throws JFException
     */
    protected IOrder submitOrderCalculatingRisk(IEngine.OrderCommand command) throws JFException {
        double stopLossPrice = StopLossTakeProfitUtils.getStopLossPrice(command, selectedInstrument, getLastTick(), stopLossPips);
        double takeProfitPrice = StopLossTakeProfitUtils.getTakeProfitPrice(command, selectedInstrument, getLastTick(), takeProfitPips);
        return submitOrderCalculatingRisk(command, stopLossPrice, takeProfitPrice);
    }

    /**
     * Submits order using risk percent
     *
     * @param command
     * @param stopLossPrice   stop loss price - which risk will be calculated
     *                        for
     * @param takeProfitPrice take profit price
     * @return
     * @throws JFException
     */
    protected IOrder submitOrderCalculatingRisk(IEngine.OrderCommand command, double stopLossPrice, double takeProfitPrice) throws JFException {
        ITick lastTick = getLastTick();
        double tradeAmount = RiskManagingUtils.calculateTradeAmount(context, selectedInstrument, command, lastTick, stopLossPrice, riskPercent);
        return submitOrder(command, tradeAmount, stopLossPrice, takeProfitPrice);
    }

    /**
     * Submits order using risk percent
     *
     * @param command
     * @param stopLossPips   stop loss in pips - which risk will be calculated
     *                       for
     * @param takeProfitPips take profit in pips
     * @return
     * @throws JFException
     */
    protected IOrder submitOrderCalculatingRiskForPips(IEngine.OrderCommand command, int stopLossPips, int takeProfitPips) throws JFException {
        double tradeAmount = RiskManagingUtils.calculateTradeAmount(context, selectedInstrument, stopLossPips, riskPercent);
        ITick lastTick = getLastTick();
        return submitOrder(command,
                tradeAmount,
                StopLossTakeProfitUtils.getStopLossPrice(command, selectedInstrument, lastTick, stopLossPips),
                StopLossTakeProfitUtils.getTakeProfitPrice(command, selectedInstrument, lastTick, takeProfitPips)
        );
    }
}
