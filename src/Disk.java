
import java.io.Serializable;

public class Disk implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PlayerColour colour;
	
	//constructor
	public Disk() {}
	
	public String toString() {
		return this.colour.toString();
	}
		
	//get and setters
	
	public PlayerColour getColour() {
		return this.colour;
	}
	
	public void setColour(PlayerColour colour) {
		this.colour = colour;
	}
	
} //end class
