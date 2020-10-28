package agency;

import java.io.Serializable;

import rental.Quote;
import rental.ICarRentalCompany;
public class AgencyQuote implements Serializable{
	private Quote quote;
	private ICarRentalCompany company;
	
	AgencyQuote(Quote quote, ICarRentalCompany company )
	{
		this.quote = quote;
		this.company = company;
	}
	
	public Quote getQuote()
	{
		return this.quote;
	}
	public ICarRentalCompany getCompany()
	{
		return this.company;
	}

}
