package easyforex.base;

import com.dukascopy.api.IAccount;
import com.dukascopy.api.IBar;
import com.dukascopy.api.IContext;
import com.dukascopy.api.IMessage;
import com.dukascopy.api.IStrategy;
import com.dukascopy.api.ITick;
import com.dukascopy.api.Instrument;
import com.dukascopy.api.JFException;
import com.dukascopy.api.Period;
import easyforex.util.StrategyUtils;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Tool for running / testing multiple strategies simultaneously TODO example
 * code
 */
public abstract class StrategyComposer implements IStrategy {

	private final List<IStrategy> strategies = new ArrayList<>();
	private IContext originalContext;
	private IContext decorator;
	
	public final void addStrategy(IStrategy strategy) {
		strategies.add(strategy);
	}
	
	@Override
	public final void onStart(IContext context) throws JFException {
		originalContext = context;
		decorator = createContextDecorator(originalContext);
		
		for (IStrategy strategy: strategies) {
			strategy.onStart(decorator);
		}
	}

	@Override
	public final void onTick(Instrument instrument, ITick tick) throws JFException {
		for (IStrategy strategy: strategies) {
			strategy.onTick(instrument, tick);
		}
	}

	@Override
	public final void onBar(Instrument instrument, Period period, IBar askBar, IBar bidBar) throws JFException {
		for (IStrategy strategy: strategies) {
			strategy.onBar(instrument, period, askBar, bidBar);
		}
	}

	@Override
	public final void onMessage(IMessage message) throws JFException {
		for (IStrategy strategy: strategies) {
			strategy.onMessage(message);
		}
	}

	@Override
	public final void onAccount(IAccount account) throws JFException {
		for (IStrategy strategy: strategies) {
			strategy.onAccount(account);
		}
	}

	@Override
	public final void onStop() throws JFException {
		for (IStrategy strategy: strategies) {
			strategy.onStop();
		}
	}

	/**
	 * Create decorator for IContext, overrides {@code setSubscribedInstrument}
	 * method
	 *
	 * @return
	 */
	private static IContext createContextDecorator(final IContext originalContext) {

		InvocationHandler handler = (Object proxy, Method method, Object[] args) -> {

			if (method.getName().equals("setSubscribedInstruments")) {
				Set<Instrument> instruments = (Set<Instrument>) args[0];
				StrategyUtils.subscribeInstruments(originalContext, instruments);
				return null;
			}
			
			return method.invoke(proxy, args);
		};

		return (IContext) Proxy.newProxyInstance(IContext.class.getClassLoader(), new Class[]{IContext.class}, handler);
	}

}
