package de.fhws.davidkupper.snakegame;

import de.fhws.davidkupper.flatgame.GameLogic;

public class SnakeGameLogic extends GameLogic {
    private Snake snake;
    private Apple apple;
    private int score;
    boolean gameOver = false;

    public static final int FIELD_WIDTH = 40;
    public static final int FIELD_HEIGHT = 30;
    public static final boolean ENDLESS_FIELD = false;

    public SnakeGameLogic() {
        snake = new Snake(FIELD_WIDTH / 2, FIELD_HEIGHT / 2, 5);
        spawnApple();
    }

    public void checkCollisions() {
        if(snake.collidesWithSelf()) {
            gameOver();
        }
        if(snake.collidesWithHead(apple))
            eatApple();
    }

    public void spawnApple() {
        int x, y;
        boolean notValid;
        do {
            x = (int) (Math.random() * FIELD_WIDTH);
            y = (int) (Math.random() * FIELD_HEIGHT);
            notValid = snake.collidesWith(x, y);
        } while(notValid);
        apple = new Apple(x, y, 1);
    }

    public void eatApple() {
        score += apple.getScore();
        spawnApple();
        snake.grow();
    }

    public void gameOver() {
        gameOver = true;
    }

    @Override
    public void updateGame() {
        boolean outOfBorder = snake.move();
        if(outOfBorder && !ENDLESS_FIELD)
            gameOver();
        else
            checkCollisions();
    }

    public Snake getSnake() {
        return snake;
    }

    public Apple getApple() {
        return apple;
    }

    public int getScore() {
        return score;
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
