package de.fhws.genericAi.neuralNetwork.VisualizeNeuralNet;

import java.awt.Color;
import java.awt.Graphics;

import de.fhws.flatgame.GameGraphics;

class NodeGraphics {

	private int xReal, yReal;
	
	int x;
	int y;
	int width = 20;
	int height = 20;
	
	Color c = new Color(0x415678);
	
	protected NodeGraphics(int x , int y) {
		this.x = x + width/2;
		this.y = y + height/2;
		this.xReal = x;
		this.yReal = y;
	}
	
	protected void draw(Graphics g) {
		g.setColor(c);
		g.fillOval(x , y + NeuralNetVisualizer.DECORATOR_OFFSET , width, height);
	}
	
	protected void setColor(int rgb) {
		c = new Color(rgb);
	}
	
	private void calcCoordinates() {
		this.x = xReal + width/2;
		this.y = yReal + height/2;
	}
	
	protected void resize(int width, int height) {
		this.width = width;
		this.height = height;
		calcCoordinates();
	}
}
