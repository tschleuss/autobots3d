package org.furb.cg.engine.structs;

public class Passo{

	private int x;

	private int y;
	
	public Passo(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}


	public void setY(int y) {
		this.y = y;
	}


	public int hashCode() {
		return x*y;
	}

	public boolean equals(Object other) {
		if (other instanceof Passo) {
			Passo o = (Passo) other;
			
			return (o.x == x) && (o.y == y);
		}
		
		return false;
	}
}