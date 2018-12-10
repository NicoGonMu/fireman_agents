import jason.environment.grid.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
  
/** class that implements the View of Domestic Robot application */
public class ForestView extends GridWorldView {

  ForestModel fmodel;
    
  public ForestView(ForestModel model) {
    super(model, "Fireman", 700);
    fmodel = model;
    defaultFont = new Font("Arial", Font.BOLD, 16); // change default font
    setVisible(true);
    repaint();
  }

  /** draw application objects */
  @Override
  public void draw(Graphics g, int x, int y, int object) {
	Color color = Color.lightGray;
	  
	switch (object) {
	  case ForestModel.LAKE:
	    color = Color.BLUE;
		if(fmodel.isEmpty) color = Color.WHITE;
		super.drawAgent(g, x, y, color, -1);
	    g.setColor(Color.black);
	    drawString(g, x, y, defaultFont, "Lake");
		break;
	  case ForestModel.FIRE:
		color = Color.RED;
	    //if(fmodel.isEmpty) color = Color.WHITE;
	    super.drawAgent(g, x, y, color, -1);
		g.setColor(Color.black);
		drawString(g, x, y, defaultFont, "Fire");
		break;
	}
  }

  @Override
  public void drawAgent(Graphics g, int x, int y, Color c, int id) {
    //Location lFireman = fmodel.getAgPos(0);
    //Location lPlane = fmodel.getAgPos(1);
        
    /*if (!lFireman.equals(fmodel.lPlane) && !lFireman.equals(fmodel.lLake)) {
      c = Color.yellow;
      if (fmodel.carryingVictim) c = Color.RED;
      super.drawAgent(g, lFireman.x, lFireman.y, c, -1);
      g.setColor(Color.black);
      super.drawString(g, lFireman.x, lFireman.y, defaultFont, "Fireman");
    }*/
    
    /*if (!lPlane.equals(fmodel.lFireman) && !lPlane.equals(fmodel.lLake)) {
      c = Color.lightGray;
      if (fmodel.carryingWater) c = Color.cyan;
      super.drawAgent(g, lPlane.x, lPlane.y, c, -1);
      g.setColor(Color.black);
      super.drawString(g, lPlane.x, lPlane.y, defaultFont, "Plane");
    }*/
    
  if (id == 0) {
    c = Color.yellow;
    if (fmodel.carryingVictim) c = Color.ORANGE;
    super.drawAgent(g, x, y, c, -1);
    g.setColor(Color.black);
    super.drawString(g, x, y, defaultFont, "Fireman");
  }
  
  if (id == 1) {
    c = Color.lightGray;
    if (fmodel.carryingWater) c = Color.cyan;
    super.drawAgent(g, x, y, c, -1);
    g.setColor(Color.black);
    super.drawString(g, x, y, defaultFont, "Plane");
  }
  }
}