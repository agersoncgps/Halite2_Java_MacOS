import hlt.*;

import java.util.ArrayList;

public class ShipAttack {

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

            for (Ship myShip : gameMap.getMyPlayer().getShips().values()) {
                for (Ship enemyShip : gameMap.getAllPlayers().get(1).getShips().values()) {


                    ThrustMove newThrustMove = Navigation.navigateShipTowardsTarget(gameMap, myShip, enemyShip, Constants.MAX_SPEED, true, Constants.MAX_NAVIGATION_CORRECTIONS, 0.1);
                    

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
