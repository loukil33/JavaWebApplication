package RentalRequests;
import java.time.LocalDateTime;

public class RentalRequest {
    private int id;
    private int bikeId;
    private int userId;
    private LocalDateTime requestDate;
    private String status;
    private LocalDateTime rentalStart;
    private LocalDateTime rentalEnd;
    
    
    
	@Override
	public String toString() {
		return "RentalRequest [id=" + id + ", bikeId=" + bikeId + ", userId=" + userId + ", requestDate=" + requestDate
				+ ", status=" + status + ", rentalStart=" + rentalStart + ", rentalEnd=" + rentalEnd + ", getId()="
				+ getId() + ", getBikeId()=" + getBikeId() + ", getUserId()=" + getUserId() + ", getRequestDate()="
				+ getRequestDate() + ", getStatus()=" + getStatus() + ", getRentalStart()=" + getRentalStart()
				+ ", getRentalEnd()=" + getRentalEnd() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getBikeId() {
		return bikeId;
	}
	public void setBikeId(int bikeId) {
		this.bikeId = bikeId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public LocalDateTime getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(LocalDateTime requestDate) {
		this.requestDate = requestDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public LocalDateTime getRentalStart() {
		return rentalStart;
	}
	public void setRentalStart(LocalDateTime rentalStart) {
		this.rentalStart = rentalStart;
	}
	public LocalDateTime getRentalEnd() {
		return rentalEnd;
	}
	public void setRentalEnd(LocalDateTime rentalEnd) {
		this.rentalEnd = rentalEnd;
	}

}
