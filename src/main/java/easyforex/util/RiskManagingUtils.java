package easyforex.util;

import com.dukascopy.api.IAccount;
import com.dukascopy.api.IContext;
import com.dukascopy.api.IEngine;
import com.dukascopy.api.ITick;
import com.dukascopy.api.Instrument;
import com.dukascopy.api.JFException;

public final class RiskManagingUtils {

    /**
     * Calculates trade amount using stop loss given in pips and risk percent
     *
     * @param context
     * @param instrument
     * @param stopLossPips
     * @param riskPercent
     * @return
     * @throws JFException
     */
    public static double calculateTradeAmount(IContext context, Instrument instrument, int stopLossPips, double riskPercent) throws JFException {
        IAccount account = context.getAccount();
        double pipPriceOfMilion = context.getUtils().convertPipToCurrency(instrument, account.getAccountCurrency()) * 1000000;
        double baseEquity = account.getBaseEquity();
        double riskEquity = baseEquity * (riskPercent / 100.0);
        // TODO available leverage
        double tradeAmount = riskEquity / (pipPriceOfMilion * stopLossPips);
        return tradeAmount;
    }

    /**
     * Calculates trade amount using stop loss price and risk percent
     *
     * @param context
     * @param instrument
     * @param command
     * @param lastTick
     * @param stopLossPrice
     * @param riskPercent
     * @return
     * @throws JFException
     */
    public static double calculateTradeAmount(IContext context, Instrument instrument, IEngine.OrderCommand command, ITick lastTick, double stopLossPrice, double riskPercent) throws JFException {
        int stopLossPips = (int) StopLossTakeProfitUtils.getStopLossPips(command, instrument, lastTick, stopLossPrice);
        return RiskManagingUtils.calculateTradeAmount(context, instrument, stopLossPips, riskPercent);
    }
}
