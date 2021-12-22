package de.fhws.davidkupper.snakegame;

import de.fhws.davidkupper.flatgame.GameGraphics;
import de.fhws.davidkupper.neuralnetwork.NeuralNet;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.function.DoubleUnaryOperator;

public class SnakeGame extends GameGraphics implements KeyListener{
    public static final int SQUARE_SIZE = 20;

    private SnakeGameLogic logic;

    public SnakeGame(SnakeGameLogic logic) {
        super(SQUARE_SIZE * SnakeGameLogic.FIELD_WIDTH, SQUARE_SIZE * SnakeGameLogic.FIELD_HEIGHT,
                32, logic);
        this.logic = logic;
        super.setTitle("Score: 0");
        super.setUndecorated(true);
        super.addKeyListener(this);


        super.setVisible(true);
        //startGameTimer();
    }

    @Override
    public void paintGame(Graphics g) {
        // paint background
        g.setColor(new Color(0x575757));
        g.fillRect(0, 0, getAreaWidthPxl(), getAreaHeightPxl());

        // paint score
        g.setColor(Color.white);
        g.drawString("Score: " + logic.getScore(), 5, 15);

        //paint snake
        logic.getSnake().paint(g);

        //paint apple
        logic.getApple().paint(g);

        g.setColor(Color.white);
        for(int x = 0; x < SnakeGameLogic.FIELD_WIDTH; x++) {
            g.drawLine(x*SQUARE_SIZE, 0, x*SQUARE_SIZE, SnakeGameLogic.FIELD_HEIGHT*SQUARE_SIZE);
        }
        for(int y = 0; y < SnakeGameLogic.FIELD_HEIGHT; y++) {
            g.drawLine(0, y*SQUARE_SIZE, SnakeGameLogic.FIELD_WIDTH*SQUARE_SIZE, y*SQUARE_SIZE);
        }
    }




    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_X)
            System.exit(0);

        else {
            if(e.getKeyCode() == KeyEvent.VK_W)
                logic.getSnake().setDirection(Snake.Direction.UP);

            else if(e.getKeyCode() == KeyEvent.VK_A)
                logic.getSnake().setDirection(Snake.Direction.LEFT);

            else if(e.getKeyCode() == KeyEvent.VK_S)
                logic.getSnake().setDirection(Snake.Direction.DOWN);

            else if(e.getKeyCode() == KeyEvent.VK_D)
                logic.getSnake().setDirection(Snake.Direction.RIGHT);

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

        SnakeAi ai = new SnakeAi(player);
        int score = ai.startPlaying(null);
        System.out.println(score);


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
