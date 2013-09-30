import java.util.Arrays;

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
	
	private static final int NUM_RESTARTS = 1;
	
	private static final int BOARD_SIZE = 8;
	
	private static Square[][] board = null;
	
	private static final int[] queensIdx = new int[BOARD_SIZE];
	
	private static boolean isValidPosition(int r, int c) {
		return (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE);
	}
	
	private static int hasQueen(Square[][] board, int rPos, int cPos) {
		return board[rPos][cPos] == Square.QUEEN ? 1 : 0;
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
		for (int i=1; i<board.length; i++) {
			if (isValidPosition(rPos-i, cPos))
				numAttacked += hasQueen(board, rPos-i, cPos);
			if (isValidPosition(rPos+i, cPos))
				numAttacked += hasQueen(board, rPos+i, cPos);
			if (isValidPosition(rPos-i, cPos-i))
				numAttacked += hasQueen(board, rPos-i, cPos-i);
			if (isValidPosition(rPos+i, cPos+i))
				numAttacked += hasQueen(board, rPos+i, cPos+i);
			if (isValidPosition(rPos, cPos+i))
				numAttacked += hasQueen(board, rPos, cPos+i);
			if (isValidPosition(rPos-i, cPos-i))
				numAttacked += hasQueen(board, rPos, cPos-i);
			if (isValidPosition(rPos-i, cPos+i))
				numAttacked += hasQueen(board, rPos-i, cPos+i);
			if (isValidPosition(rPos+i, cPos-i))
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
			queensIdx[i] = randCol;
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
		int cost = calcCost(board);
		Square[][] bestBoard = board;
		for (int i=0; (i<board.length); i++) {
			int origPos = queensIdx[i];
			for (int j=0; (j<board[i].length); j++) {
				if (j != origPos) {
					// remove queen from previous spot
					board[i][queensIdx[i]] = Square.EMPTY;
					// place queen in new spot
					board[i][j] = Square.QUEEN;
					// update queen location
					queensIdx[i] = j;
					printBoard(board);
					int thisBoardCost = calcCost(board);
					if (thisBoardCost < cost) {
						bestBoard = Arrays.copyOf(board, board.length);
						cost = thisBoardCost;
					}
				}
			}
			board[i][queensIdx[i]] = Square.EMPTY; 
			board[i][origPos] = Square.QUEEN;
			queensIdx[i] = origPos;
		}
		return bestBoard;
	}
	
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
		/*Square[][] testboard = {{Square.EMPTY, Square.EMPTY, Square.QUEEN, Square.EMPTY, Square.EMPTY, Square.EMPTY, Square.EMPTY, Square.EMPTY},
				{Square.EMPTY, Square.EMPTY, Square.EMPTY, Square.EMPTY, Square.EMPTY, Square.QUEEN, Square.EMPTY, Square.EMPTY},
				{Square.EMPTY, Square.EMPTY, Square.EMPTY, Square.QUEEN, Square.EMPTY, Square.EMPTY, Square.EMPTY, Square.EMPTY},
				{Square.EMPTY, Square.QUEEN, Square.EMPTY, Square.EMPTY, Square.EMPTY, Square.EMPTY, Square.EMPTY, Square.EMPTY},
				{Square.EMPTY, Square.EMPTY, Square.EMPTY, Square.EMPTY, Square.EMPTY, Square.EMPTY, Square.EMPTY, Square.QUEEN},
				{Square.EMPTY, Square.EMPTY, Square.EMPTY, Square.EMPTY, Square.QUEEN, Square.EMPTY, Square.EMPTY, Square.EMPTY},
				{Square.EMPTY, Square.EMPTY, Square.EMPTY, Square.EMPTY, Square.EMPTY, Square.EMPTY, Square.QUEEN, Square.EMPTY},
				{Square.QUEEN, Square.EMPTY, Square.EMPTY, Square.EMPTY, Square.EMPTY, Square.EMPTY, Square.EMPTY, Square.EMPTY}
				};
		if (isSolution(testboard)) printBoard(testboard);*/
		boolean foundSoln = false;
		for (int i=0; (i<NUM_RESTARTS) && !foundSoln; i++) {
			board = genRandomBoard();
			if (isSolution(board)) {
				foundSoln = true;
				break;
			}
			int curCost = calcCost(board);
			Square[][] neighbor = ascend(board);
			if (calcCost(neighbor) < curCost) {
				board = Arrays.copyOf(neighbor, neighbor.length);
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
		// for (int i=0; i<50000; i++) {
			climb();
		// }
	}
}
