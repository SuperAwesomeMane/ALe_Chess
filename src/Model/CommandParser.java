package Model;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandParser
{
	//====================\\
	//===== PATTERNS =====\\
	//====================\\
	
	//REGEX PATTERN FOR PLACING A CHESS PIECE
	static Pattern placePattern = Pattern.compile("(?<pieceInfo>(?<pieceType>[kqbrnp])(?<pieceColor>[ld]))(?<pieceLocation>[a-h][1-8])");
	
	//REGEX PATTERN FOR MOVING A PIECE, AND ATTACKING A PIECE
	static Pattern movePattern = Pattern.compile("(?<old>[a-h][1-8]) (?<new>[a-h\\*][1-8])");
	
	//REGEX PATTERN FOR DETECTING KING-SIDE CASTLING
	static Pattern castlePattern = Pattern.compile("(?<oldLocation1>[a-h][1-8])(?<newLocation1> [a-h][1-8])(?<oldLocation2> [a-h][1-8])(?<newLocation2> [a-h][1-8])");
	
	//Hashmap to hold the keys and values of the pieces
	static HashMap<Character, String> pieceMap = new HashMap<>();
	
	//MATCHERS FOR THE PATTERNS THAT LOOK FOR THE COMMANDS IN THE 'COMMAND' STRINGS
	static Matcher placeMatcher;
	static Matcher moveMatcher;
	static Matcher castleMatcher;
	static final int COLOR_LOCATION = 1;
	
	//READS THE CONTENT FROM THE FILE PASSED INTO THE COMMAND-LINE ARGUMENT
	public static void parseFromFile(String fileName)
	{
		FileReader inputStream = null;
		File file = null;
		
		try
		{
			file = new File(fileName);
			inputStream = new FileReader(file);
			Scanner scan = new Scanner(file);

			while(scan.hasNextLine())
			{
				String chessCommand = scan.nextLine();
				CheckCommandMatches(chessCommand);				
			}
		}
		catch(IOException e)
		{
			System.out.println("Invalid");
		}
	}

	public static void CheckCommandMatches(String command)
	{
		
		//THE HASH MAP ASSIGNS KEYS WITH THE CORRESPONDING VALUES TO DETECT WHAT CHESS PIECE IT IS
		pieceMap.put('k', "king");
		pieceMap.put('q', "queen");
		pieceMap.put('b', "bishop");
		pieceMap.put('r', "rook");
		pieceMap.put('n', "knight");
		pieceMap.put('p', "pawn");

		castleMatcher = castlePattern.matcher(command);
		CheckForCastling(castleMatcher, command);
		
	}
	
	//SEARCHES FOR CASTLING COMMANDS
	public static void CheckForCastling(Matcher matcher, String command)
	{
		if(matcher.find())
		{
			//TAKES THE FIRST COORDINATE AND MAKES IT THE KING'S OLD LOCATION
			String oldLocation1 = castleMatcher.group("oldLocation1");
			
			//THE SECOND COORDINATE ARE MADE INTO THE KING'S NEW LOCATION
			String newLocation1 = castleMatcher.group("newLocation1");
			
			//TAKES THE THIRD COORDINATE AND MAKES THEM THE ROOKS OLD LOCATION
			String oldLocation2 = castleMatcher.group("oldLocation2");
			
			//THE LAST COORDINGATE IS MADE INTO THE ROOKS NEW LOCATION
			String newLocation2 = castleMatcher.group("newLocation2");
			
			System.out.println(command + ": King-Side castle the king from " + oldLocation1 + " to " + newLocation1 + ", and " +
					"the rook from " + oldLocation2 + " to " + newLocation2 + ".");
		}
		else
		{
			placeMatcher = placePattern.matcher(command);
			CheckForPlacing(placeMatcher, command);
		}
		
	}
	
	//SEARCHES FOR PLACING COMMANDS
	public static void CheckForPlacing(Matcher matcher, String command)
	{
		String pieceName = null;
		
		if(matcher.find())
		{
			//USES THE HASHMAP TO SEARCH THE FIRST LETTER OF THE COMMAND TO DETERMINE WHAT PIECE IT IS
			pieceName = pieceMap.get(placeMatcher.group("pieceType"));
			
			//USES THE HASGMAP TO SEARCH THE SECOND LETTER OF THE COMMAND TO DETERMINE WHAT COLOR IT IS
			String pieceColor = ((command.charAt(COLOR_LOCATION) == 'l') ? "light" : "dark");
			
			//USES THE LAST TWO PIECES TO DETERMINE WHERE THE PIECE WILL BE PLACED
			String piecelocation = placeMatcher.group("pieceLocation");
			
			System.out.println(command + ": Place " + pieceColor + " " + pieceName + " at " + piecelocation + ".");
		}
		else
		{
			moveMatcher = movePattern.matcher(command);
			CheckForMoving(moveMatcher, command);
		}
	}
	
	//SEARCHES FOR MOVEMENT COMMANDS
	public static void CheckForMoving(Matcher matcher, String command)
	{
		if(matcher.find())
		{
			//MAKES THE FIRST POINT AS THE PIECE'S OLD LOCATION
			String oldLocation = moveMatcher.group("old");
			
			//MAKES THE SECOND POINT AS THE PIECE'S NEW LOCATION
			String newLocation = moveMatcher.group("new");
			
			//CHECKS TO SEE IF THE LENGTH OF THE COMMAND IS SIX BECAUSE ONLY ATTACK COMMANDS HAVE THE STAR AT THE END
			if(command.length() == 6)
			{
				System.out.println(command + ": Move piece at " + oldLocation + " to " + newLocation + " and capture it.");
			}
			else
			{
				System.out.println(command + ": Move piece at " + oldLocation + " to " + newLocation + ".");
			}
		}
		
		//IN CASE NONE OF THE COMMANDS ARE MATCHED BY THE PATTERNS, THE COMMAND IS INVALID
		else
		{
			System.out.println(command + " is invalid.");
		}
	}
	
	//READ FROM THE FILE AGAIN AND LOOKS FOR PLACING COMMMANDS AND PLACES THEM INTO A CHESSBOARD OBJECT
	public static ChessBoard parseBoardFromFile(String fileName)
	{
		//MAKES A BOARD OBJECT
		ChessBoard board = new ChessBoard();
		int chessCommandRowIndex = 2;
		int chessCommandColumnIndex = 3;
		
		try
		{
			FileReader inputStream = null;
			File file = null;
			
			file = new File(fileName);
			inputStream = new FileReader(file);
			Scanner scan = new Scanner(file);

			//WHILE READING FROM THE FILE, CHECKS FOR PLACING COMMANDS WITH THE 'placeMatcher'
			while(scan.hasNextLine())
			{
				String chessCommand = scan.nextLine();
				Matcher placeMatcher = placePattern.matcher(chessCommand);
				
				//WHEN THE PATTERN IS FOUND:
				if(placeMatcher.find())
				{
					//USES THE FIRST TWO CHARACTERS OF THE COMMAND AND USES IT FOR THE NAME OF THE CHESS PIECE
					String chessPiece = placeMatcher.group("pieceInfo");
					char x, y;
					
					//THE THIRD CHARACTER IS THE COLUMN THE PIECE IS PLACED IN
					x = chessCommand.charAt(chessCommandRowIndex);
					
					//THE FINAL CHARACTER IS THE ROW THE PIECE IS PLACED IN
					y = chessCommand.charAt(chessCommandColumnIndex);
					
					//THEN IT CALLS A '.addPieceToBoard' METHOD IN THE CHESSBOARD CLASS WHICH TAKES IN A 'chessPiece', AN 'x', AND A 'y'
					board.addPieceToBoard(chessPiece, x, y);
				}
			}
		}
		catch(IOException e)
		{
			System.out.println("Invalid");
		}
		
		return board;
	}
}


/* Regex Statements
 * ==================------=----==--==---=
 * /~~~/~~~~\~\~/~~~~/~~\~/~~~\~~/~\/~/~\~
 * ======================--=---===--==---=
 * Placing: [k,q,b,r,n,p][l,d][a-h][1-8]
 * Moving: (?<location>(?<old>[\\w]+) (?<new>[a-z\\*][1-8]))
 * King-Side Castling: [a-h][1-8] [a-h][1-8] [a-h][1-8] [a-h][1-8]
 */
