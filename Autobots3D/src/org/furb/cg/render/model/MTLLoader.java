package org.furb.cg.render.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MTLLoader {
	
	public List<MTL> materials = new ArrayList<MTL>();
	//private TextureLoader textureloader;
	
	public MTLLoader(BufferedReader ref, String pathtoimages) {
		//textureloader = new TextureLoader();
		loadobject(ref, pathtoimages);
		cleanup();
	}
	
	private void cleanup() {
		materials.clear();
	}
	
	public int getSize() {
		return materials.size();
	}
	
	public float getd(String namepass) 
	{
		float returnfloat = 1f;
		for (int i=0; i < materials.size(); i++) {
			MTL tempmtl = materials.get(i);
			if (tempmtl.getName().matches(namepass)) {
				returnfloat = tempmtl.getD();
			}
		}
		return returnfloat;
	}

	public float[] getKa(String namepass) 
	{
		float[] returnfloat = new float[3];
		for (int i=0; i < materials.size(); i++) {
			MTL tempmtl = materials.get(i);
			if (tempmtl.getName().matches(namepass)) {
				returnfloat = tempmtl.getKa();
			}
		}
		return returnfloat;
	}
	
	public float[] getKd(String namepass) 
	{
		float[] returnfloat = new float[3];
		for (int i=0; i < materials.size(); i++) {
			MTL tempmtl = materials.get(i);
			if (tempmtl.getName().matches(namepass)) {
				returnfloat = tempmtl.getKd();
			}
		}
		return returnfloat;
	}
	
	public float[] getKs(String namepass) 
	{
		float[] returnfloat = new float[3];
		for (int i=0; i < materials.size(); i++) {
			MTL tempmtl = materials.get(i);
			if (tempmtl.getName().matches(namepass)) {
				returnfloat = tempmtl.getKs();
			}
		}
		return returnfloat;
	}
	
	/*public Texture getTexture(String namepass) {
		Texture returntex = null;
		for (int i=0; i < Materials.size(); i++) {
			mtl tempmtl = (mtl)(Materials.get(i));
			if (tempmtl.name.matches(namepass)) {
				returntex = tempmtl.texture;
			}
		}
		return returntex;
	}*/

	private void loadobject(BufferedReader br, String pathtoimages) 
	{
		int linecounter = 0;
		
		try {
			
			String newline;
			boolean firstpass = true;
			MTL matset = new MTL();
			int mtlcounter = 0;
			
			while (((newline = br.readLine()) != null)) {
				linecounter++;
				newline = newline.trim();
				if (newline.length() > 0) {
					if (newline.charAt(0) == 'n' && newline.charAt(1) == 'e' && newline.charAt(2) == 'w') {
						if (firstpass) {
							firstpass = false;
						} else {
							materials.add(matset);
							matset = new MTL();
						}
						String[] coordstext = new String[2];
						coordstext = newline.split("\\s+");
						matset.setName(coordstext[1]);
						matset.setMtlnum(mtlcounter);
						mtlcounter++;
					}
					/*if (newline.charAt(0) == 'm' && newline.charAt(1) == 'a' && newline.charAt(2) == 'p' && newline.charAt(3) == '_' && newline.charAt(4) == 'K' && newline.charAt(5) == 'd') {
						String[] coordstext = new String[2];
						coordstext = newline.split("\\s+");
						matset.texture = textureloader.getTexture(pathtoimages + coordstext[1],false);
					}*/
					if (newline.charAt(0) == 'K' && newline.charAt(1) == 'a') {
						float[] coords = new float[3];
						String[] coordstext = new String[4];
						coordstext = newline.split("\\s+");
						for (int i = 1;i < coordstext.length;i++) {
							coords[i-1] = Float.valueOf(coordstext[i]).floatValue();
						}
						matset.setKa(coords);
					}
					if (newline.charAt(0) == 'K' && newline.charAt(1) == 'd') {
						float[] coords = new float[3];
						String[] coordstext = new String[4];
						coordstext = newline.split("\\s+");
						for (int i = 1;i < coordstext.length;i++) {
							coords[i-1] = Float.valueOf(coordstext[i]).floatValue();
						}
						matset.setKd(coords);
					}
					if (newline.charAt(0) == 'K' && newline.charAt(1) == 's') {
						float[] coords = new float[3];
						String[] coordstext = new String[4];
						coordstext = newline.split("\\s+");
						for (int i = 1;i < coordstext.length;i++) {
							coords[i-1] = Float.valueOf(coordstext[i]).floatValue();
						}
						matset.setKs(coords);
					}
					if (newline.charAt(0) == 'd') {
						String[] coordstext = newline.split("\\s+");
						matset.setD(Float.valueOf(coordstext[1]).floatValue());
					}
				}
			}
			materials.add(matset);
			
		}
		catch (IOException e) {
			System.out.println("Failed to read file: " + br.toString());
			e.printStackTrace();		
		}
		catch (NumberFormatException e) {
			System.out.println("Malformed MTL (on line " + linecounter + "): " + br.toString() + "\r \r" + e.getMessage());
		}
		catch (StringIndexOutOfBoundsException e) {
			System.out.println("Malformed MTL (on line " + linecounter + "): " + br.toString() + "\r \r" + e.getMessage());
		}
	}
}
