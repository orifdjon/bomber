package bomber.gameservice.controller;


import bomber.games.gamesession.GameSession;
import bomber.games.model.GameObject;
import bomber.games.model.Tickable;
import bomber.games.util.GeneratorIdSession;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


@Controller
@RequestMapping("/game")
public class GameController {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(GameController.class);
    private static AtomicInteger connectedPlayerCount = new AtomicInteger(0);
    public static final Map<Long, GameSession> gameSessionMap = new ConcurrentHashMap<>();
    /**
     * curl -i localhost:8090/game/create
     */
    @RequestMapping(
            path = "/checkstatus",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> checkStatus() {
        return ResponseEntity.ok().body(Integer.toString(connectedPlayerCount.intValue()));//возращает gameId
    }

    @RequestMapping(
            path = "/create",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> create(@RequestParam("playerCount") String playerCount) {
        final long gameId = add();
         // засовываем gameId с нулевым GameSession, т.е GameSession по логике не существует
        log.info("Game has been created playerCount={}", playerCount);
        return ResponseEntity.ok().body(Long.toString(gameId));//возращает gameId
    }

    @RequestMapping(
            path = "/start",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> start(@RequestParam("gameId") String gameIdString) {
        long gameId = Long.parseLong(gameIdString);
        if (!gameSessionMap.containsKey(gameId)) {
            log.error("Don't have games to run gameId={}", gameId);
            return ResponseEntity.badRequest().body("");
        }
        this.start(gameId);
        return ResponseEntity.ok().body(gameIdString); //возращает gameId
    }

    public static int getConnectedPlayerCount() {
        return connectedPlayerCount.intValue();
    }

    public static synchronized void setConnectedPlayerCount(int value) {
        connectedPlayerCount.set(value);
    }

    public long add() {
        final long gameId;
        synchronized (this) {
            GeneratorIdSession.getAndIncrementId();
            gameId = GeneratorIdSession.getIdGenerator();
        }
        gameSessionMap.put(gameId, new GameSession(0));
        return gameId;
    }

    public void start(final long gameId) {
        new Thread(new GameThread(gameId), "game-mechanics-gameId-" + gameId).start();// создаем новый тред для игры c gameId
    }

   /* public static void main(String[] args) {                  // Это нужно было для проверки корректности
        GameController gameController = new GameController();
        gameController.create("4");
        long gameId = gameController.add();
        log.info(String.valueOf(gameId));
        log.info(String.valueOf(GeneratorIdSession.getIdGenerator()));
        gameController.start(gameId);
        if (gameSessionMap.get(gameId).getId() == gameId)
            log.info("all is ok");
    }*/
}
