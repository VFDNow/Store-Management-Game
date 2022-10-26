package com.gonerd.store.commands.supported_commands;

import com.gonerd.commands.Argument;
import com.gonerd.commands.DataType;
import com.gonerd.store.Main;
import com.gonerd.store.commands.StoreCommand;
import com.gonerd.store.formatting.Format;
import com.gonerd.store.formatting.inflection.ModernEnglish;
import com.gonerd.store.objects.Item;

import java.util.ArrayList;
import java.util.Scanner;

public class Stock extends StoreCommand {

	public Stock(Scanner scanner) {
		this(null, scanner);
	}

	public Stock(ArrayList<Argument> args, Scanner scanner) {
		this.args = args;
		commandID = "stock";
		object = "Store";

		params = new Argument[] {
				new Argument("What would you like to stock?", DataType.STRING, "Item", null),
				new Argument("What price do you want buy the items for?", DataType.FLOAT),
				new Argument("What percent would you like to markup the items by? (30-50% is usually safe)", DataType.PERCENT)
		};

		allowRepeats = true;
		// We don't init repeatMessage here as we need to know the arguments first
	}

	@Override
	public void execute() {
		// Figure out if we need to calculate the unit price
		if (validateArgs(true)) {
			// We don't have markup or price, but that's fine, we'll get it later, if we need it, by calling validateArgs again, but with the false (default) argument

			// Get the internal name of the item
			String intName = Format.toInternal(Format.toTitleCase(args.get(0).getValue()));

			repeatMessage = "How many " + ModernEnglish.plural(intName) + " would you like to stock?";

			int repetitions = repeatPrompt();

			// We need to pop args down, as we have consumed the repeat value if it existed
			if (args.size() > params.length) {
				args.remove(args.size() - 1);
			}

			int itemIndex = -1; // We'll store the item here once we either find or create it
			// -1 is used here because it can't be an index, so it is a good init value

			int i = 0;
			for (var item : Main.items) {
				if (item.getName().equals(intName)) {
					itemIndex = i;
					break;
				}
				i++;
			}

			// Create the item if they don't exist and store the index
			if (itemIndex == -1) {
				itemIndex = Main.items.size();
				Main.items.add(new Item(intName));
			}

			// Figure out the markup percentage
			if (Main.items.get(itemIndex).getMarkup() == 0f || Main.items.get(itemIndex).getPrice() == 0f) {
				// Prompt the user for markup percentage and price
				if (validateArgs(false)) {
					Main.items.get(itemIndex).setPrice(Format.storeCur(args.get(1).getFValue()));
					Main.items.get(itemIndex).setMarkup(args.get(2).getFValue());
				} else {
					// Error
					return;
				}
			}

			// Find out what price we need to stock them for
			float totalPrice = Main.items.get(itemIndex).getPrice() * repetitions;
			if (totalPrice > Main.cash) {
				// They don't have enough money
				System.out.println("You need " + Format.fmtCurOut(totalPrice) + ", but you only have " + Format.fmtCurOut(Main.cash));
				return;
			}

			// Stock the things
			for (int c = 0; c < repetitions; c++) {
				Main.items.get(itemIndex).stockItem();
			}

			String amtOfItems = repetitions + " " + ModernEnglish.plural(intName, repetitions);

			// Charge that capitalist scum
			Main.cash -= totalPrice;

			System.out.println("Stocked " + amtOfItems + " for " + Format.fmtCurOut(totalPrice));
		}
		// If not valid, we do nothing
	}
}