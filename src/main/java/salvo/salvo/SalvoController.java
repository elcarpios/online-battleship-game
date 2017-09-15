package salvo.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
            dto.add(eachGame);
        }
        return dto;
    }

    public List<Long> getId() {
        return gameRepo.findAll().stream()
                .map(game -> game.getId())
                .collect(Collectors.toList());
    }

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

    /* STOPPED by the moment
    private Map<String,Object> getAllMap() {
        return (Map<String, Object>) gameRepo.findAll();
    }*/

}
