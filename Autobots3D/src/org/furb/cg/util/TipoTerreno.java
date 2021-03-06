package org.furb.cg.util;

public enum TipoTerreno {

	//Nao alterar o codigo destes
	GRASS		(0),
	WATER		(1),
	TREES		(2),
	
	//Outros tipos
	DIRT_GRASS	(3),
	ROBOT		(4),
	TARGET		(5);
	
	private int type;
	
	TipoTerreno(int c){
		setType(c);
	}
	
	public static TipoTerreno valueOf(int tpTerreno)
	{
		for(TipoTerreno tipo : values() )
		{
			if( tipo.getType() == tpTerreno )
			{
				return tipo;
			}
		}
		
		return null;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}
}
