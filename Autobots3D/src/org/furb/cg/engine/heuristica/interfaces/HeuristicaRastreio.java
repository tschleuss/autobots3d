package org.furb.cg.engine.heuristica.interfaces;

import org.furb.cg.engine.GameMap;
import org.furb.cg.engine.structs.Mover;


public interface HeuristicaRastreio {

	public float getCost(GameMap map, Mover mover, int x, int y, int tx, int ty);
}

