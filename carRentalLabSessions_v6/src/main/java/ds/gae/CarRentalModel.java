package ds.gae;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Transaction;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.CompositeFilter;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import ds.gae.entities.Car;
import ds.gae.entities.CarRentalCompany;
import ds.gae.entities.CarType;
import ds.gae.entities.Quote;
import ds.gae.entities.Reservation;
import ds.gae.entities.ReservationConstraints;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

public class CarRentalModel {

	private Queue queue = QueueFactory.getQueue("teyo-queue");
	// FIXME use persistence instead

	private static CarRentalModel instance;

	private static Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	public static CarRentalModel get() {
		if (instance == null) {
			instance = new CarRentalModel();
		}
		return instance;
	}

	public static Datastore getDatastore() {
		return datastore;
	}

	public Queue getQueue() {
		return queue;
	}

	/**
	 * Get the car types available in the given car rental company.
	 *
	 * @param companyName the car rental company
	 * @return The list of car types (i.e. name of car type), available in the given
	 *         car rental company.
	 */
	public Set<String> getCarTypesNames(String companyName) {
		Set<String> types = new HashSet<>();
		KeyFactory kf = getDatastore().newKeyFactory().setKind("CarRentalCompany");
		Key k = kf.newKey(companyName);
		Entity comp = getDatastore().get(k);
		CarRentalCompany c = CarRentalCompany.fromEntityToCarRentalCompany(comp);
		;
		for (CarType type : c.getAllCarTypes(getDatastore())) {
			types.add(type.getName());
		}
		return types;
	}

	/**
	 * Get the names of all registered car rental companies
	 *
	 * @return the list of car rental companies
	 */
	public Collection<String> getAllRentalCompanyNames() {
		Set<String> companies = new HashSet();
		Query<Entity> query = Query.newEntityQueryBuilder().setKind("CarRentalCompany").build();
		QueryResults<Entity> results = getDatastore().run(query);
		while (results.hasNext()) {
			companies.add(results.next().getKey().getName());

		}
		return companies;

	}

	/**
	 * Create a quote according to the given reservation constraints (tentative
	 * reservation).
	 *
	 * @param companyName name of the car renter company
	 * @param renterName  name of the car renter
	 * @param constraints reservation constraints for the quote
	 * @return The newly created quote.
	 * @throws ReservationException No car available that fits the given
	 *                              constraints.
	 */
	public Quote createQuote(String companyName, String renterName, ReservationConstraints constraints)
			throws ReservationException {
		KeyFactory kf = getDatastore().newKeyFactory().setKind("CarRentalCompany");
		Key k = kf.newKey(companyName);
		Entity comp = getDatastore().get(k);
		CarRentalCompany c = CarRentalCompany.fromEntityToCarRentalCompany(comp);
		return c.createQuote(getDatastore(), constraints, renterName);
	}

	/**
	 * Confirm the given quote.
	 *
	 * @param quote Quote to confirm
	 * @throws ReservationException Confirmation of given quote failed.
	 */
	public void confirmQuote(Quote quote) throws ReservationException {
		Transaction tx = getDatastore().newTransaction();
		KeyFactory kf = getDatastore().newKeyFactory().setKind("CarRentalCompany");
		Key k = kf.newKey(quote.getRentalCompany());
		Entity comp = getDatastore().get(k);
		CarRentalCompany c = CarRentalCompany.fromEntityToCarRentalCompany(comp);
		c.confirmQuote(getDatastore(), tx, quote);
	}

	/**
	 * Confirm the given list of quotes
	 *
	 * @param quotes the quotes to confirm
	 * @return The list of reservations, resulting from confirming all given quotes.
	 * @throws ReservationException One of the quotes cannot be confirmed. Therefore
	 *                              none of the given quotes is confirmed.
	 * @throws IOException
	 */
	public List<Reservation> confirmQuotes(List<Quote> quotes) throws ReservationException, IOException {

		// TODO: add implementation when time left, required for GAE2
		ByteArrayOutputStream payload = new ByteArrayOutputStream();
		ObjectOutputStream objectOut = new ObjectOutputStream(payload);
		objectOut.writeObject(quotes);
		getQueue().add(TaskOptions.Builder.withUrl("/worker").payload(payload.toByteArray()));

		return null;
	}

	/**
	 * Get all reservations made by the given car renter.
	 *
	 * @param renter name of the car renter
	 * @return the list of reservations of the given car renter
	 */
	public List<Reservation> getReservations(String renter) {
		List<Reservation> out = new ArrayList<>();
		try {
			Query<Entity> query = Query.newEntityQueryBuilder().setKind("Reservation")
					.setFilter(PropertyFilter.eq("renter", renter)).build();
			QueryResults<Entity> results = getDatastore().run(query);
			while (results.hasNext()) {
				Entity result = results.next();
				out.add(Reservation.fromEntityToReservation(result));
			}
		} finally {
			return out;	
		}
	}

	/**
	 * Get the car types available in the given car rental company.
	 *
	 * @param companyName the given car rental company
	 * @return The list of car types in the given car rental company.
	 */
	public Collection<CarType> getCarTypesOfCarRentalCompany(String companyName) {
		KeyFactory kf = getDatastore().newKeyFactory().setKind("CarRentalCompany");
		Key k = kf.newKey(companyName);
		Entity comp = getDatastore().get(k);
		CarRentalCompany c = CarRentalCompany.fromEntityToCarRentalCompany(comp);
		Collection<CarType> out = new ArrayList<>(c.getAllCarTypes(getDatastore()));
		return out;
	}

	/**
	 * Get the list of cars of the given car type in the given car rental company.
	 *
	 * @param companyName name of the car rental company
	 * @param carType     the given car type
	 * @return A list of car IDs of cars with the given car type.
	 */
	public Collection<Long> getCarIdsByCarType(String companyName, CarType carType) {
		Collection<Long> out = new ArrayList<>();
		for (Car c : getCarsByCarType(companyName, carType)) {
			out.add(c.getId());
		}
		return out;
	}

	/**
	 * Get the amount of cars of the given car type in the given car rental company.
	 *
	 * @param companyName name of the car rental company
	 * @param carType     the given car type
	 * @return A number, representing the amount of cars of the given car type.
	 */
	public int getAmountOfCarsByCarType(String companyName, CarType carType) {
		return this.getCarsByCarType(companyName, carType).size();
	}

	/**
	 * Get the list of cars of the given car type in the given car rental company.
	 *
	 * @param companyName name of the car rental company
	 * @param carType     the given car type
	 * @return List of cars of the given car type
	 */
	private List<Car> getCarsByCarType(String companyName, CarType carType) {
		// FIXME: use persistence instead
		List<Car> out = new ArrayList<>();
		KeyFactory kf = getDatastore().newKeyFactory().setKind("CarRentalCompany");
		Key k = kf.newKey(companyName);
		Query<Entity> query = Query.newEntityQueryBuilder().setKind("Car").setFilter(
				CompositeFilter.and(PropertyFilter.hasAncestor(k), PropertyFilter.eq("CarType", carType.getName())))
				.build();
		QueryResults<Entity> results = getDatastore().run(query);
		while (results.hasNext()) {
			Entity result = results.next();
			//System.out.println(result.getKey().getId());
			out.add(Car.fromEntityToCar(result));
		}
		return out;

	}

	/**
	 * Check whether the given car renter has reservations.
	 *
	 * @param renter the car renter
	 * @return True if the number of reservations of the given car renter is higher
	 *         than 0. False otherwise.
	 */
	public boolean hasReservations(String renter) {
		return this.getReservations(renter).size() > 0;
	}
}
