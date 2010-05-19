package org.furb.cg.engine.heuristica;

import org.furb.cg.engine.GameMap;
import org.furb.cg.engine.heuristica.interfaces.HeuristicaRastreio;
import org.furb.cg.engine.structs.Mover;

public class HeuristicaMenorCustoVizinho implements HeuristicaRastreio {

	public float getCost(GameMap map, Mover mover, int x, int y, int tx, int ty) {		
		float dx = tx - x;
		float dy = ty - y;
		
		return ((dx*dx)+(dy*dy));
	}

}
