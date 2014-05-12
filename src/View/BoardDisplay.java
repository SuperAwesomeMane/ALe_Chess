package View;

import java.util.ArrayList;
import Model.ChessBoard;
import Model.CommandParser;

public class BoardDisplay
{

	//PRINTS OUT THE BOARD ARRAY BY GETTING EACH PIECE ON THE BOARD WITH THE 'getPiece' METHOD IN THE 'ChessBoard' CLASS, AND STORES THEM INTO A STRING
	public static void printBoard(ChessBoard board)
	{
		int rowCount = 8;
		int numberOfRows = 8;
		int rowsShown = 0;
		
		//PRINTS OUT THE COLUMNS AT THE TOP
		PrintBoardColumns();
		System.out.println();
	
		for(int i = 7; i >= rowsShown; i--)
		{	
			System.out.print(rowCount + " ");
			for(int j = 0; j < numberOfRows; j++)
			{
				String boardPoint = board.getPiece(j, i);
				if(boardPoint == null)
				{
					System.out.print("[--]");
				}
				else
				{
					System.out.print("[" + boardPoint + "]");
				}
			}
			System.out.println(" " + rowCount);
			rowCount--;
		}
		PrintBoardColumns();
	}

	public static void PrintBoardColumns()
	{
		int columnLength = 8;
		for(int i = 0; i < columnLength; i++)
		{
			System.out.print("   "+ (char)('a' + i));
		}
	}
}