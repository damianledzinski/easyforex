package easyforex.util;

import com.dukascopy.api.Filter;
import com.dukascopy.api.IBar;
import com.dukascopy.api.IContext;
import com.dukascopy.api.IHistory;
import com.dukascopy.api.IIndicators;
import com.dukascopy.api.ITick;
import com.dukascopy.api.Instrument;
import com.dukascopy.api.JFException;
import com.dukascopy.api.OfferSide;
import com.dukascopy.api.Period;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Utilities for easier using of indicators All indicators are calculated for
 * BID bars
 */
public final class EasyIndicators {

    private final IContext context;
    private final Instrument instrument;
    private final IIndicators indicators;
    private final IHistory history;

    public EasyIndicators(IContext context, Instrument instrument) {
        this.context = context;
        this.instrument = instrument;
        this.indicators = context.getIndicators();
        this.history = context.getHistory();
    }

    /**
     * Calculates the Average Directional Movement with default parameters for
     * last bar
     *
     * @param period
     * @param timePeriod
     * @return
     * @throws JFException
     */
    public double adx(Period period, int timePeriod) throws JFException {
        return adx(period, timePeriod, 1).get(0);
    }

    /**
     * Calculates the Average Directional Movement with default parameters for
     * last bars
     *
     * @param period
     * @param timePeriod
     * @param number     results number
     * @return
     * @throws JFException
     */
    public List<Double> adx(Period period, int timePeriod, int number) throws JFException {
        ITick tick = history.getLastTick(instrument);
        double[] result = indicators.adx(instrument, period, OfferSide.BID, timePeriod, Filter.WEEKENDS, number, tick.getTime(), 0);
        return Arrays.stream(result).boxed().collect(Collectors.toList());
    }

    /**
     * Calculates the value of Average True Range indicator for last bar.
     *
     * @param period     timeframe period
     * @param timePeriod number of bars used in calculating a single value
     * @return
     * @throws JFException
     */
    public Double atr(Period period, Integer timePeriod) throws JFException {
        return atr(period, timePeriod, 1).get(0);
    }

    /**
     * Calculates the value of Average True Range indicator for a number of last
     * bars.
     *
     * @param period     timeframe period
     * @param timePeriod number of bars used in calculating a single value
     * @param number     number of values returned
     * @return
     * @throws JFException
     */
    public List<Double> atr(Period period, Integer timePeriod, Integer number) throws JFException {
        ITick tick = history.getLastTick(instrument);
        double[] result = indicators.atr(instrument, period, OfferSide.BID, timePeriod, Filter.WEEKENDS, number, tick.getTime(), 0);
        return Arrays.stream(result).boxed().collect(Collectors.toList());
    }

    /**
     * Calculates the Bollinger Bands with default parameters for last bar
     *
     * @param period
     * @param timePeriod
     * @param nbDevUp
     * @param nbDevDn
     * @return
     * @throws JFException
     */
    public BBandResult bbands(Period period, int timePeriod, double nbDevUp, double nbDevDn) throws JFException {
        return bbands(period, timePeriod, nbDevUp, nbDevDn, 1).get(0);
    }

    /**
     * Calculates the Bollinger Bands with default parameters for last bars
     *
     * @param period
     * @param timePeriod
     * @param nbDevUp
     * @param nbDevDn
     * @param number     results number
     * @return
     * @throws JFException
     */
    public List<BBandResult> bbands(Period period, int timePeriod, double nbDevUp, double nbDevDn, int number) throws JFException {
        ITick tick = history.getLastTick(instrument);
        double[][] result = indicators.bbands(instrument, period, OfferSide.BID, IIndicators.AppliedPrice.CLOSE, timePeriod, nbDevUp, nbDevDn, IIndicators.MaType.EMA, Filter.WEEKENDS, number, tick.getTime(), 0);

        return IntStream.range(0, result[0].length)
                .mapToObj(i -> new BBandResult(result[0][i], result[1][i], result[2][i]))
                .collect(Collectors.toList());
    }

    /**
     * Calculates the value of a Moving Average of chosen type for last bar.
     *
     * @param period
     * @param timePeriod
     * @param maType
     * @return
     * @throws JFException
     */
    public Double ma(Period period, Integer timePeriod, IIndicators.MaType maType) throws JFException {
        return ma(period, timePeriod, 1, maType).get(0);
    }

    /**
     * Calculates the value of a Moving Average of chosen type.
     *
     * @param period
     * @param timePeriod
     * @param number
     * @param maType
     * @return
     * @throws JFException
     */
    public List<Double> ma(Period period, Integer timePeriod, Integer number, IIndicators.MaType maType) throws JFException {
        ITick tick = history.getLastTick(instrument);
        double[] result = indicators.ma(instrument, period, OfferSide.BID, IIndicators.AppliedPrice.CLOSE, timePeriod, maType, Filter.WEEKENDS, tick.getTime(), 0);
        return Arrays.stream(result).boxed().collect(Collectors.toList());
    }

    /**
     * Calculates the Moving Average Convergence/Divergence with default
     * parameters for last bar
     *
     * @param period
     * @param fastPeriod
     * @param slowPeriod
     * @param signalPeriod
     * @return
     * @throws JFException
     */
    public MACDResult macd(Period period, int fastPeriod, int slowPeriod, int signalPeriod) throws JFException {
        return macd(period, fastPeriod, slowPeriod, signalPeriod, 1).get(0);
    }

    /**
     * Calculates the Moving Average Convergence/Divergence with default
     * parameters for last bars
     *
     * @param period
     * @param fastPeriod
     * @param slowPeriod
     * @param signalPeriod
     * @param number       results number
     * @return
     * @throws JFException
     */
    public List<MACDResult> macd(Period period, int fastPeriod, int slowPeriod, int signalPeriod, int number) throws JFException {
        ITick tick = history.getLastTick(instrument);
        double result[][] = indicators.macd(instrument, period, OfferSide.BID, IIndicators.AppliedPrice.CLOSE, fastPeriod, slowPeriod, signalPeriod, Filter.WEEKENDS, 5, tick.getTime(), 0);

        return IntStream.range(0, result[0].length)
                .mapToObj(i -> new MACDResult(result[0][i], result[1][i], result[2][i]))
                .collect(Collectors.toList());
    }

    /**
     * Calculates the Exponential Moving Average with default parameters for
     * last bar
     *
     * @param period
     * @param timePeriod
     * @return
     * @throws JFException
     */
    public double ema(Period period, int timePeriod) throws JFException {
        return ema(period, timePeriod, 1).get(0);
    }

    /**
     * Calculates the Exponential Moving Average with default parameters for
     * last bars
     *
     * @param period
     * @param timePeriod
     * @param number     results number
     * @return
     * @throws JFException
     */
    public List<Double> ema(Period period, int timePeriod, int number) throws JFException {
        ITick tick = history.getLastTick(instrument);
        double[] result = indicators.ema(instrument, period, OfferSide.BID, IIndicators.AppliedPrice.CLOSE, timePeriod, Filter.WEEKENDS, number, tick.getTime(), 0);
        return Arrays.stream(result).boxed().collect(Collectors.toList());
    }

    /**
     * Calculates the Parabolic SAR with default parameters for last bar
     *
     * @param period
     * @param acceleration
     * @param maximum
     * @return
     * @throws JFException
     */
    public double sar(Period period, double acceleration, double maximum) throws JFException {
        return sar(period, acceleration, maximum, 1).get(0);
    }

    /**
     * Calculates the Parabolic SAR with default parameters for last bars
     *
     * @param period
     * @param acceleration
     * @param maximum
     * @param number       results number
     * @return
     * @throws JFException
     */
    public List<Double> sar(Period period, double acceleration, double maximum, int number) throws JFException {
        ITick tick = history.getLastTick(instrument);
        double[] result = indicators.sar(instrument, period, OfferSide.BID, acceleration, maximum, Filter.WEEKENDS, number, tick.getTime(), 0);
        return Arrays.stream(result).boxed().collect(Collectors.toList());
    }

    /**
     * Calculates the Simple Moving Average with default parameters for last bar
     *
     * @param period
     * @param timePeriod
     * @return
     * @throws JFException
     */
    public Double sma(Period period, int timePeriod) throws JFException {
        return sma(period, timePeriod, 1).get(0);
    }

    /**
     * Calculates the Simple Moving Average with default parameters for last
     * bars
     *
     * @param period
     * @param timePeriod
     * @param number     results number
     * @return
     * @throws JFException
     */
    public List<Double> sma(Period period, int timePeriod, int number) throws JFException {
        ITick tick = history.getLastTick(instrument);
        double[] result = indicators.sma(instrument, period, OfferSide.BID, IIndicators.AppliedPrice.CLOSE, timePeriod, Filter.WEEKENDS, number, tick.getTime(), 0);
        return Arrays.stream(result).boxed().collect(Collectors.toList());
    }

    /**
     * Calculates the Relative Strength Index with default parameters for last
     * bar
     *
     * @param period
     * @param timePeriod
     * @return
     * @throws JFException
     */
    public Double rsi(Period period, int timePeriod) throws JFException {
        return rsi(period, timePeriod, 1).get(0);
    }

    /**
     * Calculates the Relative Strength Index with default parameters for last
     * bars
     *
     * @param period
     * @param timePeriod
     * @param number     results number
     * @return
     * @throws JFException
     */
    public List<Double> rsi(Period period, int timePeriod, int number) throws JFException {
        ITick tick = history.getLastTick(instrument);
        double[] result = indicators.rsi(instrument, period, OfferSide.BID, IIndicators.AppliedPrice.CLOSE, timePeriod, Filter.WEEKENDS, number, tick.getTime(), 0);
        return Arrays.stream(result).boxed().collect(Collectors.toList());
    }

    /**
     * Calculates volume with default parameters for last bar
     *
     * @param period
     * @param timePeriod
     * @return
     * @throws JFException
     */
    public double volume(Period period, int timePeriod) throws JFException {
        return volume(period, timePeriod, 1).get(0);
    }

    /**
     * Calculates volume with default parameters for last bars
     *
     * @param period
     * @param timePeriod
     * @param number     results number
     * @return
     * @throws JFException
     */
    public List<Double> volume(Period period, int timePeriod, int number) throws JFException {
        ITick tick = history.getLastTick(instrument);
        double[] result = indicators.volume(instrument, period, OfferSide.BID, Filter.WEEKENDS, number, tick.getTime(), 0);
        return Arrays.stream(result).boxed().collect(Collectors.toList());
    }

    /**
     * Returns bars close prices for last bars
     *
     * @param period
     * @param number
     * @return
     * @throws JFException
     */
    public List<Double> barsClosePrices(Period period, int number) throws JFException {
        ITick tick = history.getLastTick(instrument);
        List<IBar> bars = history.getBars(instrument, period, OfferSide.BID, Filter.WEEKENDS, number, tick.getTime(), 0);
        return bars.stream().map(bar -> bar.getClose()).collect(Collectors.toList());
    }

    /**
     * Returns last bars
     *
     * @param period
     * @param number
     * @return
     * @throws JFException
     */
    public List<IBar> bars(Period period, int number) throws JFException {
        ITick tick = history.getLastTick(instrument);
        long time = history.getBarStart(period, tick.getTime());
        return history.getBars(instrument, period, OfferSide.BID, Filter.WEEKENDS, number, time, 0);
    }

    public static class BBandResult {

        private final double upperBand;
        private final double middleBand;
        private final double lowerBand;

        public BBandResult(double upperBand, double middleBand, double lowerBand) {
            this.upperBand = upperBand;
            this.middleBand = middleBand;
            this.lowerBand = lowerBand;
        }

        public double getUpperBand() {
            return upperBand;
        }

        public double getMiddleBand() {
            return middleBand;
        }

        public double getLowerBand() {
            return lowerBand;
        }

    }

    public static class MACDResult {

        private final double macd;
        private final double macdSignal;
        private final double macdHist;

        public MACDResult(double macd, double macdSignal, double macdHist) {
            this.macd = macd;
            this.macdSignal = macdSignal;
            this.macdHist = macdHist;
        }

        public double getMacd() {
            return macd;
        }

        public double getMacdSignal() {
            return macdSignal;
        }

        public double getMacdHist() {
            return macdHist;
        }

    }

}
