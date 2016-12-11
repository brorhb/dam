package spill;

import java.awt.*;
import java.io.Serializable;

/** Brikken kan være rød eller svart. Klassen passer på å tegne Rute på nytt, og passer på hvor mange
 *  brikker spillerne har igjen
 */
public class Brikke implements Serializable {


    /** Den raden brikken står på*/
    private int rad;

    /** Den kolonnen brikken står på*/
    private int kolonne;

    /** Fargen brikken har */
    public Color farge;


    /** Lager en ny brikke med gitt farge og posisjon
     * @param farge			Fargen brikken skal ha
     * @param rad		Raden brikken skal være på
     * @param kolonne		Kolonnen briken skal være på
     */
    public Brikke(Color farge, int rad, int kolonne) {
        this.farge = farge;
        this.rad = rad;
        this.kolonne = kolonne;
    }

    public int getRad() {
        return rad;
    }

    public Color getFarge() {
        return farge;
    }

    public int getKolonne() {
        return kolonne;
    }

    /** Gir en ny posisjon for en brikke
     *
     * @param rad		den nye raden brikken er på
     * @param kolonne		den nye kolonnen brikken er på
     */
    public void setLokasjon(int rad, int kolonne) {
        this.rad = rad;
        this.kolonne = kolonne;
    }



    /** beskrivelsen av en brikke som en String
     * @return					Strengen som beskriver brikken
     */
    public String toString() {

        StringBuilder s = new StringBuilder();

        if(this.farge == Color.BLACK) {
            s.append("Svart ");
        } else {
            s.append("Red ");
        }

        s.append("brikke på rad " + Integer.toString(this.getRad()) +
                ", kolonne " + Integer.toString(this.getKolonne()));

        return s.toString();
    }

}