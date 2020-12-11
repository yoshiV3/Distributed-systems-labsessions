package ds.gae.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.*;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.cloud.datastore.StructuredQuery.CompositeFilter;

public class Car {

    private String carType;
    private long    uid = -1;

    /***************
     * CONSTRUCTOR *
     ***************/

    public Car(long uid, String carType) {
        this.carType = carType;
        this.uid      = uid; 
    }
    
    public Car(String type)
    {
    	this.carType = type; 
    }

    /************
     * CAR TYPE *
     ************/

    public String getType() {
        return carType;
    }
    
    public long getId()
    {
    	return this.uid;
    }

    /****************
     * PERSITANCE*
     ****************/
    public boolean isAvailabletx(Datastore ds,Transaction tx, Date start, Date end )
    {

    	if (this.uid < 0)
    	{
    		throw new IllegalStateException("Uid was not set");
    	}
    	KeyFactory kf = ds.newKeyFactory().setKind("Car");
    	Key k         =  kf.newKey(uid); 
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind("Reservation")
                .setFilter(CompositeFilter.and(PropertyFilter.hasAncestor(k),  
                		                   PropertyFilter.ge("startDate", Timestamp.of(start)), 
                		                		   PropertyFilter.le("startDate", Timestamp.of(end))))
                .build();
        QueryResults<Entity> results = tx.run(query); 
        return ! results.hasNext();	
    }
    public boolean isAvailable(Datastore ds, Date start, Date end )
    {

    	if (this.uid < 0)
    	{
    		throw new IllegalStateException("Uid was not set");
    	}
    	KeyFactory kf = ds.newKeyFactory().setKind("Car");
    	Key k         =  kf.newKey(uid); 
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind("Reservation")
                .setFilter(CompositeFilter.and(PropertyFilter.hasAncestor(k),  
                		                   PropertyFilter.ge("startDate", Timestamp.of(start)), 
                		                		   PropertyFilter.le("startDate", Timestamp.of(end))))
                .build();
        QueryResults<Entity> results = ds.run(query); 
        return ! results.hasNext();
    }

    
    public Entity persist(Datastore ds, String company)
    {
    	KeyFactory kf = ds.newKeyFactory().setKind("Car").addAncestors(PathElement.of("CarRentalCompany", company));
    	Key k         = ds.allocateId(kf.newKey());
    	Entity c      = Entity.newBuilder(k)
    			        .set("CarType", this.carType)
    			        .build();
    	ds.put(c);
    	return c;
    }

	public static Car fromEntityToCar(Entity result) {
    	if (! result.getKey().getKind().equals("Car"))
    	{
    		throw new IllegalArgumentException("Not a Car");
    	}
    	return new Car(result.getKey().getId(), result.getString("CarType")); 
	}

}
