package com.gonerd.store.commands.supported_commands;

import com.gonerd.commands.Argument;
import com.gonerd.store.commands.StoreCommand;

import java.util.ArrayList;
import java.util.Scanner;


public class Help extends StoreCommand {

	public Help(Scanner scanner) {
		this(null, scanner);
	}

	public Help(ArrayList<Argument> args, Scanner scanner) {
		this.args = args;
		commandID = "help";
		object = "Store";

		params = new Argument[] {};

		allowRepeats = false;
	}

	@Override
	public void execute() {
		if (validateArgs()) {
			System.out.println("\nList of Commands:\n====================");
			for (var command : commandReader.supportedCommands) {
				System.out.println("    - " + command);
			}
		}
	}
}
