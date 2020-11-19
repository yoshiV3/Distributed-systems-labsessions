package session;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.ejb.EJBException;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.SUPPORTS;
import javax.persistence.EntityManager;
import rental.CarRentalCompany;
import rental.CarType;
import rental.Quote;
import rental.Reservation;
import rental.ReservationConstraints;
import rental.ReservationException;
import rental.ReservationPrint;

@Stateful
public class ReservationSession extends Session implements ReservationSessionRemote {

    private String renter;
    private List<Quote> quotes = new LinkedList<Quote>();

    @Override
    public List<String> getAllRentalCompanies() {
        return super.getAllCarRentalCompanyNames();
    }

    @Override
    public List<CarType> getAvailableCarTypes(Date start, Date end) {
        return super.getAvailableCarTypes(start, end);
    }

    @Override
    public Quote createQuote(ReservationConstraints constraints) throws ReservationException {
        double price = 100000;
        CarRentalCompany selectedCRC = null;
        for (CarRentalCompany crc : super.getAllCarRentalCompanies()) {
            if (crc.canReserve(constraints)) {
                double newprice = crc.calculateRentalPrice(crc.getType(constraints.getCarType()).getRentalPricePerDay(), constraints.getStartDate(), constraints.getEndDate());
                if (price > newprice) {
                    price = newprice;
                    selectedCRC = crc;
                }
                System.out.println("total price----" + price);

            }
        }
        Quote createdQuote = new Quote(renter, constraints.getStartDate(), constraints.getEndDate(), selectedCRC.getName(), constraints.getCarType(), price);
        this.quotes.add(createdQuote);
        return createdQuote;
    }

    @Override
    @TransactionAttribute(SUPPORTS)
    public List<Quote> getCurrentQuotes() {
        return quotes;
    }

    @Override
    public List<ReservationPrint> confirmQuotes() throws ReservationException {
        try {
            List<ReservationPrint> reservations = new LinkedList();
            for (Quote quote : this.quotes) {
                CarRentalCompany crc = this.getEntityManager().find(CarRentalCompany.class, quote.getRentalCompany());
                Reservation res = crc.confirmQuote(quote);
                this.getEntityManager().persist(res);
                reservations.add(res.toReservationPrint());
            }
            return reservations;
        } catch (Exception ex) {
            throw new EJBException(ex);
        }

    }

    @Override
    public void setRenterName(String name) {
        if (renter != null) {
            throw new IllegalStateException("name already set");
        }
        renter = name;
    }

    @Override
    public String getRenterName() {
        return renter;
    }

    @Override
    public List<ReservationPrint> getMyReservations() {
        List<ReservationPrint> reservations = new LinkedList();
        for (Reservation res : super.getReservationsByClient(this.getRenterName())) {
            reservations.add(res.toReservationPrint());
        }
        return reservations;
    }

    @Override
    public String getCheapestCarType(Date start, Date end, String region) {
        return getCheapestCarTypeInRegionInDates(start, end, region);
    }

}
