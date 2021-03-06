package org.furb.cg;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.sun.opengl.util.FPSAnimator;

public class CanvasGL extends JFrame {

	private static final long serialVersionUID = 2811420030112315612L;
	
	private static final int SCREEN_WIDTH = 1000;
	private static final int SCREEN_HEIGHT = 700;
	
	private GLCanvas canvas;
	private FPSAnimator animator;
	private CanvasGLListener listener;
	
	public CanvasGL(int fps)
	{
		super("[Autobots3D] - FURB 2010 - Computacao Grafica");
		super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		Container c = getContentPane();
		c.setLayout( new BorderLayout() );
		c.add( makeRenderPanel(fps) , BorderLayout.CENTER );
		
		addWindowListener( new WindowAdapter() 
		{
			@Override
			public void windowClosing(WindowEvent e) 
			{
				new Thread( new Runnable() 
				{
					public void run() 
					{
						animator.stop();
						System.exit(0);
					}
				}).start();
			}
		});

		pack();
		setVisible(true);
		animator.start();
	}
	
	private JPanel makeRenderPanel(int fps)
	{
		JPanel renderPanel = new JPanel();
		renderPanel.setLayout( new BorderLayout() );
		renderPanel.setOpaque(false);
		renderPanel.setPreferredSize( new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT) );

		GLCapabilities c = new GLCapabilities();
		c.setRedBits(8);
		c.setBlueBits(8);
		c.setGreenBits(8);
		c.setAlphaBits(8);
		
		canvas = new GLCanvas(c);
		listener = new CanvasGLListener();
		
		canvas.addGLEventListener(listener);
		canvas.addMouseListener(listener);
		canvas.addMouseMotionListener(listener);
		canvas.addKeyListener(listener);
		
		animator = new FPSAnimator(canvas, fps, true);
		renderPanel.add(canvas, BorderLayout.CENTER);
		
		return renderPanel;
	}
	
	public FPSAnimator getAnimator() {
		return animator;
	}

	public GLCanvas getCanvas() {
		return canvas;
	}
}