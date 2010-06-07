package org.furb.cg.render.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.media.opengl.GL;

import org.furb.cg.render.Object3D;
import org.furb.cg.util.ResourceUtil;
import org.furb.cg.util.TipoTerreno;

import com.sun.opengl.util.texture.TextureCoords;

/**
 * Representa um modelo carregado a partir
 * de um arquivo .OBJ e um arquivo .MTL
 * @author Evangelos Pournaras (eu acho!)
 * @author Thyago Schleuss (alteracao)
 */
public class GLModel implements Object3D {

    private List<float[]> vertexsets;
    private List<float[]> vertexsetsnorms;
    private List<float[]> vertexsetstexs;
    
    private List<int[]> faces;
    private List<int[]> facestexs;
    private List<int[]> facesnorms;
    
    private List<String[]> mattimings;
    
    private MTLLoader	materials;
    private int			objectlist;
    private int			numpolys;
    public float		toppoint;
    public float		bottompoint;
    public float		leftpoint;
    public float		rightpoint;
    public float		farpoint;
    public float		nearpoint;
    private String		mtlPath;
    
    //Referencias ao Autobots3D
    private TipoTerreno tipoTerreno;
    private int objectID;
	private int mapX;
	private int mapY;

    public GLModel(BufferedReader ref, boolean centerit, String path, GL gl)
    {
        vertexsets		= new ArrayList<float[]>();
        vertexsetsnorms	= new ArrayList<float[]>();
        vertexsetstexs	= new ArrayList<float[]>();
        
        faces		= new ArrayList<int[]>();
        facestexs	= new ArrayList<int[]>();
        facesnorms	= new ArrayList<int[]>();
        
        mattimings = new ArrayList<String[]>();
        
        mtlPath		= path;
        numpolys	= 0;
        toppoint	= 0.0f;
        bottompoint = 0.0f;
        leftpoint	= 0.0f;
        rightpoint	= 0.0f;
        farpoint	= 0.0f;
        nearpoint	= 0.0f;
        
        loadobject(ref);
        
        if(centerit) {
            centerit();
        }
        
        opengldrawtolist(gl);
        numpolys = faces.size();
        cleanup();
    }

    private void cleanup()
    {
        vertexsets.clear();
        vertexsetsnorms.clear();
        vertexsetstexs.clear();
        faces.clear();
        facestexs.clear();
        facesnorms.clear();
    }

    private void loadobject(BufferedReader br){
    	int linecounter = 0;
    	int facecounter = 0;
        try{
            boolean firstpass = true;
            String newline;
            while((newline = br.readLine()) != null){
              	linecounter++;
                if(newline.length() > 0){
                    newline = newline.trim();
                    
                    //LOADS VERTEX COORDINATES
                    if(newline.startsWith("v "))
                    {
                        float coords[] = new float[4];
                        newline = newline.substring(2, newline.length());
                        StringTokenizer st = new StringTokenizer(newline, " ");
                        for(int i = 0; st.hasMoreTokens(); i++)
                            coords[i] = Float.parseFloat(st.nextToken());

                        if(firstpass){
                            rightpoint = coords[0];
                            leftpoint = coords[0];
                            toppoint = coords[1];
                            bottompoint = coords[1];
                            nearpoint = coords[2];
                            farpoint = coords[2];
                            firstpass = false;
                        }
                        if(coords[0] > rightpoint)
                            rightpoint = coords[0];
                        if(coords[0] < leftpoint)
                            leftpoint = coords[0];
                        if(coords[1] > toppoint)
                            toppoint = coords[1];
                        if(coords[1] < bottompoint)
                            bottompoint = coords[1];
                        if(coords[2] > nearpoint)
                            nearpoint = coords[2];
                        if(coords[2] < farpoint)
                            farpoint = coords[2];
                        vertexsets.add(coords);
                    }
                    else
                    
                    //LOADS VERTEX TEXTURE COORDINATES
                    if(newline.startsWith("vt")){
                        float coords[] = new float[4];
                        newline = newline.substring(3, newline.length());
                        StringTokenizer st = new StringTokenizer(newline, " ");
                        for(int i = 0; st.hasMoreTokens(); i++)
                            coords[i] = Float.parseFloat(st.nextToken());

                        vertexsetstexs.add(coords);
                    }
                    else
                    
                    //LOADS VERTEX NORMALS COORDINATES
                    if(newline.startsWith("vn")){
                        float coords[] = new float[4];
                        newline = newline.substring(3, newline.length());
                        StringTokenizer st = new StringTokenizer(newline, " ");
                        for(int i = 0; st.hasMoreTokens(); i++)
                            coords[i] = Float.parseFloat(st.nextToken());

                        vertexsetsnorms.add(coords);
                    }
                    else
                    
                    //LOADS FACES COORDINATES
                    if(newline.startsWith("f ")){
                    	facecounter++;
                        newline = newline.substring(2, newline.length());
                        StringTokenizer st = new StringTokenizer(newline, " ");
                        int count = st.countTokens();
                        int v[] = new int[count];
                        int vt[] = new int[count];
                        int vn[] = new int[count];
                        for(int i = 0; i < count; i++){
                            char chars[] = st.nextToken().toCharArray();
                            StringBuffer sb = new StringBuffer();
                            char lc = 'x';
                            for(int k = 0; k < chars.length; k++){
                                if(chars[k] == '/' && lc == '/')
                                    sb.append('0');
                                lc = chars[k];
                                sb.append(lc);
                            }

                            StringTokenizer st2 = new StringTokenizer
                            (sb.toString(), "/");
                            int num = st2.countTokens();
                            v[i] = Integer.parseInt(st2.nextToken());
                            if(num > 1)
                                vt[i] = Integer.parseInt(st2.nextToken());
                            else
                                vt[i] = 0;
                            if(num > 2)
                                vn[i] = Integer.parseInt(st2.nextToken());
                            else
                                vn[i] = 0;
                        }

                        faces.add(v);
                        facestexs.add(vt);
                        facesnorms.add(vn);
                    }
                    else
                    
                    //LOADS MATERIALS
                    if (newline.charAt(0) == 'm' && newline.charAt(1) == 't' && newline.charAt(2) == 'l' && newline.charAt(3) == 'l' && newline.charAt(4) == 'i' && newline.charAt(5) == 'b') {
						if(mtlPath!=null)
							loadmaterials();
					}
					else
					
					//USES MATELIALS
					if (newline.charAt(0) == 'u' && newline.charAt(1) == 's' && newline.charAt(2) == 'e' && newline.charAt(3) == 'm' && newline.charAt(4) == 't' && newline.charAt(5) == 'l') {
						String[] coords = new String[2];
						String[] coordstext = new String[3];
						coordstext = newline.split("\\s+");
						coords[0] = coordstext[1];
						coords[1] = facecounter + "";
						mattimings.add(coords);
						//System.out.println(coords[0] + ", " + coords[1]);
					}
                }
             }
        }
        catch(IOException e){
            System.out.println("Failed to read file: " + br.toString());
        }
        catch(NumberFormatException e){
            System.out.println("Malformed OBJ file: " + br.toString() + "\r \r"+ e.getMessage());
        }
    }
    
    private void loadmaterials() 
    {
		try {
			
			BufferedReader brm = new BufferedReader( new InputStreamReader( ResourceUtil.getResource(mtlPath, GLModel.class) ) );
			materials = new MTLLoader(brm,mtlPath);
			
		} catch (Exception e) {
			System.out.println("Could not open file: " + mtlPath);
			materials = null;
		}
	}

    private void centerit(){
        float xshift = (rightpoint - leftpoint) / 2.0F;
        float yshift = (toppoint - bottompoint) / 2.0F;
        float zshift = (nearpoint - farpoint) / 2.0F;
        for(int i = 0; i < vertexsets.size(); i++){
            float coords[] = new float[4];
            coords[0] = ((float[])vertexsets.get(i))[0] - leftpoint - xshift;
            coords[1] = ((float[])vertexsets.get(i))[1] - bottompoint - yshift;
            coords[2] = ((float[])vertexsets.get(i))[2] - farpoint - zshift;
            vertexsets.set(i, coords);
        }

    }

    public float getXWidth(){
        float returnval = 0.0F;
        returnval = rightpoint - leftpoint;
        return returnval;
    }

    public float getYHeight(){
        float returnval = 0.0F;
        returnval = toppoint - bottompoint;
        return returnval;
    }

    public float getZDepth(){
        float returnval = 0.0F;
        returnval = nearpoint - farpoint;
        return returnval;
    }

    public int numpolygons(){
        return numpolys;
    }

    public void opengldrawtolist(GL gl){
        ////////////////////////////////////////
		/// With Materials if available ////////
		////////////////////////////////////////
		this.objectlist = gl.glGenLists(1);
		
		int nextmat = -1;
		int matcount = 0;
		int totalmats = mattimings.size();
		String[] nextmatnamearray = null;
		String nextmatname = null;
		
		if (totalmats > 0 && materials != null) {
			nextmatnamearray = (String[])(mattimings.get(matcount));
			nextmatname = nextmatnamearray[0];
			nextmat = Integer.parseInt(nextmatnamearray[1]);
		}

		gl.glNewList(objectlist,4864);
		for (int i=0;i<faces.size();i++) {
			if (i == nextmat) {
					gl.glEnable(GL.GL_COLOR_MATERIAL);
					gl.glColor4f((materials.getKd(nextmatname))[0],(materials.getKd(nextmatname))[1],(materials.getKd(nextmatname))[2],(materials.getd(nextmatname)));
				matcount++;
				if (matcount < totalmats) {
					nextmatnamearray = (String[])(mattimings.get(matcount));
					nextmatname = nextmatnamearray[0];
					nextmat = Integer.parseInt(nextmatnamearray[1]);
				}
			}
			
			int[] tempfaces = (int[])(faces.get(i));
			int[] tempfacesnorms = (int[])(facesnorms.get(i));
			int[] tempfacestexs = (int[])(facestexs.get(i));
			
			//// Quad Begin Header ////
			int polytype;
			if (tempfaces.length == 3) {
				polytype = GL.GL_TRIANGLES;
			} else if (tempfaces.length == 4) {
				polytype = GL.GL_QUADS;
			} else {
				polytype = GL.GL_POLYGON;
			}
			gl.glBegin(polytype);
			////////////////////////////
			
			for (int w=0;w<tempfaces.length;w++) {
				if (tempfacesnorms[w] != 0) {
					float normtempx = ((float[])vertexsetsnorms.get(tempfacesnorms[w] - 1))[0];
					float normtempy = ((float[])vertexsetsnorms.get(tempfacesnorms[w] - 1))[1];
					float normtempz = ((float[])vertexsetsnorms.get(tempfacesnorms[w] - 1))[2];
					gl.glNormal3f(normtempx, normtempy, normtempz);
				}
				
				if (tempfacestexs[w] != 0) {
					float textempx = ((float[])vertexsetstexs.get(tempfacestexs[w] - 1))[0];
					float textempy = ((float[])vertexsetstexs.get(tempfacestexs[w] - 1))[1];
					float textempz = ((float[])vertexsetstexs.get(tempfacestexs[w] - 1))[2];
					gl.glTexCoord3f(textempx,1f-textempy,textempz);
				}
				
				float tempx = ((float[])vertexsets.get(tempfaces[w] - 1))[0];
				float tempy = ((float[])vertexsets.get(tempfaces[w] - 1))[1];
				float tempz = ((float[])vertexsets.get(tempfaces[w] - 1))[2];
				gl.glVertex3f(tempx,tempy,tempz);
			}
			
			
			//// Quad End Footer /////
			gl.glEnd();
			///////////////////////////
			
			
		}
		gl.glEndList();
	}

	public void draw(GL gl, TextureCoords tc) 
	{
		return;
	}

	public void draw(GL gl) 
	{
        gl.glCallList(objectlist);
        gl.glDisable(GL.GL_COLOR_MATERIAL);
	}

	public int getMapX() {
		return this.mapX;
	}

	public int getMapY() {
		return this.mapY;
	}
	
	public void setMapX(int mapX) {
		this.mapX = mapX;
	}

	public void setMapY(int mapY) {
		this.mapY = mapY;
	}
	
	public void setMapXY(int mapX, int mapY) 
	{
		this.mapX = mapX;
		this.mapY = mapY;
	}

	public int getObjectID() {
		return this.objectID;
	}

	public TipoTerreno getTipoTerreno() {
		return this.tipoTerreno;
	}

	public boolean isBindTexture() {
		return false;
	}

	public void setBindTexture(boolean bind) {
		return;
	}

	public void setColor(float red, float green, float blue) {
		return;
	}

	public void setObjectID(int id) {
		this.objectID = id;
	}

	public void setTipoTerreno(TipoTerreno tipoTerreno) {
		this.tipoTerreno = tipoTerreno;
	}
}
