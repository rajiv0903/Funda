package customObjectSerach;

import java.util.List;

public class Book {

	private String title;
	private String description;
	List <String> labels ;
	
	public Book(String title, String description, List <String> labels )
	{
		this.title = title;
		this.description = description;
		this.labels = labels;
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

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}
	
	
}
