package Annonces;

import java.time.LocalDate;
import java.util.List;

public class Annonce {
	
	private int id;
	private String title;
	private String description;
	private LocalDate startDate;
    private int duration;
    private List<Note> notes;
    
    public Annonce() {
       
    }

	public Annonce(int id, String title, String description, LocalDate startDate, int duration, List<Note> notes) {
		
		this.id = id;
		this.title = title;
		this.description = description;
		this.startDate = startDate;
		this.duration = duration;
		this.notes = notes;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	

	public List<Note> getNotes() {
		return notes;
	}


	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}

	@Override
	public String toString() {
		return "Annonce [id=" + id + ", title=" + title + ", description=" + description + ", startDate=" + startDate
				+ ", duration=" + duration + ", notes=" + notes + "]";
	}




	

}
