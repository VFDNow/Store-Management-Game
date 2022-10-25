package com.gonerd.store;

public class Store {
	private final String name; // No Mutator as name cannot change
	private final String asciiArt;

	// No Default Constructor as we cannot know what name to use
	// Generates the asciiArt from the name
	public Store(String name) {
		this.name = name;
		this.asciiArt = "                                                     ___\r\n                                             ___..--'  .`.\r\n                                    ___...--'     -  .` `.`.\r\n                           ___...--' _      -  _   .` -   `.`.\r\n                  ___...--'  -       _   -       .`  `. - _ `.`.\r\n           __..--'_______________ -Store hire  .`  _   `.   - `.`.\r\n        .`    _ /\\    -        .`      _     .`__________`. _  -`.`.\r\n      .` -   _ /  \\_     -   .` Store stock.` |" + centerName(getName()) + "|`.   - `.`.\r\n    .`-    _  /   /\\   -   .`        _   .`   |___________|  `. _   `.`.\r\n  .`________ /__ /_ \\____.`____________.`     ___       ___  - `._____`|\r\n    |   -  __  -|    | - |  ____  |   | | _  |   |  _  |   |  _ |\r\n    | _   |  |  | -  |   | |.--.| |___| |    |___|     |___|    |\r\n    |     |--|  |    | _ | |'--'| |---| |   _|---|     |---|_   |\r\n    |   - |__| _|        | |.--.| |   | |    |   |_  _ |   |    |\r\n ---``--._      |    |   |=|'--'|=|___|=|====|___|=====|___|====|\r\n -- . ''  ``--._| _  |  -|_|.--.|_______|__Store open___________|\r\n`--._           '--- |_  |:|'--'|:::::::|:::::::::::::::::::::::|\r\n_____`--._ ''      . '---'``--._|:::::::|::::Store currency:::::|\r\n----------`--._          ''      ``--.._|:::::::::::::::::::::::|\r\n`--._ _________`--._'        --     .   ''-----.................'\r\n     `--._----------`--._.  _           -- . :''           -    ''\r\n          `--._ _________`--._ :'              -- . :''      -- . ''";
	}

	public String getName() {
		return name;
	}

	// Allows us to center the name to 11 chars, so it fits within the box
	private String centerName(String name) {
		if (name.length() == 11) {
			return name;
		} else {
			int spacesNeeded = 11 - name.length();

			// Figure out if the spaces needed is odd by checking if the least significant bit is set
			// Binary literals used as we are checking bits
			if ((spacesNeeded & 0b1) == 0b1) {
				// Odd (We prefer name to be padded towards the left rather than right)
				name += ' ';
				spacesNeeded--;
			}

			// Spaces on either end
			StringBuilder halfSpaces = new StringBuilder(new String());

			halfSpaces.append(" ".repeat(Math.max(0, spacesNeeded / 2)));

			return halfSpaces + name + halfSpaces;
		}
	}

	@Override
	public String toString() {
		return asciiArt;
	}
}