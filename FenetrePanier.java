package ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import fr.iut.annotations.Responsable;
import modele.Article;
import modele.LigneCommande;
import modele.Panier;

public class FenetrePanier extends JDialog {

    private static final long serialVersionUID = 1L;
    
    // Composants du panier
    private JTable table;
    private DefaultTableModel modeleTable;
    private JLabel lblTotal;
    private JComboBox<String> comboPort;
    private JLabel lblTotalTransport;
    private JButton btnMoins;
    private JButton btnPlus;
    private JButton btnVider;
    private JButton btnContinuer;

    // Références pour les rafraîchissements
    private Panier panier;
    private PanelFromages panelFromages;
    private JButton btnPanierMain;

    @Responsable("Zaynab BOULIFA")
    public FenetrePanier(JFrame parent, Panier panier, PanelFromages panelFromages, JButton btnPanierMain) {
        super(parent, "Mon Panier", true); // Fenêtre modale
        
        this.panier = panier;
        this.panelFromages = panelFromages;
        this.btnPanierMain = btnPanierMain;
        
        setSize(600, 550);
        setLocationRelativeTo(parent);

        // Conteneur principal
        JPanel conteneur = new JPanel(new BorderLayout(10, 10));
        conteneur.setBackground(PanelFromages.COULEUR_FOND);
        conteneur.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setContentPane(conteneur);

        // Configuration de la table (non éditable)
        String[] colonnes = {"Article", "Quantité", "Prix TTC"};
        modeleTable = new DefaultTableModel(colonnes, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        // Style de la table
        table = new JTable(modeleTable);
        table.setRowHeight(35);
        table.setBackground(PanelFromages.COULEUR_LISTE);
        table.setForeground(PanelFromages.COULEUR_TEXTE);
        table.setFont(new Font("Serif", Font.PLAIN, 15));
        table.setSelectionBackground(PanelFromages.COULEUR_MARRON_CLAIR);
        table.setSelectionForeground(Color.WHITE);
        table.setShowVerticalLines(false);
        table.setGridColor(PanelFromages.COULEUR_MARRON_CLAIR);
        
        table.getTableHeader().setBackground(PanelFromages.COULEUR_MARRON);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Serif", Font.BOLD, 14));

        // Remplissage initial de la table
        for (LigneCommande ligne : panier.getLignesCommande()) {
            Object[] ligneDonnees = {
                ligne.getArticle().getFromage().getDésignation(),
                ligne.getQuantité(),
                String.format("%.2f €", ligne.getPrixTotalTTC())
            };
            modeleTable.addRow(ligneDonnees);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(PanelFromages.COULEUR_MARRON_CLAIR, 1));
        scrollPane.getViewport().setBackground(PanelFromages.COULEUR_LISTE);
        conteneur.add(scrollPane, BorderLayout.CENTER);

        // Zone du bas (Totaux et commandes)
        JPanel panelBas = new JPanel(new GridLayout(0, 1, 5, 5));
        panelBas.setBackground(PanelFromages.COULEUR_FOND);
        conteneur.add(panelBas, BorderLayout.SOUTH);

        // Label Sous-Total
        lblTotal = new JLabel();
        lblTotal.setFont(new Font("Serif", Font.PLAIN, 15));
        lblTotal.setForeground(PanelFromages.COULEUR_MARRON);
        lblTotal.setHorizontalAlignment(SwingConstants.CENTER);
        panelBas.add(lblTotal);

        // Label Promo
        JLabel lblPromo = new JLabel("✨ Frais de port offerts à partir de 120 € d'achats !");
        lblPromo.setFont(new Font("Serif", Font.ITALIC, 14));
        lblPromo.setForeground(PanelFromages.COULEUR_MARRON_CLAIR);
        lblPromo.setHorizontalAlignment(SwingConstants.CENTER);
        panelBas.add(lblPromo);

        // Mode d'expédition
        JPanel panelTransport = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelTransport.setBackground(PanelFromages.COULEUR_FOND);
        JLabel lblExpedition = new JLabel("Expédition : ");
        lblExpedition.setFont(new Font("Serif", Font.ITALIC, 14));
        lblExpedition.setForeground(PanelFromages.COULEUR_TEXTE);
        panelTransport.add(lblExpedition);
        
        String[] optionsPort = {"Colissimo (4,90 €)", "Chrono Relais (5,90 €)", "ChronoFresh (9,90 €)"};
        comboPort = new JComboBox<>(optionsPort);
        comboPort.setFont(new Font("Serif", Font.PLAIN, 14));
        panelTransport.add(comboPort);
        panelBas.add(panelTransport);

        // Label Grand Total
        lblTotalTransport = new JLabel();
        lblTotalTransport.setFont(new Font("Serif", Font.BOLD, 18));
        lblTotalTransport.setForeground(PanelFromages.COULEUR_MARRON);
        lblTotalTransport.setHorizontalAlignment(SwingConstants.CENTER);
        panelBas.add(lblTotalTransport);

        // Premier calcul au lancement
        actualiserTousLesTotaux();
        comboPort.addActionListener(e -> actualiserTousLesTotaux());

        // Boutons de modification des quantités
        JPanel panelControles = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        panelControles.setBackground(PanelFromages.COULEUR_FOND);
        JLabel lblModif = new JLabel("Modifier la quantité : ");
        lblModif.setFont(new Font("Serif", Font.ITALIC, 14));
        lblModif.setForeground(PanelFromages.COULEUR_TEXTE);
        panelControles.add(lblModif);

        // Action du bouton Moins (-)
        btnMoins = new JButton("-");
        btnMoins.setBackground(PanelFromages.COULEUR_MARRON_CLAIR);
        btnMoins.setForeground(Color.WHITE);
        btnMoins.setFont(new Font("Serif", Font.BOLD, 16));
        btnMoins.addActionListener(event -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                LigneCommande ligne = panier.getLignesCommande().get(row);
                Article article = ligne.getArticle();
                if (ligne.getQuantité() > 1) {
                    ligne.setQuantité(ligne.getQuantité() - 1);
                    article.setQuantitéEnStock(article.getQuantitéEnStock() + 1);
                    modeleTable.setValueAt(ligne.getQuantité(), row, 1);
                    modeleTable.setValueAt(String.format("%.2f €", ligne.getPrixTotalTTC()), row, 2);
                    panelFromages.actualiserStockLabel(article);
                    actualiserTousLesTotaux(); 
                }
            }
        });
        panelControles.add(btnMoins);

        // Action du bouton Plus (+)
        btnPlus = new JButton("+");
        btnPlus.setBackground(PanelFromages.COULEUR_MARRON_CLAIR);
        btnPlus.setForeground(Color.WHITE);
        btnPlus.setFont(new Font("Serif", Font.BOLD, 16));
        btnPlus.addActionListener(event -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                LigneCommande ligne = panier.getLignesCommande().get(row);
                Article article = ligne.getArticle();
                if (article.getQuantitéEnStock() > 0) { 
                    ligne.setQuantité(ligne.getQuantité() + 1); 
                    article.setQuantitéEnStock(article.getQuantitéEnStock() - 1);
                    modeleTable.setValueAt(ligne.getQuantité(), row, 1);
                    modeleTable.setValueAt(String.format("%.2f €", ligne.getPrixTotalTTC()), row, 2);
                    panelFromages.actualiserStockLabel(article);
                    actualiserTousLesTotaux(); 
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "Plus de stock disponible !");
                }
            }
        });
        panelControles.add(btnPlus);
        panelBas.add(panelControles);

        // Action du bouton Vider avec la JDialog de confirmation propre
        btnVider = new JButton("🗑 Tout enlever");
        btnVider.setBackground(new Color(101, 67, 33));
        btnVider.setForeground(new Color(255, 255, 255));
        btnVider.setBorder(BorderFactory.createLineBorder(PanelFromages.COULEUR_MARRON, 1));
        btnVider.setMargin(new java.awt.Insets(2, 25, 2, 14)); 

        btnVider.addActionListener(e -> {
            if (!panier.getLignesCommande().isEmpty()) {
                FenetreConfirmation diag = new FenetreConfirmation(this);
                diag.setVisible(true);
                
                if (diag.isConfirme()) {
                    for (LigneCommande l : panier.getLignesCommande()) {
                        Article a = l.getArticle();
                        a.setQuantitéEnStock(a.getQuantitéEnStock() + l.getQuantité());
                        panelFromages.actualiserStockLabel(a);
                    }
                    panier.getLignesCommande().clear();
                    modeleTable.setRowCount(0);
                    actualiserTousLesTotaux();
                }
            }
        });

        // Action du bouton Continuer
        btnContinuer = new JButton("Continuer mes achats");
        btnContinuer.setBackground(PanelFromages.COULEUR_MARRON);
        btnContinuer.setForeground(Color.WHITE);
        btnContinuer.setFont(new Font("Serif", Font.BOLD, 13));
        btnContinuer.setMargin(new java.awt.Insets(2, 14, 2, 25));
        btnContinuer.addActionListener(e -> setVisible(false));
        
        JPanel panelAction = new JPanel();
        panelAction.setBackground(PanelFromages.COULEUR_FOND);
        panelAction.setLayout(new BorderLayout(0, 0));
        panelAction.add(btnVider, BorderLayout.WEST);
        panelAction.add(btnContinuer, BorderLayout.EAST);
        panelBas.add(panelAction);
        
        JButton ValiderCmd = new JButton("Valider la commande");
        ValiderCmd.setForeground(new Color(255, 255, 255));
        ValiderCmd.setBackground(new Color(101, 67, 33));
        panelAction.add(ValiderCmd, BorderLayout.CENTER);
    }


    public void actualiserTousLesTotaux() {
        double total = panier.calculerTotalTTC();
        lblTotal.setText(" Total fromages : " + String.format("%.2f €", total));
        
        double frais = 0.0;
        if (total > 0 && total < 120.0) {
            int index = comboPort.getSelectedIndex();
            if (index == 0) frais = 4.90;
            else if (index == 1) frais = 5.90;
            else if (index == 2) frais = 9.90;
        }
        
        double totalFinal = (total >= 120.0 || total == 0) ? total : total + frais;
        lblTotalTransport.setText(" TOTAL AVEC TRANSPORT : " + String.format("%.2f €", totalFinal));
        
        // On met à jour le bouton d'IHM principale passé en paramètre
        if (btnPanierMain != null) {
            btnPanierMain.setText("🛒 Gestion du panier (" + String.format("%.2f €", total) + ")");
            btnPanierMain.repaint();
        }
        
    }
}