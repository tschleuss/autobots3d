package org.furb.cg.engine;

import java.io.Serializable;

import org.furb.cg.util.TipoTerreno;

public class GameMap implements Serializable {

	private static final long serialVersionUID = -2650379924476210435L;
	
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	
	private int[][]		terrain		= new int[WIDTH][HEIGHT];
	private int[][]		units		= new int[WIDTH][HEIGHT];
	private boolean[][]	visited		= new boolean[WIDTH][HEIGHT];

	public GameMap(String serverHost) {
		loadMap();
		loadBotPosition();
	}

	private void loadBotPosition() {
		
//		botPosition bp;
//		
//		try {
//			bp = this.autobotsRPC_cln.getBot();
//			units[bp.y][bp.x] = TipoTerreno.ROBOT.getType();
//		} catch (rpc_err e) {
//			e.printStackTrace();
//		}
	}

	private void loadMap() {
	
//		try {
//			this.terrain = this.autobotsRPC_cln.getMap();
//		} catch (rpc_err e) {
//			e.printStackTrace();
//		}
	}

	public void clearVisited() 
	{
		for (int x = 0; x < getWidthInTiles(); x++ ) 
		{
			for (int y = 0; y < getHeightInTiles(); y++ ) 
			{
				visited[x][y] = false;
			}
		}
	}

	public boolean visited(int x, int y) {
		return visited[x][y];
	}

	public int getTerrain(int x, int y) {
		//inverte x e y
		return terrain[y][x];
	}

	public int getUnit(int x, int y) {
		return units[x][y];
	}
	
	public void setUnit(int x, int y, int unit) {
		units[x][y] = unit;
	}
	
	public boolean blocked(Mover mover, int x, int y) {

		if (getUnit(x,y) != 0) {
			return true;
		}
		
		int unit = ((Mover) mover).getType();
		
		if (unit == TipoTerreno.ROBOT.getType()) {
			return getTerrain(x,y) != TipoTerreno.GRASS.getType();
		}

		return true;
	}

	public float getCost(Mover mover, int sx, int sy, int tx, int ty) {
		return 1;
	}

	public int getHeightInTiles() {
		return HEIGHT;
	}

	public int getWidthInTiles() {
		return WIDTH;
	}

	public void pathFinderVisited(int x, int y) {
		visited[x][y] = true;
	}

	public int[][] getTerrain() {
		return terrain;
	}

	public int[][] getUnits() {
		return units;
	}

	public boolean[][] getVisited() {
		return visited;
	}
}
