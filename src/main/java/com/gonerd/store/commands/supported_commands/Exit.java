package com.gonerd.store.commands.supported_commands;

import com.gonerd.commands.Argument;
import com.gonerd.store.commands.StoreCommand;

import java.util.ArrayList;
import java.util.Scanner;

public class Exit extends StoreCommand {

	public Exit(Scanner scanner) {
		this(null, scanner);
	}

	public Exit(ArrayList<Argument> args, Scanner scanner) {
		this.args = args;
		commandID = "exit";
		object = "Store";

		params = new Argument[] {};

		allowRepeats = false;
	}

	@Override
	public void execute() /*throws RuntimeException*/ {
		// Exit the store
		System.out.println("\nThank you for playing Aiden's Store Management Project!");
		System.out.println("\nExiting...");

		System.exit(0);
	}
}
