package de.fhws.genericAi.neuralNetwork.VisualizeNeuralNet;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import de.fhws.genericAi.neuralNetwork.Layer;

class LayerGraphics {

	List<NodeGraphics> nodes;
	double[][] weights;
	
	static final Color RED = new Color(0xd13126);
	static final Color GREEN = new Color(0x1fb012);

	public LayerGraphics(Layer layer, int x, int width, int height) {
		nodes = new ArrayList<NodeGraphics>();
		int yOffset = height / layer.getNumNodes();
		for (int i = 0; i < layer.getNumNodes(); i++) {
			nodes.add(new NodeGraphics(x, yOffset * i));
		}
		weights = layer.getWeights().getData();
	}
	
	public void draw(Graphics g, LayerGraphics prevLayer) {
		nodes.forEach(n -> n.draw(g));
		if(prevLayer == null) return;
		
		for(int y = 0; y < weights.length; y++) {
			for(int x = 0; x < weights[y].length; x++) {
				NodeGraphics n1 = nodes.get(y);
				int n1x = n1.x + n1.width/2;
				int n1y = n1.y + n1.height/2 + NeuralNetVisualizer.DECORATOR_OFFSET;
				NodeGraphics n2 = prevLayer.nodes.get(x);
				int n2x = n2.x + n2.width/2;
				int n2y = n2.y + n2.height/2 + NeuralNetVisualizer.DECORATOR_OFFSET;
				
				Graphics2D g2 = (Graphics2D) g;

				double weight = weights[y][x];
				if(weight < 0) 
					g2.setColor(RED);
				else
					g2.setColor(GREEN);
			
                g2.setStroke(new BasicStroke((float)Math.abs(weight/1000)));
                g2.draw(new Line2D.Float(n1x, n1y, n2x, n2y));
			}
		}
	}
	
	public int size() {
		return nodes.size();
	}

}
