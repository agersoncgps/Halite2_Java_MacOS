import hlt.*;

import java.util.ArrayList;

public class DoNothing {

    public static void main(final String[] args) {
        final Networking networking = new Networking();
        final GameMap gameMap = networking.initialize("DoNothing");
        final ArrayList<Move> moveList = new ArrayList<>();
   
        while (true) {
            moveList.clear();
            networking.updateMap(gameMap);
			Networking.sendMoves(moveList);
        }
           
    }
}

