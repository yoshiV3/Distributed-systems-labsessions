package ds.gae.listener;

import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import ds.gae.CarRentalModel;
import ds.gae.entities.Car;
import ds.gae.entities.CarRentalCompany;
import ds.gae.entities.CarType;

public class CarRentalServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        // This will be invoked as part of a warming request,
        // or the first user request if no warming request was invoked.

        // check if dummy data is available, and add if necessary
        if (!isDummyDataAvailable()) {
            addDummyData();
        }
    }

    private boolean isDummyDataAvailable() {
        // If the Hertz car rental company is in the datastore, we assume the dummy data
        // is available
        return CarRentalModel.get().getAllRentalCompanyNames().contains("Hertz");

    }

    private void addDummyData() {
        loadRental("Hertz", "hertz.csv");
        loadRental("Dockx", "dockx.csv");
    }

    private void loadRental(String name, String datafile) {
        Logger.getLogger(CarRentalServletContextListener.class.getName()).log(Level.INFO, "loading {0} from file {1}",
                new Object[] { name, datafile });
        try {
        	 CrcData data = loadData(name, datafile);
            CarRentalCompany company = new CarRentalCompany(name);
            company.persist(CarRentalModel.getDatastore());
        	//System.out.println("this is in loadrental for company:  " +company.getName()+" key: " +company.getKey()+company.getKey().getParent());

            for (CarType type : data.carTypes)
            {
            	type.persist(CarRentalModel.getDatastore(), company.getName());
            }
           for (Car car : data.cars)
           {
        	   car.persist(CarRentalModel.getDatastore(), company.getName());
           }
        } catch (NumberFormatException ex) {
            Logger.getLogger(CarRentalServletContextListener.class.getName()).log(Level.SEVERE, "bad file", ex);
        } catch (IOException ex) {
            Logger.getLogger(CarRentalServletContextListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static CrcData loadData(String name, String datafile) throws NumberFormatException, IOException {
        Set<Car> cars = new HashSet<Car>();
        List<CarType> carTypes = new LinkedList<CarType>();
        // open file from jar
        BufferedReader in = new BufferedReader(new InputStreamReader(
                CarRentalServletContextListener.class.getClassLoader().getResourceAsStream(datafile)));
        // while next line exists
        while (in.ready()) {
            // read line
            String line = in.readLine();
            // if comment: skip
            if (line.startsWith("#")) {
                continue;
            }
            // tokenize on ,
            StringTokenizer csvReader = new StringTokenizer(line, ",");
            // create new car type from first 5 fields
            CarType type = new CarType(csvReader.nextToken(), Integer.parseInt(csvReader.nextToken()),
                    Float.parseFloat(csvReader.nextToken()), Double.parseDouble(csvReader.nextToken()),
                    Boolean.parseBoolean(csvReader.nextToken()));
            carTypes.add(type);
            // create N new cars with given type, where N is the 5th field
            for (int i = Integer.parseInt(csvReader.nextToken()); i > 0; i--) {
                cars.add(new Car(type.getName()));
            }
        }
        CrcData data = new CrcData();
        data.cars = cars;
        data.carTypes   = carTypes;

        return data;
    }

    static class CrcData {

        public  Set<Car> cars = new HashSet<Car>();
        public List<CarType> carTypes = new LinkedList<CarType>();
    }
    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        // Please leave this method empty.
    }
}
