/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import javax.ejb.Remote;

/**
 *
 * @author yoshi
 */
@Remote
public interface ManagerSessionRemote {
    public int getNumberOfReservationsForCarTypeAtCompany(String company, String type );
    public int getNumberOfReservationsByRenter(String renter);
    
}
