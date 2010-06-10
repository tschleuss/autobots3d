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
	
	public static int WIDTH = 0;
	public static int HEIGHT = 0;
	
	private int[][]		terrain		= new int[WIDTH][HEIGHT];
	private int[][]		units		= new int[WIDTH][HEIGHT];
	private boolean[][]	visited		= new boolean[WIDTH][HEIGHT];

	private static final boolean ALLOW_DIAGONAL_MOVE = false;
	
	public GameMap() {
		loadMap();
		loadBotPosition();
		loadTargetPosition();
	}
	
	public Caminho getFasterPath(int origemX, int origemY, int destinoX, int destinoY)
	{
		int tipo = this.getUnit(origemX, origemY);
		Mover m = new Mover(tipo);
		
		return getPath(m, origemX, origemY,destinoX, destinoY );
	}
	
	private Caminho getPath(Mover mover,int origemX, int origemY, int destinoX, int destinoY)
	{
		RastreadorCaminho finder = new RastreadorCaminho(this, 500, ALLOW_DIAGONAL_MOVE);
		Caminho caminho = finder.findPath(mover, origemX, origemY, destinoX, destinoY, ALLOW_DIAGONAL_MOVE);
		
		return caminho;
	}
	
	private void loadTargetPosition()
	{
		int x, y;
		
		boolean validpos = false;
		Random r = new Random();
		
		while(!validpos)
		{
			x = r.nextInt(WIDTH);
			y = r.nextInt(HEIGHT);
			
			if( getTerrain(x, y) == TipoTerreno.GRASS.getType() )
			{
				if( getUnit(x, y) != TipoTerreno.ROBOT.getType() )
				{
					units[x][y] = TipoTerreno.TARGET.getType();
					validpos = true;	
				}
			}
		}
	}
	
	private void loadBotPosition() 
	{
		int x, y;
		
		boolean validpos = false;
		Random r = new Random();
		units = new int[WIDTH][HEIGHT];
		visited = new boolean[WIDTH][HEIGHT];
		
		while(!validpos)
		{
			x = r.nextInt(WIDTH);
			y = r.nextInt(HEIGHT);
			
			if( getTerrain(x, y) == TipoTerreno.GRASS.getType() )
			{
				units[x][y] = TipoTerreno.ROBOT.getType();
				validpos = true;
			}
		}
	}

	private void loadMap() 
	{
		this.terrain = new MapParse().getMap();
		WIDTH = terrain.length;
		HEIGHT = terrain[0].length;
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

		if (getUnit(x,y) != 0 && getUnit(x, y) != TipoTerreno.TARGET.getType() ) {
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
