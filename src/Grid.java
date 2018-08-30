import java.awt.Color;
import java.awt.GridLayout;
import java.io.Serializable;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;



public class Grid implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Disk[][] diskGrid;
	private int[] columnTotals;
	private int width;
	private int height;
	private JPanel firstPanel;
	private JFrame frame;
	private JButton[][] buttonGrid;
	final int FRAME_WIDTH =700;
	final int FRAME_HEIGHT =700;
	final int WINNING_SCORE =4;
	//matching disks needed for win will be 3, as it will not count the disk just played.
	int matchingDisksNeeded = WINNING_SCORE -1;
	
	private JButton[] columnHeaders;
	private int[] columnHeaderNumbers;
	//constructor sets width and height variables for the board size. For normal game adds 7,6
	public Grid(int width, int height)
	{
		//sets variables based on the dimensions passed into the constructor
		this.width = width; //width is being passed as 7
		this.height = height; //height is being passed as 6
		this.columnTotals = new int[width];
		this.diskGrid = new Disk[height][width];
		this.buttonGrid = new JButton[height][width];
		this.columnHeaders = new JButton[width];
		this.columnHeaderNumbers = new int[width];
		
		//creates a button above each column with the column number, for ease of reference to user. Populates an array of 1-7 ints and adds them to buttons to display above grid
		for(int counter = 0; counter<columnHeaderNumbers.length; counter++) {
			columnHeaderNumbers[counter] = counter +1;
			columnHeaders[counter] = new JButton("  " + columnHeaderNumbers[counter]);
		}
		//sets the Jframe up for the UI window.
		this.frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        this.firstPanel = new JPanel();
        // will always be +1 as columns in array start at 0. 
        this.firstPanel.setLayout(new GridLayout(height + 1, width)); 
        JButton btn = null;
        //adds column header buttons to the panel
        for(int counter = 0; counter<columnHeaders.length; counter++) {
			firstPanel.add(columnHeaders[counter]);
		}
        //creates playing grid buttons and adds them to panel.
		for (int loop =0; loop <this.height; loop ++ ) 
		{
			for(int loop2=0; loop2 < this.width; loop2++)
			{
				
				
				//ImageIcon blankSlot = new ImageIcon(getClass().getResource("C:\\Users\\Steve\\eclipse-workspace\\Coursework\\img\\1119107-200.png"));
				btn = new JButton();
				btn.setBackground(Color.white);
				this.buttonGrid[loop][loop2] = btn;
				this.firstPanel.add(btn);
			}
		}	
        mainPanel.add(firstPanel);
        this.frame.add(mainPanel);
        this.frame.setSize(FRAME_WIDTH ,FRAME_HEIGHT);
        //frame.setLocationRelativeTo(null);  //can be used to centralise window but currently turned off, due to clash with jpane.
	} //end Grid constructor
	
	////////////////////Method Section///////////////////////////	
	
	//method to closes frame for game exit.
	public void close() {
		this.frame.dispose();
	}
	
	//method resets 2d array and counter column. Any objects are replaced with null.
	public void clear() {
		//clear 2d array down
		for (int loop =0; loop<this.height; loop++ )
		{
			for(int loop2=0; loop2 < this.width; loop2++)
			{
				this.diskGrid[loop][loop2] = null;
			}
		}
		
		//clear down column totals
		for(int loop=0; loop < this.width; loop++)
		{
			this.columnTotals[loop]= 0;
		}
		
	} //end close method
	
	
	// checks if column is full, if not adds the disk to that column.
	public Boolean addDisk(Disk newDisk, int columnIndex) {
		//variables
		int nextRowIndex = this.columnTotals[columnIndex];
		int horizontalCoord = height-1; //used to calculate correct row position for disk to fall into. Will always be 1 less than height due to array starting at 0. Currently 5.
		//the check to see if the column is full. If it is then the Boolean returns false.
		if (nextRowIndex >= this.height)
		{
			return false;
		}
		else
		{
			//subtracting the number of disks currently in row array from 5 will give the x coordinate the disk should fall into. Returns true to show disk
			horizontalCoord -= nextRowIndex;
			this.diskGrid[horizontalCoord][columnIndex] = newDisk;
			this.columnTotals[columnIndex] ++;
			return true;
		}
	} //end addDisk

	
	//printGrid method brings up screen based on the disk objects in the diskGrid array.
	public void printGrid() {
		this.frame.setVisible(true);
		
		for (int loop =0; loop<this.height; loop++ ) {
			for(int loop2=0; loop2 < this.width; loop2++) {
				//sets all empty columns as white buttons
				if(this.diskGrid[loop][loop2]== null) {
					this.buttonGrid[loop][loop2].setBackground(Color.white);
				}
				// if there is a disk in 2d array, then checks the colour property and changes the relative button to that colour in display.
				else {
					
				
					if(this.diskGrid[loop][loop2].getColour() == PlayerColour.YELLOW)
					{
						this.buttonGrid[loop][loop2].setBackground(Color.yellow);
					}
					if(this.diskGrid[loop][loop2].getColour() == PlayerColour.RED)
					{
						this.buttonGrid[loop][loop2].setBackground(Color.red);
					} 
				} //end else
			} //end inner for	
			
		} //end outer for
	} //end printGrid	
        

	//checks for vertical victory
	public Boolean checkVerticleWinner(int columnIndex) {
		int diskRow;
		Disk lastPlaced;
		int disksInARow =1; //counter starts at 1 representing the disk that has already been placed. The calculation are based around this disk, as it is not included in the loop.
	
		// disk row is total column size minus number of disks already in the column. eg 6-4 = 2.
		diskRow = this.height-this.columnTotals[columnIndex]; 
		
		// ensures there are at least 4 disk in the column before checking for win. column height - winning score gives this number (6-4=2). If this fails 4 in a row is not possible vertically.
		if(diskRow > (this.height - WINNING_SCORE)) {
			return false;
		}
		
		// creates reference to the disk object to the right of the one just placed. 
		lastPlaced = this.diskGrid[diskRow][columnIndex]; 
		
		//iterates over the 3 disks below, checking for matching disks.
		for(int loop = 0; loop <matchingDisksNeeded; loop++) {
			Disk diskBelow;
			// creates reference to disk object below the one just placed
			diskBelow = this.diskGrid[diskRow+1 +loop][columnIndex]; 
			if (diskBelow.getColour() == lastPlaced.getColour()) {
				disksInARow++;
				
			} 
			else {
				//breaks out of count if there is a non matching disk, to ensure disks in a row does not reach 4
				break;
			} 			
		} //end for loop iterating over 3 disks below
		
		// returns true or false depending on if winning column of disks has been found
		return checkForWinner(disksInARow);
	} //end checkVerticlelWinner Method
	
	
	public Boolean checkHorizontalWinner (int columnIndex) {
		//variables
		int diskRow;
		Disk lastPlaced;
		int disksInARow =1;
	
		diskRow = this.height - this.columnTotals[columnIndex]; //calculates row of last disk placed
		//creates reference to the last disk object that was placed. 
		lastPlaced = this.diskGrid[diskRow][columnIndex];
		//checking if slot to right contains disk and if so if it is the same colour. Adds any same coloured disks found to the disksInARow variable.
		for(int loop = 0; loop <3; loop++) {
			Disk diskToRight;
			// checks if the disk is in row 6 (rightmost row) and breaks out of loop as there cannot be a disk to the right of it. Must subtract the loop 
			//so that the break occurs once the loop reaches the rightmost column. Width -1 =6. 
			if (columnIndex == this.width -1 -loop) {
				break;
			}	
			// creates reference to the disk object to the right of the one just placed. 
			diskToRight = this.diskGrid[diskRow][columnIndex + 1 +loop];
			//checks to see if there is a disk to the right, if not breaks out of loop
			if (diskToRight== null) {
				break;
			} //end if
			
			//if disk colours do not match break out of loop and do not add to counter
			if(diskToRight.getColour() != lastPlaced.getColour()) {
				break;
			}
			disksInARow++;
		} //end for loop checking disks to the right.
		
		//checking if slot to left contains disk and if so if it is the same colour.
		for(int loop = 0; loop <matchingDisksNeeded; loop++) { 
				
			Disk diskToLeft;
			// checks if the index is iterating over the leftmost column and breaks out of loop if so.
			if (columnIndex - loop == 0) {
				break;
			}
			//creates reference to the disk object to the left of the disk just placed.
			diskToLeft = this.diskGrid[diskRow][columnIndex -1 -loop];
			//checks if there is a disk to the left, if not breaks out of loop.
			if(diskToLeft == null) {	
				break;	
			}
			//if disks do not match colours break out of loop so it does not add to counter.
			if(diskToLeft.getColour() != lastPlaced.getColour()) {
				break;
			}
			disksInARow++;		
		} //end for loop checking disks to the left horizontally
		// returns true or false depending on if winning column of disks has been found
		return checkForWinner(disksInARow);
	} //end checkHorizontalWinner
	
	//checks for diagonal win from top to bottom(left to right).
	public Boolean checkUpDownDiagnolWinner(int columnIndex) {
		int diskRow;
		Disk lastPlaced;
		int disksInARow =1;
	
		diskRow = this.height - this.columnTotals[columnIndex]; //calculates row of last disk placed	
		//creates reference to the disk object last placed. 
		lastPlaced = this.diskGrid[diskRow][columnIndex];
		//checks bottom right diagonal disks for matches.
		for(int loop = 0; loop <matchingDisksNeeded; loop++) {
			Disk diskToBottomRight;
			//checks to make sure no iteration past bottom or rightmost rows.
			if (diskRow + loop == (this.height -1) || columnIndex == this.width -1 -loop) {
				break;
			}
			//creates reference to the disk object to the bottom right of the disk just placed.
			diskToBottomRight = this.diskGrid[diskRow +1 + loop][columnIndex+1 +loop];
			//checks if there is a disk to the bottom right diagonally, if not breaks out of loop.
			if(diskToBottomRight == null) {
				break;
			}
			//if disks do not match colours break out of loop so it does not add to counter.
			if(diskToBottomRight.getColour() != lastPlaced.getColour()) {
				break;
			}
			disksInARow++;
		} //end for loop checking bottom right diagonal.
				
		//checks top left diagonal disks for matches.
		for(int loop = 0; loop <matchingDisksNeeded; loop++) {
			Disk diskTopLeft;
			//checks to make sure no iteration past bottom or leftmost rows.
			if (diskRow - loop == 0 || columnIndex - loop == 0 ) {
				break;
			}
			//creates reference to disk objects to the top left of the disk just placed.
			diskTopLeft = this.diskGrid[diskRow -1 - loop][columnIndex -1 -loop];
			//checks if there is a disks to the top left diagonally, if not breaks out of loop.
			if(diskTopLeft == null) {
				break;
			}
			//if disks do not match colours break out of loop so it does not add to counter.
			if(diskTopLeft.getColour() != lastPlaced.getColour()) {
				break;
			}
			disksInARow++;
		} //end for loop checking top left diagonal.
		// returns true or false depending on if winning column of disks has been found 
		return checkForWinner(disksInARow);				
	} // end checkUpDownDiagonalWinner
	
	
	//checks for diagonal win from bottom to top(left to right).
	public Boolean checkDownUpDiagonolWinner(int columnIndex) {
		int diskRow;
		Disk lastPlaced;
		int disksInARow =1;
	
		diskRow = this.height - this.columnTotals[columnIndex]; //calculates row of last disk placed
		//creates reference to the disk object last placed.
		lastPlaced = this.diskGrid[diskRow][columnIndex];	
		//checks bottom left diagonal disk for matches.
		for(int loop = 0; loop <matchingDisksNeeded; loop++) {
			Disk diskToBottomLeft;
			//checks to make sure no iteration past bottom or leftmost rows.
			if (diskRow + loop == (this.height -1) || columnIndex - loop == 0 ) {
				break;
			}
			//creates reference to disk objects to the bottom left of the disk just placed.
			diskToBottomLeft = this.diskGrid[diskRow +1 + loop][columnIndex-1 -loop];
			//checks if there is a disk to the bottom left diagonally, if not breaks out of loop.
			if(diskToBottomLeft == null) {
				break;
			}	
			//if disks do not match colours break out of loop so it does not add to counter.
			if(diskToBottomLeft.getColour() != lastPlaced.getColour()) {
				break;
			}
			disksInARow++;
		} //end for loop checking for bottom left diagonal matches
		
		//checks top right diagonal disk for matches.
		for(int loop = 0; loop <matchingDisksNeeded; loop++) {
			Disk diskToTopRight;
			//checks to make sure no iteration past top or rightmost rows.
			if (diskRow - loop == 0 || columnIndex == this.width -1 -loop) {
				break;
			}
			//references disk objects to the top right of the disk just placed.
			diskToTopRight = this.diskGrid[diskRow -1 - loop][columnIndex+1 +loop];
			//checks if there is a disk to the top right diagonally, if not breaks out of loop.
			if(diskToTopRight == null) {
				break;
			}
			//if disks do not match colours break out of loop so it does not add to counter.
			if(diskToTopRight.getColour() != lastPlaced.getColour()) {
				break;
			}
			disksInARow++;
		} //end for loop checking for top right diagonal matches
		
		// returns true or false depending on if winning column of disks has been found 
		return checkForWinner(disksInARow);
	} //end checkDownUpDiagonolWinner
	
	//checks to see if there has been a victory by getting 4 in a row. Made in to separate method to avoid repetition of code.
	public Boolean checkForWinner(int disksInARow) {
		if (disksInARow >= WINNING_SCORE){
			return true;
		}
		else {
			return false;
		}	
	} ///end checkForWinner
	
	
	
	
	
	
	
	
	
	
	//get & setters
	
	public int[] getColumnTotals() {
		return this.columnTotals;
	}
	
	public Disk[][] getDiskGrid() {
		return this.diskGrid;
	} 
	
	public void setColumnTotals(int[] columnTotals) {
		this.columnTotals = columnTotals;
	}
	
	public void setDiskGrid(Disk[][] diskGrid)
	{
		for (int loop =0; loop<this.height; loop++ )
		{
			for(int loop2=0; loop2 < this.width; loop2++)
			{
				this.diskGrid[loop][loop2] = diskGrid[loop][loop2];
			}
		}
		
	} //end setdiskgrid setter

	
	public int getWidth() {
		return width;
	}


	public void setWidth(int width) {
		this.width = width;
	}


	public int getHeight() {
		return height;
	}


	public void setHeight(int height) {
		this.height = height;
	} 
	
	
	
	
	
		

} //end Grid
