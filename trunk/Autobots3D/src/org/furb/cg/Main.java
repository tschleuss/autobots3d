package org.furb.cg;

import java.awt.EventQueue;

public class Main {
	
	public static void main(String[] args) 
	{
        EventQueue.invokeLater(new Runnable() 
        {
        	CanvasGL canvasGL = null;
        	
            public void run() 
            {
            	canvasGL = new CanvasGL(80);
            	canvasGL.setLocationRelativeTo(null);
        		canvasGL.setVisible(true);
        		canvasGL.requestFocus();
        		canvasGL.getCanvas().requestFocus();
            }
        });
	}
}
