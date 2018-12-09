import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;

/** class that implements the Model of Domestic Robot application */
public class ForestModel extends GridWorldModel {

  // the grid size
  public static final int GSize = 10;
  
  ///////////////BEER VARS/////////////////////////
  // constants for the grid objects
  //public static final int FRIDGE = 16;
  //public static final int OWNER  = 32;
  
  /*boolean fridgeOpen   = false; // whether the fridge is open
  boolean carryingBeer = false; // whether the robot is carrying beer*/
  //int sipCount     = 0; // how many sip the owner did
  //int availableBeers = 2; // how many beers are available
  
  //Location lFridge = new Location(0,0);
  //Location lOwner  = new Location(GSize-1,GSize-1);
  ///////////////////////////////////////////////
  
  // Problem variables
  //public static final int FRIDGE = 16;
  public static final int FIREMAN  = 32;
  
  int availableWater = 1000; // How much available water
  boolean carryingWater = false;  // Wheter the plain is carrying water
  boolean carryingVictim = false; // Wheter the fireman is carrying a victim
  
  Location lFireman  = new Location(GSize-1,GSize-1);
  Location lPlane    = new Location(GSize-1,GSize-1);
  Location lLake     = new Location(0,0);
  
  // Action description
  public static enum ActionType {
	  NONE,
	  PLANE, RESCUEANDHELP, EXTINGUISH//, RESCUE	  
  }
  
  // Fire description
  public enum FireType { 
	  NONE, LIGHT, HEAVY 
  }
  
  public class Cell { 
	  FireType fireType;
	  int numVictims;
  }
  
  public Cell[][] mapDescription = new Cell[GSize][GSize];

  public ForestModel() {
    // create a DSize x GSize grid with one mobile agent
    super(GSize, GSize, 1);

    // initial location of plane (column 3, line 3)
    // ag code 0 means the plane
    setAgPos(0, GSize/2, GSize/2);
    
    // initial location of fridge and owner
    //add(FRIDGE, lFridge);
    add(FIREMAN, lFireman);
	
	// Initialize map description
	for(int i = 0; i < GSize; i++) {
		for(int j = 0; j < GSize; j++) {
			mapDescription[i][j] = new Cell() {{ fireType = FireType.NONE; numVictims = 0; }};
		}
	}
	
	// Set fire types and number of victims per cell
	mapDescription[2][3].fireType = FireType.HEAVY;
	mapDescription[2][3].numVictims = 2;
  }

  /*boolean openFridge() {
    if (!fridgeOpen) {
      fridgeOpen = true;
      return true;
    } else {
      return false;
    }
  }

  boolean closeFridge() {
    if (fridgeOpen) {
      fridgeOpen = false; 
      return true;
    } else {
      return false;
    }        
  }  */

  boolean moveTowards(Location dest) {
    Location r1 = getAgPos(0);
    if (r1.x < dest.x)    r1.x++;
    else if (r1.x > dest.x)   r1.x--;
    if (r1.y < dest.y)    r1.y++;
    else if (r1.y > dest.y)   r1.y--;
    setAgPos(0, r1); // move the robot in the grid
        
    // repaint the fridge and owner locations
    //view.update(lFridge.x,lFridge.y);
    view.update(lFireman.x,lFireman.y);
    return true;
  }
  
  boolean go(Location dest) {
    Location r1 = getAgPos(0);
    if (r1.x < dest.x)    r1.x++;
    else if (r1.x > dest.x)   r1.x--;
    if (r1.y < dest.y)    r1.y++;
    else if (r1.y > dest.y)   r1.y--;
    setAgPos(0, r1); // move the robot in the grid
        
    // repaint the fridge and owner locations
    //view.update(lFridge.x,lFridge.y);
    view.update(lFireman.x,lFireman.y);
    return true;
  }
  
/*  boolean loadWater() {
	if (availableWater > 99 && !carryingWater) {
      availableWater -= 100;
      carryingWater = true;
      view.update(lFridge.x,lFridge.y);
      return true;
    } else {
      return false;
    }
  }
  boolean downloadWater() {
    if (carryingWater) {
      carryingWater = false;
	  Location r1 = getAgPos(0);
	  mapDescription[r1.x][r1.y].fireType = FireType.NONE;
      view.update(lFireman.x,lOwner.y);
      return true;
    } else {
      return false;
    }
  }*/
  
  boolean extinguish(Location r) {
	  mapDescription[r.x][r.y].fireType = FireType.NONE;
	  return true;
  }
  
  boolean loadVictim() {
    if (!carryingVictim) {
	  Location r1 = getAgPos(0);
	  mapDescription[r1.x][r1.y].numVictims -= 1;
	  carryingVictim = true;
	  return true;
	} else {
	  return false;
	}
  }
  
  boolean downloadVictim() {
    Location r1 = getAgPos(0);
    
    if (carryingVictim && mapDescription[r1.x][r1.y].fireType == FireType.NONE) {
	  mapDescription[r1.x][r1.y].numVictims -= 1;
	  carryingVictim = false;
	  return true;
	} else {
	  return false;
	}
  }
	
  ActionType checkParcel(Location p) {
	ActionType type = ActionType.NONE;
	
    if (mapDescription[p.x][p.y].fireType == FireType.HEAVY) {
      type = ActionType.PLANE; //ASK PLANE
    } else if ((mapDescription[p.x][p.y].fireType == FireType.LIGHT && mapDescription[p.x][p.y].numVictims > 0) || (mapDescription[p.x][p.y].numVictims > 1)) {
    	type = ActionType.RESCUEANDHELP; //RESCUE AND HELP
      }
	else if (mapDescription[p.x][p.y].fireType == FireType.LIGHT){
	  type = ActionType.EXTINGUISH; // EXTINGUISH
	}
	/*else if (mapDescription[p.x][p.y].numVictims > 0) {
      type = ActionType.RESCUE; // RESCUE
    }*/
	
	return type;
  }
  
  boolean moveRandom(Location p) {
    
	  
	  
	  
	  
    return true;
  }
  /*boolean extinguish() {
    Location r1 = getAgPos(0);
    if (mapDescription[r1.x][r1.y].fireType == FireType.LIGHT) {
	  mapDescription[r1.x][r1.y].fireType = FireType.NONE;
	  return true;
	} else {
	  return false;
	}
  }*/
}