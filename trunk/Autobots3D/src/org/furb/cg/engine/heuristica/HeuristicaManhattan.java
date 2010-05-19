package org.furb.cg.engine.heuristica;

import org.furb.cg.engine.GameMap;
import org.furb.cg.engine.heuristica.interfaces.HeuristicaRastreio;
import org.furb.cg.engine.structs.Mover;


public class HeuristicaManhattan implements HeuristicaRastreio {
	private int minimumCost;
	
	public HeuristicaManhattan(int minimumCost) {
		this.minimumCost = minimumCost;
	}
	
	public float getCost(GameMap map, Mover mover, int x, int y, int tx,
			int ty) {
		return minimumCost * (Math.abs(x-tx) + Math.abs(y-ty));
	}

}
