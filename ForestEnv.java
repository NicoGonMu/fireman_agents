import jason.asSyntax.*;
import jason.environment.Environment;
import jason.environment.grid.Location;
import java.util.logging.Logger;

public class ForestEnv extends Environment {

  // common literals
  public static final Literal apl = Literal.parseLiteral("at(plane,fireman)");
  public static final Literal afl = Literal.parseLiteral("at(fireman,fireman)");
  public static final Literal lw = Literal.parseLiteral("load_water");
  public static final Literal dw = Literal.parseLiteral("download_water");
  public static final Literal lv = Literal.parseLiteral("load(victim)");
  public static final Literal dv = Literal.parseLiteral("download(victim)");
  public static final Literal pr = Literal.parseLiteral("proceed");
  
  // Fire state literals
  public static final Literal hf = Literal.parseLiteral("heavy_fire");
  public static final Literal rh = Literal.parseLiteral("rescue_help");
  public static final Literal lf = Literal.parseLiteral("light_fire");
  
  ForestModel model; // the model of the grid
  ForestModel.ActionType actionType;
    
  @Override
  public void init(String[] args) {
    model = new ForestModel();
    
    if (args.length == 1 && args[0].equals("gui")) { 
      ForestView view  = new ForestView(model);
      model.setView(view);
    }
    
    updatePercepts();
  }
    
  /** creates the agents percepts based on the Forest */
  void updatePercepts() {
    // clear the percepts of the agents
    clearPercepts("plane");
    clearPercepts("fireman");
    
    // get the plane location
    Location lPlane = model.getAgPos(0);

    // get the fireman location
    Location lFireman = model.getAgPos(1);
	
    // add agent location to its percepts
    /*if (lPlane.equals(model.lFridge)) {
      addPercept("plane", af);
    }*/
    
    /*if (lPlane.equals(model.lPlane)) {
      System.out.println("LPLANE");
      //addPercept("plane", apl);
    }*/

    addPercept("plane", apl);
    
    if(actionType == ForestModel.ActionType.PLANE){
      System.out.println("FUEGOOOOOOOOO");
      addPercept("fireman", hf);
    }
    else if (actionType == ForestModel.ActionType.RESCUEANDHELP) {
      addPercept("fireman",rh);
    }
    else if (actionType == ForestModel.ActionType.EXTINGUISH) {
      addPercept("fireman",lf);
    }
    
    /*if (lPlane.equals(model.lFireman)) {
      System.out.println("LFIREMAN");
      if(actionType == ForestModel.ActionType.PLANE){
        addPercept("fireman", hf);
      }
      else if (actionType == ForestModel.ActionType.RESCUEANDHELP) {
        addPercept("fireman",rh);
      }
      else if (actionType == ForestModel.ActionType.EXTINGUISH) {
        addPercept("fireman",lf);
      }
    }*/
    
    // add beer "status" to the percepts
    /*if (model.fridgeOpen) {
      addPercept("plane", Literal.parseLiteral("stock(beer,"+model.availableBeers+")"));
    }
    if (model.sipCount > 0) {
      addPercept("plane", hob);
      addPercept("fireman", hob);
    }*/
  }

  @Override
  public boolean executeAction(String ag, Structure action) {
    System.out.println("["+ag+"] doing: "+action);
    
    boolean result = false;
    
    if (action.getFunctor().equals("move_towards")) {
      int agent = -1;
      String getTermOrigin = action.getTerm(0).toString();
      String getTermDest = action.getTerm(1).toString();
      
      Location dest = getDestination(getTermDest);

      if (getTermOrigin.equals("fireman")) {
        agent = 0;
      } else if (getTermOrigin.equals("plane")) {
        agent = 1;
      }
      
      try {
	    result = model.moveTowards(dest, agent);
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else if (action.getFunctor().equals("move_random")) {
      try {
        result = model.moveRandom();
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else if (action.getFunctor().equals("check_parcel")) {      
      try {
        result = checkParcel();
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else if (action.getFunctor().equals("extinguish")) { // EXTINGUISH    	
        Location dest = getDestination(action.getTerm(0).toString());

      try {
	    result = model.extinguish(dest);
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else if (action.equals(lv)) { // LOAD VICTIM
      result = model.loadVictim();
      
    } else if (action.equals(dv)) { // DOWNLOAD VICTIM
      result = model.downloadVictim();
      
    } else if (action.equals(lw)) { // LOAD WATER
      result = model.loadWater();
      
    } else if (action.equals(dw)) { // DOWNLOAD WATER
      result = model.downloadWater();
      
    }
    else {
      System.err.println("Failed to execute action "+action);
    }

    if (result) {
      updatePercepts();
      try { Thread.sleep(500); } catch (Exception e) {}
    }
    
    return result;
  }
  
  Location getDestination(String target) {
    Location dest = null;
	  
    if (target.equals("plane")) {
      dest = model.getAgPos(1);
    } else if (target.equals("fireman")) {
      dest = model.getAgPos(0);
    } else if (target.equals("lake")) {
      dest = model.lLake;
    }
      
	return dest;
  }
  
  boolean checkParcel() {
	actionType = model.checkParcel();
		
	return true;
  }
}
