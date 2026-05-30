package ihm;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import fr.iut.annotations.Responsable;
import modele.Fromages;
import modele.OutilsBaseDonneesFromages;
import modele.Panier;

/**
 * Point d'entrée pour tester le panel P1 de façon autonome.
 */
public class MainP1 {

    @Responsable("Zeineb ESSEGHAIER")
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Responsable("Zeineb ESSEGHAIER")
            public void run() {
                Fromages base = OutilsBaseDonneesFromages.générationBaseDeFromages(
                        OutilsBaseDonneesFromages.CHEMIN_FICHIER);
                Panier panier = new Panier();

                JFrame fenêtre = new JFrame("Test P1 - Fromages");
                fenêtre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                fenêtre.add(new PanelFromages(base, panier));
                fenêtre.setSize(1100, 700);
                fenêtre.setLocationRelativeTo(null);
                fenêtre.setVisible(true);
            }
        });
    }
}