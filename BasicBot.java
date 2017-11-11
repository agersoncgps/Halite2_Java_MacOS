import hlt.*;

import java.util.ArrayList;

public class BasicBot {

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

        Log.log("\nPlanets:");
        for (Planet planet : gameMap.getAllPlanets().values()) {
        	Log.log(planet.toString());
        }

        Log.log("\nMy Ships:");
        for (Ship ship : gameMap.getMyPlayer().getShips().values()) {
        	Log.log(ship.toString());
        }

        Log.log("\nEnemy Ships:");
        for (Ship ship : gameMap.getAllPlayers().get(1).getShips().values()) {
        	Log.log(ship.toString());
        }

        
        

        while (true) {
            moveList.clear();
            networking.updateMap(gameMap);

            for (final Ship ship : gameMap.getMyPlayer().getShips().values()) {
                if (ship.getDockingStatus() == Ship.DockingStatus.Docked) {
                    continue;
                }

                for (final Planet planet : gameMap.getAllPlanets().values()) {
                    if (planet.isOwned()) {
                        continue;
                    }

                    if (ship.canDock(planet)) {
                       moveList.add(new DockMove(ship, planet));
                        break;
                    }

                    final ThrustMove newThrustMove = Navigation.navigateShipToDock(gameMap, ship, planet, Constants.MAX_SPEED/2);
                    if (newThrustMove != null) {
                        moveList.add(newThrustMove);
                    }

                    break;
                }
            }
            Networking.sendMoves(moveList);
        }
    }
}
