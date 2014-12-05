package com.advoa.sparkplugin;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image image;
	ClassLoader cl = OAAlertPlugin.class.getClassLoader();
    
    public ImagePanel() {
        image = new ImageIcon(cl.getResource("images/alert.jpg")).getImage();
    }
    
    public ImagePanel(BorderLayout borderLayout) {
    	this.setLayout(borderLayout);
    	image = new ImageIcon(cl.getResource("images/alert.jpg")).getImage();
	}


	@Override
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }
}
