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

    @Autowired // Skip configurations elsewhere of what to inject and just does it for you
    private ShipRepository shipRepo;

    @RequestMapping(value="/games", method=RequestMethod.GET) // Call getAll() when a GET for the URL /games is received
    public Map<String,Object> makeUserGameDTO(Authentication authentication) {
        Map<String,Object> dto = new HashMap<>();
        if(!isGuest(authentication)) {
            Player currentUser = playerRepo.findByUserName(authentication.getName());
            dto.put("player", makeUserDTO(currentUser));
        }
        dto.put("game", makeGameDTO());
        return dto;
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

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    @RequestMapping(value="/games", method=RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createGame(Authentication authentication) {
        if (isGuest(authentication)) {
            Map<String, Object> dto = new HashMap<>();
            return new ResponseEntity<>(makeMap("Error","No user logged in"), HttpStatus.UNAUTHORIZED);
        }
        else {
            Player user = playerRepo.findByUserName(authentication.getName());
            Game game = new Game();
            gameRepo.save(game);
            GamePlayer gp = new GamePlayer(game,user);
            gamePlayerRepo.save(gp);
            return new ResponseEntity<>(getGPId(gp), HttpStatus.CREATED);
        }
    }

    private Map<String, Object> getGPId(GamePlayer gp) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("idGP", gp.getId());
        return dto;
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
    public ResponseEntity<Object> makeGamePlayerDTO(@PathVariable("id") long id, Authentication authentication) {
        GamePlayer gamePlayer = gamePlayerRepo.findOne(id);
        // First check if gamePlayer user is the same than the actual user
        Player currentUser = playerRepo.findByUserName(authentication.getName());
        if(gamePlayer.getPlayer().getId() == currentUser.getId()) {
            Map<String, Object> dto = new LinkedHashMap<String, Object>();
            dto.put("id",gamePlayer.getId());
            dto.put("player_id",gamePlayer.getPlayer().getId());
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
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("That is not your game!", HttpStatus.FORBIDDEN);
        }

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
        if(playerRepo.findByUserName(name) == null) {
            Player newPlayer = new Player("",name,password);
            playerRepo.save(newPlayer);
            return new ResponseEntity<>("Named added", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Name already used", HttpStatus.CONFLICT);
        }
    }


    // Method to Return DTO with Players game information
    @RequestMapping(value = "/game/{id}/players")
    public ResponseEntity<Object> checkPlayersGame(@PathVariable("id") long id, Authentication authentication) {
        Map<String, Object> dto = new HashMap<>();
        // Some user is connected?
        if (isGuest(authentication)) {
            dto.put("Error","No user logged in");
            return new ResponseEntity<>(dto, HttpStatus.UNAUTHORIZED);
        }
        else {
            Game game = gameRepo.findOne(id);
            if(game == null) {
                return new ResponseEntity<>(makeMap("Error","No game with this ID"), HttpStatus.FORBIDDEN);
            } else if(isFull(game)){
                return new ResponseEntity<>(makeMap("Error","The game is full"), HttpStatus.FORBIDDEN);
            } else {
                Player user = playerRepo.findByUserName(authentication.getName());
                GamePlayer gp = new GamePlayer(game,user);
                gamePlayerRepo.save(gp);
                return new ResponseEntity<>(getGPId(gp), HttpStatus.CREATED);
            }
        }

    }

    private Map<String,Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    // Game is complete?
    private boolean isFull (Game game) {
        return game.getGameplayers().size() == 2;
    }


    // Method to create and save list of ships
    @RequestMapping(value = "/games/players/{gpId}/ships")
    public ResponseEntity<String> createShips(@PathVariable("gpId") long gpId,
                                              @RequestBody List<Ship> ships,
                                              Authentication authentication) {
        GamePlayer gp = gamePlayerRepo.findOne(gpId);
        // Current user and gp owner are the same
        Player user = playerRepo.findByUserName(authentication.getName());
        if(isGuest(authentication) || gp == null || (user.getId() != gp.getPlayer().getId()) ) {
            return new ResponseEntity<>("Unauthorized access", HttpStatus.UNAUTHORIZED);
        }
        else if(gp.getShips().size() > 0){
            return new ResponseEntity<>("Already has the ships placed", HttpStatus.UNAUTHORIZED);
        } else {
            for(Ship ship : ships) {
                saveShip(gp,ship);
            }
            return new ResponseEntity<>("Ships have been placed", HttpStatus.CREATED);
        }

    }

    private void saveShip (GamePlayer gp, Ship ship) {
        gp.addShip(ship);
        shipRepo.save(ship);
    }
}
