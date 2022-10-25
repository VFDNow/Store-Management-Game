package com.gonerd.store.commands.supported_commands;

import com.gonerd.commands.Argument;
import com.gonerd.commands.DataType;
import com.gonerd.store.Main;
import com.gonerd.store.commands.StoreCommand;
import com.gonerd.store.formatting.currencies.Currencies;
import com.gonerd.store.formatting.currencies.Currency;

import java.util.ArrayList;

public class CurrencyCommand extends StoreCommand implements Currencies {

	public CurrencyCommand() {
		this(null);
	}

	public CurrencyCommand(ArrayList <Argument> args) {
		this.args = args;
		commandID = "currency";
		object = "Store";

		params = new Argument[] {
				new Argument("What currency do you want to use?", DataType.STRING)
		};

		allowRepeats = false;
	}

	@Override
	public void execute() {
		if (validateArgs()) {

			// Get the input
			String currencyName = args.get(0).getValue();

			Currency currency = null;

			// Find the currency
			for (var elem: currencies) {
				if (elem.isCurrency(currencyName)) {
					currency = elem;
					break;
				}
			}

			// Tell them if it isn't a valid currency
			if (currency == null) {
				System.out.println(currencyName + " is not a currency supported by this program. Try <currency-list> for a list of currencies.");
				return;
			}

			if (!Main.currency.getName().equals(currency.getName())) {
				// Update the currency in our store
				Main.currency = currency;

				// Tell the user what we did
				System.out.println("Updated currency to" + ((currency.getPostfix() == null) ? ' ' + currency.getPrefix() : currency.getPostfix()) + '.');
			} else {
				System.out.println("Currency is already" + ((currency.getPostfix() == null) ? ' ' + currency.getPrefix() : currency.getPostfix()) + '!');
			}
		}
		// If not valid, we do nothing
	}
}