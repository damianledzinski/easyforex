package easyforex.util;

import com.dukascopy.api.IChart;
import com.dukascopy.api.IChartObject;
import com.dukascopy.api.IContext;
import com.dukascopy.api.IOrder;
import com.dukascopy.api.Instrument;
import com.dukascopy.api.drawings.IOhlcChartObject;
import java.awt.Color;

/**
 * Utility class for charts
 */
public final class EasyChart {

	private final IContext context;
	private final Instrument instrument;
	private final IChart chart;

	public EasyChart(IContext context, Instrument instrument) {
		this.context = context;
		this.instrument = instrument;
		this.chart = context.getChart(instrument);
	}

	/**
	 * Adds indicator to chart
	 *
	 * @param indicatorName
	 * @param params indicator parameters
	 */
	public void addIndicator(String indicatorName, Object... params) {
		addIndicator(indicatorName, null, params);
	}

	/**
	 * Adds indicator to chart
	 *
	 * @param indicatorName
	 * @param color indicator color or null if default
	 * @param params
	 */
	public void addIndicator(String indicatorName, Color color, Object... params) {
		if (chart != null) {
			chart.add(context.getIndicators().getIndicator(indicatorName), params, color != null ? new Color[]{color} : null, null, null);
		}
	}

	/**
	 * Enables showing info on chart
	 */
	public void showIndicatorInfo() {
		if (chart != null) {
			getIOhlcChartObject().setShowIndicatorInfo(true);
		}
	}

	/**
	 * Puts arrow in given place on the chart
	 *
	 * @param time
	 * @param price
	 */
	public void putUpArrow(long time, double price) {
		putUpArrow(time, price, null);
	}

	/**
	 * Puts arrow in given place on the chart
	 *
	 * @param time
	 * @param price
	 */
	public void putDownArrow(long time, double price) {
		putDownArrow(time, price, null);
	}

	/**
	 * Puts arrow in given place on the chart
	 *
	 * @param time
	 * @param price
	 * @param color
	 */
	public void putUpArrow(long time, double price, Color color) {
		if (chart != null) {
			IChartObject obj = chart.getChartObjectFactory().createSignalUp("up" + time, time, price);
			obj.setColor(color);
			chart.add(obj);
		}
	}

	/**
	 * Puts arrow in given place on the chart
	 *
	 * @param time
	 * @param price
	 * @param color
	 */
	public void putDownArrow(long time, double price, Color color) {
		if (chart != null) {
			IChartObject obj = chart.getChartObjectFactory().createSignalDown("down" + time, time, price);
			obj.setColor(color);
			chart.add(obj);
		}
	}

	public void markStopLoss(IOrder order, long time) {
		if (order.isLong()) {
			putUpArrow(time, order.getStopLossPrice(), Color.LIGHT_GRAY);
		} else {
			putDownArrow(time, order.getStopLossPrice(), Color.LIGHT_GRAY);
		}
	}

	public void markTakeProfit(IOrder order, long time) {
		if (order.getTakeProfitPrice() > 0) {
			if (order.isLong()) {
				putDownArrow(time, order.getTakeProfitPrice(), Color.GRAY);
			} else {
				putUpArrow(time, order.getTakeProfitPrice(), Color.GRAY);
			}
		}
	}

	private IOhlcChartObject getIOhlcChartObject() {
		return chart.getAll()
				.stream()
				.filter(obj -> obj instanceof IOhlcChartObject)
				.map(obj -> (IOhlcChartObject) obj)
				.findAny()
				.orElseGet(() -> {
					IOhlcChartObject ohlc = chart.getChartObjectFactory().createOhlcInformer();
					chart.add(ohlc);
					return ohlc;
				});
	}
}
