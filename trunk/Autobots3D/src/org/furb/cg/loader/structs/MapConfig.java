package org.furb.cg.loader.structs;

public class MapConfig {
	
	private int[][] map;
	private int linhas;
	private int colunas;
	
	public MapConfig() {
		super();
	}
	
	public int[][] getMap() {
		return map;
	}

	public void setMap(int[][] map) {
		this.map = map;
	}

	public int getLinhas() {
		return linhas;
	}

	public void setLinhas(int linhas) {
		this.linhas = linhas;
	}

	public int getColunas() {
		return colunas;
	}

	public void setColunas(int colunas) {
		this.colunas = colunas;
	}
}