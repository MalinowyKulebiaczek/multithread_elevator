public class Zadanie3 {
	
	public Zadanie3(int F, int t, int p, int e_size) throws InterruptedException {
		
		Elevator elevator = new Elevator(e_size, t);
		Building building = new Building(F, elevator);
		building.elevator.building = building; //przypisanie stworzonego budynku do windy, ktora sie w nim znajduje
				
		//dodanie pięter do budynku
		for(int i=0; i<F; i++) {
			Floor floor = new Floor(i);
			building.floors.add(floor);
		}
		
		//stworzenie generatora pasażerów
		PassengerGenerator passGen = new PassengerGenerator(t, p, F, building);
		
		//dwie sekundy na przeczytanie instrukcji w jaki sposob wyswietlany jest program
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//uruchomienie watkow windy i generatora pasazerow
		new Thread(elevator).start();
		new Thread(passGen).start();
		
	}

	/**
	 * @param args nie używane.
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		int F=7;		//liczba pieter
		int t=1500; 	//czas przemieszczania sie windy miedzy jednym a drugim pietrem
		int p=70;		//prawdopodobienstwo pojawienia sie pasazera (podawane w procentach 0 - 100%)
		
		int e=3;		//dodatkowy parametr - rozmiar windy
		if(e<1) e=1;	//rozmiar nie może być mniejszy niz 1
		
		//sprawdzenie czy p jest od 0 do 100
		if(p>100) p=100;
		else if (p<0) p=0;
		
		System.out.println("Elementy przedstawiane 'graficznie' to kolejno: winda i ewentualni jej pasażerowie, nr piętra, pasażerowie na piętrze");
		
		new Zadanie3(F, t, p, e);
	}
}
