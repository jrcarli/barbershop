# Barbershop

This is a Java application to practice thread-safe coding.

## Scenario

* 1 barber
* 3 seats
* Haircut takes 1 - 4 seconds (random)
 * Customer leaves and vacates seat when haircut is done
* 15 customers (configurable)
* If a seat is empty, a customer should take it
* Haircuts should be in order of customers taking seats
* If all seats are taken, customer leaves and returns 0 - 3 seconds later (random) looking for a seat

## Approach

* Seats are the first resource that `Customers` attempt to access
 * Seats are represented as a `Semaphore` with 3 permits
* The barber is the second resource that customers attempt to access
 * `Customers` can only attempt to access the barber once they are in a seat
 * The barber is represented as a single-permit `Semaphore` that enforces fairness (ensuring that seated customers are served in the order they take those seats)
* The `Barbershop` maintains the seats and barber as static class members
* `Customers` extend the `Thread` class, managing their haircuts via the `run()` method
* The `Barbershop main()` function instantiates and starts an array of Customer objects (15 by default, though the user can specify the number of customers as a single command-line parameter)
* The `Customers` follow the scenaior guildelines given above
* The `Barbershop` calls `join()` on each `Customer` thread to know when all have received haircuts

## Build and Run

This application uses `gradle` for build management. Run the following from the top-level project directory on the command line in order to compile and run the application:

	$ ./gradlew build	
	$ ./gradlew run
	
	
### Customizing the Number of Customers

The application accepts a single argument to specify the number of Customers. If provided, this argument must be a non-negative integer. If excluded, the application defaults to 15 Customers. The following example shows how to specify 5 customers when running the application with `gradle`:

	$ ./gradlew run --args=5	
	