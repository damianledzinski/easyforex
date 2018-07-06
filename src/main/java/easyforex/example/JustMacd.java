package easyforex.example;

import com.dukascopy.api.Configurable;
import com.dukascopy.api.IBar;
import com.dukascopy.api.IContext;
import com.dukascopy.api.IEngine;
import com.dukascopy.api.ITick;
import com.dukascopy.api.Instrument;
import com.dukascopy.api.JFException;
import com.dukascopy.api.Library;
import com.dukascopy.api.Period;
import easyforex.base.RiskManagingAbstractStrategy;

/**
 * Simple example strategy witch just buy/sell depends on MACD Hist
 */
@Library("easyforex-0.0.4.jar")
public class JustMacd extends RiskManagingAbstractStrategy {

    @Configurable("Selected period")
    public Period selectedPeriod = Period.FIFTEEN_MINS;
    @Configurable(value = "MACD fast period")
    public int macdFastPeriod = 12;
    @Configurable(value = "MACD slow period")
    public int macdSlowPeriod = 26;
    @Configurable(value = "MACD signal period")
    public int macdSignalPeriod = 9;

    public JustMacd() {
        // redefining default values
        stopLossPips = 25;
        takeProfitPips = 50;
    }

    @Override
    public void onStart(IContext context) throws JFException {
        super.onStart(context);

        chart.addIndicator("MACD", macdFastPeriod, macdSlowPeriod, macdSignalPeriod);
    }

    @Override
    public void onTick(Instrument instrument, ITick tick) throws JFException {
        trailingStop(tick);
    }

    @Override
    public void onBar(Instrument instrument, Period period, IBar askBar, IBar bidBar) throws JFException {
        if (!instrument.equals(selectedInstrument) || !period.equals(selectedPeriod)) {
            return;
        }

        // if there is no open order
        if (getOrders().isEmpty()) {

            double macdHist = indicators.macd(period, macdFastPeriod, macdSignalPeriod, macdSignalPeriod).getMacdHist();

            // buy or sell, depends on current macd hist
            if (macdHist > 0) {
                submitOrderCalculatingRisk(IEngine.OrderCommand.BUY);
            } else if (macdHist < 0) {
                submitOrderCalculatingRisk(IEngine.OrderCommand.SELL);
            }
        }
    }
}
