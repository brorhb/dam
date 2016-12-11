package spill;

import java.awt.*;
import java.io.Serializable;

/** En enkel rute som er på Brett. Dette extender Canvas. Ruten lagrer også informasjon
 *  om Brikken som er i ruten på denne posisjonen. Ruten vet også hvor den er plassert på brettet.
 */
@SuppressWarnings("serial")
public class Rute extends Canvas implements Serializable {


    /** Fargene rutene kan ha */
    public enum bakgrunnsFarge {
        LYS, MORK
    }


    /** Fargen på en rute */
    private bakgrunnsFarge bakgrunnsFarge;


    /** Om det er i noen brikke i ruten */
    private boolean okkupert;


    /** Brikken som okkuperer en rute. Denne kan være NULL */
    private Brikke okkupant;



    /** Raden ruten er i */
    private int rad;

    /** kolonnen ruten er i */
    private int kolonne;


    /** Lager en ny rute på en gitt posisjon med riktig bakgrunnsfarge
     *
     *  @param fargen        	fargen til ruten
     */
    public Rute(bakgrunnsFarge fargen, int minRad, int minKolonne) {
        this.setSize(64, 64);
        if(fargen == bakgrunnsFarge.MORK) {
            this.setBackground(Color.DARK_GRAY);
        } else {
            this.setBackground(Color.LIGHT_GRAY);
        }

        bakgrunnsFarge = fargen;
        okkupert = false;
        okkupant = null;

        this.rad = minRad;
        this.kolonne = minKolonne;
    }




    /** Returnerer om ruten er okkupert eller ikke
     */
    public boolean erOkkupert() {
        return this.okkupert;
    }


    /** Henter raden ruten er på
     */
    public int getRad() {
        return this.rad;
    }

    /** Henter kolonnen ruten er på
     */
    public int getKolonne() {
        return this.kolonne;
    }

    /** Henter bakgrunnsfargen i ruten
     */
    public bakgrunnsFarge getBackgroundColor() {
        return this.bakgrunnsFarge;
    }

    /** hent brikken som okkuperer ruten
     */
    public Brikke getOkkupant() {
        if(this.erOkkupert()) {
            return this.okkupant;
        }
        return null;
    }



    /** Om ruten skal være uthevet eller ikke
     * @param utforUtheving 			Om ruten skal utheves eller ikke
     */
    public void setUtheving(boolean utforUtheving) {
        Graphics g = this.getGraphics();
        if(utforUtheving) {
            if(!this.erOkkupert()) {
                g.setColor(Color.BLACK);
                //Tegner en siluett av der brikken lander
                for(int i = 0; i < 360; i+= 30) {
                    g.drawArc(5, 5, 54, 54, i, 15);
                }
            } else {
                //Tenger ett rektangel rundt ruten
                g.setColor(Color.YELLOW);
                g.draw3DRect(0, 0, 63, 63, false);

            }
        } else {
            //Hvis ruten er uthevet, fjern uthevingen
            super.update(this.getGraphics());
        }
    }


    /** Setter okkupanten av ruten
     *
     * @param besokende       Brikken som nå er i ruten
     */
    public void setOkkupant(Brikke besokende) {
        if(besokende != null) {
            this.okkupant = besokende;
            this.okkupert = true;
        } else {
            this.okkupant = null;
            this.okkupert = false;
        }
    }





    @Override
    /** Få ruten til å tegne seg på nytt, på den måten fjerner vi brikker og uthevning.
     */
    public void paint(Graphics g) {

        if(this.getBackgroundColor() == bakgrunnsFarge.MORK) {
            this.setBackground(Color.DARK_GRAY);
        } else {
            this.setBackground(Color.LIGHT_GRAY);
        }

        if(this.erOkkupert()) {

            g.setColor(okkupant.getFarge());
            g.fillOval(5, 5, 54, 54);


        } else {
            g.clearRect(0, 0, 64, 64);
        }

    }



}