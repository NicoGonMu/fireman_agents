import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;

/** class that implements the Model of Domestic Robot application */
public class ForestModel extends GridWorldModel {

  // the grid size
  public static final int GSize = 10;
  
  // Problem variables
  public static final int LAKE  = 16;
  public static final int FIRE  = 32;
  
  int availableWater = 1000; // How much available water
  boolean isEmpty = false; // Wheter the lake is empty
  boolean carryingWater = true;  // Wheter the plain is carrying water
  boolean carryingVictim = false; // Wheter the fireman is carrying a victim
  
  //Location lFireman  = new Location(GSize/2,GSize/2);
  //Location lPlane    = new Location(GSize-1,GSize-1);
  Location lFire     = new Location(5,6);
  Location lLake     = new Location(0,0);
  
  // Action description
  public static enum ActionType {
	  NONE,
	  PLANE, RESCUEANDHELP, EXTINGUISH //, RESCUE	  
  }
  
  // Fire description
  public enum FireType { 
	  NONE, 
	  LIGHT, HEAVY 
  }
  
  public class Cell { 
	  FireType fireType;
	  int numVictims;
  }
  
  public Cell[][] mapDescription = new Cell[GSize][GSize];

  public ForestModel() {
    // create a DSize x GSize grid
    super(GSize, GSize, 2);

    // initial location of agents
    // ag code 0 means the fireman
    setAgPos(0, GSize/2, GSize/2);
 // ag code 1 means the plane
    setAgPos(1, GSize-1, GSize-1);
    
    // initial location of objets
    add(LAKE, lLake);
    add(FIRE, lFire);
	
	// Initialize map description
	for(int i = 0; i < GSize; i++) {
		for(int j = 0; j < GSize; j++) {
			mapDescription[i][j] = new Cell() {{ fireType = FireType.NONE; numVictims = 0; }};
		}
	}
	
	// Set fire types and number of victims per cell
	//mapDescription[4][5].fireType = FireType.HEAVY;
	mapDescription[5][6].fireType = FireType.HEAVY;
	mapDescription[2][3].fireType = FireType.LIGHT;
	mapDescription[2][3].numVictims = 2;
  }

  boolean moveTowards(Location dest, int agent) {
    Location r1 = getAgPos(agent);
    if (r1.x < dest.x)    r1.x++;
    else if (r1.x > dest.x)   r1.x--;
    if (r1.y < dest.y)    r1.y++;
    else if (r1.y > dest.y)   r1.y--;
    setAgPos(agent, r1); // move the agent in the grid
        
    // repaint the agent locations
    //view.update(lPlane.x,lPlane.y);
    //view.update(lFireman.x,lFireman.y);
    
    return true;
  }
  
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
  
  boolean loadWater() {
    carryingWater = true;
    
    return true;
  }
  
  boolean downloadWater() {
    carryingWater = false;
    
    return true;
  }
	
  ActionType checkParcel() {
	Location p = getAgPos(0);
	ActionType type = ActionType.NONE;
	
    if (mapDescription[p.x][p.y].fireType == FireType.HEAVY) {
      type = ActionType.PLANE; //ASK PLANE
    } else if ((mapDescription[p.x][p.y].fireType == FireType.LIGHT && mapDescription[p.x][p.y].numVictims > 0) || (mapDescription[p.x][p.y].numVictims > 1)) {
      type = ActionType.RESCUEANDHELP; //RESCUE AND HELP
    }
	else if (mapDescription[p.x][p.y].fireType == FireType.LIGHT){
	  type = ActionType.EXTINGUISH; // EXTINGUISH
	}
	
	return type;
  }
  
  boolean moveRandom() {
    int random = 0, xy_random, x_random = 0, y_random = 0;
    Location r1 = getAgPos(0);
    
    int x = r1.x, y = r1.y;
    
    random = (int) (Math.random()*2);
    xy_random = (int) (Math.random()*2);
    
    while(x_random == 0 && y_random == 0) {
      x_random = (int)(Math.random()*3);
      y_random = (int)(Math.random()*3);
    }
    
    if(x_random == 0) {
      x--;
      if(x < 0) {
        x = 0;
      }
    } else if(x_random == 2) {
      x++;
      if(x == GSize) {
        x = GSize-1;
      }
    }
    
    if(y_random == 0) {
      y--;
      if(y < 0) {
        y = 0;
      }
    } else if(y_random == 2) {
      y++;
      if(y == GSize)
        y = GSize-1;
    }
    
    if((x == r1.x && y == r1.y)){
      if(xy_random == 0){ // MODIFY X
        if(random == 0) { // LEFT
          x--;
          if(x < 0)
          {
            x = x + 2;
          }
        }
        else { //RIGHT
          x++;
          if(x == GSize)
          {
            x = x - 2;
          }
        }
      }
      else { // MODIFICA Y
        if(random == 0) { // BELOW
          y--;
          if(y < 0) {
            y = y + 2;
          }
        }
        else { // ABOVE
          y++;
          if(y == GSize) {
            y = y - 2;
          }
        }
      }
    }
    
    if(x == 0 && y == 0)
    {
      if(x != r1.x && y != r1.y) {
        r1.x = x;
      }
      else if (x != r1.x) {
        r1.x = r1.x + 1;
      }
      else {
        r1.y = r1.y + 1;
      }
    }
    else if (x == GSize-1 && y == GSize-1){
      if(x != r1.x && y != r1.y) {
        r1.x = x;
      }
      else if (x != r1.x) {
        r1.x = r1.x -1;
      }
      else {
        r1.y = r1.y -1;
      }
    } else {
      r1.x = x;
      r1.y = y;
    }
    
    ////////////////77
    //r1.x = 5;
    //r1.y = 6;
    ///////////

    //lFireman.x = r1.x;
    //lFireman.y = r1.y;
    setAgPos(0, r1); // move the agent in the grid
      
    return true;
  }
}

//        Location fireman = getAgPos(1);
//        movementDone = false;
//        while !movementDone {
//          // Up, right, down, left
//       r = random(1,4);
//          switch(r) {
//            case 1:
//                  if (fireman.x > 0) {
//                    fireman.x--;
//                    movementDone = true;
//                    break;
//            case 2:
//                  if (fireman.y < GSize) {
//                    fireman.y++;
//                    movementDone = true;
//                    break;
//            case 3:
//                  if (fireman.x < GSize) {
//                    fireman.x++;
//                    movementDone = true;
//                    break;
//            case 4:
//                  if (fireman.y > 0) {
//                    fireman.y--;
//                    movementDone = true;
//                    break;
//          }
//        }
//        setAgPos(0, r1); // move the agent in the grid
//        
//     // repaint fireman locations
//     view.update(lFireman.x,lFireman.y);
//        return true;

