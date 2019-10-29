package engine;

import controller.Controller;
import controller.Situation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class GameState implements Serializable {
	public Board board;
	public ArrayList<Move> moveHistory;
	public int reversibleMoveCount;
	public boolean gameStopped;
	public HashMap<Situation, Integer> situationCounts;
}
