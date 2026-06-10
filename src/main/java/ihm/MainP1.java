package ihm;

import modele.Panier;
import modele.LigneCommande;
import modele.Fromages;
import modele.Article;
import modele.OutilsBaseDonneesFromages;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;
import fr.iut.annotations.Responsable;

public class MainP1 {

    @Responsable("Zeineb ESSEGHAIER")
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Responsable("Zaynab BOULIFA")
            public void run() {
                Fromages base = OutilsBaseDonneesFromages.générationBaseDeFromages(
                        OutilsBaseDonneesFromages.CHEMIN_FICHIER);
                Panier panier = new Panier();

                JFrame fenêtre = new JFrame("Test P1 - Fromages");
                fenêtre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                PanelFromages panelFromages = new PanelFromages(base, panier);
                
                JPanel panelPrincipal = new JPanel(new BorderLayout());
                panelPrincipal.add(panelFromages, BorderLayout.CENTER);
                
                JPanel panelHaut = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                panelHaut.setBackground(PanelFromages.COULEUR_FOND);
                
                JButton btnGererPanier = new JButton("🛒 Gestion du panier (0.00 €)");
                btnGererPanier.setBackground(PanelFromages.COULEUR_MARRON);
                btnGererPanier.setForeground(Color.WHITE);
                btnGererPanier.setFont(new Font("Serif", Font.BOLD, 14));
                btnGererPanier.setFocusPainted(false);

                // Affiche en live le totaux du panier
                panelFromages.setBoutonPanier(btnGererPanier);
                
                
                btnGererPanier.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        FenetrePanier fenetrePanier = new FenetrePanier(fenêtre, panier, panelFromages, btnGererPanier);
                        fenetrePanier.setVisible(true);
                    }
                });
                
                panelHaut.add(btnGererPanier);
                panelPrincipal.add(panelHaut, BorderLayout.NORTH);
                
                fenêtre.setContentPane(panelPrincipal);
                fenêtre.setSize(1100, 700);
                fenêtre.setLocationRelativeTo(null);
                fenêtre.setVisible(true);
            }
        });
    }
}