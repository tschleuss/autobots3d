package org.furb.cg.loader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.furb.cg.loader.structs.MapConfig;

public class MapParse {
	
	private URL source =  null;
	private MapConfig mc = null;
	private int[][] map = null;
	
	public MapParse(){
		
		try {
			
			this.source = new URL("file:src/org/furb/cg/resources/maps.list");
			
		} catch(Exception e) {
			System.err.println("Arquivo maps.list não encontrado. Exception: "+e);
		}
	}
	
    public int[][] getMap(){
        
    	if(this.map == null){
        	this.mc = this.loadMap();
        	
        	String[] mapVetor = this.mc.caminho.split("\n");
        	
        	this.map = new int[this.mc.linhas][this.mc.colunas];
        	
        	int lineNumber = 0;

    		for (String line : mapVetor) {
    			for(int i = 0; i < this.mc.colunas;i++ ){
    				map[lineNumber][i] = Integer.parseInt(Character.toString(line.charAt(i)));
    			}
    			lineNumber++;
    		} 
    		
    	}	
    	
		return map;	
    }
	
    private MapConfig loadMap() {
    	
    	String[] map = readMap();
    	String printMap = "";
    	
    	int qtdLinhas = 0;
    	int qtdColunas = 0;
    	
    	if(map != null){
    		for (String mapLine : map) {
    			
    			mapLine = mapLine.replaceAll("\\s", "");
    			
    			if(!mapLine.equals("")){
    				printMap += mapLine + "\n";
    				qtdLinhas++;
    				
    				if(mapLine.length() > qtdColunas)
    					qtdColunas = mapLine.length();
    			}
			}
    	}
    	
    	this.mc = new MapConfig();
    	
    	mc.caminho = printMap;
    	mc.linhas = qtdLinhas;
    	mc.colunas = qtdColunas;
    	
        return mc;
    }
    
	private String[] readMap() {
		String[] selectedMap = null;
		
		try {
			StringBuffer lines = new StringBuffer("");
			BufferedReader in = new BufferedReader(new InputStreamReader(this.source.openStream()));
			String line;
			
			while(null != (line = in.readLine())) {
				if (line.length() > 0 && line.charAt(0) != '/') {
					lines.append(line).append("\n");
				}
			}
			
			String[] maps = lines.toString().split("MAP");
			int mapID = 1 + (int)(Math.random() * maps.length-1); 
			
			selectedMap = maps[mapID].split("\n");
			
		} catch(Exception e) {
			System.err.println("Arquivo maps.list não encontrado. Exception: "+e);
		}

		return selectedMap;
	} 
	
}
