package service;

import java.sql.SQLException;
import java.util.List;

import dao.GameDAO;
import exceptions.NotFoundException;
import model.Game;

public class GameService 
{
	private GameDAO dao = new GameDAO();
	
	public int insert(Game game) throws SQLException
	{	
		int id = dao.insert(game);
		
		if(id < 0)
			throw new SQLException("Não foi possível inserir o jogo"); 
		
		return id; 
	}
	
	public Game get(int id) throws NotFoundException
	{
		Game game = dao.get(id);
		
		if(game == null) throw new NotFoundException("Não foi encontrado um jogo com o id " + id);
		
		return game;
	}
	
	public List<Game> getAll(OrderBy orderBy)
	{
		List<Game> list = dao.get(orderBy);
		
		return list;
	}
	
	public void update(Game game) throws NotFoundException
	{
		if(!dao.update(game)) throw new NotFoundException("Não foi encontrado um jogo com o id " + game.getId());
	}
	
	public void delete(int id) throws NotFoundException
	{
		if(!dao.delete(id)) throw new NotFoundException("Não foi encontrado um jogo com o id " + id);
	}
}