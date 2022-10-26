package com.gonerd.store.commands.supported_commands;

import com.gonerd.commands.Argument;
import com.gonerd.store.commands.StoreCommand;
import com.gonerd.store.formatting.currencies.Currencies;

import java.util.ArrayList;
import java.util.Scanner;

public class ListCurrency extends StoreCommand implements Currencies {

	public ListCurrency(Scanner scanner) {
		this(null, scanner);
	}

	public ListCurrency(ArrayList<Argument> args, Scanner scanner) {
		this.args = args;
		commandID = "currency-list";
		object = "Store";

		params = new Argument[] {
		};

		allowRepeats = false;
	}

	@Override
	public void execute() {
		if (validateArgs()) {

			// List the currencies
			// There's not much to it
			System.out.println();
			System.out.println("List of Currencies:");
			System.out.println("====================");
			for (var currency : currencies) {
				System.out.println("    - " + currency.getName() + " (" + String.format("%1$,.3f", currency.getUsdConversionRate()) + " in USD)");
			}
		}
		// If not valid, we do nothing
	}
}
