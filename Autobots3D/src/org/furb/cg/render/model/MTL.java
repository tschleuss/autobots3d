package org.furb.cg.render.model;

/**
 * Estrutura de um arquivo MTL
 * @author Thyago Schleuss
 */
public class MTL {
	
	private String name;
	private int mtlnum;
	private float d = 1f;
	private float[] Ka = new float[3];
	private float[] Kd = new float[3];
	private float[] Ks = new float[3];
	//private Texture texture = null;
	
	public MTL()
	{
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMtlnum() {
		return mtlnum;
	}

	public void setMtlnum(int mtlnum) {
		this.mtlnum = mtlnum;
	}

	public float getD() {
		return d;
	}

	public void setD(float d) {
		this.d = d;
	}

	public float[] getKa() {
		return Ka;
	}

	public void setKa(float[] ka) {
		Ka = ka;
	}

	public float[] getKd() {
		return Kd;
	}

	public void setKd(float[] kd) {
		Kd = kd;
	}

	public float[] getKs() {
		return Ks;
	}

	public void setKs(float[] ks) {
		Ks = ks;
	}
}
