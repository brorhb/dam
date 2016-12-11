package spill;/* Matthew Proetsch
 * COP3330 Section 0001
 * spill.Board.java: See Javadoc for details
 */

import java.io.Serializable;
import java.util.Vector;
import java.awt.*;


/**
 * Denne klassen lagrer spillbrettet som et 2D array av ruter. Denne klassen sørger også for funksjonaliteten
 * til en Brikke i en Rute, og muligheten til å se alle mulige trekk for en valgt brikke
 */

public class Board implements Serializable {

    /** Antall rader */
    public static final int rows = 8;
    /** Antall kolonner */
    public static final int cols = 8;
    /** Et array av Ruter som lager brettet*/
    private Square[][] gameBoard;


    /** Konsturktøren tar ingen argumenter, og produserer ett brett med gitt antall kolonner og rader,
     *  med varierende bakgrunnsfarge */
    public Board() {


        gameBoard = new Square[rows][cols];

        //Set up the game board with alternating colors
        boolean lastcolor = false;
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {

                if(lastcolor)
                    gameBoard[i][j] = new Square(Square.BackgroundColor.DARK, i, j);
                else
                    gameBoard[i][j] = new Square(Square.BackgroundColor.LIGHT, i, j);

                //Toggle lastcolor
                lastcolor = !lastcolor;
            }

            //Switch starting color for next row
            lastcolor = !lastcolor;
        }


    }






    /**
     * Sjekk om en posisjon er innen for rammene til brettet
     * @param row			Raden som sjekkes
     * @param col			Kolonnen som sjekkes
     * @return				True hvis posisjonen er lovlig, false hvis ikke
     */
    public static boolean inBounds(int row, int col) {
        if(row >= 0 && row < rows &&
                col >= 0 && col < cols)

            return true;


        return false;

    }


    /** Henter en Rute som er inne i Spillbrettet
     *
     * @param row		Raden ruten skal være i
     * @param col		Kolonnen ruten skal være i
     *
     * @return			ruten ved (row, col), eller null hvis (row, col) er utenfor rammene
     */
    public Square getSquare(int row, int col) {
        if(inBounds(row, col))
            return gameBoard[row][col];


        return null;
    }

    /** Fyller brettet med brikker. Røde på toppen, og Svarte nederst */
    public void placeStartingPieces() {

        //Plasserer røde øverst
        for(int row = 0; row < 3; row++)
            for(int col = 0; col < 8; col++)
                if(getSquare(row, col).getBackgroundColor() == Square.BackgroundColor.DARK)
                    getSquare(row,col).setOccupant(new Piece(Color.RED, row, col));

        //Plasserer svarte nederst
        for(int row = 5; row < 8; row++)
            for(int col = 0; col < 8; col++)
                if(getSquare(row, col).getBackgroundColor() == Square.BackgroundColor.DARK)
                    getSquare(row,col).setOccupant(new Piece(Color.BLACK, row, col));
    }


    /** Finner alle mulig Ruter en Brikke kan flyttes til
     *
     * @param p 				Brikken som en skal finne mulig trekk for
     *
     * @return					En vektor som viser hvor brikken kan flytte
     */
    public Vector<Square> getPossibleMoves(Piece p) {

        Vector<Square> possibleMoves = new Vector<Square>();
        Color pColor = p.getColor();

        int row = p.getRow();
        int col = p.getCol();

        //Sjekker hvilke trekk som er mulig, og passer på at bare svarte kan hoppe oppover, og røde nedover

        //Check moves to the top-left of this piece
        if(Board.inBounds(row-1, col-1) && pColor == Color.BLACK) {

            if(!this.getSquare(row-1, col-1).isOccupied())
                possibleMoves.add(this.getSquare(row-1, col-1));

                //if square is occupied, and the color of the spill.Piece in square is
                //not equal to the piece whose moves we are checking, then
                //check to see if we can make the jump by checking
                //the next square in the same direction
            else
            if(Board.inBounds(row-2, col-2))

                if(!this.getSquare(row-2, col-2).isOccupied() &&
                        (this.getSquare(row-1, col-1).getOccupant().getColor() != pColor))

                    possibleMoves.add(this.getSquare(row-2, col-2));

        }

        //Check moves to the top-right of this piece
        if(Board.inBounds(row-1, col+1) && pColor == Color.BLACK) {

            if(!this.getSquare(row-1, col+1).isOccupied())
                possibleMoves.add(this.getSquare(row-1, col+1));

            else
            if(Board.inBounds(row-2, col+2))

                if(!this.getSquare(row-2, col+2).isOccupied() &&
                        (this.getSquare(row-1, col+1).getOccupant().getColor() != pColor))

                    possibleMoves.add(this.getSquare(row-2, col+2));
        }

        //check moves to the bottom-left of this piece
        if(Board.inBounds(row+1, col-1) && pColor == Color.RED) {

            if(!this.getSquare(row+1, col-1).isOccupied())
                possibleMoves.add(this.getSquare(row+1, col-1));



            else
            if(Board.inBounds(row+2, col-2))

                if(!this.getSquare(row+2, col-2).isOccupied() &&
                        (this.getSquare(row+1, col-1).getOccupant().getColor() != pColor))

                    possibleMoves.add(this.getSquare(row+2, col-2));
        }

        //check moves to the bottom-right of this piece
        if(Board.inBounds(row+1, col+1) && pColor == Color.RED) {

            if(!this.getSquare(row+1, col+1).isOccupied())
                possibleMoves.add(this.getSquare(row+1, col+1));

            else
            if(Board.inBounds(row+2, col+2))

                if(!this.getSquare(row+2, col+2).isOccupied() &&
                        (this.getSquare(row+1, col+1).getOccupant().getColor() != pColor))

                    possibleMoves.add(this.getSquare(row+2, col+2));
        }


        return possibleMoves;

    }


    /** Highlight all the possible moves that can be made
     *
     * @param p 				The spill.Piece whose possible moves we are highlighting
     * @param doHighlight		Whether or not these possible moves should be highlighted
     */
    public void setMovesHighlighted(Piece p, boolean doHighlight) {

        Vector<Square> possibleMoves = getPossibleMoves(p);

        if(doHighlight) {
            for(Square highlight : possibleMoves)
                highlight.setHighlight(true);
        }

        else {
            for(Square highlight : possibleMoves)
                highlight.setHighlight(false);
        }
    }


    /** Perform a move on the board. This function does not perform input checking, as it is only called
     * once a move has been validated by getPossibleMoves
     *
     *
     * @param from 				The square from which we are moving
     * @param to				The square to which we are moving
     * @return					True if a jump has been performed, false if it's just a normal move
     */
    public boolean move(Square from, Square to) {
        boolean jumpPerformed = false;

        Piece beingMoved = from.getOccupant();

        int oldRow = from.getRow(), newRow = to.getRow();
        int oldCol = from.getCol(), newCol = to.getCol();

        from.setOccupant(null);
        beingMoved.setLoc(to.getRow(), to.getCol());
        to.setOccupant(beingMoved);

        if(Math.abs(oldRow - newRow) > 1 || Math.abs(oldCol - newCol) > 1) {
            //A jump has been performed, so get the spill.Square that lies between from and to
            int takeRow = (oldRow + newRow) / 2;
            int takeCol = (oldCol + newCol) / 2;

            Square takeSquare = getSquare(takeRow, takeCol);
            takeSquare.setOccupant(null);
            takeSquare.update(takeSquare.getGraphics());

            jumpPerformed = true;

        }

        from.update(from.getGraphics());
        to.update(to.getGraphics());

        return jumpPerformed;


    }





}