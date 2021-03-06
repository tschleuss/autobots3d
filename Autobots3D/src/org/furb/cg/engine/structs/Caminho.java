package org.furb.cg.engine.structs;

import java.util.ArrayList;
import java.util.List;

public class Caminho {

	private List<Passo> steps = new ArrayList<Passo>();
	private List<Passo> stepsOld = new ArrayList<Passo>();
	
	public Caminho() 
	{
		super();
	}

	public int getLength() {
		return steps.size();
	}
	
	public Passo getStep(int index) {
		return (Passo) steps.get(index);
	}
	
	public List<Passo> getSteps(){
		return steps;
	}
	
	public int getX(int index) {
		return getStep(index).getX();
	}

	public int getY(int index) {
		return getStep(index).getY();
	}

	public void appendStep(int x, int y) {
		steps.add(new Passo(x,y));
	}

	public void appenStepOld(int x, int y){
		stepsOld.add(new Passo(x,y));
	}
	
	public void prependStep(int x, int y) {
		steps.add(0, new Passo(x, y));
	}
	
	public boolean contains(int x, int y) {
		return steps.contains(new Passo(x,y));
	}

	public boolean containsOld(int x, int y){
		return stepsOld.contains(new Passo(x,y));
	}
	
	public Passo getLastStep()
	{
		if (getLength() > 0){
			return getStep(getLength()-1);
		}
		
		return null;
	}
}
