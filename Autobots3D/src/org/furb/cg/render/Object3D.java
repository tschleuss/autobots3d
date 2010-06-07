package org.furb.cg.render;

import javax.media.opengl.GL;

import org.furb.cg.util.TipoTerreno;

import com.sun.opengl.util.texture.TextureCoords;

/**
 * Interface para os objetos 3D
 */
public interface Object3D {

	/**
	 * Metodos para desenho com ou sem textura
	 * do objeto 3D
	 * @param gl
	 * @param tc
	 */
	void draw(GL gl, TextureCoords tc);
	void draw(GL gl);
	
	/**
	 * Metodo para recuperar o tipo
	 * do terreno do objeto 3D
	 * @return
	 */
	TipoTerreno getTipoTerreno();
	void setTipoTerreno(TipoTerreno tipoTerreno);
	
	/**
	 * Metodos para indicar se o objeto
	 * tera ou nao textura
	 * @param bind
	 */
	void setBindTexture(boolean bind);
	boolean isBindTexture();
	
	/**
	 * Metodo para setar a cor do wireframe
	 * @param red
	 * @param green
	 * @param blue
	 */
	void setColor(float red, float green, float blue);
	
	/**
	 * Metodos para setar ou recuperar
	 * o id do objeto 3D
	 * @param id
	 */
	void setObjectID(int id);
	int getObjectID();
	
	/**
	 * Metodo para setar e recuperar
	 * a posicao X,Y no mapa do objeto
	 * @return
	 */
	int getMapX();
	int getMapY();
	void setMapXY(int mapX, int mapY);
	void setMapX(int mapX);
	void setMapY(int mapY);
}
