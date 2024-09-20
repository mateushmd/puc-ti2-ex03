package app;

import static spark.Spark.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import exceptions.NotFoundException;
import model.Game;
import service.GameService;
import service.OrderBy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class Application 
{
	private static GameService gameService = new GameService();
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	static
	{
		objectMapper.registerModule(new JavaTimeModule());
	}
	
	public static void main(String[] args)
	{
		port(6789);
		
		staticFiles.externalLocation("src/main/resources/public");
		
		get("/hello", (req, res) -> 
		{
			return "Hello, Spark";
		});
		
		get("/game/:id", (req, res) -> 
		{
			int id = Integer.parseInt(req.params(":id"));
			
			res.type("application/json");
			
			ObjectMapper objectMapper = new ObjectMapper();
			
			String json = "{}";
			
			try
			{
				json = objectMapper.writeValueAsString(gameService.get(id));
				res.status(200);
			}
			catch(NotFoundException ex)
			{
				json = createErrorMessageJson("Não foi encontrado um jogo com o id " + id);
				res.status(404);
			}
			
			return json;
		});
		
		get("/game/list/any-order", (req, res) ->
		{
			List<Game> list = gameService.getAll(OrderBy.DEFAULT);
			String json = objectMapper.writeValueAsString(list);
			res.status(200);
			
			return json;
		});
		
		get("/game/list/:orderby", (req, res) -> 
		{	
			String orderByParam = req.params(":orderby");
			
			String json = "{}";
			
			try
			{
				OrderBy orderBy = OrderBy.get(orderByParam);
				
				List<Game> list = gameService.getAll(orderBy);
				json = objectMapper.writeValueAsString(list);
				res.status(200);
			}
			catch(IllegalArgumentException ex)
			{
				json = createErrorMessageJson("Critério de ordenação inválido");
				res.status(400);
			}
			
			return json;
		});
		
		post("/game/insert", (req, res) ->
		{
			String body = req.body();
			
			Game game = objectMapper.readValue(body, Game.class);
		
			String json = "{}";
			
			try
			{
				int id = gameService.insert(game);
				
				res.status(202);
				json = createSuccessMessageJson(String.valueOf(id));				
			}
			catch(SQLException ex)
			{
				res.status(500);
				json = createErrorMessageJson(ex.getMessage());
			}
			
			return json;
		});
		
		put("/game/update", (req, res) -> 
		{
			Game game = new Game(
					Integer.parseInt(req.queryParams("id")),
					req.queryParams("title"),
					req.queryParams("description"),
					req.queryParams("genre"),
					req.queryParams("developer"),
					req.queryParams("publisher"),
					Double.parseDouble(req.queryParams("price")),
					LocalDate.parse(req.queryParams("publishDate"))
				);
			
			String json = "{}";
			
			try
			{
				gameService.update(game);
				res.status(202);
				json = createSuccessMessageJson("Jogo atualizado com sucesso");
			}
			catch(NotFoundException ex)
			{
				json = createErrorMessageJson("Jogo não encontrado");
				res.status(404);
			}
			
			return json;
			
		});
		
		get("/game/delete/:id", (req, res) -> 
		{
			int id = Integer.parseInt(req.params(":id"));
			
			String json = "{}";
			
			try
			{
				gameService.delete(id);				
				res.status(200);
				json = createSuccessMessageJson("Jogo deletado com sucesso");
			}
			catch(NotFoundException ex)
			{
				json = createErrorMessageJson("Não foi encontrado um jogo com o id " + id);
				res.status(404);
			}			
			
			return json;
		});
	}
	
	private static String createSuccessMessageJson(String message)
	{
		return "{\"success\":\"" + message + "\"}";
	}
	
	private static String createErrorMessageJson(String errorMessage)
	{
		return "{\"error\":\"" + errorMessage + "\"}";
	}
}
