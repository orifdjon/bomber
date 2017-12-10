package bomber.connectionhandler;

import bomber.gameservice.controller.GameController;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

@Component
public class Json extends TextWebSocketHandler implements WebSocketHandler {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Json.class);



    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        log.info("WebSocket connection established - " + session);
        GameController.setConnectedPlayerCount(GameController.getConnectedPlayerCount() + 1);
        log.info("Prolonging WS connection for 60 SEC for player #" + GameController.getConnectedPlayerCount());
        sleep(TimeUnit.SECONDS.toMillis(300));
        log.info("Closing connection for player #" + "asd");
        session.close();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        GameController.setConnectedPlayerCount(GameController.getConnectedPlayerCount() - 1);
        log.info("Socket Closed: [" + closeStatus.getCode() + "] " + closeStatus.getReason());
        super.afterConnectionClosed(session, closeStatus);
    }





    /*public static String handleReplica(@NotNull Replica replica, @NotNull Map<Integer, ? extends GameObject> map) {
        DataReplica dataReplica = replica.getData();
        dataReplica.setExampleEEE(map);
        String json = JsonHelper.toJson(replica);
        return json;
    }*/




}