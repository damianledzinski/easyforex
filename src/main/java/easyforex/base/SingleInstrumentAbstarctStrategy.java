package easyforex.base;

import com.dukascopy.api.Configurable;
import com.dukascopy.api.Filter;
import com.dukascopy.api.IBar;
import com.dukascopy.api.IContext;
import com.dukascopy.api.IEngine;
import com.dukascopy.api.IOrder;
import com.dukascopy.api.ITick;
import com.dukascopy.api.Instrument;
import com.dukascopy.api.JFException;
import com.dukascopy.api.OfferSide;
import com.dukascopy.api.Period;
import easyforex.util.EasyChart;
import easyforex.util.EasyIndicators;
import easyforex.util.SarUtils;
import easyforex.util.StopLossTakeProfitUtils;

/**
 * Base calss for strategies, that uses single instrument Adds chart support
 * methods
 */
public abstract class SingleInstrumentAbstarctStrategy extends AbstractStrategy {

	protected EasyIndicators indicators;
	protected EasyChart chart;

	@Configurable("Selected instrument")
	public Instrument selectedInstrument = Instrument.EURUSD;
	@Configurable("Stop loss in pips, dissabeld=0")
	public int stopLossPips = 0;
	@Configurable("Take profit in pips, dissabeld=0")
	public int takeProfitPips = 0;

	/**
	 * Run this method when you ovvedide this TODO example usage
	 *
	 * @param context
	 * @throws JFException
	 */
	@Override
	public void onStart(IContext context) throws JFException {
		super.onStart(context);
		this.indicators = new EasyIndicators(context, selectedInstrument);
		this.chart = new EasyChart(context, selectedInstrument);

		subscribeInstruments(selectedInstrument);
	}

	/**
	 * Submits order
	 *
	 * @param command
	 * @param amount
	 * @return
	 * @throws JFException
	 */
	protected IOrder submitOrder(IEngine.OrderCommand command, double amount) throws JFException {
		return submitOrder(command, amount, 0, 0);
	}

	/**
	 * Submits order
	 *
	 * @param command
	 * @param amount
	 * @param stopLossPrice alternative stop loss, 0 - dissabled
	 * @param takeProfitPrice alternative take profit, 0 - disabled
	 * @return
	 * @throws JFException
	 */
	protected IOrder submitOrder(IEngine.OrderCommand command, double amount, double stopLossPrice, double takeProfitPrice) throws JFException {
		ITick lastTick = getLastTick();
		double sl = command.isLong()
				? Math.min(stopLossPrice, StopLossTakeProfitUtils.getStopLossPrice(command, selectedInstrument, lastTick, stopLossPips))
				: Math.max(stopLossPrice, StopLossTakeProfitUtils.getStopLossPrice(command, selectedInstrument, lastTick, stopLossPips));
		double tp = command.isLong()
				? Math.min(takeProfitPrice, StopLossTakeProfitUtils.getTakeProfitPrice(command, selectedInstrument, lastTick, takeProfitPips))
				: Math.max(takeProfitPrice, StopLossTakeProfitUtils.getTakeProfitPrice(command, selectedInstrument, lastTick, takeProfitPips));

		IOrder order = submitOrder(selectedInstrument, command, amount, sl, tp);

		chart.markStopLoss(order, lastTick.getTime());
		chart.markTakeProfit(order, lastTick.getTime());
		return order;
	}

	/**
	 * Put this method to {@code onTick} for enablin trailing stop
	 *
	 * @param tick
	 * @throws JFException
	 */
	protected void trailingStop(ITick tick) throws JFException {
		for (IOrder order : getOrders()) {
			double stopLossPrice = StopLossTakeProfitUtils.getStopLossPrice(order.getOrderCommand(), selectedInstrument, tick, stopLossPips);

			if (order.isLong()) {
				if (stopLossPrice > order.getStopLossPrice() + selectedInstrument.getPipValue()) {
					order.setStopLossPrice(stopLossPrice);
					chart.markStopLoss(order, tick.getTime());
				}
			} else if (stopLossPrice < order.getStopLossPrice() - selectedInstrument.getPipValue()) {
				order.setStopLossPrice(stopLossPrice);
				chart.markStopLoss(order, tick.getTime());
			}
		}
	}

	/**
	 * Put this method to {@code onTick} for enablin SAR based trailing stop
	 *
	 * @param period
	 * @param askBar
	 * @param bidBar
	 * @param sarAcceleration SAR acceleration
	 * @param sarMaximum SAR maximum
	 * @throws JFException
	 */
	protected void sarBasedTrailingStop(Period period, IBar askBar, IBar bidBar, double sarAcceleration, double sarMaximum) throws JFException {
		for (IOrder order : getOrders()) {
			double stopLoss = SarUtils.getSarBasedStopLoss(context, order.isLong(), selectedInstrument, period, bidBar, askBar, sarAcceleration, sarMaximum);
			if (order.isLong()) {
				if (stopLoss > order.getStopLossPrice() + selectedInstrument.getPipValue()) {
					order.setStopLossPrice(stopLoss);
					chart.markStopLoss(order, getLastTick().getTime());
				}
			} else {
				if (stopLoss < order.getStopLossPrice() - selectedInstrument.getPipValue()) {
					order.setStopLossPrice(stopLoss);
					chart.markStopLoss(order, getLastTick().getTime());
				}
			}
		}
	}

	protected ITick getLastTick() throws JFException {
		return context.getHistory().getLastTick(selectedInstrument);
	}

}
