package edu.escuelaing.arsw.labs.boardsecurews.endpoints;

import java.io.IOException;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.escuelaing.arsw.labs.boardsecurews.model.Board;
import edu.escuelaing.arsw.labs.boardsecurews.model.Point;

@Component
@ServerEndpoint("/boardService")
public class BoardEndPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(BoardEndPoint.class);

    // Queue for all open WebSocket sessions
    private static Queue<Session> queue = new ConcurrentLinkedQueue<>();

    private static Session ownSession = null;

    private static Board board = Board.getInstance();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static void send(List<Point> points, Session session) {
        try {
            for (Point point : points) {
                session.getBasicRemote().sendText(MAPPER.writeValueAsString(point));
            }
        } catch (IOException e) {
            LOGGER.debug(e.getLocalizedMessage());
        }
    }

    // Call this method to send a message to all clients
    public static void send(String msg) {
        try {
            // Send updates to all open WebSocket sessions
            for (Session session : queue) {
                if (!session.equals(ownSession)) {
                    session.getBasicRemote().sendText(msg);
                }
                LOGGER.info("Sent: {}", msg);
            }
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
        }
    }

    public static void send(String msg, boolean includeOwnSession) {
        try {
            // Send updates to all open WebSocket sessions
            for (Session session : queue) {
                if (includeOwnSession) {
                    session.getBasicRemote().sendText(msg);
                } else {
                    if (!session.equals(ownSession)) {
                        session.getBasicRemote().sendText(msg);
                    }
                }
                LOGGER.info("Sent: {}", msg);
            }
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
        }
    }

    @OnMessage
    public static void processPoint(String message, Session session) {
        ownSession = session;
        LOGGER.info("Point received: {}. From session: {}", message, session);
        try {
            Point point = MAPPER.readValue(message, Point.class);
            if (!board.getPoints().contains(point)) {
                board.addPoint(point);
                send(message);
            }
        } catch (JsonProcessingException e) {
            LOGGER.debug(e.getLocalizedMessage());
        }
    }

    @OnOpen
    public static void openConnection(Session session) {
        // Register this connection in the queue
        ownSession = session;
        if (!queue.contains(session)) {
            queue.add(session);
        }
        LOGGER.info("Connection opened. Id: {}", session.getId());
        try {
            session.getBasicRemote().sendText("Connection established.");
            if (!board.getPoints().isEmpty()) {
                send(board.getPoints(), session);
            }
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
        }
    }

    @OnClose
    public void closedConnection(Session session) {
        // Remove this connection from the queue
        queue.remove(session);
        LOGGER.info("Connection closed. Id: {}", session.getId());
    }

    @OnError
    public void error(Session session, Throwable t) {
        // Remove this connection from the queue
        queue.remove(session);
        LOGGER.info(t.getMessage());
        LOGGER.info("Connection error. Id: {}", session.getId());
    }

}
