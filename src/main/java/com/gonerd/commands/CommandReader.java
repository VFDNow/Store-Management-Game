package com.gonerd.commands;

import com.gonerd.store.formatting.Format;

import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class CommandReader {

	public static Command[] supportedCommands = new Command[0];
	protected static List<List<? extends CommandObject>> objects = Collections.emptyList();

	// Get a list of object keywords
	static ArrayList<String> loadObjectKeywords() {
		ArrayList<String> result = new ArrayList<>();
		for (var objectArray : objects) {
			for (var obj : objectArray) {
				result.add(obj.getID());
			}
		}

		return result;
	}

	// In your commands, make sure the object class is capitalized to match the object class name
	// Ambiguity is not allowed; only one command can exist per commandID
	static String findClass(String objID) throws RuntimeException {
		ArrayList<String> commandKeywords = loadCommandKeywords(supportedCommands);
		// Handle store & null cases
		if (objID == null) {
			return null;
		}

		objID = Format.toInternal(objID);
		if (commandKeywords.contains(objID)) {
			return null;
		}

		for (var objectArray : objects) {
			for (var obj : objectArray) {
				if (obj.getID().equals(objID)) {
					return obj.getClass().toString().substring(6);
				}
			}
		}

		throw new RuntimeException("Expected an object type for object " + objID); // Someone didn't correctly add their object type to both of the required methods
	}

	// Find the command

	static Command findCommand(String object, String cmdID, ArrayList<Argument> args) {
		String objClass = findClass(object);

		for (var cmd : supportedCommands) {
			if (cmd.toString().equals(cmdID)) {
				// Name matches, now let's validate the object
				if (cmd.getObject().equals(objClass) || objClass == null) {
					cmd.passArgs(args);
					return cmd;
				} else {
					System.out.println("Object type <" + objClass + "> doesn't support the command <" + cmd + ">.");
					return null;
				}
			}
		}

		throw new RuntimeException("Expected a command type for keyword " + cmdID); // Someone didn't correctly add their command to both of the required methods
	}

	// Process a line of input
	protected static void execCommand(String command) {

		ArrayList<String> commandKeywords = loadCommandKeywords(supportedCommands);
		ArrayList<String> objectKeywords = loadObjectKeywords();

		// Figure out how many commands are in the string using regex
		String[] commands = command.split(";");
		command = commands[0];

		// Now we know what command to execute!
		// Get all tokens ex: {hire, worker, 100}
		String[] tokens = command.split("\\s");

		// Prepare to parse the arguments
		ArrayList<Argument> args = new ArrayList<>();

		// Prepare to parse the command
		String commandID;
		String object = null;

		// Figure out if the user entered a command as the first line or an object
		if (objectKeywords.contains(Format.toInternal(tokens[0])) && tokens.length > 1) {
			// This is a pseudo-argument
			object = tokens[0];
			args.add(new Argument(tokens[0]));
			// It is up to the command to check that the correct object is passed; we do not implement that here
		}

		// Make sure the command is valid
		String cmdToken = (object != null)? tokens[1] : tokens[0];
		if (commandKeywords.contains(cmdToken)) {
			commandID = cmdToken;
			if (object == null && tokens.length > 1) {
				args.add(new Argument(tokens[1]));
			}
		} else {
			// Unrecognized command or object
			// We customize the error based on the length of the message
			System.out.println("<" + cmdToken + "> is not a command" + ((tokens.length > 1 && !Objects.equals(cmdToken, tokens[1]))? " or object." : '.'));
			return;
		}

		// Parse the definitive arguments (Non-Object arguments, always tokens 2 and up)
		for (int i = 2; i < tokens.length; i++) {
			String token = tokens[i];
			args.add(new Argument(token));
		}

		// Find the command and exec it
		Command cmd = findCommand(object, commandID, args);
		if (cmd != null) {
			cmd.execute();
		}

		// Recursively process all other commands on the line (We do this after we exec the current command)
		// We do this now in case the commands rely on each other
		for (int i = 1; i < commands.length; i++) {
			execCommand(prepInput(commands[i]));
		}
	}

	// We are not expecting a keyword
	static boolean isKeyword(String input, String allowOf) {
		String inputInt = Format.toInternal(input);
		ArrayList<String> commandKeywords = loadCommandKeywords(supportedCommands);
		ArrayList<String> objectKeywords = loadObjectKeywords();

		// Check that it is not the name of an object or command
		if (objectKeywords.contains(inputInt) || Objects.equals(inputInt, "store")) {
			if (!Objects.equals(findClass(input), allowOf)) {
				System.out.println("Unexpected keyword <" + input + ">. You cannot create an object with the name of a command or another object.");
				return true;
			}
		} else if (commandKeywords.contains(input)) {
			System.out.println("Unexpected keyword <" + input + ">. You cannot create an object with the name of a command or another object.");
			return true;
		}
		return false;
	}

	// We are expecting an object
	static boolean isNotObject(String input, String type) {
		input = Format.toInternal(input);
		ArrayList<String> objectKeywords = loadObjectKeywords();

		// Check that it is the name of an object
		if (!objectKeywords.contains(input)) {
			System.out.println("Unknown object <" + input + ">. Expecting the name of an existing object.");
			return true;
		} else if (!Objects.equals(findClass(input), type) && !type.equals("any")) {
			System.out.println("Expected a <" + type + "> instead of a <" + findClass(input) + ">.");
			return true;
		}
		return false;
	}

	protected static String prepInput(String input) {
		input = input.trim();
		input = input.toLowerCase();
		input = input.replaceAll("\\s+", " ");

		Scanner scanner = new Scanner(System.in);

		// Make sure we don't do anything unless they actually enter something
		if (input.matches("\\s*")) {
			System.out.print("> ");
			input = prepInput(scanner.nextLine());
		}

		// Check for illegal characters
		String[] matches = Pattern.compile("[^-a-zA-Z0-9_%;. 	]+")
				                   .matcher(input)
				                   .results()
				                   .map(MatchResult::group)
				                   .toArray(String[]::new);

		if (matches.length > 0) {
			System.out.println();
			System.out.print("You have entered " + matches.length + " invalid characters (");
			for (var match : matches) {
				System.out.print(" " + match + " ");
			}
			System.out.println("). Please re-enter your command.");
			System.out.print("> ");
			return prepInput(scanner.nextLine());
		}
		scanner.close();
		return input;
	}

	static boolean typeCheck(String str, DataType expected, boolean repeatArg) {
		DataType result;
		String floatIntErrorMsg = "";

		// Prep str for our checks
		str = str.replaceAll("\\s", "");

		// Make sure it passes percentage check
		if (expected == DataType.PERCENT) {
			str = str.replaceAll("(%|percent)", "");
		}

		if (str.matches("(?!0+$)[0-9]{1,9}")) {
			// Nothing but numbers, so it must be an int
			result = DataType.INT;
		} else if (str.matches("[0-9]*(\\.[0-9]+)?")) {
			// Can't be an integer, must be a float because it has only digits and a dot
			// We don't truncate floats into ints unless they end in .0
			// Start out by checking if it is an int disguised as a float
			float f = Float.parseFloat(str);

			if (f % 1 == 0) {
				// No decimal
				if (f <= 2147483647 && f > 0) {
					// Valid integer
					result = DataType.INT;
				} else {
					// float; it is too big or small to be an int
					floatIntErrorMsg = " (Integers can't exceed 2147483647 and have to be at least 1).";
					result = DataType.FLOAT;
				}
			} else {
				// float; it has a decimal
				result = DataType.FLOAT;
			}

		} else {
			// Must be a string
			result = DataType.STRING;
		}

		// Figure out if and what an error message should be
		// ! This defines what causes the typeCheck to fail !
		String errMsg = "";
		if (expected != null && expected != result) {
			if (expected == DataType.INT) {
				if (result == DataType.FLOAT) {
					errMsg = "Expected integer instead of (" + str + ")." + floatIntErrorMsg;
				} else {
					errMsg = "Expected integer instead of (" + str + ").";
				}
			} else if (expected == DataType.FLOAT) {
				if (result == DataType.STRING) {
					errMsg = "Expected float/double instead of (" + str + ").";
				}
			} else if (expected == DataType.PERCENT) {
				if (result == DataType.STRING) {
					errMsg = "Expected percentage instead of (" + str + ").";
				}
			}
			// No if (STRING) as anything is technically a string!
		}

		if (repeatArg && !errMsg.equals("")) {
			errMsg += " Running command once.";
		}

		if (!errMsg.equals("")) {
			System.out.println("\r\n" + errMsg);
			return false;
		}

		return true;
	}

	static float parsePercent(String str) {
		// Prep str for conversion
		str = str.replaceAll("\\s", "");
		str = str.replaceAll("(%|percent)", "");

		DataType dataType;

		// Find if it's stored as an integer or float
		if (str.matches("(?!0+$)[0-9]{1,9}")) {
			// Nothing but numbers, so it must be an int
			dataType = DataType.INT;
		} else {
			// We don't truncate floats into ints unless they end in .0
			// Start out by checking if it is an int disguised as a float
			float f = Float.parseFloat(str);

			if (f % 1 == 0) {
				// No decimal
				if (f <= 2147483647 && f > 0) {
					// Valid integer
					dataType = DataType.INT;
				} else {
					// float; it is too big or small to be an int
					dataType = DataType.FLOAT;
				}
			} else {
				// float; it has a decimal
				dataType = DataType.FLOAT;
			}
		}

		// Figure out if we need to div by 100
		if (dataType == DataType.INT) {
			return Float.parseFloat(str) / 100.0f;
		} else {
			return Float.parseFloat(str);
		}
	}

	static ArrayList<String> loadCommandKeywords(Command[] supportedCommands) {
		ArrayList<String> result = new ArrayList<>();
		for (var cmd : supportedCommands) {
			result.add(cmd.toString());
		}
		return result;
	}

}
