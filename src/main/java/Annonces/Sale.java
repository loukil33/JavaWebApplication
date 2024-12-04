package Annonces;

import java.time.LocalDate;
import java.util.List;

import Users.User;

public class Sale extends Annonce{

	private double salePrice;

	public Sale(int id,String title, String description, LocalDate startDate, int duration,int userid,List<Note> notes, double salePrice) {
		super(id,title, description, startDate, duration,userid,notes);
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
