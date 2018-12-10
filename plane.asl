/* Initial beliefs */

at(P) :- pos(P,X,Y) & pos(plane,X,Y).
pos(lake,0,0).

// initially, I believe that there is some beer in the fridge
carrying_water(plane).

// Lake only has 1000 liters a day (10 loads of 100L)
limit(water, 10).

/* Rules */ 

too_much(B) :- 
   .date(YY,MM,DD) &
   .count(consumed(YY,MM,DD,_,_,_,B),QtdB) &
   limit(B,Limit) &
   QtdB > Limit.

/* Plans */

@h1
+!load_water(plane)
   : not carrying_water(plane)
   <- !at(lake);
      load_water;
      +carrying_water(plane);
      +consumed(YY,MM,DD,HH,NN,SS,water).
      
@h2
+!load_water(plane)
   : too_much(water) & limit(water,L)    
   <- .concat("Lake has no water right now.",M);
      .send(fireman,tell,msg(M)).    
@h3
+!download_water(plane,P)
   : not carrying_water(plane)
   <- !load_water(plane);
      !download_water(plane,P).
@h4
+!download_water(plane,P)
   : carrying_water(plane)
   <- !at(P);
      extinguish(P);
      download_water;
      -carrying_water(plane);
      !load_water(plane).
	  
@m1
+!at(L) : at(L).
@m2
+!at(L) <- ?pos(L,X,Y);
           move_towards(plane,X,Y);
           !at(L).
   
@a1
+!exists_fire(P)[source(Ag)] : true
  <- !download_water(plane,P).
