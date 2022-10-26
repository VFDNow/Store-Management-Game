package com.gonerd.commands;

import java.util.ArrayList;
import java.util.Scanner;

public abstract class Command {

	protected static CommandReader commandReader;
	protected String commandID; /* No Accessor or Mutator, constructor modification only
	 * toString() works as an accessor*/
	protected Scanner scanner;
	protected Argument[] params;

	protected String object; // Capitalize object as you would a class name

	protected ArrayList<Argument> args;

	protected String repeatMessage;

	protected boolean allowRepeats;

	public final String getObject() {
		return object;
	}

	public final Argument[] getParams() {
		return params;
	}

	public final ArrayList<Argument> getArgs() {
		return args;
	}

	public final void passArgs(ArrayList<Argument> args) {
		this.args = args;
	}

	public abstract void execute();

	public boolean validateArgs() {
		return validateArgs(false);
	}

	// We update the args if some are missing, but can still return false under a few conditions
	public boolean validateArgs(boolean ignoreTooMany) {
		// First, let's make sure that they didn't give us too many args
		if (!ignoreTooMany && (args.size() > params.length && (!allowRepeats || args.size() - params.length != 1))) {
			System.out.println("The command <" + commandID + "> only accepts " + (params.length + ((allowRepeats) ? 1 : 0)) + " arguments, but you provided " + args.size() + '.');
			return false;
		}

		// Now, let's check that we have enough, and if we don't let's get them!
		if (args.size() < params.length) {
			// Loop through params and find the ones we are missing, and add them
			for (int i = 0; i < params.length; i++) {
				if (i > args.size() - 1) {
					System.out.println();
					System.out.println(params[i].getMissingMessage());
					System.out.print("> ");
					String argValue = commandReader.prepInput(scanner.nextLine(), scanner);
					args.add(new Argument(params[i].getMissingMessage(), params[i].getDataType(), argValue));

					// Make sure they didn't give us a keyword unless we expect an OBJECT
					if (params[i].getDataType() != DataType.OBJECT && commandReader.isKeyword(argValue, params[i].getAllowOf())) {
						return false;
					} else if (params[i].getDataType() == DataType.OBJECT && commandReader.isNotObject(argValue, params[i].getAllowOf())) {
						return false;
					}
				}
			}
		}

		// Allows us to validate the repeat count
		Argument[] validators;

		if (allowRepeats) {
			validators = new Argument[params.length + 1];
			int i = 0;
			for (var param : params) {
				validators[i] = param;
				i++;
			}
			validators[validators.length - 1] = new Argument("REPEATER", DataType.INT);
		} else {
			validators = params;
		}

		// Now we finally have all the args!
		// What next?
		// Oh darn it, we have to check if they are valid.

		int i = 0;

		for (var arg : args) {
			String argNoSpaces = arg.getValue().replaceAll("\\s", "");
			DataType expected = validators[i].getDataType();

			if (commandReader.typeCheck(argNoSpaces, expected, validators[i].getMissingMessage().equals("REPEATER"))) {
				if (expected == DataType.INT) {
					args.get(i).setIValue((int) Float.parseFloat(argNoSpaces));
				} else if (expected == DataType.FLOAT) {
					args.get(i).setFValue(Float.parseFloat(argNoSpaces));
				} else if (expected == DataType.PERCENT) {
					// We store percents as floats, but we need to be careful as it could be an integer or not. This uses a separate function
					args.get(i).setFValue(commandReader.parsePercent(argNoSpaces));
				} else {
					// Make sure they didn't give us a keyword unless we expect an OBJECT
					if (expected != DataType.OBJECT && commandReader.isKeyword(arg.getValue(), validators[i].getAllowOf())) {
						return false;
					} else if (expected == DataType.OBJECT && commandReader.isNotObject(arg.getValue(), validators[i].getAllowOf())) {
						return false;
					}
				}
			} else {
				return false;
			}
			i++;
		}

		// Passes all checks
		return true;
	}

	// Ask the user if they wish to repeat this command
	// We automatically account for an extra argument at the end and use it instead if it's present
	public int repeatPrompt() throws RuntimeException {
		if (!allowRepeats) {
			throw new RuntimeException("The command " + commandID + " calls repeatPrompt(), but doesn't accept repeats!");
		}

		// Find the amount of times we need to repeat and store as a string to parse in result
		String result;

		if (args.size() > params.length) {
			result = Integer.toString(args.get(args.size() - 1).getIValue());
		} else {
			System.out.println();
			System.out.println(repeatMessage);

			System.out.print("> ");
			String input = scanner.nextLine();
			input = commandReader.prepInput(input, scanner);
			input = input.replaceAll("\\s", "");

			if (commandReader.typeCheck(input, DataType.INT, true)) {
				result = input;
			} else {
				result = "1";
			}
		}

		return (int) Float.parseFloat(result);
	}

  /*
  Easy toString to find the command name
  Accessor method not provided for commandID, as toString takes its place
  Final as no Command should behave unpredictably.
  */

	@Override
	public final String toString() {
		return commandID;
	}

}
