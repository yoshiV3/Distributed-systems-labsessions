package ds.gae;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Transaction;

import ds.gae.entities.CarRentalCompany;
import ds.gae.entities.Quote;

@SuppressWarnings("serial")
public class Worker extends HttpServlet {

	private static Datastore ds = DatastoreOptions.getDefaultInstance().getService();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ObjectInputStream payloadData = new ObjectInputStream(req.getInputStream());

		String content = "Thank you for using our service";
		String renter = "test";
		String email = "test@test.com";
		List quotes;
		try {
			quotes = (List) payloadData.readObject();
			confirmQuotes(quotes);
			sendMail(renter, email, "Confirmation: Booking Success", content);
		} catch (ClassNotFoundException | IOException | ReservationException e) {
			// TODO Auto-generated catch block
			sendMail(renter, email, "Confirmation Status: Booking Failed", content);
			e.printStackTrace();
		}

	}

	private void confirmQuotes(List<Quote> quotes) throws ReservationException {
		Transaction tx = ds.newTransaction();

		try {
			for (Quote quote : quotes) {
//                        confirmQuoteTx(quote, tx);              
				Key crcKey = ds.newKeyFactory().setKind("CarRentalCompany").newKey(quote.getRentalCompany());
				Entity comp = ds.get(crcKey);
				CarRentalCompany c = CarRentalCompany.fromEntityToCarRentalCompany(comp);
				c.confirmQuote(ds, tx, quote);
			}
			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
				throw new ReservationException("Quote unavailable. Booking failed, rollback all reservations. ");
			}

		}

	}

	public void confirmQuoteTx(Quote quote, Transaction tx) throws ReservationException {
		if (tx == null) {
			tx = ds.newTransaction();
		}
//        Key crcKey = ds.newKeyFactory().setKind("CarRentalCompany").newKey(quote.getRentalCompany());
//        Entity comp = ds.get(crcKey);
//        CarRentalCompany c = CarRentalCompany.fromEntityToCarRentalCompany(comp);
//        c.confirmQuote(ds,tx,quote);        
	}

	public static void sendMail(String renter, String email, String subject, String content) {
		Session session = Session.getDefaultInstance(new Properties(), null);
		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("customersupport@kul.be", "Car Rental Applcation"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email, ""));
			msg.setSubject(subject);
			msg.setText(content);
			Transport.send(msg);
			System.out.println("Sending email to : <" + renter + "> subject: <" + msg.getSubject() + ">");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
