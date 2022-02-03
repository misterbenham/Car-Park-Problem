import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class CarSimulation {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter total number of parking spaces: ");
        int parkingSpaces = scanner.nextInt();
        // User input for total number of spaces at start of program.

        System.out.println("Enter total number of cars that need parking: ");
        int totalCars = scanner.nextInt();
        // User input for total number of cars that need parking during program.

        ParkingManager parkingManager = new ParkingManager(parkingSpaces);

        for (int i = 1; i <= totalCars; i++) { //go to park a car in car park after random time
            parkingManager.park(new Car(i));
            TimeUnit.MILLISECONDS.sleep((int)(Math.random() * 2000)); //random time between 0 and 2000 milliseconds
        }
        parkingManager.emptyCarPark(); //wait for car park to become empty
    }
}
