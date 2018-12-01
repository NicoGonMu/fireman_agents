import jason.environment.grid.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
  
/** class that implements the View of Domestic Robot application */
public class ForestView extends GridWorldView {

  ForestModel fmodel;
    
  public ForestView(ForestModel model) {
    super(model, "Domestic Robot", 700);
    fmodel = model;
    defaultFont = new Font("Arial", Font.BOLD, 16); // change default font
    setVisible(true);
    repaint();
  }

  /** draw application objects */
  @Override
  public void draw(Graphics g, int x, int y, int object) {
    Location lRobot = fmodel.getAgPos(0);
    super.drawAgent(g, x, y, Color.lightGray, -1);
    switch (object) {
      case ForestModel.FRIDGE: 
        if (lRobot.equals(fmodel.lFridge)) {
          super.drawAgent(g, x, y, Color.yellow, -1);
        }
        g.setColor(Color.black);
        drawString(g, x, y, defaultFont, "Fridge ("+fmodel.availableBeers+")");  
        break;
      case ForestModel.OWNER:
        if (lRobot.equals(fmodel.lOwner)) {
          super.drawAgent(g, x, y, Color.yellow, -1);
        }
        String o = "Owner";
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
    Location lRobot = fmodel.getAgPos(0);
    if (!lRobot.equals(fmodel.lOwner) && !lRobot.equals(fmodel.lFridge)) {
      c = Color.yellow;
      if (fmodel.carryingBeer) c = Color.orange;
      super.drawAgent(g, x, y, c, -1);
      g.setColor(Color.black);
      super.drawString(g, x, y, defaultFont, "Robot");
    }
  }
}
