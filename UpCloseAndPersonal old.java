import hlt.*;

import java.util.ArrayList;
import java.util.Map;

public class UpCloseAndPersonal {

    public static void main(final String[] args) {
        final Networking networking = new Networking();
        final GameMap gameMap = networking.initialize("MrGBot");

        // We now have 1 full minute to analyse the initial map.
        final String initialMapIntelligence =
                "width: " + gameMap.getWidth() +
                "; height: " + gameMap.getHeight() +
                "; players: " + gameMap.getAllPlayers().size() +
                "; planets: " + gameMap.getAllPlanets().size();
        
		Log.log("Map:");
        Log.log(initialMapIntelligence);

        final ArrayList<Move> moveList = new ArrayList<>();        

        while (true) {
            moveList.clear();
            networking.updateMap(gameMap);

             

            for (final Ship ship : gameMap.getMyPlayer().getShips().values()) {
                if (ship.getDockingStatus() != Ship.DockingStatus.Undocked) {
                    continue;
                }
                
                Log.log("Ship: " + ship);
                Planet nearestPlanet = null;
                Map<Double, Entity> entitiesByDistance = gameMap.nearbyEntitiesByDistance(ship);

                for (Map.Entry<Double, Entity> entry : entitiesByDistance.entrySet()) {
                    if (entry.getValue() instanceof Planet) {
                        Log.log("Key : " + entry.getKey() + " Value : " + entry.getValue());
                    }
                }

                for (Map.Entry<Double, Entity> entry : entitiesByDistance.entrySet()) {
                    if (entry.getValue() instanceof Planet) {
                        Planet currentPlanet = (Planet)entry.getValue();
                        if (currentPlanet.getOwner() == gameMap.getMyPlayerId()) {
                            continue;
                        }
                        Log.log("currentPlanet -- " + currentPlanet.toString());
                        nearestPlanet = currentPlanet;
                        break;
                    }
                }
                Log.log("nearestPlanet");
                Log.log(nearestPlanet.toString());
                
                if (ship.canDock(nearestPlanet)) {
                   moveList.add(new DockMove(ship, nearestPlanet));
                    break;
                }

                final ThrustMove newThrustMove = Navigation.navigateShipToDock(gameMap, ship, nearestPlanet, Constants.MAX_SPEED/2);
                if (newThrustMove != null) {
                    moveList.add(newThrustMove);
                }

                break;
            }
            Networking.sendMoves(moveList);
        }
    }
}
