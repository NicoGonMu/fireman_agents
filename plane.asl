/* Initial beliefs */

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
   <- !at(plane, lake);
      +carrying_water(plane);
      +consumed(YY,MM,DD,HH,NN,SS,water).
@h2
+!load_water(plane)
   : too_much(water) & limit(water,L)    
   <- .concat("Lake has no water right now.",M);
      .send(fireman,tell,msg(M)).    
@h3
+!download_water(plane, P)
   : not carrying_water(plane)
   <- !load_water(plane);
      !download_water(plane, P).
@h4
+!download_water(plane, P)
   : carrying_water(plane)
   <- !at(plane, P);
      extinguish(P);
      -carrying_water(plane).
	  
@m1
+!at(plane,P) : at(plane,P) <- true.
@m2
+!at(plane,P) : not at(plane,P)
  <- move_towards(P);
     !at(plane,P).
   
@a1
+fire_detected(M)[source(fireman)] : true
  <- !download_water(plane,fireman);
     -fire_detected.
