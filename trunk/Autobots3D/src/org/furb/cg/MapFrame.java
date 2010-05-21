package org.furb.cg;


import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;

import org.furb.cg.engine.GameMap;


public class MapFrame extends JFrame {

	private static final long serialVersionUID = -7999646168812247942L;
	
	private GLCanvas canvas	= null;
	private Canvas jogl		= null;
    private JPanel pnCanvas	= null;

    /**
     * Construtor da classe
     */
	public MapFrame() 
	{
		final int screenWidth = 500;
		final int screenHeight = 500;
		
		super.setMinimumSize(new Dimension(screenWidth, screenHeight));
		
		super.setTitle("[Autobots3D] - FURB 2010 - Computação Gráfica");
		super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		super.getContentPane().setLayout(new BorderLayout());
		
		this.initJOGL();
	}
	
	private void initJOGL() 
	{
		pnCanvas = new JPanel();
		
		pnCanvas.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		pnCanvas.setLayout(new BorderLayout());
        
        super.getContentPane().add(pnCanvas, BorderLayout.CENTER);
		
		GLCapabilities c = new GLCapabilities();
		c.setRedBits(8);
		c.setBlueBits(8);
		c.setGreenBits(8);
		c.setAlphaBits(8);
		
		jogl = new Canvas();
		jogl.setGameMap( new GameMap() );
		this.canvas = new GLCanvas(c);
		pnCanvas.add(this.canvas, BorderLayout.CENTER);
		this.getContentPane().add(canvas,BorderLayout.CENTER);
		canvas.addGLEventListener(jogl);        
		canvas.addKeyListener(jogl);
		canvas.requestFocus();		
	}

	public GLCanvas getCanvas() {
		return canvas;
	}
	
	
}
