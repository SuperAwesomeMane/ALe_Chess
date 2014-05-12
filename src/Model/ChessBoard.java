package Model;

import java.util.Arrays;

public class ChessBoard 
{
	char[] boardColumns = new char[]{
			'a','b','c','d','e','f','g','h'};

	int[] boardRows = new int[]{
			1,2,3,4,5,6,7,8};

	String[][] chessBoard = new String [boardColumns.length][boardRows.length];
	
	//ADDS PIECES TO THE 'chessBoard' DOUBLE ARRAY FROM THE 'parseBoardFromFile' METHOD IN THE 'CommandParser' CLASS
	public void addPieceToBoard(String piece, char x, char y)
	{
		chessBoard[(int) (x - 97)][Integer.parseInt(y + "") - 1] = piece;
	}

	public String getPiece(int x, int y)
	{
		return chessBoard[x][y];
	}
}