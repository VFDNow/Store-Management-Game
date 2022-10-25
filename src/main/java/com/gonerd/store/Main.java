package com.gonerd.store;

import com.gonerd.commands.CommandReader;
import com.gonerd.store.formatting.Format;
import com.gonerd.store.formatting.currencies.Currency;
import com.gonerd.store.objects.Item;
import com.gonerd.store.objects.Worker;

import java.util.Scanner;
import java.util.ArrayList;

public class Main extends CommandReader {
	public static ArrayList<Worker> workers = new ArrayList<>();
	public static ArrayList<Item> items = new ArrayList<>();
	public static Currency currency = Format.currencies[0];
	public static float cash = 10000f;
	public static boolean continueLoop = true;
	public static Store store;

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		store = new Store("Test Store");
		System.out.println(store + "\r\n");
		System.out.println("Welcome to " + store.getName() + ". Start by <hire>-ing employees or <stock>-ing items");
		while (continueLoop) {
			System.out.print("> ");
			CommandReader.execCommand(CommandReader.prepInput(scanner.nextLine()));
				System.out.println();
				printInfo();
				System.out.println();
				System.out.println("Enter a command. Type <help> for a list of commands!");
		}
	}

	private static void printInfo() {
		System.out.println("Store:");
		System.out.println("    Cash: " + Format.fmtCurOut(cash));
		System.out.println();
		System.out.println("    Workers:");
		for (var worker : workers) {
			System.out.print("        " + worker);
		}
		System.out.println();
		System.out.println("    Stock:");
		for (var item : items) {
			System.out.print("        " + item);
		}
	}

	private static void printSaveString() {
		// TODO: Implement
		// TODO: Not a string of commands, but json object.
		// TODO: Do this last!
	}
}