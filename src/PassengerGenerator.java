
public class PassengerGenerator implements Producer{
	private int numOfFloors;
	private int t;
	private int p;
	Building building;
	
	public PassengerGenerator(int t, int p, int n, Building building) {
		this.t = t;
		this.p = p;
		this.numOfFloors = n;
		this.building=building;
	}
	@Override
	public void run() {
		
		while(true) {
			if(shouldIproduce()) produce();
			
			try {
				Thread.sleep(t);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public void produce() {
		
		int initFloor = getRandomNumber(0, numOfFloors); //losowanie pietra poczatkowego pasazera
		int goalFloor;
		do {
			goalFloor = getRandomNumber(0, numOfFloors); //losowanie jego pietra docelowego tak dlugo az nie bedzie to to samo pietro na ktorym jest
		}while(goalFloor==initFloor);
		
		Passenger passenger = new Passenger(initFloor, goalFloor);
		
		try {
			synchronized(building.elevator) {
				building.floors.get(initFloor).passengers.put(passenger); //dodanie pasazera do listy pasazerow na danym pietrze
				building.elevator.addPassengersOrders(passenger);	//dodanie pasażera do listy (o nazwie elevator.passengersOrders) żądań, ktore wplywaja na sterowanie windy
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	public int getRandomNumber(int min, int max) {
	    return (int) ((Math.random() * (max - min)) + min);
	}
	
	public boolean shouldIproduce() {
		int num = getRandomNumber(1,100);
		return num<=p;
	}
}
