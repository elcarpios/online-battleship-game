package salvo.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
// Returns the object and the object data is written directly to the HTTP response as JSON/XML
// by registered HttpMessageConverters.
@RequestMapping("/api")
public class SalvoController {

    @Autowired // Skip configurations elsewhere of what to inject and just does it for you
    private GameRepository gameRepo;

    @RequestMapping("/games") // Call getAll() when a GET for the URL /games is received
    public List<Object> makeGameDTO() {
        List<Game> games = gameRepo.findAll();
        List<Object> dto = new ArrayList<Object>(); // dto = Data Transfer Object
        for(Game game : games) {
            Map<String, Object> eachGame = new LinkedHashMap<String, Object>();
            eachGame.put("id",game.getId());
            eachGame.put("created",game.getCreationDate());
            eachGame.put("gamePlayers", game.getGameplayers().stream()
                    .map(gamePlayer -> makeGamePlayerDTO(gamePlayer))
                    .collect(Collectors.toList()));
            dto.add(eachGame);
        }
        return dto;
    }

    private Map<String,Object> makeGamePlayerDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id",gamePlayer.getId());
        dto.put("player", makePlayerDTO(gamePlayer.getPlayer()));
        if(gamePlayer.getScore() != null) {
            dto.put("score", gamePlayer.getScore().getScore());
        }
        return dto;
    }

    private Map<String,Object> makePlayerDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id",player.getId());
        dto.put("email",player.getUserName());
        return dto;
    }


    public List<Long> getId() {
        return gameRepo.findAll().stream()
                .map(game -> game.getId())
                .collect(Collectors.toList());
    }

    /* // Create a new List <Map> to store de info about gamePlayers
            List<Map> gamePlayersInfo =  new ArrayList<Map>();
            List<Long> gamePlayersIds = new ArrayList<Long>();
            gamePlayersIds = gameRepo.findAll().stream()
                    .map(gamePlayers -> gamePlayers.getId())
                    .collect(Collectors.toList());
            eachGame.put("gamePlayers",gamePlayersIds);

            */
     /* FIRST VERSION OF GETID
    @RequestMapping("/games") // Call getAll() when a GET for the URL /games is received
    public List<Long> getId() {
    List<Game> games = gameRepo.findAll();
    List<Long> ids = new ArrayList<Long>();
    for (Game game : games) {
        long id = game.getId();
        ids.add(id);
    }
    return ids;*/

    /* RETURN a list of games
    public List<Game> getAll() {
        return gameRepo.findAll();
    }*/

    @Autowired
    private PlayerRepository playerRepo;

    @RequestMapping(value = "/leaderboard")
    public List<Object> makeleaderboardDTO() {
        List<Object> dto = new ArrayList<>();
        List<Player> players = playerRepo.findAll();
        for(Player player : players) {
            Map<String,Object> dtoScore = new HashMap<>();
            dtoScore.put(player.getUserName(), makeScoresDTO(player.getScores()));
            dto.add(dtoScore);
        }
        return dto;
    }

    public Map<String,Double> makeScoresDTO(Set<Score> scores) {
        Map<String,Double> dto = new HashMap<>();
        Double total = 0.0;
        Double win = 0.0;
        Double lost = 0.0;
        Double draw = 0.0;

        for(Score score : scores) {
            Double points = score.getScore();
            switch (points.toString()) {
                case "0.0": lost++;
                break;
                case "0.5": draw++;
                break;
                case "1.0": win++;
                break;
                default:
                    break;
            }
            total += points;
        }
        dto.put("total", total);
        dto.put("win(s)", win);
        dto.put("draw(s)", draw);
        dto.put("lost(s)", lost);
        return dto;
    }


    // Method to Return DTO with GamePlayer Information
    @Autowired // Skip configurations elsewhere of what to inject and just does it for you
    private GamePlayerRepository gamePlayerRepo;

    @RequestMapping(value = "/game_view/{id}")
    public Map<String,Object> makeGamePlayerDTO(@PathVariable("id") long id) {
        GamePlayer gamePlayer = gamePlayerRepo.findOne(id);
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id",gamePlayer.getId());
        dto.put("created",gamePlayer.getGame().getCreationDate());
        dto.put("gamePlayers", gamePlayer.getGame().getGameplayers()
                .stream()
                .map(gp-> makeGamePlayerDTO(gp)) // For each gamePlayer(gp) we send it to the function
                .collect(Collectors.toList()));
        dto.put("ships", MakeShipsDTO(gamePlayer.getShips()));
        dto.put("salvoes", gamePlayer.getGame().getGameplayers()
                .stream()
                .map(gp-> MakeSalvoesDTO(gp.getSalvoes())) // For each gamePlayer(gp) we send it to the function
                .collect(Collectors.toList()));
        return dto;
    }

    private List<Object> MakeShipsDTO(Set<Ship> ships) {
        List<Object> dto = new ArrayList<>();
        for(Ship ship : ships){
            Map<String,Object> eachShip = new HashMap<>();
            eachShip.put("Type", ship.getType());
            eachShip.put("Locations", ship.getLocations());
            dto.add(eachShip);
        }
        return dto;
    }

    private List<Object> MakeSalvoesDTO(Set<Salvo> salvoes) {
        List<Object> dto = new ArrayList<>();
        for(Salvo salvo : salvoes){
            dto.add(salvo.getLocations());
        }
        return dto;
    }
}
