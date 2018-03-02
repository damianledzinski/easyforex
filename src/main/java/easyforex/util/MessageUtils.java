package easyforex.util;

import com.dukascopy.api.IConsole;
import com.dukascopy.api.IContext;
import com.dukascopy.api.IMessage;

/**
 * Utility methods for messages
 *
 * @author damian
 */
public class MessageUtils {

	public static void printMessage(IContext context, IMessage message) {
		IConsole console = context.getConsole();
		if (message.getOrder() != null) {
			String label = message.getOrder().getLabel();
			switch (message.getType()) {
				case ORDER_FILL_OK:
				case ORDER_CHANGED_OK:
					break;
				case ORDER_SUBMIT_OK:
					console.getOut().println(label + " <INFO> " + message.getType() + " amount=" + message.getOrder().getAmount());
					break;
				case ORDER_CLOSE_OK:
				case ORDERS_MERGE_OK:
					if (message.getOrder().getProfitLossInPips() > 0) {
						console.getInfo().println(label + " <INFO> "
								+ message.getType() + "/" + message.getReasons() + " "
								+ message.getOrder().getProfitLossInPips() + "/" + message.getOrder().getProfitLossInAccountCurrency());
					} else {
						console.getWarn().println(label + " <INFO> "
								+ message.getType() + "/" + message.getReasons() + " "
								+ message.getOrder().getProfitLossInPips() + "/" + message.getOrder().getProfitLossInAccountCurrency());
					}
					break;
				case NOTIFICATION:
					console.getNotif().println(label + " <NOTICE> " + message.getContent());
					break;
				case ORDER_CHANGED_REJECTED:
				case ORDER_CLOSE_REJECTED:
				case ORDER_FILL_REJECTED:
				case ORDER_SUBMIT_REJECTED:
				case ORDERS_MERGE_REJECTED:
					console.getErr().println(label + " <WARN> " + message.getType());
					break;
				default:
					console.getErr().println(label + " (" + message.getType() + ") " + message.getContent());
					break;
			}
		}
	}

}
