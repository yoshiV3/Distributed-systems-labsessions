package ds.gae.entities;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.cloud.datastore.*;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.cloud.datastore.StructuredQuery.CompositeFilter;

import ds.gae.ReservationException;

public class CarRentalCompany {

	private static final Logger logger = Logger.getLogger(CarRentalCompany.class.getName());

	private String name;
	private Key key;

	/***************
	 * CONSTRUCTOR *
	 ***************/

	public CarRentalCompany(String name) {
		setName(name);
	}

	public Entity persist(Datastore ds) {
		KeyFactory kf = ds.newKeyFactory().setKind("CarRentalCompany");
		Key k = kf.newKey(getName());
		Entity crc = Entity.newBuilder(k).build();
		ds.put(crc);
		setKey(k);
		return crc;
	}

	/********
	 * NAME *
	 ********/

	public String getName() {
		return name;
	}

	public Key getKey() {
		return key;
	}

	private void setName(String name) {
		this.name = name;
	}

	private void setKey(Key key) {
		this.key = key;
	}

	/*************
	 * CAR TYPES *
	 *************/

	public Collection<CarType> getAllCarTypes(Datastore ds) {
		HashSet<CarType> carTypes = new HashSet<CarType>();
		KeyFactory kf = ds.newKeyFactory().setKind("CarRentalCompany");
		Key k = kf.newKey(getName());
		Query<Entity> query = Query.newEntityQueryBuilder().setKind("CarType").setFilter(PropertyFilter.hasAncestor(k))
				.build();
		QueryResults<Entity> results = ds.run(query);
		while (results.hasNext()) {
			Entity result = results.next();
			carTypes.add(CarType.fromEntityToCarType(result));
		}
		return carTypes;
	}

	public CarType getCarType(String carTypeName, Datastore ds) {
		KeyFactory kf = ds.newKeyFactory().setKind("CarRentalCompany");
		Key k = kf.newKey(getName());
		Query<Entity> query = Query.newEntityQueryBuilder().setKind("CarType")
				.setFilter(CompositeFilter.and(PropertyFilter.eq("name", carTypeName), PropertyFilter.hasAncestor(k)))
				.build();
		QueryResults<Entity> results = ds.run(query);
		Entity result = results.next();
		if (result == null) {
			throw new IllegalArgumentException("Type does not exist at this company");
		}
		return CarType.fromEntityToCarType(result);
	}

	public boolean isAvailable(Datastore ds, String carTypeName, Date start, Date end) {
		logger.log(Level.INFO, "<{0}> Checking availability for car type {1}", new Object[] { name, carTypeName });
		return this.getAllCarTypes(ds).contains(this.getCarType(carTypeName, ds));

	}

	public Set<CarType> getAvailableCarTypes(Datastore ds, Date start, Date end) {
		Set<CarType> availableCarTypes = new HashSet<>();
		for (Car car : getCars(ds)) {
			if (car.isAvailable(ds, start, end)) {
				availableCarTypes.add(this.getCarType(car.getType(), ds));
			}
		}
		return availableCarTypes;
	}

	/*********
	 * CARS *
	 *********/

	private Car getCar(Datastore ds, long uid) {
		KeyFactory kf = ds.newKeyFactory().setKind("Car");
		Key k = kf.newKey(uid);
		Entity result = ds.get(k);
		if (result == null) {
			throw new IllegalArgumentException("<" + name + "> No car with uid " + uid);
		}
		return Car.fromEntityToCar(result);

	}

	public Set<Car> getCars(Datastore ds) {
		HashSet<Car> cars = new HashSet();
		KeyFactory kf = ds.newKeyFactory().setKind("CarRentalCompany");
		Key k = kf.newKey(getName());
		Query<Entity> query = Query.newEntityQueryBuilder().setKind("Car").setFilter(PropertyFilter.hasAncestor(k))
				.build();
		QueryResults<Entity> results = ds.run(query);
		while (results.hasNext()) {
			Entity result = results.next();
			cars.add(Car.fromEntityToCar(result));
		}
		return cars;
	}

	private List<Car> getAvailableCars(Datastore ds, String carType, Date start, Date end) {
		List<Car> availableCars = new LinkedList<>();
		for (Car car : this.getCars(ds)) {
			if (car.isAvailable(ds, start, end) && car.getType().equals(carType)) {
				availableCars.add(car);
			}
		}
		return availableCars;
	}

	/****************
	 * RESERVATIONS *
	 ****************/

	public Quote createQuote(Datastore ds, ReservationConstraints constraints, String client)
			throws ReservationException {
		logger.log(Level.INFO, "<{0}> Creating tentative reservation for {1} with constraints {2}",
				new Object[] { name, client, constraints.toString() });

		CarType type = getCarType(constraints.getCarType(), ds);

		if (!isAvailable(ds, constraints.getCarType(), constraints.getStartDate(), constraints.getEndDate())) {
			throw new ReservationException("<" + name + "> No cars available to satisfy the given constraints.");
		}

		double price = calculateRentalPrice(type.getRentalPricePerDay(), constraints.getStartDate(),
				constraints.getEndDate());

		Quote quote = new Quote(client, constraints.getStartDate(), constraints.getEndDate(), getName(),
				constraints.getCarType(), price);
		quote.persist(ds);
		return quote;
	}

	// Implementation can be subject to different pricing strategies
	private double calculateRentalPrice(double rentalPricePerDay, Date start, Date end) {
		return rentalPricePerDay * Math.ceil((end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24D));
	}

	public Reservation confirmQuote(Datastore ds, Transaction tx, Quote quote) throws ReservationException {
		logger.log(Level.INFO, "<{0}> Reservation of {1}", new Object[] { name, quote.toString() });
		//Transaction tx = ds.newTransaction();
			List<Car> availableCars = getAvailableCars(ds, quote.getCarType(), quote.getStartDate(),
					quote.getEndDate());
			if (availableCars.isEmpty()) {
				throw new ReservationException("Reservation failed, all cars of type " + quote.getCarType()
						+ " are unavailable from " + quote.getStartDate() + " to " + quote.getEndDate());
			}
			Car car = availableCars.get((int) (Math.random() * availableCars.size()));

			Reservation res = new Reservation(quote, car.getId());
			res.persist(ds, tx);
			return res;

	}

	public void cancelReservation(Entity res) {
		logger.log(Level.INFO, "<{0}> Cancelling reservation {1}", new Object[] { name, res.toString() });
		// TO DO

	}

	static public CarRentalCompany fromEntityToCarRentalCompany(Entity comp) {
		return new CarRentalCompany(comp.getKey().getName());
	}
}
