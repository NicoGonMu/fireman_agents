/* Initial beliefs */

at(P) :- pos(P,X,Y) & pos(fireman,X,Y).
pos(lake,0,0).

!explore(fireman).  // initial goal

/* Plans */

@h1
+!load_victim(fireman)
   : not carrying_victim(fireman)
   <- +carrying_victim(fireman);
      !download_victim(fireman).

@h2
+!download_victim(fireman, P)
   : carrying_victim(fireman)
   <- !at(lake);
   	  download_victim(P);
   	  -carrying_victim(fireman).

@h3
+!extinguish_fire(fireman, P)
   : not carrying_victim(fireman)
   <- extinguish(P).

@h4
+!explore(fireman)
   : fireman_alert(P)
   <- !at(P);
      check_parcel;
	  !proceed(fireman);
      -fireman_alert.

@h5
+!explore(fireman)
   : not fireman_alert & not carrying_victim(fireman)
   <- move_random;
      check_parcel;
	  !proceed(fireman);
	  !explore(fireman).
@p1
+!proceed(fireman)
  : not heavy_fire & not rescue_help & not light_fire
  <- true.
@p2
+!proceed(fireman)
  : heavy_fire//(Fire)
  <- !notify_plane(fireman);//(Fire);
     !explore(fireman).
@p3
+!proceed(fireman)
  : rescue_help
  <- !notify_fireman(fireman);
     !load_victim(fireman).
@p4
+!proceed(fireman)
  : light_fire
  <- extinguish(fireman).
	 
@n1
+!notify_plane(P)
   : true
   <- .send(plane, achieve, exists_fire(P)).

@n2
+!notify_fireman(fireman,P)
   : true
   <- .send(fireman2, achieve, exists_fire(P)). //DUDA DE CÓMO ENVIAR A OTRO BOMBERO

@m1
+!at(L) : at(L).
@m2
+!at(L) <- ?pos(L,X,Y);
           move_towards(fireman,X,Y);
           !at(L).

@a1
+fireman_alert(P)[source(fireman2)] : true
   <- explore(P). //DUDA DE CÓMO ENVIAR A OTRO BOMBERO

@a2
+msg(M)[source(plane)] : true 
   <- .print("Message from ",plane,": ",M); 
      -msg(M).
      