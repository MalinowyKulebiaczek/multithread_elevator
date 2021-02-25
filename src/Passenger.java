
public class Passenger {
	private int initFloor;
	private int goalFloor;
	private int number;
	static int totalNumOfPassengers=0;
	
	public Passenger(int init, int goal) {
		this.initFloor = init;
		this.goalFloor = goal;
		this.number = totalNumOfPassengers;
		totalNumOfPassengers++;
	}
	
	public int getNumber() {
		return this.number;
	}
	
	public int getInitFloor() {
		return this.initFloor;
	}
	
	public int getGoalFloor() {
		return this.goalFloor;
	}
}
