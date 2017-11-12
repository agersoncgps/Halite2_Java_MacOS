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
            
            Collection planetCollection = gameMap.getAllPlanets().values();
            ArrayList<Planet> planets = new ArrayList<Planet>(planetCollection);

            Log.log(ships.get(new Integer(0)).getClosestPoint(planets.get(new Integer(0))));
            
            /*
            for (Ship ship : gameMap.getMyPlayer().getShips().values()) {
                if (ship.getDockingStatus() == Ship.DockingStatus.Docked) {
                    continue;
                }
                Map<Double, Entity> entitiesByDistance = gameMap.nearbyEntitiesByDistance(ship);

                for (Map.Entry<Double, Entity> entry : entitiesByDistance.entrySet()) {
                    Log.log("Key : " + entry.getKey() + " Value : " + entry.getValue());
                    if (entry.getValue() instanceof Planet) {
                        Planet planet = (Planet)entry.getValue();
                        if (planet.isOwned()) {
                        //if (planet.getOwner() == gameMap.getMyPlayerId()) {
                            continue;
                        }

                        if (ship.canDock(planet)) {
                           moveList.add(new DockMove(ship, planet));
                            break;
                        }

                        ThrustMove newThrustMove = Navigation.navigateShipToDock(gameMap, ship, planet, Constants.MAX_SPEED/2);
                        if (newThrustMove != null) {
                            moveList.add(newThrustMove);
                        }

                        break;
                    }
                }
            }
            */

            Networking.sendMoves(moveList);
        }
    }
}
