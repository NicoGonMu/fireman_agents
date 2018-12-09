!explore(fireman).  // initial goal

/* Plans */ 

/* @g 
+!get(beer) : true 
   <- .send(robot, achieve, has(owner,beer)).

@h1 
+has(owner,beer) : true 
   <- !drink(beer).
@h2 
-has(owner,beer) : true 
   <- !get(beer).

// while I have beer, sip   
@d1 
+!drink(beer) : has(owner,beer)
   <- sip(beer);
      !drink(beer).
@d2 
+!drink(beer) : not has(owner,beer)
   <- true.*/

@h1
+!load_victim(fireman)
   : not carrying_victim(fireman)
   <- +carrying_victim(fireman);
      !download_victim(fireman).

+!go_to_base(fireman)
   : carrying_victim(fireman)
   <- move_towards(base).

@h2
+!download_victim(fireman, P)
   : carrying_victim(fireman)
   <- !go_to_base(P);
   	  download_victim(P);
   	  -carrying_victim(fireman).

@h3
+!extinguish_fire(fireman, P)
   : not carrying_victim(fireman)
   <- extinguish(P).

@h4
+!explore(fireman, P)
   : fireman_alert(P)
   <- !at(fireman,P);
      CHECK
      -fireman_alert(P).

@h5
+!explore(fireman, P)
   : not fireman_alert(P) & not carrying_victim(fireman)
   <- move_random(P).

@n1
+notify_plane(fireman, P)
   : true
   <- .send(plane, achieve, exists_fire(P)).
   
@n2
+notify_fireman(fireman, P)
   : true
   <- .send(fireman2, achieve, exists_fire(P)). //DUDA DE CÓMO ENVIAR A OTRO BOMBERO

@m1
+!at(fireman,P) : at(fireman,P) <- true. //COMPROBAR SI ES VALIDO EL NOMBRE DE AT
@m2
+!at(fireman,P) : not at(fireman,P)
  <- move_towards(P);
     !at(fireman,P).

@a1
+fireman_alert(P)[source(fireman)] : true
   <- explore(fireman2,P). //DUDA DE CÓMO ENVIAR A OTRO BOMBERO

@a2
+msg(M)[source(plane)] : true 
   <- .print("Message from ",plane,": ",M); 
      -msg(M).
      