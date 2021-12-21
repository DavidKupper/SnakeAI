package de.fhws.davidkupper.snakegame;

import de.fhws.davidkupper.flatgame.GameGraphics;
import de.fhws.davidkupper.neuralnetwork.LinearVector;
import de.fhws.davidkupper.neuralnetwork.NeuralNet;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.function.DoubleUnaryOperator;
import java.util.function.IntUnaryOperator;

public class SnakeGame extends GameGraphics implements KeyListener{
    public static final int SQUARE_SIZE = 20;
    public static final int FIELD_WIDTH = 40;
    public static final int FIELD_HEIGHT = 30;
    public static final boolean ENDLESS_FIELD = false;

    private Snake snake;
    private Apple apple;
    private int score;

    private NeuralNet player;

    private int rgb = 0;

    public SnakeGame(NeuralNet player) {
        super(SQUARE_SIZE *FIELD_WIDTH, SQUARE_SIZE *FIELD_HEIGHT, 32);
        super.setTitle("Score: 0");
        super.setUndecorated(true);
        super.addKeyListener(this);
        this.player = player;

        snake = new Snake(FIELD_WIDTH / 2, FIELD_HEIGHT / 2, 5);
        spawnApple();
        super.setVisible(true);
        //startGameTimer();
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
        //stopGameTimer();
        JOptionPane.showMessageDialog(this, "Game Over!\nYour score: " + score);
    }

    @Override
    public void updateGame() {
        boolean outOfBorder = snake.move();
        if(outOfBorder && !ENDLESS_FIELD)
            gameOver();
        else
            checkCollisions();
    }

    @Override
    public void paintGame(Graphics g) {
        // paint background
        g.setColor(new Color(0x575757));
        g.fillRect(0, 0, getAreaWidthPxl(), getAreaHeightPxl());

        // paint score
        g.setColor(Color.white);
        g.drawString("Score: " + score, 5, 15);

        //paint snake
        snake.paint(g);

        //paint apple
        apple.paint(g);

        g.setColor(Color.white);
        for(int x = 0; x < FIELD_WIDTH; x++) {
            g.drawLine(x*SQUARE_SIZE, 0, x*SQUARE_SIZE, FIELD_HEIGHT*SQUARE_SIZE);
        }
        for(int y = 0; y < FIELD_HEIGHT; y++) {
            g.drawLine(0, y*SQUARE_SIZE, FIELD_WIDTH*SQUARE_SIZE, y*SQUARE_SIZE);
        }
    }

    public void getDirectionFromNN() {
        LinearVector output = player.calcOutput(getViewVector());
        System.out.println(output);
        System.out.println(output.getIndexOfBiggest());
        System.out.println("----");
        switch (output.getIndexOfBiggest()) {
            case 0 -> snake.setDirection(Snake.Direction.UP);
            case 1 -> snake.setDirection(Snake.Direction.LEFT);
            case 2 -> snake.setDirection(Snake.Direction.RIGHT);
            case 3 -> snake.setDirection(Snake.Direction.DOWN);
            default -> throw new IndexOutOfBoundsException("index out of bounds");
        }
    }

    public LinearVector getFieldVector() {
        // for snake top down view
        double[] field = new double[FIELD_WIDTH * FIELD_HEIGHT];
        for(Part p : snake.getParts()) {
            field[p.getX()*p.getY()] = 1.0;
        }
        field[apple.getX()*apple.getY()] = 0.5;
        return new LinearVector(field);
    }

    public LinearVector getViewVector() {
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

    public int[] distances(int modifyX, int modifyY) {
        final int WALL = 0;
        final int SNAKE = 1;
        final int APPLE = 2;
        int[] distances = new int[3];
        Item head = snake.getHead();
        int x = head.getX();
        int y = head.getY();
        for(;;) {
            if(distances[APPLE] == 0 && x == apple.getX() && y == apple.getY()) {
                distances[APPLE] = calcDistance(x, y, head.getX(), head.getY());
            }
            else if(distances[SNAKE] == 0) {
                for(Item body : snake.getParts()) {
                    if(x == body.getX() && y == body.getY()) {
                        distances[SNAKE] = calcDistance(x, y, head.getX(), head.getY());
                        break;
                    }
                }
            }

            if(x < 0 || x >= FIELD_WIDTH || y < 0 || y >= FIELD_HEIGHT) {
                distances[WALL] = calcDistance(x, y, head.getX(), head.getY());
                break;
            }
            x += modifyX;
            y += modifyY;
        }
        return distances;
    }

    public int calcDistance(int x1, int y1, int x2, int y2) {
        return Math.abs((x1 - x2)) + Math.abs(y1 - y2);
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_X)
            System.exit(0);

        else if(e.getKeyCode() == KeyEvent.VK_T) {
            getDirectionFromNN();
        }

        else {
            if(e.getKeyCode() == KeyEvent.VK_W)
                snake.setDirection(Snake.Direction.UP);

            else if(e.getKeyCode() == KeyEvent.VK_A)
                snake.setDirection(Snake.Direction.LEFT);

            else if(e.getKeyCode() == KeyEvent.VK_S)
                snake.setDirection(Snake.Direction.DOWN);

            else if(e.getKeyCode() == KeyEvent.VK_D)
                snake.setDirection(Snake.Direction.RIGHT);

        }

        tick();

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    public static void main(String[] args) {
        DoubleUnaryOperator reLu = d -> d < 0 ? 0 : d;
        DoubleUnaryOperator sigmoid = d -> (1 + Math.tanh(d / 2)) / 2;


        NeuralNet player = new NeuralNet.Builder(10, true, 10, false, sigmoid)
                // for snake top down view
        //        .addLayers(FIELD_WIDTH*FIELD_HEIGHT, 200, 100, 50, 25, 4)
                // for directional snake view
                .addLayers(3*8, 200, 100, 50, 25, 4)
                .build();
        new SnakeGame(player);


        /*
        // Test NN
        NeuralNet nn = new NeuralNet.NeuralNetBuilder(10, true, 10, false, sigmoid)
                .addLayers(10, 4, 2)
                .build();
        LinearVector in = LinearVector.createRandomLinearVector(10, 1, false);
        System.out.println(in);
        nn.calcAllLayer(in).forEach(v -> System.out.println(v));

         */
    }


}
