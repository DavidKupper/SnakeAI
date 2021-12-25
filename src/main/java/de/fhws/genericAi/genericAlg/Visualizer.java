package de.fhws.genericAi.genericAlg;

public interface Visualizer {


	public void draw();
	
	public <T> void setVisualizedObject(T object);
}
