/*
Rather than having all your ships travel to the same single planet, you could diversify and
send ships to different planets. Without diversifying, if the single planet you’re attempting to
conquer has already been conquered by an enemy in the midst of your voyage, you will have wasted
many turns. But with this strategy, you have a better chance of landing on a vacant planet.
You can get all the planets, get all the ships, and then assign them to each other in the map’s
default order on a round robin basis.
*/

import hlt.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class DivideAndConquer {

    public static void main(String[] args) {
        Networking networking = new Networking();
        GameMap gameMap = networking.initialize("MrGBot");

        // We now have 1 full minute to analyse the initial map.
        String initialMapIntelligence =
                "width: " + gameMap.getWidth() +
                "; height: " + gameMap.getHeight() +
                "; players: " + gameMap.getAllPlayers().size() +
                "; planets: " + gameMap.getAllPlanets().size();
        
		Log.log("Map:");
        Log.log(initialMapIntelligence);

        ArrayList<Move> moveList = new ArrayList<>();        

        
        while (true) {
            moveList.clear();
            networking.updateMap(gameMap);

            Collection shipCollection = gameMap.getMyPlayer().getShips().values();
            ArrayList<Ship> ships = new ArrayList<Ship>(shipCollection);
            
            
            Log.log("Ships Size: " + ships.size());
            Log.log("++++++++++++++++++++++++++++");
            for (int current = 0; current < ships.size(); current++) {

                //Position cloestPoint = ships.get(new Integer(current)).getClosestPoint(planets.get(new Integer(current%planets.size())));
                 

                Ship ship = ships.get(new Integer(current));
                Log.log(ship.toString());
                Map<Double, Entity> entitiesByDistance = gameMap.nearbyEntitiesByDistance(ship);

                ArrayList<Planet> planets = new ArrayList<Planet>();
                for (Map.Entry<Double, Entity> entry : entitiesByDistance.entrySet()) {
                    //Log.log("Key : " + entry.getKey() + " Value : " + entry.getValue());
                    if (entry.getValue() instanceof Planet) {
                        Planet planet = (Planet)entry.getValue();
                        if (planet.isOwned() && planet.getOwner() != gameMap.getMyPlayerId()) {
                            continue;
                        }
                        if (planet.isFull()) {
                            continue;
                        }
                        planets.add(planet);
                    }
                }


                Planet planet = planets.get(new Integer(current%planets.size()));
                Log.log(planet.toString());
                 
                if (ship.canDock(planet)) {
                   moveList.add(new DockMove(ship, planet));
                   continue;
                }

                ThrustMove newThrustMove = Navigation.navigateShipToDock(gameMap, ship, planet, Constants.MAX_SPEED/4);
                if (newThrustMove != null) {
                    moveList.add(newThrustMove);
                }
                continue;
               
            }

            Networking.sendMoves(moveList);
        }
        
    }
}
