package edu.escuelaing.arsw.labs.boardsecurews.model;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private List<Point> points;
    private static Board instance;

    public Board() {
        points = new ArrayList<>();
    }

    public void clear() {
        points.clear();
    }

    public void addPoint(Point point) {
        if (!points.contains(point)) {
            points.add(point);
        }
    }

    public List<Point> getPoints() {
        return points;
    }

    public static Board getInstance() {
        if (instance == null) {
            instance = new Board();
            return instance;
        }
        return instance;
    }

    @Override
    public String toString() {
        return "Board [points=" + points + "]";
    }

}
