package ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import fr.iut.annotations.Responsable;

public class FenetreConfirmation extends JDialog {

    private static final long serialVersionUID = 1L;
    
    // Composants graphiques
    private JLabel lblMessageConfirmation;
    private JButton btnConfirmerOui;
    private JButton btnConfirmerNon;
    
    // Variable pour mémoriser le choix de l'utilisateur
    private boolean confirme = false;

    /**
     * Constructeur compatible avec WindowBuilder et FenetrePanier
     */
    @Responsable("Zaynab BOULIFA")
    public FenetreConfirmation(Window parent) {
        super(parent, "Confirmation", ModalityType.APPLICATION_MODAL); // Rend la fenêtre modale proprement
        
        setSize(350, 150);
        setLocationRelativeTo(parent); // Centre l'alerte par rapport au panier
        
        // Conteneur principal
        JPanel conteneurConf = new JPanel(new BorderLayout(10, 10));
        conteneurConf.setBackground(PanelFromages.COULEUR_FOND);
        conteneurConf.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setContentPane(conteneurConf);
        
        // Message central
        lblMessageConfirmation = new JLabel("Voulez-vous vraiment vider votre panier ?", SwingConstants.CENTER);
        lblMessageConfirmation.setFont(new Font("Serif", Font.PLAIN, 14));
        lblMessageConfirmation.setForeground(PanelFromages.COULEUR_TEXTE);
        conteneurConf.add(lblMessageConfirmation, BorderLayout.CENTER);
        
        // Panel des boutons
        JPanel panelBoutonsConf = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBoutonsConf.setBackground(PanelFromages.COULEUR_FOND);
        conteneurConf.add(panelBoutonsConf, BorderLayout.SOUTH);
        
        // Bouton Oui
        btnConfirmerOui = new JButton("Oui");
        btnConfirmerOui.setBackground(PanelFromages.COULEUR_MARRON);
        btnConfirmerOui.setForeground(Color.WHITE);
        btnConfirmerOui.addActionListener(e -> {
            confirme = true; // L'utilisateur valide
            dispose();       // Ferme la fenêtre
        });
        panelBoutonsConf.add(btnConfirmerOui);
        
        // Bouton Non
        btnConfirmerNon = new JButton("Non");
        btnConfirmerNon.setBackground(PanelFromages.COULEUR_MARRON_CLAIR);
        btnConfirmerNon.setForeground(Color.WHITE);
        btnConfirmerNon.addActionListener(e -> {
            confirme = false; // L'utilisateur annule
            dispose();        // Ferme la fenêtre
        });
        panelBoutonsConf.add(btnConfirmerNon);
    }

    // Getter permettant à FenetrePanier de récupérer la réponse
    @Responsable("Zaynab BOULIFA")
    public boolean isConfirme() {
        return confirme;
    }
}