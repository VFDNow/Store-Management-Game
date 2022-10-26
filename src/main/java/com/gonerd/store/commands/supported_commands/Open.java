package com.gonerd.store.commands.supported_commands;

import com.gonerd.commands.Argument;
import com.gonerd.store.Main;
import com.gonerd.store.commands.StoreCommand;
import com.gonerd.store.formatting.Format;
import com.gonerd.store.formatting.inflection.ModernEnglish;
import com.gonerd.store.objects.Item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

public class Open extends StoreCommand {

	private final HashSet<String> complaints = new HashSet<>();

	public Open(Scanner scanner) {
		this(null, scanner);
	}

	public Open(ArrayList<Argument> args, Scanner scanner) {
		this.args = args;
		commandID = "open";
		object = "Store";

		params = new Argument[] {};

		allowRepeats = false;
	}

	@Override
	public void execute() {
		// Clear complaints
		complaints.clear();

		// We need at least one worker and at least one item
		if (Main.workers.size() == 0) {
			System.out.println("\nTo open your store, you need at least one worker!");
			return;
		}

		if (Main.items.size() == 0) {
			System.out.println("\nTo open your store, you need at least one item!");
			return;
		}

		// See if the workers strike for more pay
		// TODO: Add Strikes / Discontent w/ pay by looking at pay to store earnings
		// TODO: Add bargaining to resolve strikes 
		int workerCount = 0;
		for (var worker : Main.workers) {
			if (worker.getSalary() < 7.25f) {
				complaints.add("    - " + ModernEnglish.plural(worker.getName(), 2) + " don't want to work for below minimum wage (" + Format.fmtCurOut(7.25f) + "). They decided to strike! <wage> to change wage.");
			} else {
				workerCount += worker.getCount();
			}
		}

		// Check if they have an item that is stocked
		// Prepare to calculate average markup price
		double avgMarkup = 0d;
		double avgPrice = 0d;
		int stockCount = 0;
		boolean passedCheck = false;
		for (var item: Main.items) {
			if (item.getCount() > 0) {
				stockCount += Math.min(item.getCount(), 5000); // Clamped to 5000 per item to disable a few dirty hacks
				passedCheck = true;
			}
			avgPrice += item.getSalesPrice();
			avgMarkup += item.getMarkup();
		}

		// Find the average markup price & average price
		// We use this later
		avgMarkup /= Main.items.size();
		avgPrice /= Main.items.size();

		if (!passedCheck) {
			System.out.println("\nTo open your store, you need to have at least one item in stock!");
			return;
		}

		// We are ready to open the store!

		System.out.println("\nOpening " + Main.store.getName() + "!");    // Find number of employees!

		// The below economic model is based off a system of equations designed by Aiden Davis
		// It uses inverse variation, logistical growth, and logarithmic equations in conjunction to simulate a store
		Random random = new Random();

		long customerNumber;

		// Anon. Block to protect coefficients from being used elsewhere
		{
			double wsCoefficientA = 3d * Math.log10(random.nextDouble() + 0.0005d) + 10d;
			double wsCoefficientB = 1d / (1 + (Math.pow(2, -3.8416d * (1.05263158e-7 * stockCount - 1))));
			customerNumber = Math.max(Math.round(wsCoefficientA * wsCoefficientB * (workerCount + stockCount)), 0);
		}

		// Prep averages for logging
		double avgBC = 0d;
		int salesCount = 0;
		double salesCash = 0d;

		for (int i = 0; i < customerNumber; i++) {
			// Each customer has actions that they can do
			// The total actions cannot exceed 25, but the exact number is random; it could be 0.
			// Action number is weighted to the average markup price; eg: the store's overall attractiveness
			// Lower avgMarkup is better for attractiveness

			int browseCount;

			// Anon. Block to protect coefficients from being used elsewhere
			{
				double actRandCoefficient = 20 * Math.log10(random.nextDouble() + 0.0011d) + 60;
				browseCount = Math.toIntExact(Math.min(Math.max(Math.round(actRandCoefficient * Math.log10((100d * (double)workerCount) / (2d * (double)stockCount) + 1) - 10d / avgPrice), 0), 500));
			}

			// Remember those stats?
			avgBC += browseCount;

			// We look at a random range of items that are consecutive
			// This simulates laziness, so we don't walk across the store
			// We see the list of items as an infinite list, so if we start at the last index, we will go to the first
			// Number of item allows us to start at a random point in the list
			int nItem;
			nItemCalc: {
				int bound = Main.items.size() - 1;
				// Make sure nItem is always 0 if the length is 1
				if (bound == 0) {
					nItem = 0;
					break nItemCalc;
				}
				nItem = random.nextInt(bound);
			}

			for (int j = 0; j < browseCount; j++) {
				// Find the item
				Item item = Main.items.get(nItem);

				if (lookAtItem(random, item, avgPrice, avgMarkup, browseCount)) {
					// We want the item, let's buy it
					salesCash += item.getSalesPrice();
					salesCount++;

					// Decrement the stock
					Main.items.get(nItem).removeItem();
				}

				// We circle back to the start when we run out!
				nItem++;
				if (nItem > Main.items.size() - 1) {
					nItem = 0;
				}
			}
		}

		double totalPay = 0d;
		for (var worker : Main.workers) {
			totalPay += worker.getSalary() * 8d; // 8 hours in a store day
		}

		// Report of day
		// Start by calculating various statistics
		double avgSales = (double)salesCount / (double)customerNumber;
		avgBC /= (double)customerNumber;


		System.out.println("\nComplaints:\n====================");
		for (var complaint : complaints) {
			System.out.println(complaint);
		}
		System.out.println("\nDaily Statistics:\n====================");
		System.out.println("    " + customerNumber + " customers visited your store.");
		System.out.println("    Each of those customers looked at an average of " + String.format( "%.2f", Math.min(avgBC, Main.items.size())) + " items.");
		System.out.println("    Each of those customers bought an average of " + String.format( "%.2f", avgSales) + " items.");
		System.out.println("\nFinancial Report:\n====================");
		System.out.println("    Revenue: " + Format.fmtCurOut((float)salesCash));
		System.out.println("    Operating Costs: " + Format.fmtCurOut((float)totalPay));
		System.out.println("        - Employee Salaries: " + Format.fmtCurOut((float)totalPay));
		System.out.println("    Profit: " + Format.fmtCurOut((float)(salesCash - totalPay)));

		// Money for the day's work
		Main.cash += salesCash - totalPay;
	}

	private boolean lookAtItem(Random random, Item item, double avgPrice, double avgMarkup, int browseCount) {
		// Figure out if we want to buy it or not
		// The threshold variable decides the required percent of buying

		// BrowseCount can be 0, but if it is, the below code would never even get executed, so we don't have to worry about infinite results!
		double threshold = Math.min(Math.max(((1000/(item.getSalesPrice()+1000)) * (avgMarkup/(item.getMarkup()+avgMarkup)) * (avgPrice/(Math.abs(avgPrice - item.getSalesPrice()) + avgPrice)) / (double)browseCount), 0d), 1d);

		if (item.getCount() <= 10) {
			// Low stock, let's panic buy it
			threshold = Math.min(Math.max(threshold * 2, 0d), 1d);
		}

		if (!(random.nextDouble() <= threshold)) {
			return false;
		}
		// Below this point means that we do want it

		if (item.getCount() > 0) {
			// Now let's make sure the markup is somewhat reasonable
			if (item.getMarkup() < 5f) {
				return true;
			} else {
				// Let's complain, as we want it, but it's far too expensive!
				complaints.add("    - " + "I wanted to buy a " + item.getName() + ", but it is ridiculously expensive!");
				return false;
			}
		} else {
			// Let's complain, as we want it, but it's out of stock!
			complaints.add("    - " + "I wanted to buy a " + item.getName() + ", but it was out of stock!");
			// Well, we can't exactly buy it
			return false;
		}
	}
}
