package dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Game;
import service.OrderBy;

public class GameDAO extends DAO 
{
	public GameDAO()
	{
		super();
		connect();
	}
	
	public void dispose()
	{
		close();
	}
	
	public int insert(Game game)
	{
		int id = -1;
		
		try
		{
			String sql = "INSERT INTO game (title, description, genre, developer, publisher, price, publish_date) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
			
			PreparedStatement st = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			st.setString(1, game.getTitle());
			st.setString(2, game.getDescription());
			st.setString(3, game.getGenre());
			st.setString(4, game.getDeveloper());
			st.setString(5, game.getPublisher());
			st.setDouble(6, game.getPrice());
			st.setDate(7, Date.valueOf(game.getPublishDate()));
			
			int affectedRows = st.executeUpdate();
			
			if(affectedRows == 0)
				throw new SQLException("Falha ao criar jogo, nenhuma linha afetada");
			
			try (ResultSet generatedKeys = st.getGeneratedKeys())
			{
				if(!generatedKeys.next())
					throw new SQLException("Falha ao criar jogo, nenhum id obtido");
				
				id = generatedKeys.getInt(1);
			}
			
			st.close();
		}
		catch(SQLException ex)
		{
			ex.printStackTrace();
		}
		
		return id;
	}
	
	public Game get(int id)
	{
		Game game = null;
		
		try 
		{
			Statement st = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM game WHERE id = " + id;
			ResultSet rs = st.executeQuery(sql);
			
			if(rs.next())
			{
				game = new Game(rs.getInt("id"), rs.getString("title"), rs.getString("description"), rs.getString("genre"), 
						rs.getString("developer"), rs.getString("publisher"), rs.getFloat("price"), rs.getDate("publish_date").toLocalDate());
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return game;
	}
	
	public List<Game> get(OrderBy orderBy)
	{
		return get(orderBy.value);
	}
	
	private List<Game> get(String orderBy)
	{
		List<Game> games = new ArrayList<Game>();
		
		try
		{
			Statement st = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM game" + ((orderBy.trim().length() == 0) ? "" : " ORDER BY " + orderBy);	
			ResultSet rs = st.executeQuery(sql);
			
			while(rs.next())
			{
				games.add(new Game(rs.getInt("id"), rs.getString("title"), rs.getString("description"), rs.getString("genre"), 
						rs.getString("developer"), rs.getString("publisher"), rs.getFloat("price"), rs.getDate("publish_date").toLocalDate()));
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return games;
	}
	
	public boolean update(Game game)
	{
		boolean status = false;
		
		try
		{
			String sql = "UPDATE game SET title = ?, description = ?, genre = ?, developer = ?, "
					+ "publisher = ?, price = ?, publish_date = ? WHERE id = ?";
			PreparedStatement st = connection.prepareStatement(sql);
			st.setString(1, game.getTitle());
			st.setString(2, game.getDescription());
			st.setString(3, game.getGenre());
			st.setString(4, game.getDeveloper());
			st.setString(5, game.getPublisher());
			st.setDouble(6, game.getPrice());
			st.setDate(7, Date.valueOf(game.getPublishDate()));
			st.setInt(8, game.getId());
			
			int affectedRows = st.executeUpdate();
			st.close();
			
			status = affectedRows > 0;
		}	
		catch(SQLException ex)
		{
			throw new RuntimeException(ex);
		}
		
		return status;
	}
	
	public boolean delete(int id)
	{
		boolean status = false;
		
		try
		{
			Statement st = connection.createStatement();
			String sql = "DELETE FROM game WHERE id = " + id;
			int affectedRows = st.executeUpdate(sql);
			st.close();
			
			status = affectedRows > 0;
		}
		catch(SQLException ex)
		{
			throw new RuntimeException(ex);
		}
		
		return status;
	}
}
