import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.nonNull;

public class ParkingManager {
    private final AtomicInteger numberOfParkingSpaces;
    private final BlockingQueue<Car> waitingCars;
    private final AtomicBoolean waitingCarAvailableForParking = new AtomicBoolean();
    private final List<CompletableFuture<Void>> completableFutureList = new ArrayList<>();


    public ParkingManager(int numberOfParkingSpaces) {
        this.numberOfParkingSpaces = new AtomicInteger(numberOfParkingSpaces);
        this.waitingCars = new LinkedBlockingDeque<>();
    }

    public void park(Car car){
        if (numberOfParkingSpaces.decrementAndGet() < 0) { //slots not available
            numberOfParkingSpaces.incrementAndGet();
            System.out.println("Space is not currently available for carId: " + car.getCarId() + ", adding to waiting list.");
            try {
                waitingCars.put(car); //wait for slots to be available
                return;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        parkCar(car);
    }

    private void parkCar(Car car) {
        System.out.println("Parking carId: " + car.getCarId());
        CompletableFuture completableFuture = CompletableFuture
                .supplyAsync(() -> car.park()) // park car using its own thread.
                .thenAccept(c -> makeSpaceAvailable(c)); // when car parking is done this function is called to make space available.
        completableFutureList.add(completableFuture);
        waitingCarAvailableForParking.set(false);
    }

    private void makeSpaceAvailable(Car car) {
        System.out.println("Making a space available since carId: " + car.getCarId() + " has finished parking.");
        waitingCarAvailableForParking.set(true);
        Car waitingCar = waitingCars.poll();
        if (nonNull(waitingCar)) {
            parkCar(waitingCar);
        } else {
            waitingCarAvailableForParking.set(false);
            numberOfParkingSpaces.incrementAndGet(); // make space available.
        }
    }

    public void emptyCarPark() {
        while (!waitingCars.isEmpty() || waitingCarAvailableForParking.get()) { //Still cars in waiting list
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[0])).join(); //check all cars have finished parking
        System.out.println("Car Park has become empty.");
    }
}
