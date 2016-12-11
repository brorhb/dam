package spill;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/** Starter en ny instans av DamSpill, og tegner det det nødvendige grafiske innholdet.
 *  Gir også valg fra en menybar som å starte og avsluttet spillet.
 */


public class DamSpill implements MouseListener, ActionListener, Serializable {

    /** Rammen som holder spillet*/
    private JFrame vindu;

    /** Panelet som holder på brettet*/
    private JPanel brettPanel;

    /** labelen som holder styr på gjennværende brikker på hver side*/
    private JLabel brikkeLabel;

    /** Menubar containing Exit and New Game options
     * Menybar som holder på avslutt og Nytt Spill knapper*/
    private JMenuBar menybar;

    /** File menu */
    private JMenu fileMenu;

    /** Nytt spill valg */
    private JMenuItem nyttSpill;

    /** Avslutt valg */
    private JMenuItem avslutt;

    /** Holder styr på hvem sin tur det er */
    private Color naaverendeSpiller;

    /** Border width mellom rutene på brettet */
    private final int borderWidth = 1;

    /** brettet som vil lagre spillets status */
    private Brett brett;

    /** Antallet brikker Svart spiller har igjen */
    private int svarteBrikkerSomErIgjen;

    /** Antallet brikker Rød spiller har igjen */
    private int RodeBrikkerSomErIgjen;

    /** Holder en referanse til valgt rute */
    private Rute valgtRute;



    /** Konstruktør som ikke tar i mot noen argumenter, og starter ett nytt spill*/
    public DamSpill() {

        //Viser grafikken
        LagOgVisGUI();

        //Definerer første spiller
        naaverendeSpiller = Color.GREEN;

        //antall brikker hver spiller starter med
        RodeBrikkerSomErIgjen = 12;
        svarteBrikkerSomErIgjen = 12;

        //hvor mange brikker som er igjen
        oppdaterStatus();
    }

    /** Setter opp den grafiske visningen av spillet */
    public void LagOgVisGUI() {

        //Setter opp informasjon om vinduet
        vindu = new JFrame("Dam");
        vindu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        vindu.setLayout(new FlowLayout());

        vindu.getContentPane().setLayout(
                new BoxLayout(vindu.getContentPane(), BoxLayout.Y_AXIS));

        //Gir en visuell representasjon av Brettet
        brettPanel = new JPanel(new GridLayout(8, 8));
        brettPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        brett = new Brett();
        brett.plasserBrikkerForStart();

        //Holder styr på hvor mange brikker som er igjen
        brikkeLabel = new JLabel(" ");
        brikkeLabel.setHorizontalTextPosition(JLabel.LEFT);
        brikkeLabel.setVerticalTextPosition(JLabel.BOTTOM);

        //Legger til menybar på toppen av vinduet
        menybar = new JMenuBar();
        fileMenu = new JMenu("Valg");

        nyttSpill = new JMenuItem("Nytt Spill");
        nyttSpill.addActionListener(this);

        avslutt = new JMenuItem("Avslutt");
        avslutt.addActionListener(this);

        fileMenu.add(nyttSpill);
        fileMenu.add(avslutt);
        menybar.add(fileMenu);

        //Legger Brettet til BrettPanel slik at alt synes i vinduet
        leggBrettTilPanel(brett, brettPanel);
        vindu.add(brettPanel);
        vindu.add(brikkeLabel);
        vindu.setJMenuBar(menybar);
        vindu.pack();

        //Endre størrelse på vinduet fordi det av en eller annen grunn ønsker å kutte det siste tegnet av vår JLabel
        Rectangle boundingRect = vindu.getBounds();
        vindu.setBounds(boundingRect.x, boundingRect.y, boundingRect.width + 5, boundingRect.height);

        vindu.setVisible(true);


    }

    @Override
    public void mouseClicked(MouseEvent e) {


        Rute valgt = (Rute)e.getComponent();

        //Passer på at riktig spiller velger riktig farge
        //Fargen på brikkene skal være lik fargen spilleren har. Med mindre det er første trekk
        //da er naaverendeSpiller Color.GREEN
        if(valgt.erOkkupert())
            if(valgt.getOkkupant().getFarge() != naaverendeSpiller && naaverendeSpiller != Color.GREEN) {
                brikkeLabel.setText("Dette er ikke din brikke");
                return;
            }

		/* Siden vi vet at brukeren kun kan velge sine brikker er det bare 4 mulige ting brukeren kan gjøre.
		 *  - Brukeren har ikke uthevet noen rute.
		 *  - Brukeren ønsker å velge en annen rute
		 *  - Brukeren ønsker å fjerne valg av rute
		 *  - Brukeren ønsker å gjøre ett trekk
		 */


        if(valgt.erOkkupert() && valgtRute == null) {
            //Det er ingen valgt rute, så uthev alle mulige trekk
            valgtRute = valgt;
            valgtRute.setUtheving(true);
            brett.setUthevningPaaMuligeTrekk(valgtRute.getOkkupant(), true);
            return;

        }




        else if(valgt.erOkkupert() && !valgt.equals(valgtRute)) {
            //Brukeren har klikket på en ny brikke
            valgtRute.setUtheving(false);
            brett.setUthevningPaaMuligeTrekk(valgtRute.getOkkupant(), false);

            //Endre uthevet rute
            valgtRute = valgt;
            valgtRute.setUtheving(true);
            brett.setUthevningPaaMuligeTrekk(valgtRute.getOkkupant(), true);
            return;

        }


        else if(valgt.equals(valgtRute)) {
            //brukeren har valgt vekk valgt rute
            valgtRute.setUtheving(false);
            brett.setUthevningPaaMuligeTrekk(valgtRute.getOkkupant(), false);
            valgtRute = null;
        }



        //Sjekker trekk
        else if(!valgt.erOkkupert() && valgtRute != null) {
            //Brukeren prøver å utføre ett trekk fra den den valgte ruten

            boolean funn = false;
            boolean trekk = false;

            Vector<Rute> gamleMuligeTrekk = brett.hentMuligeTrekk(valgtRute.getOkkupant());

            for(Rute valg : gamleMuligeTrekk) {
                if(valg.equals(valgt)) {

                    //Hvis trekket er mulig, utfør det

                    //Se om første trekk er utført
                    if(naaverendeSpiller == Color.GREEN)
                        naaverendeSpiller = valgtRute.getOkkupant().getFarge();

                    //lagre variablen uansett om noen brikke er flyttet eller ikke
                    trekk = brett.move(valgtRute, valgt);

                    //funn av trekk og og trekket er utført
                    funn = true;


                }
            }



            if(funn) {
                if(trekk) {
                    if(naaverendeSpiller == Color.BLACK) {
                        RodeBrikkerSomErIgjen--;
                    } else {
                        svarteBrikkerSomErIgjen--;
                    }
                }

                // Fjern uthevningen trekkene brikken kunne ta fra sin forrige posisjon
                valgtRute.setUtheving(false);
                for (Rute fjernUthevning : gamleMuligeTrekk)
                    fjernUthevning.setUtheving(false);
                valgtRute = null;

                sluttTur();
                //Oppdaterer antall brikker
                oppdaterStatus();


                //Sjekk om trekket gjorde slik at noen vant
                String vinnerStr = vinner();
                if(vinnerStr != null) {
                    int restart = JOptionPane.showConfirmDialog(null, vinnerStr + " Vil du starte ett nytt spill?", "Nytt spill?", JOptionPane.YES_NO_OPTION);

                    if(restart == JOptionPane.YES_OPTION) {
                        restartSpill();
                    } else {
                        vindu.setVisible(false);
                        vindu.dispose();
                    }

                }
            }

            else if(!funn)
                //Forteller brukeren at trekket ikke er lov
                brikkeLabel.setText("No can do baby doll!");
        }


    }

    //Må implementerer siden musen blir brukt i spillet
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}


    @Override
    /** Utfører riktig handlig i menyen */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == nyttSpill) {
            restartSpill();
        }
        else if(e.getSource() == avslutt) {
            vindu.setVisible(false);
            vindu.dispose();
        }

    }

    /** Legger til brettet i ett Panel, som lager utseende til ett sjakkbrett
     * @param brett					Brettet som legges til panelet
     * @param panel					panelet
     */
    public void leggBrettTilPanel(Brett brett, JPanel panel) {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                Rute sq = brett.getRute(i, j);
                sq.addMouseListener(this);

                JPanel innhold = new JPanel(new FlowLayout());
                innhold.setBorder(BorderFactory.createLineBorder(Color.BLACK,
                        borderWidth));
                innhold.add(sq);
                if(sq.getBackgroundColor() == Rute.bakgrunnsFarge.MORK)
                    innhold.setBackground(Color.DARK_GRAY);
                else
                    innhold.setBackground(Color.LIGHT_GRAY);
                panel.add(innhold);
            }
        }
    }



    /** Oppdaterer Stringen for antall brikker igjen */
    public void oppdaterStatus() {
        brikkeLabel.setText("Røde brikker igjen: " + RodeBrikkerSomErIgjen + "             Svarte Brikker igjen: " + svarteBrikkerSomErIgjen);
    }


    /** Find out, if the game is over, who won and how that side won
     * Finner ut om spillet er ferdig eller ikke
     *
     * @return 				En string som forteller hvordan spilleren vant
     */
    public String vinner() {

        //Sjekker om noen ikke har flere brikker igjen.
        if(svarteBrikkerSomErIgjen == 0) {
            return "Rød spiller har vunnet, fordi Svart ikke har flere brikker igjen!";
        }
        if(RodeBrikkerSomErIgjen == 0) {
            return "Svart spiller har vunnet, fordi Rød ikke har flere brikker igjen!";
        }


        //Sjekker om det er flere mulige trekk for en spiller
        boolean rodKanFlytte = false;
        boolean svartKanFlytte = false;

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {

                //Henter alle mulige trekk for alle brikker på brettet
                if(brett.getRute(i, j).erOkkupert()) {
                    Vector<Rute> potensielleTrekk = brett.hentMuligeTrekk(brett.getRute(i, j).getOkkupant());

                    if(! potensielleTrekk.isEmpty()) {
                        //En side kan alltid gjøre ett trekk.
                        if(brett.getRute(i, j).getOkkupant().getFarge() == Color.black) {
                            svartKanFlytte = true;
                        } else {
                            rodKanFlytte = true;
                        }

                    }
                }
            }
        }

        if(rodKanFlytte && !svartKanFlytte) {
            return "Rød spiller vinner, fordi Svart har ikke flere trekk!";
        } else if(svartKanFlytte && !rodKanFlytte) {
            return "Svart spiller vinner, fordi Rød har ikke flere trekk!";
        } else if(!rodKanFlytte && !svartKanFlytte) {
            return "Ingen har flere mulige trekk! Uavgjort!?";
        }

        //Hvis det ikke blir returnert en verdi, er ikke spillet ferdig ennå
        return null;
    }


    /** Endrer tur på for spillerene*/
    public void sluttTur() {
        if(naaverendeSpiller == Color.BLACK) {
            naaverendeSpiller = Color.RED;
        }
        else {
            naaverendeSpiller = Color.BLACK;
        }
    }

    /** Avslutter spillet og starter ett nytt */
    public void restartSpill() {

        vindu.setVisible(false);
        valgtRute = null;

        vindu.remove(brettPanel);
        brettPanel = new JPanel(new GridLayout(8, 8));
        brettPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        brett = new Brett();
        brett.plasserBrikkerForStart();

        leggBrettTilPanel(brett, brettPanel);
        vindu.add(brettPanel, 0);

        RodeBrikkerSomErIgjen = 12;
        svarteBrikkerSomErIgjen = 12;

        naaverendeSpiller = Color.BLACK;

        oppdaterStatus();
        vindu.pack();
        vindu.setVisible(true);

    }

}