package org.furb.cg.render;

import java.util.ArrayList;
import javax.media.opengl.GL;

public class Cube3D {

	private GL gl;
	
	private ArrayList<Ponto> coordenadas;
	private float red;
	private float green;
	private float blue;
	
	private int mapX;
	private int mapY;
	
	public Cube3D(GL gl, int deslocX, int deslocY, int deslocZ){
		this.gl = gl;
		this.coordenadas = new ArrayList<Ponto>();
		this.initPoints(deslocX, deslocY, deslocZ);
		
		this.setColor(1, 0,0);
		this.setMapXY(0,0);
	}
	
	private void initPoints(int deslocX, int deslocY, int deslocZ){
		
		Ponto p = new Ponto(-1 + deslocX, -1 + deslocY, 1 + deslocZ, 0);
		this.coordenadas.add(p);
		
		p = new Ponto(-1+ deslocX, 1 + deslocY, 1 + deslocZ, 0);
		this.coordenadas.add(p);
		
		p = new Ponto( 1+ deslocX, 1 + deslocY, 1 + deslocZ, 0);
		this.coordenadas.add(p);
		
		p = new Ponto( 1+ deslocX, -1 + deslocY, 1 + deslocZ, 0);
		this.coordenadas.add(p);
		
		p = new Ponto( -1+ deslocX, -1 + deslocY, -1 + deslocZ, 0);
		this.coordenadas.add(p);
		
		p = new Ponto( -1+ deslocX, 1 + deslocY, -1 + deslocZ, 0);
		this.coordenadas.add(p);
		
		p = new Ponto( 1+ deslocX, 1 + deslocY, -1 + deslocZ, 0);
		this.coordenadas.add(p);
		
		p = new Ponto( 1+ deslocX, -1 + deslocY, -1 + deslocZ, 0);
		this.coordenadas.add(p);
	}
	
	public void setColor(float red, float green, float blue){
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public void setMapXY(int mapX, int mapY) {
		this.mapX = mapX;
		this.mapY = mapY;
	}

	public int getMapX() {
		return mapX;
	}

	public int getMapY() {
		return mapY;
	}
	
	public void draw()
	{
		//cor
		gl.glColor3f(this.red, this.green, this.blue);
		
		//desenha contornando o objeto
		
		//frente
		drawPolygon(gl, 0, 3, 2, 1); 
		//direita
		drawPolygon(gl, 2, 3, 7, 6);
		//costas
		drawPolygon(gl, 4, 5, 6, 7);
		//esquerda
		drawPolygon(gl, 5, 4, 0, 1);		
		
		//cima
		drawPolygon(gl, 1, 2, 6, 5);
		//baixo
		drawPolygon(gl, 3, 0, 4, 7);
	}
	
	
	private void drawPolygon(GL gl, int index1, int index2,int index3, int index4)
	{
		gl.glBegin(GL.GL_LINE_LOOP);
		
		gl.glVertex3f(this.coordenadas.get(index1).getX(),this.coordenadas.get(index1).getY(), this.coordenadas.get(index1).getZ());
		gl.glVertex3f(this.coordenadas.get(index2).getX(),this.coordenadas.get(index2).getY(), this.coordenadas.get(index2).getZ());
		gl.glVertex3f(this.coordenadas.get(index3).getX(),this.coordenadas.get(index3).getY(), this.coordenadas.get(index3).getZ());
		gl.glVertex3f(this.coordenadas.get(index4).getX(),this.coordenadas.get(index4).getY(), this.coordenadas.get(index4).getZ());
		
		gl.glEnd();
	}
	
}
