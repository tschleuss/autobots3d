package org.furb.cg.engine.structs;

import java.io.Serializable;

public class Mover implements Serializable {

	private static final long serialVersionUID = -2629552755055708433L;
	
	private int type;
	
	public Mover(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
}
