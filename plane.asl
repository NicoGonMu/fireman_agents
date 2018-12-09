/* Initial beliefs */

// initially, I believe that there is some beer in the fridge
//available(beer,fridge).
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

//@h1
//+!has(owner,beer)
//   :  available(beer,fridge) & not too_much(beer)
//   <- !at(robot,fridge);
//      open(fridge);
//      get(beer);
//      close(fridge);
//      !at(robot,owner);
//      hand_in(beer);
      // remember that another beer has been consumed
//      .date(YY,MM,DD); .time(HH,NN,SS);
//      +consumed(YY,MM,DD,HH,NN,SS,beer).

//@h2
//+!has(owner,beer)
//   :  not available(beer,fridge)
//   <- .send(supermarket, achieve, order(beer,5));
//      !at(robot,fridge). // go to fridge and wait there.

//@h3
//+!has(owner,beer)
//   :  too_much(beer) & limit(beer,L)    
//   <- .concat("The Department of Health does not allow me ",
//              "to give you more than ", L,
//              " beers a day! I am very sorry about that!",M);
//      .send(owner,tell,msg(M)).    

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

// when the supermarket makes a delivery, try the 'has' 
// goal again   
//@a1
//+delivered(beer,Qtd,OrderId)[source(supermarket)] : true
//  <- +available(beer,fridge);
//     !has(owner,beer). 

// when the fridge is opened, the beer stock is perceived
// and thus the available belief is updated
//@a2
//+stock(beer,0) 
//   :  available(beer,fridge)
//   <- -available(beer,fridge).

//@a3
//+stock(beer,N) 
//   :  N > 0 & not available(beer,fridge)
//   <- +available(beer,fridge).
   
@a1
+fire_detected(M)[source(fireman)] : true
  <- !download_water(plane,fireman);
     -fire_detected.
