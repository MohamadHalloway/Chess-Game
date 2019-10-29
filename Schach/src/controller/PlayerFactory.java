/*
	Author: Adrian Samoticha
 */

package controller;

import controller.computerPlayers.*;

@SuppressWarnings("WeakerAccess")
public class PlayerFactory {
	public static final String[] playerTypeStrings = {"Human", "CPU Random", "CPU Shallow", "CPU Deep", "CPU Deeper", "CPU Aggressive", "CPU Cautious"};

	private static int findPlayer(String name) {
		for (int i = 0; i < playerTypeStrings.length; i++) if (name.equals(playerTypeStrings[i])) return i;
		throw new IllegalArgumentException(String.format("Unknown player name \"%s\".", name));
	}

	public static Player createComputerPlayer(boolean isWhite, int level) {
		if (level == 1) return new ComputerPlayerRandom(isWhite);
		if (level == 2) return new ComputerPlayerShallow(isWhite);
		if (level == 3) return new ComputerPlayerDeep(isWhite, 3, 3);
		if (level == 4) return new ComputerPlayerDeep(isWhite, 3, 5);
		if (level == 5) return new ComputerPlayerSimon(isWhite, new AggressiveMoveSelector());
		if (level == 6) return new ComputerPlayerSimon(isWhite, new SafeMoveSelector());
		throw new IllegalArgumentException(String.format("Invalid level: %d", level));
	}

	/**
	 * reads the string from the new game dialog combo boxes and returns a fitting instance of a player
	 */
	public static Player getPlayerByString(String name, boolean isWhite, HumanPlayerInput input) {
		int ind = findPlayer(name);

		// create player
		if (ind == 0) return new HumanPlayer(isWhite, input);
		else return createComputerPlayer(isWhite, ind);
	}
}
