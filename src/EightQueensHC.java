/**
 * CSE486 - Fall 2013
 * Dr. Zmuda
 * 
 * Homework #3 - Eight Queens hill climbing algorithm with 5 restarts.
 * 
 * Uses steepest ascent method
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
	
	private static int[] queensIdx = new int[BOARD_SIZE];
	
	/**
	 * Convenience method to determine if the provided position
	 * coordinates are valid.
	 * @param r the row of the coordinate
	 * @param c the column of the coordinate
	 * @return true if the coordinates are on the board and false
	 * 				otherwise
	 */
	private static boolean isValidPosition(int r, int c) {
		return (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE);
	}
	
	private static int hasQueen(Square[][] board, int rPos, int cPos) {
		return board[rPos][cPos] == Square.QUEEN ? 1 : 0;
	}
	
	private static int calcCost(Square[][] board) {
		int boardCost = 0;
		Square[][] considered = new Square[BOARD_SIZE][BOARD_SIZE];
		for (int i=0; i<considered.length; i++) {
			for (int j=0; j<considered[i].length; j++) {
				considered[i][j] = Square.EMPTY;
			}
		}
		for (int i=0; i<board.length; i++) {
			for (int j=0; j<board[i].length; j++) {
				if (board[i][j] == Square.QUEEN) {
					boardCost += numAttacked(board,i,j,considered);
					considered[i][j] = Square.QUEEN;
				}
			}
		}
		return boardCost;
	}
	
	private static int numAttacked(Square[][] board, int rPos, int cPos,
			Square[][] considered) {
		int numAttacked = 0;
		for (int i=1; i<board.length; i++) {
			if (isValidPosition(rPos-i, cPos) && considered[rPos-i][cPos] != Square.QUEEN)
				numAttacked += hasQueen(board, rPos-i, cPos);
			if (isValidPosition(rPos+i, cPos) && considered[rPos+i][cPos] != Square.QUEEN)
				numAttacked += hasQueen(board, rPos+i, cPos);
			if (isValidPosition(rPos-i, cPos-i) && considered[rPos-i][cPos-i] != Square.QUEEN)
				numAttacked += hasQueen(board, rPos-i, cPos-i);
			if (isValidPosition(rPos+i, cPos+i) && considered[rPos+i][cPos+i] != Square.QUEEN)
				numAttacked += hasQueen(board, rPos+i, cPos+i);
			if (isValidPosition(rPos, cPos+i) && considered[rPos][cPos+i] != Square.QUEEN)
				numAttacked += hasQueen(board, rPos, cPos+i);
			if (isValidPosition(rPos, cPos-i) && considered[rPos][cPos-i] != Square.QUEEN)
				numAttacked += hasQueen(board, rPos, cPos-i);
			if (isValidPosition(rPos-i, cPos+i) && considered[rPos-i][cPos+i] != Square.QUEEN)
				numAttacked += hasQueen(board, rPos-i, cPos+i);
			if (isValidPosition(rPos+i, cPos-i) && considered[rPos+i][cPos-i] != Square.QUEEN)
				numAttacked += hasQueen(board, rPos+i, cPos-i);
		}
		return numAttacked;
	}
	
	private static Square[][] genRandomBoard() {
		board = new Square[BOARD_SIZE][BOARD_SIZE];
		// fill the board with empty squares
		for (int i=0; i<board.length; i++) {
			for (int j=0; j<board[i].length; j++) {
				board[i][j] = Square.EMPTY;
			}
		}
		// place a queen randomly in each column
		for (int i=0; i<board.length; i++) {
			int randCol = (int) (Math.random()*BOARD_SIZE);
			board[i][randCol] = Square.QUEEN;
		}
		// generate index array of where queens are located
		queensIdx = genIdx(board);
		
		return board;
	}
	
	private static int[] genIdx(Square[][] board) {
		int[] idx = new int[board.length];
		for (int i=0; i<board.length; i++) {
			for (int j=0; j<board[i].length; j++) {
				if (board[i][j] == Square.QUEEN) idx[i] = j;
			}
		}
		return idx;
	}
	
	private static boolean isSolution(Square[][] board) {
		return (calcCost(board) == 0);
	}
	
	private static Square[][] copy(Square[][] source) {
		Square[][] dest = new Square[source.length][source.length];
		for (int i=0; i<source.length; i++) {
			for (int j=0; j<source[i].length; j++) {
				dest[i][j] = source[i][j];
			}
		}
		return dest;
	}
	
	private static Square[][] ascend(Square[][] board) {
		int cost = calcCost(board);
		Square[][] bestBoard = copy(board);
		for (int i=0; (i<board.length); i++) {
			int origPos = queensIdx[i];
			for (int j=0; (j<board[i].length); j++) {
				if (j != origPos) {
					board = copy(bestBoard);
					queensIdx = genIdx(board);
					// remove queen from previous spot
					board[i][queensIdx[i]] = Square.EMPTY;
					// place queen in new spot
					board[i][j] = Square.QUEEN;
					// update queen location
					queensIdx[i] = j;
					int thisBoardCost = calcCost(board);
					if (thisBoardCost < cost) {
						// save this board configuration
						// for use in next iteration
						bestBoard = copy(board);
						cost = thisBoardCost;
					}
				}
			}
		}
		return bestBoard;
	}
	
	/**
	 * Adapted from Dr. Zmuda's recursive 8-queens solution.
	 * @param board
	 */
	private static void printBoard(Square[][] board) {
		String str = ".Q";
		for (int i=0; i<board.length; i++) {
			for (int j=0; j<board.length; j++) {
				System.out.print(str.charAt(board[i][j].ordinal()));
			}
			System.out.println();
		}
		System.out.println();
	}
	
	private static void climb() {
		boolean foundSoln = false;
		for (int i=0; (i<NUM_RESTARTS) && !foundSoln; i++) {
			board = genRandomBoard();
			if (isSolution(board)) {
				foundSoln = true;
				break;
			}
			while (true) {
				int curCost = calcCost(board);
				Square[][] neighbor = ascend(board);
				if (calcCost(neighbor) >= curCost) {
					break;
				}
				board = neighbor.clone();
			}
			if (isSolution(board)) foundSoln = true;
		}
		
		if (foundSoln) {
			System.out.println("Solution found:");
			printBoard(board);
		} else {
			System.out.println("No solution found this time. Maybe next time!");
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for (int i=0; i<10; i++) {
			climb();
		}
	}
}
