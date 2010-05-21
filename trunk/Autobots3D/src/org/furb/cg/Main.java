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
        		
//        		GameMap gMap = new GameMap();
//        		Caminho c = gMap.getFasterPath(5, 0, 26, 10);
//        		
//        		for(Passo p : c.getSteps())
//        		{
//					System.out.println(p.getX() + "/" + p.getY());
//				}        		
            }
        });
	}
}
