
import java.io.Serializable;


public class GameState implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Player[] playersToStore;
	private int playerIndexToStore;
	private int totalTurnsToStore;
	private int[] columnTotalsToStore;
	private Disk[][] diskGridToStore;
	//getters and setters
	
	public int getPlayerIndexToStore() {
		return playerIndexToStore;
	}
	public void setPlayerIndexToStore(int playerIndexToStore) {
		this.playerIndexToStore = playerIndexToStore;
	}
	public int getTotalTurnsToStore() {
		return totalTurnsToStore;
	}
	public void setTotalTurnsToStore(int totalTurnsToStore) {
		this.totalTurnsToStore = totalTurnsToStore;
	}
	public int[] getColumnTotalsToStore() {
		return columnTotalsToStore;
	}
	public void setColumnTotalsToStore(int[] columnTotalsToStore) {
		this.columnTotalsToStore = columnTotalsToStore;
	}
	public Player[] getPlayersToStore() {
		return playersToStore;
	}
	public void setPlayersToStore(Player[] playersToStore) {
		this.playersToStore = playersToStore;
	}
	public Disk[][] getDiskGridToStore() {
		return diskGridToStore;
	}
	public void setDiskGridToStore(Disk[][] diskGridToStore) {
		this.diskGridToStore = diskGridToStore;
	}
	
	
} //end class
