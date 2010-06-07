package org.furb.cg.loader;

import javax.media.opengl.GL;

import org.furb.cg.util.ResourceUtil;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

/**
 * Classe utilitaria para carregar
 * textura para os objetos.
 * @author Thyago Schleuss
 */
public final class TextureLoader {
	
	private static TextureLoader tl = null;
	
	//Texturas
	private static Texture grassTex;
	private static Texture dirtGrassText;
	private static Texture waterTex;
	private static Texture groundTex;
	
	private TextureLoader()
	{
		super();
		loadTextures();
	}
	
	public static TextureLoader getInstance()
	{
		if( tl == null ) {
			tl = new TextureLoader();
		}
		
		return tl;
	}
	
	/**
	 * Carrega as texturas necessarias
	 */
	private void loadTextures()
	{
		grassTex = loadTexture("/org/furb/cg/resources/grass.png");
		dirtGrassText = loadTexture("/org/furb/cg/resources/dirtGrass.png");
		waterTex = loadTexture("/org/furb/cg/resources/water.png");
		groundTex = loadTexture("/org/furb/cg/resources/ground.jpg");
	}
	
	/**
	 * Carrega a textura
	 * @param texture
	 * @return
	 */
	public Texture loadTexture(String texture)
	{
		Texture tex = null;
		
		try {
			
			tex = TextureIO.newTexture( ResourceUtil.getResource(texture, TextureLoader.class) , false , null);
			tex.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
			tex.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return tex;
	}

	public Texture getGrassTex() {
		return grassTex;
	}

	public Texture getWaterTex() {
		return waterTex;
	}

	public Texture getGroundTex() {
		return groundTex;
	}

	public Texture getDirtGrassText() {
		return dirtGrassText;
	}
}
