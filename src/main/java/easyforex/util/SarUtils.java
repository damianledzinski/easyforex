package easyforex.util;

import com.dukascopy.api.Filter;
import com.dukascopy.api.IBar;
import com.dukascopy.api.IContext;
import com.dukascopy.api.Instrument;
import com.dukascopy.api.JFException;
import com.dukascopy.api.OfferSide;
import com.dukascopy.api.Period;
import java.util.List;

/**
 * Utility class for methods based on SAR
 */
public final class SarUtils {

	/**
	 * Returms stop loss price based on sar
	 *
	 * @param context
	 * @param isLong Order command direction
	 * @param instrument
	 * @param period
	 * @param bidBar
	 * @param askBar
	 * @param sarAcceleration
	 * @param sarMaximum
	 * @return
	 * @throws JFException
	 */
	public static double getSarBasedStopLoss(IContext context, boolean isLong, Instrument instrument, Period period, IBar bidBar, IBar askBar, double sarAcceleration, double sarMaximum) throws JFException {
		if (isLong) {
			double[] sar = context.getIndicators().sar(instrument, period, OfferSide.BID, sarAcceleration, sarMaximum, Filter.WEEKENDS, 1, bidBar.getTime(), 0);
			return StopLossTakeProfitUtils.round(instrument, sar[0]);
		} else {
			double[] sar = context.getIndicators().sar(instrument, period, OfferSide.ASK, sarAcceleration, sarMaximum, Filter.WEEKENDS, 1, askBar.getTime(), 0);
			return StopLossTakeProfitUtils.round(instrument, sar[0]);
		}
	}

	/**
	 * Returns true if SAR for given bars is rising
	 *
	 * @param sarValue
	 * @param bar
	 * @return
	 */
	public static boolean isSarRising(double sarValue, IBar bar) {
		if (sarValue < bar.getLow()) {
			return true;
		}
		if (sarValue > bar.getHigh()) {
			return false;
		}
		throw new IllegalStateException("Sar value: " + sarValue + " does not fit to bar: " + bar);
	}

	/**
	 * Returns true if SAR for given bars is rising
	 *
	 * @param sarValue
	 * @param bar
	 * @return
	 */
	public static boolean isSarFalling(double sarValue, IBar bar) {
		if (sarValue > bar.getHigh()) {
			return true;
		}
		if (sarValue < bar.getLow()) {
			return false;
		}
		throw new IllegalStateException("Sar value: " + sarValue + " does not fit to bar: " + bar);
	}

	/**
	 * Returns true if last SAR is rising
	 *
	 * @param indicators
	 * @param period
	 * @param sarAcceleration
	 * @param sarMaximum
	 * @return
	 * @throws JFException
	 */
	public static boolean isSarCurrentlyRising(EasyIndicators indicators, Period period, double sarAcceleration, double sarMaximum) throws JFException {
		double sar = indicators.sar(period, sarAcceleration, sarMaximum);
		IBar bar = indicators.bars(period, 1).get(0);
		return isSarRising(sar, bar);
	}

	/**
	 * Returns true if last SAR is falling
	 *
	 * @param indicators
	 * @param period
	 * @param sarAcceleration
	 * @param sarMaximum
	 * @return
	 * @throws JFException
	 */
	public static boolean isSarCurrentlyFalling(EasyIndicators indicators, Period period, double sarAcceleration, double sarMaximum) throws JFException {
		double sar = indicators.sar(period, sarAcceleration, sarMaximum);
		IBar bar = indicators.bars(period, 1).get(0);
		return isSarFalling(sar, bar);
	}

	/**
	 * Returns number of last SAR values which are rising
	 *
	 * @param indicators
	 * @param period
	 * @param sarAcceleration
	 * @param sarMaximum
	 * @param maxNumber maximumum number to check
	 * @return
	 * @throws JFException
	 */
	public static int lastRisingSarsNumber(EasyIndicators indicators, Period period, double sarAcceleration, double sarMaximum, int maxNumber) throws JFException {
		List<Double> sars = indicators.sar(period, sarAcceleration, sarMaximum, maxNumber);
		List<IBar> bars = indicators.bars(period, maxNumber);
		int counter = 0;
		for (int i = maxNumber - 1; i >= 0; i--) {
			if (isSarRising(sars.get(i), bars.get(i))) {
				counter++;
			} else {
				break;
			}
		}
		return counter;
	}

	/**
	 * Returns number of last SAR values which are falling
	 *
	 * @param indicators
	 * @param period
	 * @param sarAcceleration
	 * @param sarMaximum
	 * @param maxNumber maximumum number to check
	 * @return
	 * @throws JFException
	 */
	public static int lastFallingSarsNumber(EasyIndicators indicators, Period period, double sarAcceleration, double sarMaximum, int maxNumber) throws JFException {
		List<Double> sars = indicators.sar(period, sarAcceleration, sarMaximum, maxNumber);
		List<IBar> bars = indicators.bars(period, maxNumber);
		int counter = 0;
		for (int i = maxNumber - 1; i >= 0; i--) {
			if (isSarFalling(sars.get(i), bars.get(i))) {
				counter++;
			} else {
				break;
			}
		}
		return counter;
	}
}
