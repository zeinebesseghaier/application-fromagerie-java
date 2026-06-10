package ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fr.iut.annotations.Responsable;
import modele.Article;
import modele.Fromage;
import modele.Fromages;
import modele.Panier;
import modele.TypeLait;

/**
 * Panel P1 : liste des fromages avec filtre par type de lait,
 * affichage du détail et ajout au panier.
 */
public class PanelFromages extends JPanel {

    private static final long serialVersionUID = 1L;

    // Palette de couleurs
    public static final Color COULEUR_FOND        = new Color(250, 245, 235);
    public static final Color COULEUR_MARRON       = new Color(101, 67, 33);
    public static final Color COULEUR_MARRON_CLAIR = new Color(180, 130, 70);
    public static final Color COULEUR_LISTE        = new Color(255, 252, 245);
    public static final Color COULEUR_TEXTE        = new Color(60, 40, 20);

    private Fromages base;
    public Panier panier;

    // --- Zone gauche ---
    private DefaultListModel<Fromage> modèleListe;
    private JList<Fromage> listFromages;
    private JRadioButton rbTous;
    private JRadioButton rbVache;
    private JRadioButton rbChèvre;
    private JRadioButton rbBrebis;

    // --- Zone droite ---
    private JLabel lblNomFromage;
    private JLabel lblImage;
    private JTextArea txtDescription;
    private JLabel lblArticle;
    private JLabel lblPrix;
    private JLabel lblStock;
    private JSpinner spinnerQuantité;
    private JButton btnAjouterPanier;
    
    // --- NOUVEAUTÉ : Référence au bouton du panier principal ---
    private JButton btnPanierMain;

    // Article courant affiché
    private List<Article> articlesFromageCourant;
    private int indexArticle;

    /**
     * Construit le panel P1.
     *
     * @param base   la base de données fromages
     * @param panier le panier courant
     */
    @Responsable("Zeineb ESSEGHAIER")
    public PanelFromages(Fromages base, Panier panier) {
        this.base = base;
        this.panier = panier;
        initialiserComposants();
        chargerListe(base.getFromages());
    }
    
    @Responsable("Zeineb ESSEGHAIER")
    public void setBoutonPanier(JButton btn) {
        this.btnPanierMain = btn;
    }

    @Responsable("Zeineb ESSEGHAIER")
    private void initialiserComposants() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(COULEUR_FOND);
        this.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ========== ZONE GAUCHE ==========
        JPanel panelGauche = new JPanel(new BorderLayout(5, 10));
        panelGauche.setPreferredSize(new Dimension(320, 0));
        panelGauche.setBackground(COULEUR_FOND);

        // Filtres
        JPanel panelFiltres = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panelFiltres.setBackground(COULEUR_FOND);
        panelFiltres.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COULEUR_MARRON_CLAIR, 1),
                "Filtrer par lait",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 12),
                COULEUR_MARRON));

        this.rbTous   = créerRadioButton("Tous", true);
        this.rbVache  = créerRadioButton("Vache", false);
        this.rbChèvre = créerRadioButton("Chèvre", false);
        this.rbBrebis = créerRadioButton("Brebis", false);

        ButtonGroup groupe = new ButtonGroup();
        groupe.add(this.rbTous);
        groupe.add(this.rbVache);
        groupe.add(this.rbChèvre);
        groupe.add(this.rbBrebis);

        panelFiltres.add(this.rbTous);
        panelFiltres.add(this.rbVache);
        panelFiltres.add(this.rbChèvre);
        panelFiltres.add(this.rbBrebis);

        // Liste
        this.modèleListe  = new DefaultListModel<Fromage>();
        this.listFromages = new JList<Fromage>(this.modèleListe);
        this.listFromages.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.listFromages.setBackground(COULEUR_LISTE);
        this.listFromages.setForeground(COULEUR_TEXTE);
        this.listFromages.setFont(new Font("SansSerif", Font.PLAIN, 13));
        this.listFromages.setFixedCellHeight(28);
        this.listFromages.setSelectionBackground(COULEUR_MARRON);
        this.listFromages.setSelectionForeground(Color.WHITE);
        this.listFromages.setCellRenderer(new javax.swing.DefaultListCellRenderer() {
            @Responsable("Zeineb ESSEGHAIER")
            public java.awt.Component getListCellRendererComponent(
                    javax.swing.JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                if (value instanceof Fromage) {
                    Fromage f = (Fromage) value;
                    
                    // Calcul du stock total pour ce fromage
                    int stockTotal = f.getArticles().stream().mapToInt(Article::getQuantitéEnStock).sum();
                    
                    // Affichage simple et direct
                    if (stockTotal <= 0) {
                        setText(f.getDésignation() + " - rupture stock");
                        setForeground(Color.RED); // Met tout en rouge si rupture
                    } else {
                        setText(f.getDésignation() + " - en stock");
                        // Rétablit la couleur par défaut (noir ou celle de la sélection)
                        setForeground(isSelected ? Color.GREEN : COULEUR_TEXTE);
                    }
                }
                return this;
            }
        });

        JScrollPane scrollListe = new JScrollPane(this.listFromages);
        scrollListe.setBorder(BorderFactory.createLineBorder(COULEUR_MARRON_CLAIR, 1));

        panelGauche.add(panelFiltres, BorderLayout.NORTH);
        panelGauche.add(scrollListe,  BorderLayout.CENTER);

        // ========== ZONE DROITE ==========
        JPanel panelDétail = new JPanel(new BorderLayout(10, 10));
        panelDétail.setBackground(COULEUR_FOND);
        panelDétail.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COULEUR_MARRON_CLAIR, 1),
                "Détail du fromage",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 12),
                COULEUR_MARRON));

        // Nom du fromage
        this.lblNomFromage = new JLabel("Sélectionnez un fromage", JLabel.CENTER);
        this.lblNomFromage.setFont(new Font("Serif", Font.BOLD, 20));
        this.lblNomFromage.setForeground(COULEUR_MARRON);
        this.lblNomFromage.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));

        // Image
        this.lblImage = new JLabel();
        this.lblImage.setPreferredSize(new Dimension(210, 210));
        this.lblImage.setHorizontalAlignment(JLabel.CENTER);
        this.lblImage.setBorder(BorderFactory.createLineBorder(COULEUR_MARRON_CLAIR, 1));
        this.lblImage.setBackground(Color.WHITE);
        this.lblImage.setOpaque(true);

        // Description
        this.txtDescription = new JTextArea(7, 25);
        this.txtDescription.setEditable(false);
        this.txtDescription.setLineWrap(true);
        this.txtDescription.setWrapStyleWord(true);
        this.txtDescription.setFont(new Font("SansSerif", Font.ITALIC, 13));
        this.txtDescription.setForeground(COULEUR_TEXTE);
        this.txtDescription.setBackground(new Color(255, 252, 240));
        this.txtDescription.setMargin(new Insets(8, 8, 8, 8));
        JScrollPane scrollDesc = new JScrollPane(this.txtDescription);
        scrollDesc.setBorder(BorderFactory.createLineBorder(COULEUR_MARRON_CLAIR, 1));

        JPanel panelInfos = new JPanel(new BorderLayout(10, 0));
        panelInfos.setBackground(COULEUR_FOND);
        panelInfos.add(this.lblImage, BorderLayout.WEST);
        panelInfos.add(scrollDesc,    BorderLayout.CENTER);

        // ========== ZONE COMMANDER ==========
        JPanel panelArticle = new JPanel(new GridBagLayout());
        panelArticle.setBackground(COULEUR_FOND);
        panelArticle.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COULEUR_MARRON_CLAIR, 1),
                "Commander",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 12),
                COULEUR_MARRON));

        Font fontLabel = new Font("SansSerif", Font.BOLD, 13);
        Font fontValeur = new Font("SansSerif", Font.PLAIN, 13);

        this.lblArticle = new JLabel("-");
        this.lblArticle.setFont(fontValeur);
        this.lblArticle.setForeground(COULEUR_TEXTE);

        this.lblPrix = new JLabel("-");
        this.lblPrix.setFont(new Font("SansSerif", Font.BOLD, 14));
        this.lblPrix.setForeground(COULEUR_MARRON);

        this.lblStock = new JLabel("-");
        this.lblStock.setFont(fontValeur);
        this.lblStock.setForeground(COULEUR_TEXTE);

        this.spinnerQuantité = new JSpinner(new SpinnerNumberModel(1, 1, 1, 1));
        this.spinnerQuantité.setFont(fontValeur);
        this.spinnerQuantité.setPreferredSize(new Dimension(70, 30));

        this.btnAjouterPanier = new JButton("🛒  Ajouter au panier");
        this.btnAjouterPanier.setEnabled(false);
        this.btnAjouterPanier.setBackground(COULEUR_MARRON);
        this.btnAjouterPanier.setForeground(Color.WHITE);
        this.btnAjouterPanier.setFont(new Font("SansSerif", Font.BOLD, 13));
        this.btnAjouterPanier.setFocusPainted(false);
        this.btnAjouterPanier.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        // Ligne Article
        GridBagConstraints gbcL0 = new GridBagConstraints();
        gbcL0.insets = new Insets(5, 10, 5, 5);
        gbcL0.anchor = GridBagConstraints.WEST;
        gbcL0.gridy = 0; gbcL0.gridx = 0; gbcL0.gridwidth = 1;
        JLabel lArticle = new JLabel("Article :");
        lArticle.setFont(fontLabel);
        lArticle.setForeground(COULEUR_MARRON);
        panelArticle.add(lArticle, gbcL0);
        GridBagConstraints gbcV0 = new GridBagConstraints();
        gbcV0.insets = new Insets(5, 5, 5, 10);
        gbcV0.anchor = GridBagConstraints.WEST;
        gbcV0.gridy = 0; gbcV0.gridx = 1; gbcV0.gridwidth = 1;
        panelArticle.add(this.lblArticle, gbcV0);

        // Ligne Prix
        GridBagConstraints gbcL1 = new GridBagConstraints();
        gbcL1.insets = new Insets(5, 10, 5, 5);
        gbcL1.anchor = GridBagConstraints.WEST;
        gbcL1.gridy = 1; gbcL1.gridx = 0; gbcL1.gridwidth = 1;
        JLabel lPrix = new JLabel("Prix TTC :");
        lPrix.setFont(fontLabel);
        lPrix.setForeground(COULEUR_MARRON);
        panelArticle.add(lPrix, gbcL1);
        GridBagConstraints gbcV1 = new GridBagConstraints();
        gbcV1.insets = new Insets(5, 5, 5, 10);
        gbcV1.anchor = GridBagConstraints.WEST;
        gbcV1.gridy = 1; gbcV1.gridx = 1; gbcV1.gridwidth = 1;
        panelArticle.add(this.lblPrix, gbcV1);

        // Ligne Stock
        GridBagConstraints gbcL2 = new GridBagConstraints();
        gbcL2.insets = new Insets(5, 10, 5, 5);
        gbcL2.anchor = GridBagConstraints.WEST;
        gbcL2.gridy = 2; gbcL2.gridx = 0; gbcL2.gridwidth = 1;
        JLabel lStock = new JLabel("Stock :");
        lStock.setFont(fontLabel);
        lStock.setForeground(COULEUR_MARRON);
        panelArticle.add(lStock, gbcL2);
        GridBagConstraints gbcV2 = new GridBagConstraints();
        gbcV2.insets = new Insets(5, 5, 5, 10);
        gbcV2.anchor = GridBagConstraints.WEST;
        gbcV2.gridy = 2; gbcV2.gridx = 1; gbcV2.gridwidth = 1;
        panelArticle.add(this.lblStock, gbcV2);

        // Ligne Quantité
        GridBagConstraints gbcL3 = new GridBagConstraints();
        gbcL3.insets = new Insets(5, 10, 5, 5);
        gbcL3.anchor = GridBagConstraints.WEST;
        gbcL3.gridy = 3; gbcL3.gridx = 0; gbcL3.gridwidth = 1;
        JLabel lQte = new JLabel("Quantité :");
        lQte.setFont(fontLabel);
        lQte.setForeground(COULEUR_MARRON);
        panelArticle.add(lQte, gbcL3);
        GridBagConstraints gbcV3 = new GridBagConstraints();
        gbcV3.insets = new Insets(5, 5, 5, 10);
        gbcV3.anchor = GridBagConstraints.WEST;
        gbcV3.gridy = 3; gbcV3.gridx = 1; gbcV3.gridwidth = 1;
        panelArticle.add(this.spinnerQuantité, gbcV3);

        // Bouton
        GridBagConstraints gbcBtn = new GridBagConstraints();
        gbcBtn.insets = new Insets(10, 10, 10, 10);
        gbcBtn.anchor = GridBagConstraints.CENTER;
        gbcBtn.gridy = 4; gbcBtn.gridx = 0; gbcBtn.gridwidth = 2;
        panelArticle.add(this.btnAjouterPanier, gbcBtn);

        panelDétail.add(this.lblNomFromage, BorderLayout.NORTH);
        panelDétail.add(panelInfos,         BorderLayout.CENTER);
        panelDétail.add(panelArticle,       BorderLayout.SOUTH);

        this.add(panelGauche, BorderLayout.WEST);
        this.add(panelDétail, BorderLayout.CENTER);

        ajouterÉcouteurs();
    }

    /**
     * Crée un bouton radio avec le style de l'application.
     */
    @Responsable("Zeineb ESSEGHAIER")
    private JRadioButton créerRadioButton(String texte, boolean sélectionné) {
        JRadioButton rb = new JRadioButton(texte, sélectionné);
        rb.setBackground(COULEUR_FOND);
        rb.setForeground(COULEUR_TEXTE);
        rb.setFont(new Font("SansSerif", Font.PLAIN, 13));
        rb.setFocusPainted(false);
        return rb;
    }

    @Responsable("Zeineb ESSEGHAIER")
    private void ajouterÉcouteurs() {

        this.rbTous.addActionListener(new ActionListener() {
            @Responsable("Zeineb ESSEGHAIER")
            public void actionPerformed(ActionEvent e) {
                chargerListe(base.getFromages());
            }
        });

        this.rbVache.addActionListener(new ActionListener() {
            @Responsable("Zeineb ESSEGHAIER")
            public void actionPerformed(ActionEvent e) {
                chargerListe(base.fromagesAuLaitDe(TypeLait.VACHE));
            }
        });

        this.rbChèvre.addActionListener(new ActionListener() {
            @Responsable("Zeineb ESSEGHAIER")
            public void actionPerformed(ActionEvent e) {
                chargerListe(base.fromagesAuLaitDe(TypeLait.CHEVRE));
            }
        });

        this.rbBrebis.addActionListener(new ActionListener() {
            @Responsable("Zeineb ESSEGHAIER")
            public void actionPerformed(ActionEvent e) {
                chargerListe(base.fromagesAuLaitDe(TypeLait.BREBIS));
            }
        });

        this.listFromages.addListSelectionListener(new ListSelectionListener() {
            @Responsable("Zeineb ESSEGHAIER")
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    Fromage f = listFromages.getSelectedValue();
                    if (f != null) {
                        afficherDétail(f);
                    }
                }
            }
        });

        this.btnAjouterPanier.addActionListener(new ActionListener() {
            @Responsable("Zeineb ESSEGHAIER")
            public void actionPerformed(ActionEvent e) {
                ajouterAuPanier();
            }
        });
    }

    @Responsable("Zeineb ESSEGHAIER")
    private void chargerListe(List<Fromage> fromages) {
        this.modèleListe.clear();
        for (Fromage f : fromages) {
            this.modèleListe.addElement(f);
        }
        viderDétail();
    }

    @Responsable("Zeineb ESSEGHAIER")
    private void afficherDétail(Fromage fromage) {
        this.lblNomFromage.setText(fromage.getDésignation());
        this.txtDescription.setText(fromage.getDescription());
        this.txtDescription.setCaretPosition(0);
        chargerImage(fromage.getNomImage());
        this.articlesFromageCourant = fromage.getArticles();
        this.indexArticle = 0;
        if (this.articlesFromageCourant != null
                && !this.articlesFromageCourant.isEmpty()) {
            afficherArticle(this.articlesFromageCourant.get(0));
        }
    }

    @Responsable("Zeineb ESSEGHAIER & BOULIFA Zaynab")
    private void afficherArticle(Article article) {
        this.lblArticle.setText(article.toStringIHM());
        this.lblPrix.setText(String.format("%.2f €", article.getPrixTTC()));
        
        int stock = article.getQuantitéEnStock();
        
        // --- Vérification immédiate du stock ---
        if (stock > 0) {
            this.lblStock.setText(String.valueOf(stock));
            this.spinnerQuantité.setModel(new SpinnerNumberModel(1, 1, stock, 1));
            this.btnAjouterPanier.setEnabled(true);
            this.btnAjouterPanier.setBackground(COULEUR_MARRON);
        } else {
            // Affichage avec le texte rouge en petit
            this.lblStock.setText("<html>0 &nbsp; <span style='color:#D32F2F; font-size:10px;'><i>rupture de stock</i></span></html>");
            this.spinnerQuantité.setModel(new SpinnerNumberModel(0, 0, 0, 1));
            this.btnAjouterPanier.setEnabled(false);
            this.btnAjouterPanier.setBackground(Color.GRAY);
        }
    }
    
    

    @Responsable("Zeineb ESSEGHAIER")
    private void chargerImage(String nomImage) {
        try {
            String chemin = "/images/fromages/hauteur200/" + nomImage + ".jpg";
            InputStream is = getClass().getResourceAsStream(chemin);
            if (is != null) {
                Image img = ImageIO.read(is);
                Image imgRedim = img.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                this.lblImage.setIcon(new ImageIcon(imgRedim));
                this.lblImage.setText("");
            } else {
                this.lblImage.setIcon(null);
                this.lblImage.setText("Image non disponible");
            }
        } catch (Exception ex) {
            this.lblImage.setIcon(null);
            this.lblImage.setText("Image non disponible");
        }
    }

    @Responsable("Zeineb ESSEGHAIER")
    private void viderDétail() {
        this.lblNomFromage.setText("Sélectionnez un fromage");
        this.lblImage.setIcon(null);
        this.lblImage.setText("");
        this.txtDescription.setText("");
        this.lblArticle.setText("-");
        this.lblPrix.setText("-");
        this.lblStock.setText("-");
        this.btnAjouterPanier.setEnabled(false);
        this.btnAjouterPanier.setBackground(Color.GRAY);
        this.articlesFromageCourant = null;
    }

    @Responsable("Zeineb ESSEGHAIER & Zaynab BOULIFA")
    private void ajouterAuPanier() {
        if (this.articlesFromageCourant == null || this.articlesFromageCourant.isEmpty()) {
            return;
        }
        Article article = this.articlesFromageCourant.get(this.indexArticle);
        int quantité = (Integer) this.spinnerQuantité.getValue();
        
        // --- SÉCURITÉ STOCK LORS DE L'AJOUT MANUEL ---
        if (quantité > article.getQuantitéEnStock()) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Action impossible : Le stock restant est insuffisant (" + article.getQuantitéEnStock() + " disponible(s)).", 
                "Rupture de Stock", 
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return; 
        }

        this.panier.ajouterArticle(article, quantité);
        afficherArticle(article);
        
        // Mise à jour du prix sur le bouton en direct ---
        if (this.btnPanierMain != null) {
            this.btnPanierMain.setText("🛒 Gestion du panier (" + String.format("%.2f €", this.panier.calculerTotalTTC()) + ")");
       
            javax.swing.SwingUtilities.getWindowAncestor(this).repaint();
        }
        
        
    }

    /**
     * Permet de mettre à jour dynamiquement l'affichage du stock
     * lorsque les quantités bougent dans l'interface du panier.
     */
    @Responsable("Zeineb ESSEGHAIER & BOULIFA Zaynab")
    public void actualiserStockLabel(Article articleModifie) {
        if (this.articlesFromageCourant != null) {
            for (Article a : this.articlesFromageCourant) {
                if (a.equals(articleModifie)) {
                    this.lblStock.setText(String.valueOf(a.getQuantitéEnStock()));
                    break;
                }
            }
        }
    }
}