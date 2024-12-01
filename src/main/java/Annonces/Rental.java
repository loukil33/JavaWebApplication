package Annonces;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import Users.User;
public class Rental extends Annonce{

	private double rentPrice;
    private Time start_time;
    private Time end_time;
    private int rentDuration;
    private List<User> waitingList;
    private User CurrentWinner;
    
    public Rental() {
    	
    }
	public Rental(int id,String title, String description, LocalDate startDate, int duration,List<Note> notes, double rentPrice,
			Time start_time,Time end_time, int rentDuration,List<User> waitingList,User CurrentWinner) {
		super(id,title, description, startDate, duration,notes);
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

	

	public Time getStart_time() {
		return start_time;
	}
	public void setStart_time(Time start_time) {
		this.start_time = start_time;
	}
	public Time getEnd_time() {
		return end_time;
	}
	public void setEnd_time(Time end_time) {
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
		return "Rental [rentPrice=" + rentPrice + ", start_time=" + start_time + ", end_time=" + end_time
				+ ", rentDuration=" + rentDuration + ", waitingList=" + waitingList + ", CurrentWinner=" + CurrentWinner
				+ "]";
	}

	

	
    
    
}
