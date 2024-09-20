package service;

public enum OrderBy
{
	DEFAULT(""),
	ID("id"),
	TITLE("title"),
	GENRE("genre"),
	PRICE("price"),
	PUBLISH_DATE("publish_date");
	
	public final String value;
	
	OrderBy(String value) {
		this.value = value;
	}
	
	public static OrderBy get(String value) throws IllegalArgumentException
	{
		for(OrderBy orderBy : OrderBy.values())
		{
			if(orderBy.value.equals(value))
				return orderBy;
		}
		
		throw new IllegalArgumentException("Não existe um enum com o valor " + value);
	}
} 	