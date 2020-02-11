package ensta;

import java.io.Serializable;
import java.util.List;

public class Player {
    /*
     * ** Attributs
     */
    protected Board board;
    protected Board opponentBoard;
    protected int destroyedCount;
    protected AbstractShip[] ships;
    protected boolean lose;

    /*
     * ** Constructeur
     */
    public Player(Board board, Board opponentBoard, List<AbstractShip> ships) {
        this.board = board;
        this.ships = ships.toArray(new AbstractShip[0]);
        this.opponentBoard = opponentBoard;
    }



    /*
     * ** Méthodes
     */

    /**
     * Read keyboard input to get ships coordinates. Place ships on given
     * coodrinates.
     */
    public void putShips() {
        boolean done = false;
        int i = 0;

        do {
            AbstractShip s = ships[i];
            String msg = String.format("placer %d : %s(%d)", i + 1, s.getName(), s.getSize());
            System.out.println(msg);
            InputHelper.ShipInput res = InputHelper.readShipInput();
            switch (res.orientation) {
            case "n":
                s.setDirection(Direction.NORTH);
                break;
            case "s":
                s.setDirection(Direction.SOUTH);
                break;
            case "e":
                s.setDirection(Direction.EAST);
                break;
            case "w":
                s.setDirection(Direction.WEST);
                break;
            }
            boolean success = false;
            try {
                System.out.print(res.x + " ");
                System.out.println(res.y);
                board.putShip(s, res.y,res.x);
                success = true;
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                if (success) {
                    ++i;  
                    done = i == 4;         
                }
                
                board.printBoards();
            }
        } while (!done);
    }

    // public Hit sendHit(int[] coords) {
    // boolean done;
    // Hit hit = null;

    // do {
    // System.out.println("où frapper?");
    // InputHelper.CoordInput hitInput = InputHelper.readCoordInput();
    // // TODO call sendHit on this.opponentBoard

    // // TODO : Game expects sendHit to return BOTH hit result & hit coords.
    // // return hit is obvious. But how to return coords at the same time ?
    // } while (!done);

    // return hit;
    // }

    public AbstractShip[] getShips() {
        return ships;
    }

    public void setShips(AbstractShip[] ships) {
        this.ships = ships;
    }

}