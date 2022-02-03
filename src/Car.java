import java.util.concurrent.TimeUnit;

public class Car {
    private int carId = 0;
    public Car(int carId) {
        this.carId = carId;
        // Declares a car and gives it a unique ID.
    }

    public Car park() {
        // Method for parking.
        int milliseconds = (int)(Math.random() * 10000);
        System.out.println("CarId: " + carId + ", parking for " + milliseconds + " milliseconds.");
        try {
            TimeUnit.MILLISECONDS.sleep(milliseconds); // parking for random time
        } catch (InterruptedException e) {
            e.printStackTrace();
            // prints an errors to console.
        }
        System.out.println("CarId: " + carId + ", leaving parking...");
        // prints to console that car is leaving.
        return this;
    }

    public int getCarId() {
        return carId;
        // returns car and ID.
    }
}
