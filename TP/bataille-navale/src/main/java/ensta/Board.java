package ensta;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.Serializable;

public class Board implements IBoard, java.io.Serializable {
    private Character[] charArray = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
    protected String name;
    protected ShipState[][] boats_array;
    protected Boolean[][] hits_array;
    protected int size;

    private static final long serialVersionUID = 1L;

    public Board(String name, int size) { // ( Check size < 26 later)
        this.name = name;
        this.boats_array = new ShipState[size][size];
        for (int i = 0; i < boats_array.length; i++)
            for (int j = 0; j < boats_array[0].length; j++)
                boats_array[i][j] = new ShipState();
        this.hits_array = new Boolean[size][size];
        this.size = size;
    }

    public Board(String name) {
        this(name, 10);
    }

    public int getGridSize() {
        return this.size;
    }

    private void printFirstLine() {
        System.out.print("   ");
        for (int i = 0; i < size; i++) {
            System.out.print(charArray[i].toString() + " ");
        }
        System.out.println("");
    }

    public void print() {
        System.out.print("Navires :");
        for (int i = 1; i < size - 1; i++) {
            System.out.print("  ");
        }
        System.out.println(" Frappes :");
        System.out.print("   ");
        for (int i = 0; i < size; i++) {
            System.out.print(charArray[i].toString() + " ");
        }
        System.out.print("   ");
        System.out.print("   ");
        for (int i = 0; i < size; i++) {
            System.out.print(charArray[i].toString() + " ");
        }
        System.out.println("");

        for (int i = 1; i < size + 1; i++) {
            if (i < 10)
                System.out.print(i + "  ");
            else
                System.out.print(i + " ");
            for (int j = 0; j < size; j++) {
                if (boats_array[i - 1][j] == null)
                    System.out.print(". ");
                else
                    System.out.print(boats_array[i - 1][j].toString() + " ");
            }
            System.out.print("   ");
            if (i < 10)
                System.out.print(i + "  ");
            else
                System.out.print(i + " ");
            for (int j = 0; j < size; j++) {
                if (hits_array[i - 1][j] == null)
                    System.out.print(". ");
                else if (!hits_array[i - 1][j])
                    System.out.print("x ");
                else
                    System.out.print(ColorUtil.colorize("x ", ColorUtil.Color.RED));

            }
            System.out.println("");
        }

    }

    public int getSize() {
        return this.size;
    }

    @Override
    public void putShip(AbstractShip ship, int x, int y) throws OutOfBound, IncorrectPosition {
        if (x < 0 || x >= size || y < 0 || y >= size)
            throw new OutOfBound("Positions incorrectes");
        switch (ship.getDirection()) {
            case NORTH:
                if (x - ship.getSize() + 1 < 0)
                    throw new OutOfBound("Positions incorrectes");
                for (int i = 0; i < ship.getSize(); i++)
                    if (this.boats_array[x - i][y].getShip() != null)
                        throw new IncorrectPosition("A ship is already at this position");
                for (int i = 0; i < ship.getSize(); i++) {
                    this.boats_array[x - i][y].setShip(ship);
                }
                break;
            case SOUTH:
                if (x + ship.getSize() - 1 >= size)
                    throw new OutOfBound("Positions incorrectes");
                for (int i = 0; i < ship.getSize(); i++)
                    if (this.boats_array[x + i][y].getShip() != null)
                        throw new IncorrectPosition("A ship is already at this position");
                for (int i = 0; i < ship.getSize(); i++)
                    this.boats_array[x + i][y].setShip(ship);
                break;
            case WEST:
                if (y - ship.getSize() + 1 < 0)
                    throw new OutOfBound("Positions incorrectes");
                for (int i = 0; i < ship.getSize(); i++)
                    if (this.boats_array[x][y - 1].getShip() != null)
                        throw new IncorrectPosition("A ship is already at this position");
                for (int i = 0; i < ship.getSize(); i++)
                    this.boats_array[x][y - i].setShip(ship);
                break;

            case EAST:
                if (y + ship.getSize() - 1 >= size)
                    throw new OutOfBound("Positions incorrectes");
                for (int i = 0; i < ship.getSize(); i++)
                    if (this.boats_array[x][y + i].getShip() != null)
                        throw new IncorrectPosition("A ship is already at this position");
                for (int i = 0; i < ship.getSize(); i++)
                    this.boats_array[x][y + i].setShip(ship);
                break;

        }

    }

    public boolean hasShip(int x, int y) throws OutOfBound {
        if (x < 0 || x >= size || y < 0 || y >= size)
            throw new OutOfBound("Positions incorrectes");
        if ((boats_array[x][y].getShip() != null) && (boats_array[x][y].getShip().isSunk()))
            return false;
        return (boats_array[x][y].getShip() != null);
    }

    @Override
    public void setHit(Boolean hit, int x, int y) throws OutOfBound {
        if (x < 0 || x >= size || y < 0 || y >= size)
            throw new OutOfBound("Positions incorrectes");
        hits_array[x][y] = hit;
    }

    @Override
    public Boolean getHit(int x, int y) throws OutOfBound {
        if (x < 0 || x >= size || y < 0 || y >= size)
            throw new OutOfBound("Positions incorrectes");
        return hits_array[x][y];
    }

    @Override
    public Hit sendHit(int x, int y) {
        try {

            if (this.hasShip(x, y)) {
                AbstractShip ship_at_hitpoint = boats_array[x][y].getShip();
                boats_array[x][y].addStrike();
                if (ship_at_hitpoint.isSunk()) {
                    switch (ship_at_hitpoint.getLabel()) {
                        case 'B':
                            System.out.println("Battleship sunk");
                            return Hit.BATTLESHIP;
                        case 'C':
                            System.out.println("Aircraft Carrier sunk");
                            return Hit.CARRIER;
                        case 'D':
                            System.out.println("Destroyer sunk");
                            return Hit.DESTROYER;
                        case 'S':
                            System.out.println("Submarine sunk");
                            return Hit.SUBMARINE;
                    }
                }
                return Hit.STIKE;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return Hit.MISS;
    };
}