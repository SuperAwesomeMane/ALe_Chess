package Controller;

import Model.CommandParser;
import View.BoardDisplay;


public class Driver 
{
	public static void main(String[] args) 
	{
		BoardDisplay.printBoard(CommandParser.parseBoardFromFile(args[0]));
		System.out.println();
		CommandParser.parseFromFile(args[0]);
	}
}