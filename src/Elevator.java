import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Elevator implements Runnable{
	private int size;
	private boolean inMotion = false;
	private boolean motionDirection = true; //true == up, false == down
	private boolean elevatorGoingToPickup = true; //true - jedziemy odebrac czekajacego pasazera, false - jedziemy odstawic pasazera z windy
	private int currentFloor = 0;
	private int goalFloor = 0;
	private int t;
	Building building;
	private BlockingQueue<Passenger> passengers = new LinkedBlockingQueue<Passenger>(100);	//pasazerowie w windzie
	private BlockingQueue<Passenger> passengersOrders = new LinkedBlockingQueue<Passenger>(100); //żądania wszystkich pasażerów w budynku
	
	public Elevator(int size, int t) {
		this.size=size;
		this.t=t;
	}
	
	@Override
	public void run() {
		int freeSpace;
		while(true) {
			System.out.print("\n\n\n\n");
			System.out.print("------------------------------------------------------\n");
			printGoals();
			System.out.println("Obecny cel windy to piętro nr: "+ goalFloor);
			building.printBuilding();
			
			//jesli, ktorys z pasazerow chce wysiasc, to wysiada
			for(Passenger passenger: passengers) {
				if(passenger.getGoalFloor()==currentFloor) {
					passengers.remove(passenger); 		//usuniecie go z pasazerow windy
					synchronized(this) { passengersOrders.remove(passenger); }//usuniecie go z listy żądań wszystkich pasażerów w budynku
					System.out.println("Pasazer P" + passenger.getNumber() +" dostarczony na miejsce, czyli pietro nr: " + passenger.getGoalFloor());
				}	
			}
			
			//gdy dotrzemy do celu, to szukamy nastepnego celu
			if(currentFloor == goalFloor) goalFloor = getGoalFloorUpdated();
			
			//jesli jest miejsce to zabieramy pasazerow z obecnego pietra
			freeSpace = size - passengers.size();
			if(currentFloor != goalFloor && elevatorGoingToPickup) --freeSpace; //jeśli nie jestesmy na miejscu i jedziemy po czekajacego pasazera to zostawiamy jedno miejsce dla pasazera po ktorego jedziemy
			if(freeSpace>0 && !building.floors.get(currentFloor).passengers.isEmpty()){
				
				for(int i=0; i<freeSpace; i++) {
					if(building.floors.get(currentFloor).passengers.size()<=0) break;
					try {
						synchronized(this) {
							
							Passenger pass = building.floors.get(currentFloor).passengers.take(); //usuniecie pasazera z pietra
							System.out.println("Pasazer P" + pass.getNumber() +" wsiada do windy");
							passengers.add(pass); //dodanie pasazera do windy
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				}
			}
			
			//zmieniamy polozenie windy
			moveElevator();
			
			try {
				Thread.sleep(t);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private void moveElevator() {
		if(currentFloor<goalFloor) {currentFloor++; motionDirection = true;}
		else if(currentFloor>goalFloor) {currentFloor--; motionDirection = false;}
		else if(currentFloor == goalFloor) inMotion = false;
	}
	
	private boolean checkIfPassengerInElevator(Passenger p) {
		for(Passenger temp_pass: passengers) {
			if(p.getNumber()==temp_pass.getNumber()) return true;
		}
		return false;
	}
	
	private int getGoalFloorUpdated() {
		int goal = currentFloor; 				//gdy nie ma w budynku zadnych pasazerow to celem jest pozostanie na obecnym pietrze
		if(!passengersOrders.isEmpty()) { 		//gdy w budynku sa pasazerowie, to wykonujemy żadanie od pasazera, ktory najdluzej byl wbudynku
			Passenger p=passengersOrders.peek();
			if(checkIfPassengerInElevator(p)) { //jesli pasazer jest w windzie, to jedziemy go odstawic na jego pietro docelowe
				goal=p.getGoalFloor();
				elevatorGoingToPickup = false;	
			}
			else {
				goal=p.getInitFloor();			//jezeli pasazer czeka na pietrze, to jedziemy na pietro ktore czeka
				elevatorGoingToPickup = true;
			}
		}
		return goal;
	}
	
	
	//---------------metody wypisujace na ekran ----------------//
	public void printEmptyElevator() {
		
		System.out.print("|  ");
		for(int i = 0; i<size; i++) {
			System.out.print("    ");
		}
		System.out.print(" | ");
	}
	
	public void printElevator() {
		System.out.print("|[ ");
		
		int i=0;
		for(Passenger temp_pass: passengers) {
			i++;
			if(temp_pass.getNumber()<10) System.out.print("P" + temp_pass.getNumber() + "  ");
			else System.out.print("P" + temp_pass.getNumber() + " ");
			
		}
		for(; i<size; i++) {
			System.out.print("    ");
		}
		
		System.out.print("]| ");
	}
	
	public void printGoals() {
		System.out.print("Żądania pasażerów: ");
		for(Passenger p: passengersOrders) {
			System.out.print("P" + p.getNumber() + "(" +p.getInitFloor()+"->"+p.getGoalFloor()+") ");
		}
		System.out.print("\n");
	}
	
	
	//------------------ Gettery i settery --------------------//
	public boolean isInMotion() {
		return inMotion;
	}

	public void setInMotion(boolean inMotion) {
		this.inMotion = inMotion;
	}

	public boolean isMotionDirection() {
		return motionDirection;
	}

	public void setMotionDirection(boolean motionDirection) {
		this.motionDirection = motionDirection;
	}

	public void setSize(int size) {
		this.size = size;
	}
	public int getSize() {
		return this.size;
	}
	public int getCurrentFloor() {
		return currentFloor;
	}

	public void setCurrentFloor(int currentFloor) {
		this.currentFloor = currentFloor;
	}
	
	public void addPassengersOrders(Passenger p) {
		try {
			this.passengersOrders.put(p);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
