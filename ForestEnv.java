import jason.asSyntax.*;
import jason.environment.Environment;
import jason.environment.grid.Location;
import java.util.logging.Logger;

public class ForestEnv extends Environment {

  // common literals
  ///////////////////////BEER LITERALS//////////////////////
  public static final Literal of  = Literal.parseLiteral("open(fridge)");
  public static final Literal clf = Literal.parseLiteral("close(fridge)");
  public static final Literal gb  = Literal.parseLiteral("get(beer)");
  public static final Literal hb  = Literal.parseLiteral("hand_in(beer)");
  public static final Literal sb  = Literal.parseLiteral("sip(beer)");
  public static final Literal hob = Literal.parseLiteral("has(owner,beer)");

  public static final Literal af = Literal.parseLiteral("at(robot,fridge)");
  public static final Literal ao = Literal.parseLiteral("at(robot,owner)");
  /////////////////////////////////////////////////////////
  
  public static final Literal apl = Literal.parseLiteral("at(plane,lake)");
  public static final Literal afl = Literal.parseLiteral("at(fireman,lake)");
  public static final Literal lw = Literal.parseLiteral("load(water)");
  public static final Literal dw = Literal.parseLiteral("download(water)");
  public static final Literal lv = Literal.parseLiteral("load(victim)");
  public static final Literal dv = Literal.parseLiteral("download(victim)");
  public static final Literal ex = Literal.parseLiteral("extinguish(fire)");
  
  ForestModel model; // the model of the grid

  @Override
  public void init(String[] args) {
    model = new ForestModel();
    
    if (args.length == 1 && args[0].equals("gui")) { 
      ForestView view  = new ForestView(model);
      model.setView(view);
    }
    
    updatePercepts();
  }
    
  /** creates the agents percepts based on the HouseModel */
  void updatePercepts() {
    // clear the percepts of the agents
    clearPercepts("plane");
    clearPercepts("fireman");
    
    // get the plane location
    Location lPlane = model.getAgPos(0);

    // get the fireman location
    Location lFireman = model.getAgPos(1);
	
    // add agent location to its percepts
    if (lPlane.equals(model.lFridge)) {
      addPercept("plane", af);
    }
    if (lPlane.equals(model.lFireman)) {
      addPercept("plane", ao);
    }
    
    // add beer "status" to the percepts
    if (model.fridgeOpen) {
      addPercept("plane", Literal.parseLiteral("stock(beer,"+model.availableBeers+")"));
    }
    if (model.sipCount > 0) {
      addPercept("plane", hob);
      addPercept("fireman", hob);
    }
  }

  @Override
  public boolean executeAction(String ag, Structure action) {
    System.out.println("["+ag+"] doing: "+action);
    boolean result = false;
    if (action.equals(of)) { // of = open(fridge)
      result = model.openFridge();
      
    } else if (action.equals(clf)) { // clf = close(fridge)
      result = model.closeFridge();
      
    } else if (action.getFunctor().equals("move_towards")) {
      Location dest = getDestination(action.getTerm(0).toString());

      try {
	result = model.moveTowards(dest);
      } catch (Exception e) {
        e.printStackTrace();
      }
            
    } else if (action.equals(gb)) {
      result = model.getBeer();
      
    } else if (action.equals(hb)) {
      result = model.handInBeer();
      
    } else if (action.equals(sb)) {
      result = model.sipBeer();
      
    } else if (action.getFunctor().equals("deliver")) {
    // wait 4 seconds to finish "deliver"
      try { Thread.sleep(4000); } catch (Exception e) {}
      try { result = model.addBeer( (int)((NumberTerm)action.getTerm(1)).solve()); } catch (Exception e) {}
      
    } else if (action.equals(ex)) {
      Location dest = getDestination(action.getTerm(0).toString());

      try {
	    result = model.extinguish(dest);
      } catch (Exception e) {
        e.printStackTrace();
      }
      
    } else if (action.equals(lv)) {
      result = model.loadVictim();
      
    } else if (action.equals(dv)) {
      result = model.downloadVictim();
      
    } else {
      System.err.println("Failed to execute action "+action);
    }

    if (result) {
      updatePercepts();
      try { Thread.sleep(100); } catch (Exception e) {}
    }
    return result;
  }
  
  
  Location getDestination(String target) {
	  Location dest = null;
      if (target.equals("plane")) {
        dest = model.lPlane;
      } else if (target.equals("fireman")) {
        dest = model.lFireman;
      } else if (target.equals("lake")) {
        dest = model.lLake;
      }
	  return dest;
  }
}
