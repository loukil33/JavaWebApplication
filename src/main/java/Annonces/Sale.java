package Annonces;

import java.time.LocalDate;
import java.util.List;

public class Sale extends Annonce{

	private double salePrice;

	public Sale(int id, int bikeid, String title, String description, LocalDate startDate, int duration,List<Note> notes, double salePrice) {
		super(id,bikeid,title,description,startDate,duration,notes);
		this.salePrice = salePrice;
	}

	public double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}

	@Override
	public String toString() {
		return "Sale [salePrice=" + salePrice + "]";
	}
	
	
}
