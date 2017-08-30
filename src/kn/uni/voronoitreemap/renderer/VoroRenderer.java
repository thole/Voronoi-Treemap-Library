/*******************************************************************************
 * Copyright (c) 2013 Arlind Nocaj, University of Konstanz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * For distributors of proprietary software, other licensing is possible on request: arlind.nocaj@gmail.com
 * 
 * This work is based on the publication below, please cite on usage, e.g.,  when publishing an article.
 * Arlind Nocaj, Ulrik Brandes, "Computing Voronoi Treemaps: Faster, Simpler, and Resolution-independent", Computer Graphics Forum, vol. 31, no. 3, June 2012, pp. 855-864
 ******************************************************************************/
package kn.uni.voronoitreemap.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JLayeredPane;

import kn.uni.voronoitreemap.gui.Colors;
import kn.uni.voronoitreemap.helper.InterpolColor;
import kn.uni.voronoitreemap.j2d.Point2D;
import kn.uni.voronoitreemap.j2d.PolygonSimple;
import kn.uni.voronoitreemap.treemap.VoroNode;
import kn.uni.voronoitreemap.treemap.VoronoiTreemap;

/**
 * Renderer which should draw the polygons of a Voronoi Treemap into a
 * Graphics2D. Mainly used for debugging and testing.
 * 
 * @author Arlind Nocaj
 * 
 */

public class VoroRenderer {
	private boolean renderText = true;

	boolean drawNames = true;
	Graphics2D g;

	protected VoronoiTreemap treemap;

	private JLayeredPane layeredPane;

	private BufferedImage bufferImage;

	public VoroRenderer() {
		init();
	}

	private void init() {
		layeredPane = new JLayeredPane();
	}

	public void setTreemap(VoronoiTreemap treemap) {
		this.treemap = treemap;
	}

	public VoronoiTreemap getTreemap() {
		return treemap;
	}

	public void setGraphics2D(Graphics2D graphics) {
		this.g = graphics;
	}

	public void renderTreemap(String filename) {
		PolygonSimple rootPolygon = treemap.getRootPolygon();
		Rectangle rootRect = rootPolygon.getBounds();

		if (g == null) {
			int border = 5;
			bufferImage = new BufferedImage(rootRect.width + border, rootRect.height + border,
					BufferedImage.TYPE_INT_ARGB);
			g = bufferImage.createGraphics();
			g.translate(border, border);
		}
		double translateX = -rootRect.getMinX();
		double translateY = -rootRect.getMinY();
		g.translate(translateX, translateY);

		int maxHeight = 0;
		LinkedList<VoroNode> nodeList = new LinkedList<VoroNode>();
		LinkedList<VoroNode> nodeListReverse = new LinkedList<VoroNode>();
		for (VoroNode child : (VoronoiTreemap) treemap) {
			// JPolygon2 jp = new JPolygon2(child.getNodeID(), new
			// Integer(child.getNodeID()).toString());
			// layeredPane.add(jp, -child.getHeight());
			//
			// jp.setVisible(true);
			if (child.getPolygon() != null) {
				nodeList.add(child);
				nodeListReverse.addFirst(child);
				if (child.getHeight() > maxHeight) {
					maxHeight = child.getHeight();
				}
			}
		}

		// System.out.println("Elements:" + nodeList.size());

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		//
		int hue = 342;
		// hue=180;
		InterpolColor colRed = new InterpolColor(0, maxHeight + 1, hue / 360.0, 0.0, 1.0, hue / 360.0, 0.6, 0.40);
		// draw polygon border
		InterpolColor grayGetDarker = new InterpolColor(0, maxHeight, 0, 0, 0.5, 0, 0, 1.0);

		// InterpolColor grayGetBrighter = new InterpolColor(1, maxHeight, 0, 0,
		// 1.0, 0,
		// 0, 0.9); //white border
		InterpolColor grayGetBrighter = new InterpolColor(1, maxHeight, 0, 0, 0.4, 0, 0, 0.6);

		g.setColor(Color.black);
		g.setColor(colRed.getColorLinear(2));
		g.fill(rootPolygon);
		layeredPane.setSize(5000, 5000);
		layeredPane.setVisible(true);
		layeredPane.paintAll(g);

		int showLayouer = 100;
		// fill polygon
		for (VoroNode child : nodeList) {
			PolygonSimple poly = child.getPolygon();

			int height = child.getHeight();
			if (height > showLayouer)
				continue;
			int level = Math.min(child.getHeight(), Colors.getColors().size() - 1);
			Color fillColor = Colors.getColors().get(level);
			fillColor = colRed.getColorLinear(height, 50);

			g.setColor(fillColor);
			g.fillPolygon(poly.getXpointsClosed(), poly.getYpointsClosed(), poly.length + 1);

			g.setColor(Color.DARK_GRAY);
			Color textCol = grayGetDarker.getColorLinear(height, 180);
			textCol = new Color(255, 255, 255, 200);
			g.setColor(textCol);
			drawName(child, g);

		}

		// draw border in reverse order
		for (VoroNode child : nodeListReverse) {
			if (child.getHeight() > showLayouer)
				continue;
			PolygonSimple poly = child.getPolygon();
			// Color col = grayScale.getColorLinear(child.getHeight());
			Color col = grayGetBrighter.getColorLinear(child.getHeight(), 170);
			double width = 5 * (1.0 / (child.getHeight()));
			g.setStroke(new BasicStroke((int) width));
			g.setColor(col);

			g.drawPolygon(poly.getXpointsClosed(), poly.getYpointsClosed(), poly.length + 1);
		}

		if (filename != null) {
			try {
				ImageIO.write(bufferImage, "png", new File(filename + ".png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void drawName(VoroNode child, Graphics2D g) {
		if (!renderText)
			return;

		if (child.getHeight() > 3)
			return;
		// if(child.getHeight()>2) continue;
		//// if(child.getHeight()==3 && rand.nextDouble()<0.50) continue;

		double percent = child.getPolygon().getArea() / treemap.getRootPolygon().getArea();

		if (percent < 0.015)
			return;

		if (child.getParent().getChildren().size() == 1)
			return;

		// draw name
		PolygonSimple poly = child.getPolygon();
		if (poly == null)
			return;

		Point2D center = poly.getCentroid();
		String name = child.getName();
		int maxChar = 11;
		if (name.length() > maxChar)
			name = name.substring(0, maxChar) + "..";

		Font res = scaleFont(name, poly, g, g.getFont());
		if (res == null)
			return;
		g.setFont(res);
		FontMetrics fm = g.getFontMetrics(res);
		Rectangle2D bounds = fm.getStringBounds(name, g);
		double posX = (center.x - bounds.getWidth() / 2.0);
		double posY = (center.y + bounds.getHeight() / 4.0);
		g.drawString(name, (int) posX, (int) ((int) posY));

	}

	public Font scaleFont(String text, Rectangle rect, Graphics2D g, Font pFont) {
		float nextTry = 100.0f;
		Font font = pFont;

		while (true) {
			font = g.getFont().deriveFont(nextTry);
			FontMetrics fm = g.getFontMetrics(font);
			int width = fm.stringWidth(text);
			if (width <= rect.width)
				return font;
			nextTry *= .9;
		}
		// return font;
	}

	public Font scaleFont(String text, PolygonSimple poly, Graphics2D g, Font pFont) {
		float nextTry = 200.0f;
		Font font = pFont;
		Point2D center = poly.getCentroid();
		int count = 0;
		while (true && count++ < 100) {
			font = g.getFont().deriveFont(nextTry);
			FontMetrics fm = g.getFontMetrics(font);
			Rectangle2D bounds = fm.getStringBounds(text, g);
			// int width=fm.stringWidth(text);
			double cx = center.x - bounds.getWidth() * 0.5;
			double cy = center.y - bounds.getHeight() * 0.5;
			Rectangle2D.Double rect = new Rectangle2D.Double(cx, cy, bounds.getWidth(), bounds.getHeight());
			if (poly.contains(rect))
				// if(width <= rect.width)
				return font;

			nextTry *= .9;
		}
		return font;
	}

	public boolean isRenderText() {
		return renderText;
	}

	public void setRenderText(boolean renderText) {
		this.renderText = renderText;
	}

}
