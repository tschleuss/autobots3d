package org.furb.cg;

import java.awt.EventQueue;

public class Main {
	
	public static void main(String[] args)
	{
        EventQueue.invokeLater(new Runnable() 
        {
        	MapFrame frame = null;
        	
            public void run() 
            {
        		frame = new MapFrame();
        		frame.setLocationRelativeTo(null);
        		frame.setVisible(true);
        		frame.getCanvas().requestFocus();   		
            }
        });
	}
}
