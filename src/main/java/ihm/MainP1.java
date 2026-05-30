package ihm;
import modele.Panier;
import modele.LigneCommande;
import modele.Fromages;
import modele.Article;
import modele.OutilsBaseDonneesFromages;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
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
            @Responsable("Zeineb ESSEGHAIER")
            public void run() {
                Fromages base = OutilsBaseDonneesFromages.générationBaseDeFromages(
                        OutilsBaseDonneesFromages.CHEMIN_FICHIER);
                Panier panier = new Panier();

                JFrame fenêtre = new JFrame("Test P1 - Fromages");
                fenêtre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                PanelFromages panelFromages = new PanelFromages(base, panier);
                fenêtre.getContentPane().add(panelFromages);
                
                JPanel panelPanier = new JPanel();
                panelPanier.setBackground(PanelFromages.COULEUR_FOND);
                panelFromages.add(panelPanier, BorderLayout.NORTH);
                panelPanier.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
                
                JButton btnGererPanier = new JButton("🛒 Gérer le Panier");
                btnGererPanier.setHorizontalAlignment(SwingConstants.RIGHT);
                
                // Style du bouton s'accordant au reste du catalogue
                btnGererPanier.setBackground(PanelFromages.COULEUR_MARRON);
                btnGererPanier.setForeground(Color.WHITE);
                btnGererPanier.setFocusPainted(false);
                
                
                
 //     @Responsable BOULIFA Zaynab
                panelPanier.add(btnGererPanier);
                
                btnGererPanier.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        final javax.swing.JDialog fenetrePanier = new javax.swing.JDialog();
                        fenetrePanier.setTitle("Mon Panier");
                        fenetrePanier.setSize(550, 480);
                        fenetrePanier.setLocationRelativeTo(null);
                        fenetrePanier.setModal(true);

                        JPanel conteneur = new JPanel(new BorderLayout(10, 10));
                        conteneur.setBackground(PanelFromages.COULEUR_FOND);
                        conteneur.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

                        String[] colonnes = {"Article", "Quantité", "Prix TTC"};
                        final javax.swing.table.DefaultTableModel modeleTable = new javax.swing.table.DefaultTableModel(colonnes, 0) {
                            @Override
                            public boolean isCellEditable(int row, int column) { return false; }
                        };

                        final javax.swing.JTable table = new javax.swing.JTable(modeleTable);
                        table.setRowHeight(35);
                        
                        // Utilisation stricte des couleurs de l'application pour la JTable
                        table.setBackground(PanelFromages.COULEUR_LISTE);
                        table.setForeground(PanelFromages.COULEUR_TEXTE);
                        table.setSelectionBackground(PanelFromages.COULEUR_MARRON_CLAIR);
                        table.setSelectionForeground(Color.WHITE);
                        table.setShowVerticalLines(false);
                        table.setGridColor(PanelFromages.COULEUR_MARRON_CLAIR);
                        
                        // En-tête de la table harmonisé
                        table.getTableHeader().setBackground(PanelFromages.COULEUR_MARRON);
                        table.getTableHeader().setForeground(Color.WHITE);
                        table.getTableHeader().setBorder(BorderFactory.createEmptyBorder());

                        for (modele.LigneCommande ligne : panier.getLignesCommande()) {
                            Object[] ligneDonnees = {
                                ligne.getArticle().getFromage().getDésignation(),
                                ligne.getQuantité(),
                                String.format("%.2f €", ligne.getPrixTotalTTC())
                            };
                            modeleTable.addRow(ligneDonnees);
                        }

                        JPanel panelBas = new JPanel(new java.awt.GridLayout(0, 1, 5, 5));
                        panelBas.setBackground(PanelFromages.COULEUR_FOND);

                        final JLabel lblTotal = new JLabel(" TOTAL COMMANDE : " + String.format("%.2f €", panier.calculerTotalTTC()));
                        lblTotal.setForeground(PanelFromages.COULEUR_MARRON);
                        lblTotal.setHorizontalAlignment(SwingConstants.CENTER);
                        panelBas.add(lblTotal);

                        JPanel panelControles = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
                        panelControles.setBackground(PanelFromages.COULEUR_FOND);
                        
                        JButton btnMoins = new JButton("-");
                        btnMoins.setBackground(PanelFromages.COULEUR_MARRON_CLAIR);
                        btnMoins.setForeground(Color.WHITE);
                        btnMoins.setFocusPainted(false);
                        
                        btnMoins.addActionListener(event -> {
                            int rowSelectionnee = table.getSelectedRow();
                            if (rowSelectionnee != -1) {
                                LigneCommande ligne = panier.getLignesCommande().get(rowSelectionnee);
                                Article article = ligne.getArticle();
                                
                                if (ligne.getQuantité() > 1) {
                                    ligne.setQuantité(ligne.getQuantité() - 1);
                                    article.setQuantitéEnStock(article.getQuantitéEnStock() + 1); 
                                    
                                    modeleTable.setValueAt(ligne.getQuantité(), rowSelectionnee, 1);
                                    modeleTable.setValueAt(String.format("%.2f €", ligne.getPrixTotalTTC()), rowSelectionnee, 2);
                                    lblTotal.setText(" TOTAL COMMANDE : " + String.format("%.2f €", panier.calculerTotalTTC()));
                                    
                                    panelFromages.actualiserStockLabel(article);
                                }
                            }
                        });

                        JButton btnPlus = new JButton("+");
                        btnPlus.setBackground(PanelFromages.COULEUR_MARRON_CLAIR);
                        btnPlus.setForeground(Color.WHITE);
                        btnPlus.setFocusPainted(false);
                        
                        btnPlus.addActionListener(event -> {
                            int rowSelectionnee = table.getSelectedRow();
                            if (rowSelectionnee != -1) {
                                LigneCommande ligne = panier.getLignesCommande().get(rowSelectionnee);
                                Article article = ligne.getArticle();
                                
                                if (article.getQuantitéEnStock() > 0) { 
                                    ligne.setQuantité(ligne.getQuantité() + 1); 
                                    article.setQuantitéEnStock(article.getQuantitéEnStock() - 1); 
                                    
                                    modeleTable.setValueAt(ligne.getQuantité(), rowSelectionnee, 1);
                                    modeleTable.setValueAt(String.format("%.2f €", ligne.getPrixTotalTTC()), rowSelectionnee, 2);
                                    lblTotal.setText(" TOTAL COMMANDE : " + String.format("%.2f €", panier.calculerTotalTTC()));
                                    
                                    panelFromages.actualiserStockLabel(article);
                                } else {
                                    javax.swing.JOptionPane.showMessageDialog(fenetrePanier, "Plus de stock disponible pour cet article !");
                                }
                            }
                        });

                        JLabel lblModif = new JLabel("Modifier la quantité (sélectionnez un article) : ");
                        lblModif.setForeground(PanelFromages.COULEUR_TEXTE);
                        
                        panelControles.add(lblModif);
                        panelControles.add(btnMoins);
                        panelControles.add(btnPlus);
                        panelBas.add(panelControles);
                       
                        JButton btnVider = new JButton("🗑 Tout enlever");
                        btnVider.setBackground(PanelFromages.COULEUR_FOND);
                        btnVider.setForeground(PanelFromages.COULEUR_MARRON);
                        btnVider.setFocusPainted(false);
                        btnVider.setBorder(BorderFactory.createLineBorder(PanelFromages.COULEUR_MARRON, 1));
                        
                        btnVider.addActionListener(e2 -> {
                            int reponse = javax.swing.JOptionPane.showConfirmDialog(fenetrePanier, "Voulez-vous vraiment vider tout votre panier ?");
                            if (reponse == javax.swing.JOptionPane.YES_OPTION) {
                                for(LigneCommande l : panier.getLignesCommande()){
                                    Article a = l.getArticle();
                                    a.setQuantitéEnStock(a.getQuantitéEnStock() + l.getQuantité());
                                    panelFromages.actualiserStockLabel(a);
                                }
                                panier.getLignesCommande().clear();
                                modeleTable.setRowCount(0);
                                lblTotal.setText(" TOTAL COMMANDE : 0.00 €");
                            }
                        });
                        
                        JPanel panelVider = new JPanel();
                        panelVider.setBackground(PanelFromages.COULEUR_FOND);
                        panelVider.add(btnVider);
                        panelBas.add(panelVider);
                        
                        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(table);
                        scrollPane.setBorder(BorderFactory.createLineBorder(PanelFromages.COULEUR_MARRON_CLAIR, 1));
                        scrollPane.getViewport().setBackground(PanelFromages.COULEUR_LISTE);
                        
                        conteneur.add(scrollPane, BorderLayout.CENTER);
                        conteneur.add(panelBas, BorderLayout.SOUTH);

                        fenetrePanier.setContentPane(conteneur);
                        fenetrePanier.setVisible(true);
                    }
                });
                
                fenêtre.setSize(1100, 700);
                fenêtre.setLocationRelativeTo(null);
                fenêtre.setVisible(true);
            }
        });
    }
}