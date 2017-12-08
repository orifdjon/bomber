package bomber.games.gameobject;

import bomber.games.geometry.Point;
import bomber.games.model.Positionable;
import org.slf4j.LoggerFactory;


public final class Wall implements Positionable {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Wall.class);

    private final long id;
    private final Point position;

    public Wall(final long id, final Point position) {
        this.id = id;
        this.position = position;
        log.info("New Wall: id={},  id={}, position({}, {})", id, position.getX(), position.getY());
    }


    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public long getId() {
        return id;
    }
}