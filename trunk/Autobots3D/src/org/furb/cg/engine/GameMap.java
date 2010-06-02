package org.furb.cg.engine;

import java.io.Serializable;
import java.util.Random;

import org.furb.cg.engine.heuristica.RastreadorCaminho;
import org.furb.cg.engine.structs.Caminho;
import org.furb.cg.engine.structs.Mover;
import org.furb.cg.loader.MapParse;
import org.furb.cg.util.TipoTerreno;
public class GameMap implements Serializable {

	private static final long serialVersionUID = -2650379924476210435L;
	
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	
	private int[][]		terrain		= new int[WIDTH][HEIGHT];
	private int[][]		units		= new int[WIDTH][HEIGHT];
	private boolean[][]	visited		= new boolean[WIDTH][HEIGHT];

	public GameMap() {
		loadMap();
		loadBotPosition();
	}

	public Caminho getFasterPath(int origemX, int origemY, int destinoX, int destinoY){
		
		this.units[origemX][origemY] = TipoTerreno.ROBOT.getType(); 
		
		int tipo = this.getUnit(origemX, origemY);
		Mover m = new Mover(tipo);
		
		return getPath(m, origemX, origemY,destinoX, destinoY );
		
	}
	
	private Caminho getPath(Mover mover,int origemX, int origemY, int destinoX, int destinoY){

		RastreadorCaminho finder = new RastreadorCaminho(this, 500, true);
		Caminho caminho = finder.findPath(mover, origemX, origemY, destinoX, destinoY,true);
		
		return caminho;
	}
	
	private void loadBotPosition() 
	{
		int x, y;
		
		boolean validpos = false;
		
		Random r = new Random();
		
		while(!validpos){
			
			x = r.nextInt(this.WIDTH);
			y = r.nextInt(this.HEIGHT);
			
			if(getTerrain(x, y) == TipoTerreno.GRASS.getType()){

				units[x][y] = TipoTerreno.ROBOT.getType();
				
				validpos = true;
			}
		}
		
		
	}

	private void loadMap() 
	{
		this.terrain = new MapParse().getMap();
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
		return terrain[x][y];
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
		
		int unit = mover.getType();
		
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
