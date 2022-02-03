*** Car Parking Problem ***

This application simulates a car park with a certain number of spaces available (chosen by user).
A car park manager lets in a car if the car park is not full and blocks cars if it is full, for a
random time. The total number of cars wanting to park is also chosen by the user. The car park
manager also has the responsibility of letting a car out, at random park times and opening the space
for a car waiting outside. This simulation uses many threads that represent a car that parks for a
random amount of time, then leaves and the application ends when all cars have left the car park.


Car Class:

This class declares new cars in the variable 'Car' and assigns each car a unique ID (carID). A method
called park() uses a try catch statement to park a car (carID) for a random amount of time and
displays onto the console. Once this random time is over, the try statement breaks out and displays
to the console that the car is leaving the parking space. The catch statement prints any exceptions.


ParkingManager Class:

This class (ParkingManager) passes the 'numberOfParkingSpaces' variable (user input) and uses 'AtomicInteger' to make it
thread safe. Multiple car parking/unparking threads will decrement/increment the variable. The class also passes
'waitingCars' variable; This is a BlockingQueue which is thread safe. Any car wanting to park when spaces are currently
not available is put into this queue.

waitingCarAvailableForParking: AtomicBoolean, this is boolean whenever a car from waiting list is to be parked this is
set to TRUE, so that emptyParkingPlaza function waits until this is false. So when waiting car is parked this becomes false.
completableFutureList: when a car is parked, it’s future object is saved in this list, so that later on we can determine
in emptyCarPark() if all cars have exited.

ParkingManager.java has below methods:

	park(Car car): First it checks if numberOfParkingSpaces is zero, then Car is added to waitingCars queue, otherwise it
    is parked by called parkCar method and decrementing numberOfParkingSpaces.
	parkCar(Car car): car.park() method is executed in new thread using CompleteableFuture, whenever in future this car
    finished parking, makeSlotAvailable method is called. Future for this car parking is added to completableFutureList
    for later checking if car has finished parking and then waitingCarAvailableForParking is set to False.
	makeSpaceAvailable(Car car): if there’s any car waiting in waitingCars queue then it is parked using parkCar method
    and setting waitingCarAvailableForParking to true. Otherwise numberOfParkingSpaces is incremented to make the space
    available for any incoming car.
	emptyCarPark(): check if there’s no cars in waiting list for every 500 milliseconds. Otherwise wait for all car
    park threads to be completed.


CarSimulation Class:

	Get parkingSpaces input from user using Scanner object.
	Get totalCars input from user using Scanner object.
	Create instance of ParkingManager with parkingSpaces.
	For Total number of cars, create an instance of with an ID, and park it using parking Manager every 0 to 2000
    milliseconds randomly.
(In the meantime parkingManager is doing its work by creating separate thread for each parking car as explained above)
	At the end wait for car park to become empty by calling emptyCarPark() on parkingManager instance.


