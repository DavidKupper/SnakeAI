package de.fhws.genericAi.neuralNetwork.VisualizeNeuralNet;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import de.fhws.flatgame.GraphicsWindow;
import de.fhws.genericAi.NeuralNetSolution;
import de.fhws.genericAi.genericAlg.Visualizer;
import de.fhws.genericAi.neuralNetwork.Layer;
import de.fhws.genericAi.neuralNetwork.NeuralNet;

public class NeuralNetVisualizer extends GraphicsWindow implements Visualizer {

	int width;
	int height;

	List<LayerGraphics> layers;
	NeuralNet nn;
	
	static final int DECORATOR_OFFSET = 20;
	
	public NeuralNetVisualizer(int width, int height) {
		super(width, height);
		this.width = width;
		this.height = height - DECORATOR_OFFSET;
		//setResizable(true);
	}
	
	/**
	 * this Method creates a Window and draws the created NeuralNetwork
	 *
	 */
	@Override
	public void draw() {
		if (isVisible() != true)
			setVisible(true);
		

		repaint();
	}

	@Override
	public void paint(Graphics g) {
		//draw background
		g.fillRect(0, 0, width, height + DECORATOR_OFFSET);

		
		if(layers == null) return;

	
		for(int i = 1; i < layers.size(); i++) {
			layers.get(i).draw(g, layers.get(i-1));
		}
		layers.get(0).draw(g, null);
	}
	
	private void createGraphicComponents() {
		List<Layer> realLayers = nn.getLayers();
		layers = new ArrayList<>();
		int xOffset = width / realLayers.size();
		for(int i = 0; i < realLayers.size(); i++) {
			layers.add(new LayerGraphics(realLayers.get(i),xOffset/2 + xOffset*i, width,height));
		}
	}
	
	@Override
	public <T> void setVisualizedObject(T nn) {
		if(nn instanceof NeuralNetSolution) {
			this.nn = ((NeuralNetSolution) nn).getNeuralNetwork();
			createGraphicComponents();
			return;
		}
		throw new IllegalArgumentException("Visualized Object wasnt a NeuralNet");
	}
	
}