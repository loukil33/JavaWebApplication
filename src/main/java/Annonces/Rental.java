package Annonces;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.json.bind.annotation.JsonbTransient;

import org.codehaus.jackson.annotate.JsonIgnore;

import Bikes.Bike;
import Users.User;

public class Rental extends Annonce{

	private double rentPrice;
    private LocalTime start_time;
    private LocalTime end_time;
    private int rentDuration;
    @JsonIgnore
    private List<User> waitingList;
    @JsonIgnore
    private User CurrentWinner;
    
    public Rental() {
    	
    }
	public Rental(int id,String title, String description, LocalDate startDate, int duration,int userid,Bike bike,List<Note> notes, double rentPrice,
			LocalTime start_time,LocalTime end_time, int rentDuration,List<User> waitingList,User CurrentWinner) {
		super(id,title, description, startDate, duration,userid,bike,notes);
		this.rentPrice = rentPrice;
		this.start_time = start_time;
		this.end_time = end_time;
		this.rentDuration = rentDuration;
		this.waitingList = waitingList;
		this.CurrentWinner = CurrentWinner;
	}

	public double getRentPrice() {
		return rentPrice;
	}

	public void setRentPrice(double rentPrice) {
		this.rentPrice = rentPrice;
	}

	

	public LocalTime getStart_time() {
		return start_time;
	}
	public void setStart_time(LocalTime start_time) {
		this.start_time = start_time;
	}
	public LocalTime getEnd_time() {
		return end_time;
	}
	public void setEnd_time(LocalTime end_time) {
		this.end_time = end_time;
	}
	public int getRentDuration() {
		return rentDuration;
	}

	public void setRentDuration(int rentDuration) {
		this.rentDuration = rentDuration;
	}

	public List<User> getWaitingList() {
		return waitingList;
	}

	public void setWaitingList(List<User> waitingList) {
		this.waitingList = waitingList;
	}

	public User getCurrentWinner() {
		return CurrentWinner;
	}

	public void setCurrentWinner(User currentWinner) {
		CurrentWinner = currentWinner;
	}
	
	
	@Override
	public String toString() {
		return super.toString() +"Rental [rentPrice=" + rentPrice + ", start_time=" + start_time + ", end_time=" + end_time
				+ ", rentDuration=" + rentDuration + ", waitingList=" + waitingList + ", CurrentWinner=" + CurrentWinner
				+ "]";
	}

	

	
    
    
}
