package modele;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import fr.iut.annotations.Responsable;
import modele.Article;
import modele.Fromage;
import modele.Panier;

public class PanierTest {

    private Panier panier;
    private Article articleFactice;

    @Before
    public void setUp() {
        // Initialisation du panier (Modèle métier)
        panier = new Panier();
        
        // Création d'un article factice avec un prix unitaire de 1.00 € TTC
        // Permet de définir un montant de panier précis simplement via la quantité
        Fromage fromageFactice = new Fromage("Fromage de Test", "image_test.png");
        articleFactice = new Article(fromageFactice, "Portion", 1.00f, 1000);
    }

    /**
     * Méthode utilitaire pour injecter un montant précis dans le panier
     */
    private void fixerMontantPanier(double montant) {
        panier.getLignesCommande().clear();
        panier.ajouterArticle(articleFactice, (int) montant);
    }

    // =========================================================================
    // TESTS : COLISSIMO (Index 0)
    // =========================================================================
    @Responsable("Zaynab BOULIFA")
    @Test
    public void testColissimoMoinsDe60Euros() {
        fixerMontantPanier(45.00); 
        // 45.00 € (fromages) + 14.90 € (frais) = 59.90 €
        assertEquals(59.90, panier.calculerTotalAvecTransport(0), 0.001);
    }
    @Responsable("Zaynab BOULIFA")
    @Test
    public void testColissimoEntre60Et90Euros() {
        fixerMontantPanier(75.00); 
        // 75.00 € (fromages) + 9.90 € (frais) = 84.90 €
        assertEquals(84.90, panier.calculerTotalAvecTransport(0), 0.001);
    }
    @Responsable("Zaynab BOULIFA")
    @Test
    public void testColissimoPlusDe90Euros() {
        fixerMontantPanier(105.00); 
        // 105.00 € (fromages) + 4.90 € (frais) = 109.90 €
        assertEquals(109.90, panier.calculerTotalAvecTransport(0), 0.001);
    }

    // =========================================================================
    // TESTS : CHRONO RELAIS (Index 1)
    // =========================================================================
    @Responsable("Zaynab BOULIFA")
    @Test
    public void testChronoRelaisMoinsDe60Euros() {
        fixerMontantPanier(35.00); 
        // 35.00 € (fromages) + 14.90 € (frais) = 49.90 €
        assertEquals(49.90, panier.calculerTotalAvecTransport(1), 0.001);
    }
    @Responsable("Zaynab BOULIFA")
    @Test
    public void testChronoRelaisEntre60Et90Euros() {
        fixerMontantPanier(80.00); 
        // 80.00 € (fromages) + 9.90 € (frais) = 89.90 €
        assertEquals(89.90, panier.calculerTotalAvecTransport(1), 0.001);
    }
    @Responsable("Zaynab BOULIFA")
    @Test
    public void testChronoRelaisPlusDe90Euros() {
        fixerMontantPanier(115.00); 
        // 115.00 € (fromages) + 4.90 € (frais) = 119.90 €
        assertEquals(119.90, panier.calculerTotalAvecTransport(1), 0.001);
    }

    // =========================================================================
    // TESTS : CHRONOFRESH (Index 2)
    // =========================================================================
    @Responsable("Zaynab BOULIFA")
    @Test
    public void testChronoFreshMoinsDe50Euros() {
        fixerMontantPanier(25.00); 
        // 25.00 € (fromages) + 23.80 € (frais) = 48.80 €
        assertEquals(48.80, panier.calculerTotalAvecTransport(2), 0.001);
    }
    @Responsable("Zaynab BOULIFA")
    @Test
    public void testChronoFreshEntre50Et80Euros() {
        fixerMontantPanier(65.00); 
        // 65.00 € (fromages) + 17.80 € (frais) = 82.80 €
        assertEquals(82.80, panier.calculerTotalAvecTransport(2), 0.001);
    }
    @Responsable("Zaynab BOULIFA")
    @Test
    public void testChronoFreshPlusDe80Euros() {
        fixerMontantPanier(95.00); 
        // 95.00 € (fromages) + 9.90 € (frais) = 104.90 €
        assertEquals(104.90, panier.calculerTotalAvecTransport(2), 0.001);
    }

    // =========================================================================
    // SEUIL DE GRATUITÉ FRANCO DE PORT (>= 120.00 €)
    // =========================================================================
    @Responsable("Zaynab BOULIFA")
    @Test
    public void testGratuiteFrancoDePort() {
        fixerMontantPanier(130.00); 
        
        // Gratuité totale (frais = 0.00 €) peu importe le choix du mode de livraison
        assertEquals(130.00, panier.calculerTotalAvecTransport(0), 0.001); // Colissimo
        assertEquals(130.00, panier.calculerTotalAvecTransport(1), 0.001); // Chrono Relais
        assertEquals(130.00, panier.calculerTotalAvecTransport(2), 0.001); // Chronofresh
    }
}