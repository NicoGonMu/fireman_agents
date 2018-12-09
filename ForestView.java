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
    Location lObject = fmodel.getAgPos(0);
    
    Color color = Color.lightGray;
	super.drawAgent(g, x, y, color, -1);
    
    switch (object) {
      case ForestModel.LAKE:{
    	color = Color.BLUE;
    	if(fmodel.isEmpty) color = Color.WHITE;
        //if (lObject.equals(fmodel.lLake)) {
          super.drawAgent(g, x, y, color, -1);
        //}
	  
        g.setColor(Color.black);
        drawString(g, x, y, defaultFont, "Lake");
        break;
      }
      case ForestModel.PLANE:{
    	if(fmodel.carryingWater) color = Color.cyan;
        //if (lObject.equals(fmodel.lPlane)) {
      	  super.drawAgent(g, x, y, color, -1);
        //}
    	  
        g.setColor(Color.black);
        drawString(g, x, y, defaultFont, "Plane");
        break;
      }
    }
  }

  @Override
  public void drawAgent(Graphics g, int x, int y, Color c, int id) {
    Location lFireman = fmodel.getAgPos(0);
    if (!lFireman.equals(fmodel.lPlane) && !lFireman.equals(fmodel.lLake)) {
      c = Color.yellow;
      if (fmodel.carryingVictim) c = Color.RED;
      super.drawAgent(g, x, y, c, -1);
      g.setColor(Color.black);
      super.drawString(g, x, y, defaultFont, "Fireman");
    }
  }
}