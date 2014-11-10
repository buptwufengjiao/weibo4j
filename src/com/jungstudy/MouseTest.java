package com.jungstudy;

import java.awt.*;
import java.awt.event.*; 
public class MouseTest extends Frame 
{ 
	public MouseTest() { 
		Button b = new Button("JavaWorld JavaQ&A"); 
		b.addActionListener( new ActionListener() { 
			public void actionPerformed(ActionEvent e) 
			{ System.exit(0); 
			} 
			} 
		); 
		add(b,BorderLayout.NORTH);
		addMouseListener(new MouseTest.MouseHandler()); 
		addMouseMotionListener(new MouseTest.MouseMotionHandler()); }
	// MouseHandler is an inner class that implements the MouseListener.
	// Each method simply prints out a message to the command line. 
	private class MouseHandler implements MouseListener { 
		public void mousePressed(MouseEvent e) 
		{ System.out.println("mouse pressed"); } 
		public void mouseClicked(MouseEvent e) 
		{ System.out.println("moused clicked"); } 
		public void mouseReleased(MouseEvent e) 
		{ System.out.println("mouse released"); } 
		public void mouseEntered(MouseEvent e) 
		{ System.out.println("mouse entered"); } 
		public void mouseExited(MouseEvent e) 
		{ System.out.println("mouse exited"); } }
	// MouseMotionHandler is an inner class that implements the MouseMotionListener. 
	// Each method simply prints out a message to the command line. 
	private class MouseMotionHandler implements MouseMotionListener{
		public void mouseMoved(MouseEvent e)
		{ System.out.println("mouse moved"); }
		public void mouseDragged(MouseEvent e) 
		{ System.out.println("mouse dragged"); } } 
	public static void main(String[] args) 
	   { 
		new MouseTest().show(); 
		} 
	}
