package de.fhws.snakegame;

import de.fhws.genericAi.neuralNetwork.LinearVector;
import de.fhws.genericAi.neuralNetwork.NeuralNet;

import java.util.Arrays;

public class SnakeAi {
    NeuralNet nn;
    SnakeGameLogic logic;
    SnakeGame game;
    static int gamespeed = 100;

    public SnakeAi(NeuralNet nn) {
        logic = new SnakeGameLogic();
        this.nn = nn;
    }

    public double startPlaying(int max) {
        int counterToApple = 0;
        int survCounter = 0;
        while (!logic.isGameOver() && counterToApple++ < max) {
            getDirectionFromNN();
            int score = logic.getScore();
            logic.updateGame();
            if(logic.getScore() > score)
                counterToApple = 0;
            survCounter++;
        }
        return logic.getScore()*200 + survCounter/10d;
    }

    public double startPlayingWithDisplay() {
        game = new SnakeGame(logic);
        while (!logic.isGameOver()) {
            getDirectionFromNN();
            logic.updateGame();
            game.paint();
            try {
                Thread.sleep(gamespeed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return logic.getScore();
    }

    public void getDirectionFromNN() {
        LinearVector output = nn.calcOutput(getViewVector());
        switch (output.getIndexOfBiggest()) {
            case 0 -> logic.getSnake().setDirection(Snake.Direction.UP);
            case 1 -> logic.getSnake().setDirection(Snake.Direction.LEFT);
            case 2 -> logic.getSnake().setDirection(Snake.Direction.RIGHT);
            case 3 -> logic.getSnake().setDirection(Snake.Direction.DOWN);
            default -> throw new IndexOutOfBoundsException("index out of bounds");
        }
    }

    private LinearVector getFieldVector() {
        // for logic.getSnake() top down view
        double[] field = new double[SnakeGameLogic.FIELD_WIDTH * SnakeGameLogic.FIELD_HEIGHT];
        for(Part p : logic.getSnake().getParts()) {
            field[p.getX()*p.getY()] = 1.0;
        }
        field[logic.getApple().getX()*logic.getApple().getY()] = 0.5;
        return new LinearVector(field);
    }

    private LinearVector getViewVector() {
        int[] distances = new int[3*8];
        int counter = 0;
        // calculates all distances
        for(int x = -1; x <= 1; x++) {
            for(int y = -1; y <= 1; y++) {
                if(x == 0 && y == 0)
                    continue;
                int[] tmp = distances(x, y);
                for(int i = 0; i < 3; i++) {
                    distances[counter++] = tmp[i];
                }
            }
        }
        return new LinearVector(Arrays.stream(distances).mapToDouble(x -> x == 0 ? 0 : (1 / ((x-1) / 2.0 + 1))).toArray()); // 1 -> directly there, 0 -> not visible
    }

    private int[] distances(int modifyX, int modifyY) {
        final int WALL = 0;
        final int SNAKE = 1;
        final int APPLE = 2;
        int[] distances = new int[3];
        Item head = logic.getSnake().getHead();
        int x = head.getX();
        int y = head.getY();
        for(;;) {
            if(distances[APPLE] == 0 && x == logic.getApple().getX() && y == logic.getApple().getY()) {
                distances[APPLE] = calcDistance(x, y, head.getX(), head.getY());
            }
            else if(distances[SNAKE] == 0) {
                for(Item body : logic.getSnake().getParts()) {
                    if(x == body.getX() && y == body.getY()) {
                        distances[SNAKE] = calcDistance(x, y, head.getX(), head.getY());
                        break;
                    }
                }
            }

            if(x < 0 || x >= SnakeGameLogic.FIELD_WIDTH || y < 0 || y >= SnakeGameLogic.FIELD_HEIGHT) {
                distances[WALL] = calcDistance(x, y, head.getX(), head.getY());
                break;
            }
            x += modifyX;
            y += modifyY;
        }
        return distances;
    }

    private int calcDistance(int x1, int y1, int x2, int y2) {
        return Math.abs((x1 - x2)) + Math.abs(y1 - y2);
    }
}
