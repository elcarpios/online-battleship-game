package salvo.salvo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired // Skip configurations elsewhere of what to inject and just does it for you
    private GamePlayerRepository gamePlayerRepo;

    @RequestMapping("/games") // Call getAll() when a GET for the URL /games is received
    public Map<String,Object> makeUserGameDTO(Authentication authentication) {
        Map<String,Object> dto = new HashMap<>();
        if(!isGuest(authentication)) {
            Player currentUser = playerRepo.findByUserName(authentication.getName()).get(0);
            dto.put("player", makeUserDTO(currentUser));
        }
        dto.put("game", makeGameDTO());
        return dto;
    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    public Map<String,Object> makeUserDTO(Player user) {
        Map<String,Object> dto = new HashMap<>();
        dto.put("id", user.getId());
        dto.put("name", user.getUserName());
        return dto;
    }

    public List<Object> makeGameDTO() {
        List<Game> games = gameRepo.findAll();
        List<Object> dto = new ArrayList<Object>(); // dto = Data Transfer Object
        for(Game game : games) {
            Map<String, Object> eachGame = new LinkedHashMap<String, Object>();
            eachGame.put("id",game.getId());
            eachGame.put("created",game.getCreationDate());
            eachGame.put("gamePlayers", game.getGameplayers().stream()
                    .map(this::makeGamePlayerDTO)
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


    @RequestMapping(value = "/leaderboard")
    public List<Map<String,Object>> makeleaderboardDTO() {
        List<Map<String,Object>> dto = new ArrayList<>();
        List<Player> players = playerRepo.findAll();
        for(Player player : players) {
            Map<String,Object> dtoPlayer = new HashMap<>();
            Map<String,Double> dtoScore = makeScoresDTO(player.getScores());

            dtoPlayer.put("user",player.getUserName());
            dtoPlayer.put("total", dtoScore.get("total"));
            dtoPlayer.put("win", dtoScore.get("win").intValue());
            dtoPlayer.put("draw", dtoScore.get("draw").intValue());
            dtoPlayer.put("lost", dtoScore.get("lost").intValue());
            dto.add(dtoPlayer);
        }

        dto.sort((a,b) -> {
            if((Double)a.get("total") > (Double)b.get("total")) {
                return -1;
            }
            else {
                return 1;
            }
        });
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
        dto.put("win", win);
        dto.put("draw", draw);
        dto.put("lost", lost);
        return dto;
    }


    // Method to Return DTO with GamePlayer Information
    @RequestMapping(value = "/game_view/{id}")
    public Map<String,Object> makeGamePlayerDTO(@PathVariable("id") long id) {
        GamePlayer gamePlayer = gamePlayerRepo.findOne(id);
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id",gamePlayer.getId());
        dto.put("created",gamePlayer.getGame().getCreationDate());
        dto.put("gamePlayers", gamePlayer.getGame().getGameplayers()
                .stream()
                .map(this::makeGamePlayerDTO) // For each gamePlayer(gp) we send it to the function
                .collect(Collectors.toList()));
        dto.put("ships", makeShipsDTO(gamePlayer.getShips()));
        dto.put("salvoes", gamePlayer.getGame().getGameplayers()
                .stream()
                .map(gp-> makeSalvoesDTO(gp.getSalvoes())) // For each gamePlayer(gp) we send it to the function
                .collect(Collectors.toList()));
        return dto;
    }

    private List<Object> makeShipsDTO(Set<Ship> ships) {

        return ships.stream()
                .map(this::getShipDTO)
                .collect(Collectors.toList());
    }

    private Map<String, Object> getShipDTO(Ship ship) {

        Map<String,Object> shipDTO = new HashMap<>();
        shipDTO.put("Type", ship.getType());
        shipDTO.put("Locations", ship.getLocations());

        return shipDTO;
    }

    private List<Object> makeSalvoesDTO(Set<Salvo> salvoes) {
        List<Object> dto = new ArrayList<>();
        for(Salvo salvo : salvoes){
            dto.add(salvo.getLocations());
        }
        return dto;
    }

    // Method to responds to a request to create a new player
    @RequestMapping("/players")
    //@RequestParam
    // Annotation which indicates that a method parameter should be bound to a web request parameter.
    public ResponseEntity<String> createPlayer(@RequestParam String name, @RequestParam String password) {
        if (name.isEmpty()) {
            return new ResponseEntity<>("No name given", HttpStatus.FORBIDDEN);
        }
        if(playerRepo.findByName(name).size() == 0) {
            Player newPlayer = new Player(name,"",password);
            playerRepo.save(newPlayer);
            return new ResponseEntity<>("Named added", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Name already used", HttpStatus.CONFLICT);
        }
    }

}
