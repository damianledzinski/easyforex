package easyforex.util;

import com.dukascopy.api.Filter;
import com.dukascopy.api.IBar;
import com.dukascopy.api.IContext;
import com.dukascopy.api.IEngine;
import com.dukascopy.api.ITick;
import com.dukascopy.api.Instrument;
import com.dukascopy.api.JFException;
import com.dukascopy.api.OfferSide;
import com.dukascopy.api.Period;
import java.math.BigDecimal;

/**
 * Stop loss and take profit utilities TODO unit test for sl calculating
 */
public final class StopLossTakeProfitUtils {

	/**
	 * Calculates stop loss price from stop loss in pips
	 *
	 * @param command
	 * @param instrument
	 * @param lastTick
	 * @param stopLossPips stop loss, if zero then result will be 0
	 * @return
	 */
	public static double getStopLossPrice(IEngine.OrderCommand command, Instrument instrument, ITick lastTick, double stopLossPips) {
		if (stopLossPips <= 0) {
			return 0;
		}
		if (command.isLong()) {
			double price = lastTick.getBid();
			return price - stopLossPips * instrument.getPipValue();
		} else {
			double price = lastTick.getAsk();
			return price + stopLossPips * instrument.getPipValue();
		}
	}

	/**
	 * Calculates stop loss in pips from stop loss price
	 *
	 * @param command
	 * @param instrument
	 * @param lastTick
	 * @param stopLossPrice stop loss, if zero then result will be 0
	 * @return
	 */
	public static double getStopLossPips(IEngine.OrderCommand command, Instrument instrument, ITick lastTick, double stopLossPrice) {
		if (stopLossPrice <= 0) {
			return 0;
		}
		if (command.isLong()) {
			double price = lastTick.getBid();
			return (price - stopLossPrice) / instrument.getPipValue();
		} else {
			double price = lastTick.getAsk();
			return (stopLossPrice - price) / instrument.getPipValue();
		}
	}

	/**
	 * Calculates take profit price from take profit in pips
	 *
	 * @param command
	 * @param instrument
	 * @param lastTick
	 * @param takeProfitPips take profit, if zero then result will be 0
	 * @return
	 */
	public static double getTakeProfitPrice(IEngine.OrderCommand command, Instrument instrument, ITick lastTick, double takeProfitPips) {
		if (takeProfitPips <= 0) {
			return 0;
		}
		if (command.isLong()) {
			double price = lastTick.getBid();
			return price + takeProfitPips * instrument.getPipValue();
		} else {
			double price = lastTick.getAsk();
			return price - takeProfitPips * instrument.getPipValue();
		}
	}

	/**
	 * Calculates take profit in pips from take profit price
	 *
	 * @param command
	 * @param instrument
	 * @param lastTick
	 * @param takeProfitPrice take profit, if zero then result will be 0
	 * @return
	 */
	public static double getTakeProfitPips(IEngine.OrderCommand command, Instrument instrument, ITick lastTick, double takeProfitPrice) {
		if (takeProfitPrice <= 0) {
			return 0;
		}
		if (command.isLong()) {
			double price = lastTick.getBid();
			return (price - takeProfitPrice) / instrument.getPipValue();
		} else {
			double price = lastTick.getAsk();
			return (takeProfitPrice - price) / instrument.getPipValue();
		}
	}

	/**
	 * Rounds value to pip scale + 1 scale
	 *
	 * @param instrument
	 * @param value
	 * @return
	 */
	public static double round(Instrument instrument, double value) {
		int decimalPlaces = instrument.getPipScale() + 1;
		return (new BigDecimal(value)).setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}
