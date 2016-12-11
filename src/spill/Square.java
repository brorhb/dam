package spill;/* Matthew Proetsch
 * COP3330 Section 0001
 * spill.Square.java: See Javadoc comment for details
 */

import java.awt.*;
import java.io.Serializable;

/** Represents a single square contained in spill.Board, represented as a Canvas
 *  Also stores the occupant of this position on the board, and squares
 *  are aware of their position within the board.
 *
 *
 * @author Matthew Proetsch
 * @version 0.9b
 */
@SuppressWarnings("serial")
public class Square extends Canvas implements Serializable {


    /** The background color that a square should be */
    public enum BackgroundColor { LIGHT, DARK };


    /** The background color of this spill.Square */
    private BackgroundColor bgColor;


    /** Whether or not this spill.Square is occupied */
    private boolean occupied;


    /** The spill.Piece that occupies this spill.Square, may be NULL */
    private Piece occupant;



    /** The row of the game board that this square represents */
    private int row;

    /** The column of the game board that this square represents */
    private int col;


    /** Make a new spill.Square at the given position with the specified BackgroundColor
     *
     *  @param c        	The background color of this spill.Square
     */
    public Square(BackgroundColor c, int myrow, int mycol) {

        this.setSize(64, 64);

        if(c == BackgroundColor.DARK)
            this.setBackground(Color.DARK_GRAY);
        else
            this.setBackground(Color.LIGHT_GRAY);

        bgColor = c;
        occupied = false;
        occupant = null;

        this.row = myrow;
        this.col = mycol;

    }




    /** Return whether or not this spill.Square is occupied
     *
     * @return 					Whether or not this spill.Square is selected
     */
    public boolean isOccupied() {
        return this.occupied;
    }


    /** Get the row of the game board that this square represents
     *
     * @return 			The row on the game board represented by this spill.Square
     */
    public int getRow() {
        return this.row;
    }

    /** Get the column of the game board that this square represents
     *
     * @return 			The column on the game board represented by this spill.Square
     */
    public int getCol() {
        return this.col;
    }

    /** Get the background color of this spill.Square */
    public Square.BackgroundColor getBackgroundColor() {
        return this.bgColor;
    }

    /** Get the piece that occupies this spill.Square
     *
     * @return				The piece that occupies this spill.Square, if any
     */
    public Piece getOccupant() {
        if(this.isOccupied())
            return this.occupant;

        return null;
    }



    /** Set whether or not this spill.Square is highlighted
     *
     * @param doHighlight 			Whether or not this square should be highlighted
     */
    public void setHighlight(boolean doHighlight) {


        Graphics g = this.getGraphics();

        if(doHighlight) {

            if(!this.isOccupied()) {

                g.setColor(Color.BLACK);

                //Draw a dotted oval where this piece may land
                for(int i = 0; i < 360; i+= 30)
                    g.drawArc(5, 5, 54, 54, i, 15);
            }

            else {
                //Draw a yellow rect around the border of this spill.Square
                g.setColor(Color.YELLOW);
                g.draw3DRect(0, 0, 63, 63, false);

            }
        }
        else
            //If this square has a highlight in it, erase it by redrawing the spill.Square
            super.update(this.getGraphics());

    }


    /** Set the occupant of this spill.Square
     *
     * @param visitor       The spill.Piece that should now reside here
     */
    public void setOccupant(Piece visitor) {
        if(visitor != null) {

            this.occupant = visitor;
            this.occupied = true;

        }

        else {

            this.occupant = null;
            this.occupied = false;

        }
    }





    @Override
    /** Have this square redraw itself, removing potential highlighting and adding/removing an occupant spill.Piece as necessary
     *
     * @param g 						The Graphics object belonging to this spill.Square
     *
     */
    public void paint(Graphics g) {

        //Set the Canvas' background color equal to the spill.Square's bgcolor
        if(this.getBackgroundColor() == Square.BackgroundColor.DARK)
            this.setBackground(Color.DARK_GRAY);

        else
            this.setBackground(Color.LIGHT_GRAY);

        //Either draw a square or clear the rectangle
        if(this.isOccupied()) {

            g.setColor(occupant.getColor());
            g.fillOval(5, 5, 54, 54);


        }

        else
            g.clearRect(0, 0, 64, 64);

    }



}