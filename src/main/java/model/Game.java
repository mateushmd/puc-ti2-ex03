package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Game 
{
	private int id;
	private String title;
	private String description;
	private String genre;
	private String developer;
	private String publisher;
	private double price;
	private LocalDate publishDate;
	
	public Game()
	{
		id = -1;
		title = "";
		description = "";
		genre = "";
		developer = "";
		publisher = "";
		price = 0;
		publishDate = null;
	}
	
	public Game(String title, String description, String genre, String developer, String publisher, double price,
			LocalDate publishDate) {
		this.title = title;
		this.description = description;
		this.genre = genre;
		this.developer = developer;
		this.publisher = publisher;
		this.price = price;
		this.publishDate = publishDate;
	}

	public Game(int id, String title, String description, String genre, String developer, String publisher, double price,
			LocalDate publishDate)
	{
		this(title, description, genre, developer, publisher, price, publishDate);
		this.id = id;
	}
		
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) 
	{
		this.title = title;
	}

	public String getDescription() 
	{
		return description;
	}

	public void setDescription(String description) 
	{
		this.description = description;
	}

	public String getGenre() 
	{
		return genre;
	}

	public void setGenre(String genre) 
	{
		this.genre = genre;
	}

	public String getDeveloper() 
	{
		return developer;
	}

	public void setDeveloper(String developer) 
	{
		this.developer = developer;
	}

	public String getPublisher() 
	{
		return publisher;
	}

	public void setPublisher(String publisher) 
	{
		this.publisher = publisher;
	}

	public double getPrice() 
	{
		return price;
	}

	public void setPrice(double price) 
	{
		this.price = price;
	}

	public LocalDate getPublishDate() 
	{
		return publishDate;
	}

	public void setPublishDate(LocalDate publishDate) 
	{
		this.publishDate = publishDate;
	}

	public int getId() 
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}

	public boolean wasPublished()
	{
		return LocalDateTime.now().isAfter(publishDate.atTime(0, 0));
	}
	
	@Override
	public String toString()
	{
		return String.format("Game [id: %d, title: %s, description: %s, genre: %s, developer: %s, publisher: %s, price: %.2f, publishDate: %s]", 
				id, title, description, genre, developer, publisher,  price, publishDate);
	}
	
	@Override
	public boolean equals(Object obj) 
	{
		return (id == ((Game) obj).getId());
	}
}
