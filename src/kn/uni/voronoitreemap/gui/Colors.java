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
package kn.uni.voronoitreemap.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Arlind Nocaj
 *
 */
public class Colors {
	public static List<Color> getColors() {

		int alpha = 50;
		List<Color> colors = new ArrayList<Color>();
		colors.add(new Color(247, 251, 255, alpha));
		colors.add(new Color(222, 235, 247, alpha));
		colors.add(new Color(198, 219, 239, alpha));
		colors.add(new Color(158, 202, 225, alpha));
		colors.add(new Color(107, 174, 214, alpha));
		colors.add(new Color(66, 146, 198, alpha));
		colors.add(new Color(33, 113, 181, alpha));
		colors.add(new Color(8, 81, 156, alpha));
		colors.add(new Color(8, 48, 107, alpha));
		return colors;
	}

	public static List<Color> getColorsRed() {
		
		List<Color> colors = new ArrayList<Color>();
		colors.add(new Color(255, 255, 178));
		colors.add(new Color(255, 255, 178));
		colors.add(new Color(254, 204, 92));
		colors.add(new Color(240, 59, 32));
		colors.add(new Color(189, 0, 38));
		colors.add(new Color(189, 0, 38).darker());
		return colors;
	}
	
	public static List<Color> getColorsIsi() {
		int alpha = 100;
		List<Color> colors = new ArrayList<Color>();
	
		colors.add(new Color(236, 219, 191,alpha));
		colors.add(new Color(148, 62, 104,alpha));
		colors.add(new Color(80, 116, 157,alpha));
		colors.add(new Color(161, 48, 67,alpha));
		colors.add(new Color(203, 115, 123,alpha));
		colors.add(new Color(19, 129, 157,alpha));
		colors.add(new Color(38, 101, 126,alpha));
		colors.add(new Color(219, 186, 145,alpha));
		
		return colors;
	}

}
