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
        super.setUndecorated(false);
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
        LinearVector output = player.calcOutput(getFieldVector());
        System.out.println(output);
        System.out.println(output.getIndexOfBiggest());
        switch (output.getIndexOfBiggest()) {
            case 0:
                snake.setDirection(Snake.Direction.UP);
                break;
            case 1:
                snake.setDirection(Snake.Direction.LEFT);
                break;
            case 2:
                snake.setDirection(Snake.Direction.RIGHT);
                break;
            case 3:
                snake.setDirection(Snake.Direction.DOWN);
                break;
            default:
                throw new IndexOutOfBoundsException("index out of bounds");
        }
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

    public LinearVector geViewVector() {
        double[] distances = new double[3*8];
        // calculate all distances from head to apple, border, and body
        return new LinearVector(Arrays.stream(distances).map(x -> (1 / (x / 2 + 1))).toArray());
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_T)
            tick();



    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    public static void main(String[] args) {
        DoubleUnaryOperator reLu = d -> d < 0 ? 0 : d;
        DoubleUnaryOperator sigmoid = d -> (1 + Math.tanh(d / 2)) / 2;


        NeuralNet player = new NeuralNet.NeuralNetBuilder(10, true, 10, false, sigmoid)
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
