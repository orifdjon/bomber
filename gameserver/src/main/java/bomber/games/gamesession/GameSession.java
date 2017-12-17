package bomber.games.gamesession;


import bomber.connectionhandler.PlayerAction;
import bomber.games.model.GameObject;
import bomber.games.model.Tickable;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class GameSession {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(GameSession.class);
    private final Map<Integer, GameObject> replica = new ConcurrentHashMap<>();
    private final int id;
    private final AtomicInteger idGenerator = new AtomicInteger(0); // У каждой сессии свой набор id
    private final ConcurrentLinkedQueue<PlayerAction> inputQueue = new ConcurrentLinkedQueue<>();
    private int connectedPlayerCount = 0;
    public static final int DEFAULT_SETTING = 0;

    private static final int MAX_PLAYER_IN_GAME = 4;
    private GameMechanics gameMechanics = new GameMechanics(DEFAULT_SETTING, MAX_PLAYER_IN_GAME);

    private volatile boolean gameOver = false;

    public ConcurrentLinkedQueue<PlayerAction> getInputQueue() {
        return inputQueue;
    }

    public GameSession(int id, Set<Tickable> tickables) {
        this.id = id;
        gameMechanics.setTickables(tickables);
    }

    public void setupGameMap() {
        gameMechanics.setupGame(replica, idGenerator);

    }

    public int getConnectedPlayerCount() {
        return connectedPlayerCount;
    }

    public void incConnectedPlayerCount() {
        this.connectedPlayerCount++;
    }

    public void decConnectedPlayerCount() {
        this.connectedPlayerCount--;
    }

    public Integer getInc() {
        return idGenerator.getAndIncrement();
    }

    public Integer getId() {
        return id;
    }

    public AtomicInteger getIdGenerator() {
        return idGenerator;
    }

    public Map<Integer, GameObject> getReplica() {
        return replica;
    }

    public GameMechanics getGameMechanics() {
        return gameMechanics;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            if (obj instanceof GameSession) {
                GameSession gameSession = (GameSession) obj;
                return this.id == gameSession.id;
            }
            return false;
        }
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}