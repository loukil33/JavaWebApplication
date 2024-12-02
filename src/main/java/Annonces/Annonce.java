package Annonces;

import java.time.LocalDate;
import java.util.List;

public class Annonce {
	
	private int id;
	private int bikeid;
	private String title;
	private String description;
	private LocalDate startDate;
    private int duration;
    private List<Note> notes;
    
    public Annonce() {
       
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBikeid() {
		return bikeid;
	}

	public void setBikeid(int bikeid) {
		this.bikeid = bikeid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public List<Note> getNotes() {
		return notes;
	}

	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}

	public Annonce(int id, int bikeid, String title, String description, LocalDate startDate, int duration,
			List<Note> notes) {
		this.id = id;
		this.bikeid = bikeid;
		this.title = title;
		this.description = description;
		this.startDate = startDate;
		this.duration = duration;
		this.notes = notes;
	}

	


	

	

}
