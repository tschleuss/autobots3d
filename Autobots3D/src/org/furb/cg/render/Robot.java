package org.furb.cg.render;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;

import org.furb.cg.util.TipoTerreno;

import com.sun.opengl.util.texture.TextureCoords;

public class Robot implements Object3D 
{
	private int objectID;
	private boolean bindTexture;
	
	private TipoTerreno tipoTerreno;
	private List<Ponto> coordenadas;
	
	private float red;
	private float green;
	private float blue;
	
	private int mapX;
	private int mapY;
	
	public Robot(int deslocX, int deslocY, int deslocZ)
	{
		this.coordenadas = new ArrayList<Ponto>();
		this.initPoints(deslocX, deslocY, deslocZ);
		
		this.setColor(1, 0,0);
		this.setMapXY(0,0);
		this.setBindTexture(true);
	}
	
	private void initPoints(int deslocX, int deslocY, int deslocZ){
		
		Ponto p = new Ponto(-1 + deslocX, -1 + deslocY, 1 + deslocZ, 0);
		coordenadas.add(p);
		
		p = new Ponto(-1+ deslocX, 1 + deslocY, 1 + deslocZ, 0);
		coordenadas.add(p);
		
		p = new Ponto( 1+ deslocX, 1 + deslocY, 1 + deslocZ, 0);
		coordenadas.add(p);
		
		p = new Ponto( 1+ deslocX, -1 + deslocY, 1 + deslocZ, 0);
		coordenadas.add(p);
		
		p = new Ponto( -1+ deslocX, -1 + deslocY, -1 + deslocZ, 0);
		coordenadas.add(p);
		
		p = new Ponto( -1+ deslocX, 1 + deslocY, -1 + deslocZ, 0);
		coordenadas.add(p);
		
		p = new Ponto( 1+ deslocX, 1 + deslocY, -1 + deslocZ, 0);
		coordenadas.add(p);
		
		p = new Ponto( 1+ deslocX, -1 + deslocY, -1 + deslocZ, 0);
		coordenadas.add(p);
	}
	

	public void moveTo(int deslocX, int deslocY, int deslocZ)
	{
		coordenadas.clear();
		initPoints(deslocX, deslocY, deslocZ);
	}
	
	public void draw(GL gl)
	{
		gl.glColor3f(this.red, this.green, this.blue); //cor
		
		drawPolygon(gl, 0, 3, 2, 1); //frente
		drawPolygon(gl, 2, 3, 7, 6); //direita
		drawPolygon(gl, 4, 5, 6, 7); //costas
		drawPolygon(gl, 5, 4, 0, 1); //esquerda
		drawPolygon(gl, 1, 2, 6, 5); //cima
		drawPolygon(gl, 3, 0, 4, 7); //baixo
	}
	
	public void draw(GL gl, TextureCoords tc)
	{
		return;
	}
	
	private void drawPolygon(GL gl, int index1, int index2,int index3, int index4)
	{
		gl.glBegin(GL.GL_POLYGON);
		
		gl.glVertex3f(this.coordenadas.get(index1).getX(),this.coordenadas.get(index1).getY(), this.coordenadas.get(index1).getZ());
		gl.glVertex3f(this.coordenadas.get(index2).getX(),this.coordenadas.get(index2).getY(), this.coordenadas.get(index2).getZ());
		gl.glVertex3f(this.coordenadas.get(index3).getX(),this.coordenadas.get(index3).getY(), this.coordenadas.get(index3).getZ());
		gl.glVertex3f(this.coordenadas.get(index4).getX(),this.coordenadas.get(index4).getY(), this.coordenadas.get(index4).getZ());
		
		gl.glEnd();
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

	public TipoTerreno getTipoTerreno() {
		return tipoTerreno;
	}

	public void setTipoTerreno(TipoTerreno tipoTerreno) {
		this.tipoTerreno = tipoTerreno;
	}

	public List<Ponto> getCoordenadas() {
		return coordenadas;
	}

	public void setCoordenadas(ArrayList<Ponto> coordenadas) {
		this.coordenadas = coordenadas;
	}

	public float getRed() {
		return red;
	}

	public void setRed(float red) {
		this.red = red;
	}

	public float getGreen() {
		return green;
	}

	public void setGreen(float green) {
		this.green = green;
	}

	public float getBlue() {
		return blue;
	}

	public void setBlue(float blue) {
		this.blue = blue;
	}

	public void setMapX(int mapX) {
		this.mapX = mapX;
	}

	public void setMapY(int mapY) {
		this.mapY = mapY;
	}

	public int getObjectID() {
		return this.objectID;
	}

	public void setObjectID(int id) {
		this.objectID = id;
	}

	public boolean isBindTexture() {
		return bindTexture;
	}

	public void setBindTexture(boolean bindTexture) {
		this.bindTexture = bindTexture;
	}
}
