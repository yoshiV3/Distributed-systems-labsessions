/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import javax.ejb.Stateful;
import rental.Car;
import rental.CarRentalCompany;
import rental.RentalStore;
import rental.Reservation;

/**
 *
 * @author yoshi
 */
@Stateful
public class ManagerSession implements ManagerSessionRemote {
    public int getNumberOfReservationsForCarTypeAtCompany(String company, String type )
    {
        int result = 0;
        CarRentalCompany comp = RentalStore.getRental(company);
        for (Car car: comp.getCars())
        {
            if (car.getType().getName().equals(type))
            {
                result = result + car.getAllReservations().size();
            }
        }
        return result;
    }
    public int getNumberOfReservationsByRenter(String renter)
    {
        int result = 0;
        for(CarRentalCompany comp: RentalStore.getRentals().values())
        {
            for (Car car: comp.getCars())
            {
                for (Reservation rsv : car.getAllReservations())
                {
                    if(rsv.getCarRenter().equals(renter))
                    {
                        result = result + 1;
                    }
                }
            }
            
        }
        return result;
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
