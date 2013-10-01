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
	/**
	 * Basic data structure to handle square representation for 8 queens
	 * Idea for this representation from Dr. Zmuda's n-queens recursive solution
	 */
	private enum Square {
		EMPTY,
		QUEEN
	};
	
	private static int solnFound = 0;
	
	/**
	 * A constant for the number of restarts to do in the algorithm
	 */
	private static final int NUM_RESTARTS = 5;
	
	/**
	 * The size of the board (and subsequently the number of queens) to simulate
	 */
	private static final int BOARD_SIZE = 30;
	
	/**
	 * The two dimensional Square array representing the board
	 */
	private static Square[][] board = null;
	
	/**
	 * The index array that contains the locations of the queens on the board
	 */
	private static int[] queensIdx = new int[BOARD_SIZE];
	
	/**
	 * Convenience method to determine if the provided position
	 * coordinates are valid.
	 * 
	 * @param r the row of the coordinate
	 * @param c the column of the coordinate
	 * @return true if the coordinates are on the board and false
	 * 				otherwise
	 */
	private static boolean isValidPosition(int r, int c) {
		return (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE);
	}
	
	/**
	 * Convenience method for determining if the coordinates contain a queen.
	 * Used primarily by numAttacked()
	 * 
	 * @param board the board to test
	 * @param rPos the row of the coordinates to test
	 * @param cPos the column of the coordinates to test
	 * @return 1 if the coordinate square has a queen, 0 otherwise
	 */
	private static int hasQueen(Square[][] board, int rPos, int cPos) {
		return board[rPos][cPos] == Square.QUEEN ? 1 : 0;
	}
	
	/**
	 * Method for the determining the heuristic cost estimate of
	 * the given board.
	 * 
	 * @param board the board to calculate the cost of
	 * @return an int representing the cost of the board
	 */
	private static int calcCost(Square[][] board) {
		int boardCost = 0;
		// considered variable so that queens are not double counted
		Square[][] considered = new Square[BOARD_SIZE][BOARD_SIZE];
		for (int i=0; i<considered.length; i++) {
			for (int j=0; j<considered[i].length; j++) {
				considered[i][j] = Square.EMPTY;
			}
		}
		// find each queen and check the number of queens it attacks
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
	
	/**
	 * A very messy convenience method for determining the number of queens
	 * the queen at the given coordinates attacks.
	 * 
	 * @param board the board configuration to check
	 * @param rPos the row of the queen
	 * @param cPos the column of the queen
	 * @param considered a board showing the queens that have already been
	 * 						considered, so as not to double count.
	 * @return an int representing the number of queens the queen at the given
	 * 			coordinates attacks
	 */
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
	
	/**
	 * Method to generate a random chessboard.
	 * 
	 * @return a Square[][] representing the random chessboard.
	 */
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
	
	/**
	 * Generates an int index array containing the locations of
	 * the queens given a certain board.
	 * 
	 * @param board the board to use for calculations
	 * @return an int array containing the locations of the queens
	 * 			for the supplied board.
	 */
	private static int[] genIdx(Square[][] board) {
		int[] idx = new int[board.length];
		for (int i=0; i<board.length; i++) {
			for (int j=0; j<board[i].length; j++) {
				if (board[i][j] == Square.QUEEN) idx[i] = j;
			}
		}
		return idx;
	}
	
	/**
	 * Method to determine if a board is a solution.
	 * 
	 * @param board the board to check
	 * @return true if the board is a solution, false otherwise.
	 */
	private static boolean isSolution(Square[][] board) {
		return (calcCost(board) == 0);
	}
	
	/**
	 * Convenience method for deep copying a board.
	 * 
	 * @param source the board to copy from
	 * @return a Square[][] that is a copy of source
	 */
	private static Square[][] copy(Square[][] source) {
		Square[][] dest = new Square[source.length][source.length];
		for (int i=0; i<source.length; i++) {
			for (int j=0; j<source[i].length; j++) {
				dest[i][j] = source[i][j];
			}
		}
		return dest;
	}
	
	/**
	 * Successor function that zeroes in on better and better states.
	 * 
	 * @param board the board configuration to work with
	 * @return the best successor state of all the children explored
	 * 			in the state space.
	 */
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
	 * Prints a board using '.' for empty and 'Q' for queen.
	 * Adapted from Dr. Zmuda's recursive 8-queens solution.
	 * 
	 * @param board the board to print.
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
	
	/**
	 * Function that contains the basic logic of the hillclimbing algorithm.
	 * 
	 * Generates a random board, generates its neighbors and saves the
	 * best one for use while its cost is less than the parent's cost.
	 * 
	 * After the loop breaks, if a solution has been generated, then it
	 * exits and prints that solution, otherwise it restarts with a random
	 * board and tries again. This cycle repeats until the random-restart
	 * count has been exhausted.
	 */
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
				Square[][] child = ascend(board);
				if (calcCost(child) >= curCost) {
					break;
				}
				board = child.clone();
			}
			if (isSolution(board)) foundSoln = true;
		}
		
		if (foundSoln) {
			System.out.println("Solution found:");
			solnFound++;
			printBoard(board);
		} else {
			System.out.println("No solution found this time. Maybe next time!");
		}
	}
	
	/**
	 * Main method, starts the climb
	 */
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		for (int i=0; i<1000; i++) {
			climb();
		}
		long end = System.currentTimeMillis();
		System.out.println(solnFound + " solutions found out of 1000 runs.");
		System.out.println("Average time to run: " + ((end-start)/1000) + "ms.");
	}
}
