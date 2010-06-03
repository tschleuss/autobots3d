package org.furb.cg;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GLCanvas;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sun.opengl.util.FPSAnimator;

public class CanvasGL extends JFrame {

	private static final long serialVersionUID = 2811420030112315612L;
	
	private static final int screenWidth = 700;
	private static final int screenHeight = 600;
	
	private FPSAnimator animator;
	private CanvasGLListener listener;
	
	public CanvasGL(int fps)
	{
		super("Programa");
		
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
		renderPanel.setPreferredSize( new Dimension(screenWidth,screenHeight) );

		GLCanvas canvas = new GLCanvas();
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
}