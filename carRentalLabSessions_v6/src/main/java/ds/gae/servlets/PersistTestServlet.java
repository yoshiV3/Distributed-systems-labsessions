package ds.gae.servlets;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ds.gae.CarRentalModel;
import ds.gae.ReservationException;
import ds.gae.entities.Quote;
import ds.gae.entities.ReservationConstraints;
import ds.gae.view.JSPSite;
import ds.gae.view.Tools;

@SuppressWarnings("serial")
public class PersistTestServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(PersistTestServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String companyName = "Dockx";
        String email = "tej.user@kuleuven.be";

        req.getSession().setAttribute("renter", email);

        try {

            if (CarRentalModel.get().getReservations(email).size() == 0) {

                ReservationConstraints c = new ReservationConstraints(Tools.DATE_FORMAT.parse("30.11.2020"),
                        Tools.DATE_FORMAT.parse("4.12.2020"), "Special");

                
                ArrayList<Quote> qs = new ArrayList<Quote>();
                final Quote q1 = CarRentalModel.get().createQuote(companyName, email, c);
                final Quote q2 = CarRentalModel.get().createQuote(companyName, email, c);
                final Quote q3 = CarRentalModel.get().createQuote(companyName, email, c);
                final Quote q4 = CarRentalModel.get().createQuote(companyName, email, c);
                qs.add(q1);qs.add(q2);qs.add(q3);qs.add(q4);
                CarRentalModel.get().confirmQuotes(qs);
            }

            resp.sendRedirect(JSPSite.PERSIST_TEST.url());
        } catch (ParseException | ReservationException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
