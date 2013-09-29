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
	
	private static boolean isValidPosition(int r, int c) {
		return (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE);
	}
	
	private static int hasQueen(Square[][] board, int rPos, int cPos) {
		return board[rPos-1][cPos] == Square.QUEEN ? 1 : 0;
	}
	
	private static int calcCost(Square[][] board) {
		int boardCost = 0;
		for (int i=0; i<board.length; i++) {
			for (int j=0; j<board[i].length; j++) {
				if (board[i][j] == Square.QUEEN) {
					boardCost += numAttacked(board,i,j);
				}
			}
		}
		return boardCost;
	}
	
	private static int numAttacked(Square[][] board, int rPos, int cPos) {
		int numAttacked = 0;
		for (int i=0; i<board.length; i++) {
			if (isValidPosition(rPos-1, cPos))
				numAttacked += hasQueen(board, rPos-1, cPos);
			if (isValidPosition(rPos+1, cPos))
				numAttacked += hasQueen(board, rPos+1, cPos);
			if (isValidPosition(rPos-1, cPos-1))
				numAttacked += hasQueen(board, rPos-1, cPos-1);
			if (isValidPosition(rPos+1, cPos+1))
				numAttacked += hasQueen(board, rPos+1, cPos+1);
			if (isValidPosition(rPos, cPos+1))
				numAttacked += hasQueen(board, rPos, cPos+1);
			if (isValidPosition(rPos-1, cPos-1))
				numAttacked += hasQueen(board, rPos, cPos-1);
			if (isValidPosition(rPos-1, cPos+1))
				numAttacked += hasQueen(board, rPos-1, cPos+1);
			if (isValidPosition(rPos+1, cPos-1))
				numAttacked += hasQueen(board, rPos+1, cPos-1);
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
		return board;
	}
	
	private static boolean isSolution(Square[][] board) {
		for (int i=0; i<board.length; i++) {
			for (int j=0; j<board.length; j++) {
				if (board[i][j] == Square.QUEEN) {
					if (numAttacked(board,i,j) > 0) return false;
				}
			}
		}
		return true;
	}
	
	private static Square[][] ascend(Square[][] board) {
		
		return null;
	}
	
	private static void printBoard(Square[][] board) {
		
	}
	
	private static void climb() {
		boolean foundSoln = false;
		for (int i=0; (i<NUM_RESTARTS) && !foundSoln; i++) {
			board = genRandomBoard();
			int curCost = calcCost(board);
			Square[][] neighbor = ascend(board);
			if (calcCost(neighbor) < curCost) {
				board = neighbor;
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
		climb();
	}
}
