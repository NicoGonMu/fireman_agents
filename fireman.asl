!explore(fireman, x).  // initial goal

/* Plans */

@h1
+!load_victim(fireman)
   : not carrying_victim(fireman)
   <- +carrying_victim(fireman);
      !download_victim(fireman).

@h2
+!download_victim(fireman, P)
   : carrying_victim(fireman)
   <- !at(fireman, lake);
   	  download_victim(P);
   	  -carrying_victim(fireman).

@h3
+!extinguish_fire(fireman, P)
   : not carrying_victim(fireman)
   <- extinguish(P).

@h4
+!explore(fireman, P)
   : fireman_alert
   <- !at(fireman,P);
      check_parcel;
	  !proceed(fireman);
      -fireman_alert.

@h5
+!explore(fireman, P)
   : not fireman_alert & not carrying_victim(fireman)
   <- move_random;
      check_parcel; // fireman,
	  !proceed(fireman); // fireman,
	  !explore(fireman, P).
@p1
+!proceed(fireman)
  : not heavy_fire & not rescue_help & not light_fire
  <- true.
@p2
+!proceed(fireman)
  : heavy_fire
  <- !notify_plane(fireman).
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
+!notify_plane(fireman)
   : true
   <- .send(plane, achieve, exists_fire).
@n2
+!notify_fireman(fireman, P)
   : true
   <- .send(fireman2, achieve, exists_fire(P)). //DUDA DE CÓMO ENVIAR A OTRO BOMBERO

@m1
+!at(fireman,P) : at(fireman,P) <- true. //COMPROBAR SI ES VALIDO EL NOMBRE DE AT
@m2
+!at(fireman,P) : not at(fireman,P)
  <- move_towards(P);
     !at(fireman,P).

@a1
+fireman_alert[source(fireman2)] : true
   <- explore(fireman,fireman2). //DUDA DE CÓMO ENVIAR A OTRO BOMBERO

@a2
+msg(M)[source(plane)] : true 
   <- .print("Message from ",plane,": ",M); 
      -msg(M).
      