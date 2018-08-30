
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


import javax.swing.JOptionPane;


public class ConnectFourGame implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		// TODO Auto-generated method stub

		// variables
		PlayerColour[] options = {PlayerColour.RED, PlayerColour.YELLOW};
		String[] optionsAsStrings = playColoursToString(options);
		int totalTurns = 0;
		int maximumTurns = 42;
		int playerClosedMessageWindow = -1;
		final int GRID_WIDTH =7;
		final int GRID_HEIGHT =6;
		Boolean gameWon = false;
		int saveGameChoice;
		int playerStartChoice;
		int exitCurrentGameChoice = GRID_WIDTH;
		int quitGameChoice =2;
		Grid theBoard = new Grid(GRID_WIDTH,GRID_HEIGHT);
		String[] rowChoices = new String[GRID_WIDTH];
		
		////creates array of numbers for Jpane window options based on grid size which will be used in dialogue option. Put here so it doesn't have to be reran each while loop.
		for(int counter= 0; counter < GRID_WIDTH; counter++) {
			rowChoices[counter] = Integer.toString((counter +1));
		}
		
		//asks user whether they wish to play a new game, load a game or quit.
		playerStartChoice = getPlayerStartChoice();
		//activates while new game or load game have been selected.
		while(playerStartChoice == 0 || playerStartChoice ==1)
		{
			//initilialisation variables inside current game loop
			Player player1 = new Player();
			Player player2 = new Player();
			Player[] allPlayers = {player1, player2};
			int currentPlayerIndex =0;
			int playerColourChoice = -1;
			
			//load game option here
			if(playerStartChoice == 1) {
				GameState gameState;
				gameState = loadGameState();
				//sets properties stored within the gamestate object into the the variables of the current game
				totalTurns = gameState.getTotalTurnsToStore();
				currentPlayerIndex = gameState.getPlayerIndexToStore();
				theBoard.setColumnTotals(gameState.getColumnTotalsToStore());
				allPlayers = gameState.getPlayersToStore();
				theBoard.setDiskGrid(gameState.getDiskGridToStore());	
			} //end load game option if.
			
			//if statement activated if new game is selected.
			if(playerStartChoice == 0) { 
				// allows the player to enter their names.
				player1.setPlayerName(JOptionPane.showInputDialog("Player 1, please enter your name"));
				//allows player to select their colour, also checks for window closure (no selection).
				while (playerColourChoice == playerClosedMessageWindow)
				{
					playerColourChoice = JOptionPane.showOptionDialog(null, player1.getPlayerName() + ", please choose red or yellow", "Choose a colour", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, optionsAsStrings, optionsAsStrings[0]);
				}
				// assigns player 1s chosen colour and sets player 2s colour to the other colour.
				player1.setColour(options[playerColourChoice]);
				player2.setColour(options[(playerColourChoice + 1) % options.length]);
				
				player2.setPlayerName(JOptionPane.showInputDialog("Player 2, please enter your name"));
				
				JOptionPane.showMessageDialog(null, player2.getPlayerName() + " you are " + player2.getColour().toString().toLowerCase());
			} //end check for player choice being new game
			
			theBoard.printGrid();
			
			//will loop until a player wins or all disks are use up
			while(totalTurns < maximumTurns && gameWon == false) {
			//variables inside loop	
			int playerColumnChoice =0;
			Disk diskForThisTurn = new Disk();
			Player currentPlayer = allPlayers[currentPlayerIndex];
			Boolean diskWasPlaced =false;
			
			//assigns the colour property to the current disk object in use
			diskForThisTurn.setColour(currentPlayer.getColour());
			//returns a int based on the column the player would like to put the disk in. Passing in player is just for the string in the question.
			playerColumnChoice = getColumnChoice(currentPlayer, GRID_WIDTH);
			//checks for the wish to quit game, this has been implemented in the same dialogue box as the player column choice, so we must check if it has been selected first.
			if(playerColumnChoice == exitCurrentGameChoice)
			{
				saveGameChoice = JOptionPane.showConfirmDialog(null, "Would you like to save the game?", "Choose", JOptionPane.YES_NO_OPTION);
				//if statement based on whether player wishes to save the game. Passes all the data to be saved to the method to store.
				if(saveGameChoice == JOptionPane.YES_OPTION) {
					saveGameAndExit(totalTurns, allPlayers, currentPlayerIndex, theBoard.getColumnTotals() , theBoard.getDiskGrid());
				}	
				playerStartChoice = quitGameChoice;
				break;
			} //end check for player exit game option
			
			//adds current disk object to the correct column based on player choice. Returns True or False based on whether the column was full.
			diskWasPlaced = theBoard.addDisk(diskForThisTurn, playerColumnChoice);
			//activated if the disk was not successfully placed due to column being full
			while (diskWasPlaced == false) {
				playerColumnChoice = playerClosedMessageWindow;
				//ensures window cannot be closed in error crashing the game
				while (playerColumnChoice == playerClosedMessageWindow) 
				{	
					//returns another column integer.
					playerColumnChoice = JOptionPane.showOptionDialog(null, "Sorry " + currentPlayer.getPlayerName() + ",  that row is full, please choose another", "Choose a row", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, rowChoices, rowChoices[0]);
				} //end inner while checking for window closure
				//attempts to place the disk again and a boolean is returned again.
				diskWasPlaced = theBoard.addDisk(diskForThisTurn, playerColumnChoice);
			} //end outer while checking for full columns
		
			//board is made visible to user
			theBoard.printGrid();
			
			//checks to see if there has been a victory by calling all the methods which check for 4 in a row.
			if(theBoard.checkVerticleWinner(playerColumnChoice) == true || theBoard.checkHorizontalWinner(playerColumnChoice) == true  
			|| theBoard.checkDownUpDiagonolWinner(playerColumnChoice) == true || theBoard.checkUpDownDiagnolWinner(playerColumnChoice) == true) {
				JOptionPane.showMessageDialog(null, currentPlayer.getPlayerName() + " Wins");
				theBoard.printGrid();
				gameWon = true;
			}
			//checks for a full grid which is a  game draw.
			if(totalTurns == maximumTurns-1) {
				JOptionPane.showMessageDialog(null, " The Game was a draw.");
				theBoard.printGrid();
			}
			//switches player array between 1 and 0 so turns can alternate.
			currentPlayerIndex = (currentPlayerIndex + 1) % allPlayers.length;
			totalTurns++;
			} //end play choice while loop, this will iterate while game is active.
		//after game ends or is closed, the user will be prompted to select between a new game, load game, and closing.	
		playerStartChoice = getPlayerStartChoice();
		//resets variables so if necessary, the game can recommence.
		gameWon = false;
		totalTurns = 0;
		theBoard.clear();
		theBoard.close();
		} //end loop active until Quit is chosen, keeps the screen active
	
	
	} //end main	
	
	////////////////////Method Section///////////////////////////
	
	//prompts player to select a  column to place disk and returns the index of the choice in the method. Can return '7' for exit game. Starts at -1. Player is passed into fucntion
	//so that it can be used in a string.
	public static int getColumnChoice(Player p, int gridWidth) {
		//variables
		int playerRowChoice = -1;
		int playerClosedMessageWindow = -1;
		String[] rowChoices = new String[gridWidth+1]; 
		//populates the option array for the JOption dialogue. Always the number of columns in game + 1 for Exit Game.
		for(int counter= 0; counter <= gridWidth; counter++) {
			if (counter<gridWidth) {
			rowChoices[counter] = Integer.toString((counter +1));
			}
			if(counter== gridWidth) {
				rowChoices[counter] = "Exit Game";
			}
		}
		//iterates until player chooses an option.
		while (playerRowChoice == playerClosedMessageWindow) {
			playerRowChoice = JOptionPane.showOptionDialog(null, p.getPlayerName() + ",  please choose a row", "Choose a row", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, rowChoices, rowChoices[0]);
		} //end while loop
		return playerRowChoice;
	} //end getColumnChoice

	
	//prompts player to choose a start option and returns the index of the choice in the method. Defaults as 2 which will close window.
	public static int getPlayerStartChoice() {
		//variables (defaults as 2 so if window closed it will be the same as selecting exit)
		int playerStartChoice = 2;
		String[] onStartOptions = {"New Game", "Load Game", "Quit"};
		playerStartChoice = JOptionPane.showOptionDialog(null, "Please choose an option", "Select", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, onStartOptions, onStartOptions[0]);
		return playerStartChoice;
	} //end getPlayerStartChoice
	
	//saves the games current status as a binary file. 
	public static void saveGameAndExit(int totalTurnsToStore, Player[] playersInGameToStore, int playerIndexToStore, int[] columnTotalsToStore, Disk[][] diskGridToStore) throws FileNotFoundException {
		GameState currentGameState = new GameState();
		FileOutputStream fileOut= new FileOutputStream("GameState.bin");
		
		//passes all the variables into the currentGameState object, which is then stored.
		currentGameState.setTotalTurnsToStore(totalTurnsToStore);
		currentGameState.setPlayerIndexToStore(playerIndexToStore);
		currentGameState.setColumnTotalsToStore(columnTotalsToStore);
		currentGameState.setDiskGridToStore(diskGridToStore);
		currentGameState.setPlayersToStore(playersInGameToStore);
		try 
		{
			//writes currentGameState object to file.
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOut);
			objectOutputStream.writeObject(currentGameState);
			fileOut.close();
			objectOutputStream.close();
		} catch (IOException i) {
			i.printStackTrace();
		} //end catch
	} //end saveGameAndExit

	
	//method which loads game from .bin file and returns it to where the method is called from in the main window.
	public static GameState loadGameState( ) throws IOException, ClassNotFoundException {
		//loads file into new gameState object.
		GameState currentGameState = new GameState();
		FileInputStream fileIn = new FileInputStream("GameState.bin");
		ObjectInputStream objectInputStream = new ObjectInputStream(fileIn);
		currentGameState = (GameState) objectInputStream.readObject();
		objectInputStream.close();
		fileIn.close();
		return currentGameState;	
	} //end loadGameState
	

	public static String[] playColoursToString(PlayerColour[] playerColours) {
		String[] stringOptions = new String[playerColours.length];
		for(int loop =0; loop<playerColours.length; loop++) {
			stringOptions[loop] = playerColours[loop].toString().toLowerCase();
		}
		return stringOptions;
	}
} //end class
