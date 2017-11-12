import hlt.*;

import java.util.ArrayList;
import java.util.Map;

public class UpCloseAndPersonal {

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
            Networking.sendMoves(moveList);
        }
    }
}
