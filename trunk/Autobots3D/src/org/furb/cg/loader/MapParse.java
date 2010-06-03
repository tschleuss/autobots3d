package org.furb.cg.loader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.furb.cg.loader.structs.MapConfig;
import org.furb.cg.util.ResourceUtil;

public class MapParse {
	
	private MapConfig	mc		= null;
	private int[][]		map		= null;
	
	public MapParse() 
	{
		super();
	}
	
	/**
	 * Carrega o mapa e retorna
	 * um vetor de int com os valores
	 * do mapa.
	 * @return
	 */
    public int[][] getMap()
    {    
    	this.loadMap();
    	return map;
    }
	
    public void mapToString(){
    	for(int i=0; i< mc.getLinhas(); i++){
        	for(int j=0; j< mc.getColunas(); j++){
        		System.out.print(this.map[i][j]);
        	}
        	System.out.print("\n");
    	}

    }
    
    /**
     * Criar o objeto map config
     * com as propriedades do mapa
     * carregadas do arquivo de texto
     * @return
     */
    private void loadMap() 
    {	
    	map = this.readMap();
 
    	mc = new MapConfig();
    	mc.setMap( map );
    	mc.setColunas( map.length );
    	mc.setLinhas( map[0].length );
    }
    
    /**
     * Le o arquivo com o mapa e popula
     * os valores em um vetor de int.
     * @return
     */
	private int[][] readMap() 
	{
		List<String>	mapLines = null;
		int[][]			map = null;
		BufferedReader	in = null;
		InputStream		is = null;
		String			line = null;
		
		try {
			
			//Carrega a lista com as linhas do mapa
			mapLines = new ArrayList<String>();
			
			is = ResourceUtil.getResource("/org/furb/cg/resources/maps.list", MapParse.class);
			in = new BufferedReader( new InputStreamReader(is) );
			
			while( (line = in.readLine()) != null ) 
			{
				mapLines.add(line);
			}
			
			//Carrega o vetor que vai armazenar o mapa
			map = new int[mapLines.get(0).length()][mapLines.size()];
			
			//Ppula o vetor com os valores do mapa
			for (int y = 0; y < mapLines.size(); y++ ) 
			{
				final String currentLine = mapLines.get(y);
				
				for( int x = 0; x < currentLine.length(); x++ )
				{
					map[x][y] = Integer.valueOf(String.valueOf(currentLine.charAt(x)));
				}
			}
			
			if( is != null ) {
				is.close();
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}

		return map;
	} 
}
