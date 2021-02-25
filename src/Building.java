import java.util.ArrayList;
import java.util.List;

public class Building {
	private int numOfFloors;
	//Set<Floor> floors = new HashSet<Floor>();
	Elevator elevator;
	List<Floor> floors = new ArrayList<Floor>();
	
	public Building(int n, Elevator e) {
		this.numOfFloors = n;
		this.elevator = e;
	}
	
	public void printBuilding() {
		/**
		 * Wyswietlanie budynku w 'graficznej' formie na konsoli
		 */
		for(Floor temp_floor: floors) {
			
			if(elevator.getCurrentFloor() == temp_floor.getNumber()) elevator.printElevator(); //narysowanie windy z zawartoscia
			else elevator.printEmptyElevator();	//narysowanie pustego slotu na winde
			
			System.out.print("[" + temp_floor.getNumber() + "] ");
			
			for(Passenger temp_passenger: temp_floor.passengers) {
				System.out.print("P" + temp_passenger.getNumber());
				System.out.print("(" + temp_passenger.getInitFloor() + "->" + temp_passenger.getGoalFloor() + ") ");	
			}
			System.out.print("\n");
		}
	}
}
