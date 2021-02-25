import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Floor {
	private int number;
	BlockingQueue<Passenger> passengers  = new ArrayBlockingQueue<Passenger>(100);
	public Floor(int n) {
		this.number = n;
	}
	public int getNumber() {
		return this.number;
	}
}
