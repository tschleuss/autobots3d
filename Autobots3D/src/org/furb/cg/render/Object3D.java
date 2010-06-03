package org.furb.cg.render;

import javax.media.opengl.GL;

import org.furb.cg.util.TipoTerreno;

import com.sun.opengl.util.texture.TextureCoords;

public interface Object3D {

	void draw(GL gl, TextureCoords tc);
	TipoTerreno getTipoTerreno();
	
}
