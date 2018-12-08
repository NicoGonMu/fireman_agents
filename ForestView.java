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
    Location lPlane = fmodel.getAgPos(0);
    super.drawAgent(g, x, y, Color.lightGray, -1);
    switch (object) {
      case ForestModel.FRIDGE:
        if (lPlane.equals(fmodel.lFridge)) {
          super.drawAgent(g, x, y, Color.yellow, -1);
        }
        g.setColor(Color.black);
        drawString(g, x, y, defaultFont, "Fridge ("+fmodel.availableBeers+")");  
        break;
      case ForestModel.FIREMAN:
        if (lPlane.equals(fmodel.lFireman)) {
          super.drawAgent(g, x, y, Color.yellow, -1);
        }
        String o = "Fireman";
        if (fmodel.sipCount > 0) {
          o +=  " ("+fmodel.sipCount+")";
        }
        g.setColor(Color.black);
        drawString(g, x, y, defaultFont, o);  
        break;
    }
  }

  @Override
  public void drawAgent(Graphics g, int x, int y, Color c, int id) {
    Location lPlane = fmodel.getAgPos(0);
    if (!lPlane.equals(fmodel.lFireman) && !lPlane.equals(fmodel.lFridge)) {
      c = Color.yellow;
      if (fmodel.carryingBeer) c = Color.orange;
      super.drawAgent(g, x, y, c, -1);
      g.setColor(Color.black);
      super.drawString(g, x, y, defaultFont, "Plane");
    }
  }
}
