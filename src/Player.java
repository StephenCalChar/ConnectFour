
import java.io.Serializable;

public class Player implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String playerName;
	private PlayerColour colour;
	
	//constructor
	public Player() {
		// TODO Auto-generated constructor stub
	}
	
	
	//methods
	
	public String toString() {
	 return this.playerName + this.colour;	
	}

	//get and set
	
	public String getPlayerName() {
		return this.playerName;
	}
	
	public void setPlayerName(String playerName) {
		this.playerName= playerName;
	}
	public PlayerColour getColour() {
		return this.colour;
	}
	
	public void setColour(PlayerColour colour) {
		this.colour= colour;
	}

	
} //end player
