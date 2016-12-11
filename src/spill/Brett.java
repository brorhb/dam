package spill;

import java.io.Serializable;
import java.util.Vector;
import java.awt.*;


/**
 * Denne klassen lagrer spillbrettet som et 2D array av ruter. Denne klassen sørger også for funksjonaliteten
 * til en Brikke i en Rute, og muligheten til å se alle mulige trekk for en valgt brikke
 */

public class Brett implements Serializable {

    /** Antall rader */
    public static final int rader = 8;
    /** Antall kolonner */
    public static final int kolonner = 8;
    /** Et array av Ruter som lager brettet*/
    private Rute[][] spillBrett;


    /** Konsturktøren tar ingen argumenter, og produserer ett brett med gitt antall kolonner og rader,
     *  med varierende bakgrunnsfarge */
    public Brett() {


        spillBrett = new Rute[rader][kolonner];

        //Set up the game board with alternating colors
        boolean forrigeFarge = false;
        for(int i = 0; i < rader; i++) {
            for(int j = 0; j < kolonner; j++) {

                if(forrigeFarge) {
                    spillBrett[i][j] = new Rute(Rute.bakgrunnsFarge.MORK, i, j);
                } else {
                    spillBrett[i][j] = new Rute(Rute.bakgrunnsFarge.LYS, i, j);
                }
                //Toggle lastcolor
                forrigeFarge = !forrigeFarge;
            }
            //Switch starting farge for next row
            forrigeFarge = !forrigeFarge;
        }

    }






    /**
     * Sjekk om en posisjon er innen for rammene til brettet
     * @param rad			Raden som sjekkes
     * @param kolonne		Kolonnen som sjekkes
     * @return				True hvis posisjonen er lovlig, false hvis ikke
     */
    public static boolean innenforRammene(int rad, int kolonne) {
        if(rad >= 0 && rad < rader &&
                kolonne >= 0 && kolonne < kolonner)

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
    public Rute getRute(int row, int col) {
        if(innenforRammene(row, col))
            return spillBrett[row][col];


        return null;
    }

    /** Fyller brettet med brikker. Røde på toppen, og Svarte nederst */
    public void plasserBrikkerForStart() {

        //Plasserer røde øverst
        for(int row = 0; row < 3; row++)
            for(int col = 0; col < 8; col++)
                if(getRute(row, col).getBackgroundColor() == Rute.bakgrunnsFarge.MORK)
                    getRute(row,col).setOkkupant(new Brikke(Color.RED, row, col));

        //Plasserer svarte nederst
        for(int row = 5; row < 8; row++)
            for(int col = 0; col < 8; col++)
                if(getRute(row, col).getBackgroundColor() == Rute.bakgrunnsFarge.MORK)
                    getRute(row,col).setOkkupant(new Brikke(Color.BLACK, row, col));
    }


    /** Finner alle mulig Ruter en Brikke kan flyttes til
     *
     * @param brikke 			Brikken som en skal finne mulig trekk for
     *
     * @return					En vektor som viser hvor brikken kan flytte
     */
    public Vector<Rute> hentMuligeTrekk(Brikke brikke) {

        Vector<Rute> muligeTrekk = new Vector<Rute>();
        Color brikkeFarge = brikke.getFarge();

        int rad = brikke.getRad();
        int kolonne = brikke.getKolonne();

        //Sjekker hvilke trekk som er mulig, og passer på at bare svarte kan hoppe oppover, og røde nedover

        if(Brett.innenforRammene(rad-1, kolonne-1) && brikkeFarge == Color.BLACK) {

            //sjekk trekk opp til venstre
            if(!this.getRute(rad-1, kolonne-1).erOkkupert())
                muligeTrekk.add(this.getRute(rad-1, kolonne-1));

                // hvis plassen er opptatt, og fargen på Brikken i Rute ikke er lik brikken som
                // vi skal sjekke, så sjekk for å se om vi hoppe ved å sjekke neste plassen i samme retning.
            else
            if(Brett.innenforRammene(rad-2, kolonne-2))

                if(!this.getRute(rad-2, kolonne-2).erOkkupert() &&
                        (this.getRute(rad-1, kolonne-1).getOkkupant().getFarge() != brikkeFarge))

                    muligeTrekk.add(this.getRute(rad-2, kolonne-2));

        }

        //Sjekk trekk opp til høyre
        if(Brett.innenforRammene(rad-1, kolonne+1) && brikkeFarge == Color.BLACK) {

            if(!this.getRute(rad-1, kolonne+1).erOkkupert())
                muligeTrekk.add(this.getRute(rad-1, kolonne+1));

            else
            if(Brett.innenforRammene(rad-2, kolonne+2))

                if(!this.getRute(rad-2, kolonne+2).erOkkupert() &&
                        (this.getRute(rad-1, kolonne+1).getOkkupant().getFarge() != brikkeFarge))

                    muligeTrekk.add(this.getRute(rad-2, kolonne+2));
        }

        //sjekk trekk ned til venstre
        if(Brett.innenforRammene(rad+1, kolonne-1) && brikkeFarge == Color.RED) {

            if(!this.getRute(rad+1, kolonne-1).erOkkupert())
                muligeTrekk.add(this.getRute(rad+1, kolonne-1));



            else
            if(Brett.innenforRammene(rad+2, kolonne-2))

                if(!this.getRute(rad+2, kolonne-2).erOkkupert() &&
                        (this.getRute(rad+1, kolonne-1).getOkkupant().getFarge() != brikkeFarge))

                    muligeTrekk.add(this.getRute(rad+2, kolonne-2));
        }

        //sjekk trekk ned til høyre
        if(Brett.innenforRammene(rad+1, kolonne+1) && brikkeFarge == Color.RED) {

            if(!this.getRute(rad+1, kolonne+1).erOkkupert())
                muligeTrekk.add(this.getRute(rad+1, kolonne+1));

            else
            if(Brett.innenforRammene(rad+2, kolonne+2))

                if(!this.getRute(rad+2, kolonne+2).erOkkupert() &&
                        (this.getRute(rad+1, kolonne+1).getOkkupant().getFarge() != brikkeFarge))

                    muligeTrekk.add(this.getRute(rad+2, kolonne+2));
        }


        return muligeTrekk;

    }


    /** Uthev alle mulige trekk som brikken kan gjøre.
     *
     * @param p 				Brikken som vi skal vise trekkene til
     * @param uthev		Stedene som skal merkes
     */
    public void setUthevningPaaMuligeTrekk(Brikke p, boolean uthev) {

        Vector<Rute> muligeTrekk = hentMuligeTrekk(p);

        if(uthev) {
            for(Rute highlight : muligeTrekk)
                highlight.setUtheving(true);
        }

        else {
            for(Rute highlight : muligeTrekk)
                highlight.setUtheving(false);
        }
    }


    /** utfør trekket på brettet. Denne metoden sjekker om trekket er lovlig.
     *
     *
     * @param fra 				Ruten vi flytter fra
     * @param til				Ruten vi flytter til
     * @return					True, trekket er gjort og er lovlig. False da skjer det ikke noe.
     */
    public boolean move(Rute fra, Rute til) {
        boolean trekkUtfort = false;

        Brikke blirFlyttet = fra.getOkkupant();

        int gammelRad = fra.getRad(), nyRad = til.getRad();
        int gammelKolonne = fra.getKolonne(), nyKolonne = til.getKolonne();

        fra.setOkkupant(null);
        blirFlyttet.setLokasjon(til.getRad(), til.getKolonne());
        til.setOkkupant(blirFlyttet);

        if(Math.abs(gammelRad - nyRad) > 1 || Math.abs(gammelKolonne - nyKolonne) > 1) {
            //Hoppet har blitt gjennomført.
            int taRad = (gammelRad + nyRad) / 2;
            int taKolonne = (gammelKolonne + nyKolonne) / 2;

            Rute takeRute = getRute(taRad, taKolonne);
            takeRute.setOkkupant(null);
            takeRute.update(takeRute.getGraphics());

            trekkUtfort = true;

        }

        fra.update(fra.getGraphics());
        til.update(til.getGraphics());

        return trekkUtfort;


    }





}