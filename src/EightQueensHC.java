/**
 * CSE486 - Fall 2013
 * Dr. Zmuda
 * 
 * Homework #3 - Eight Queens hill climbing algorithm with 5 restarts.
 * 
 * Uses first choice/steepest ascent/random selection
 * 
 * @author Steven Angles
 *
 */
public class EightQueensHC {
	private enum Square {
		EMPTY,
		QUEEN
	};
	
	private static final int NUM_RESTARTS = 5;
	
	private static final int BOARD_SIZE = 8;
	
	private static Square[][] board = null;
	
	private static boolean isValidPosition(Square[][] board, int r, int c) {
		return (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE);
	}
	
	private static int calcCost(Square[][] board) {
		return -1;
	}
	
	private static int numAttacked(Square[][] board, int rPos, int cPos) {
		return -1;
	}
	
	private static Square[][] genRandomBoard() {
		board = new Square[BOARD_SIZE][BOARD_SIZE];
		return null;
	}
	
	private static boolean isSolution(Square[][] board) {
		return false;
	}
	
	private static Square[][] ascend() {
		return null;
	}
	
	private static void climb() {
		board = genRandomBoard();
		boolean foundSoln = false;
		Square[][] best = board;
		for (int i=0; (i<NUM_RESTARTS) && !foundSoln; i++) {
			int curCost = calcCost(best);
			Square[][] neighbor = ascend();
			if (calcCost(neighbor) < curCost) {
				best = neighbor;
			}
			if (isSolution(best)) foundSoln = true;
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}
}
