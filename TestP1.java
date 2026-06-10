package modele;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.iut.annotations.Responsable;

/**
 * Tests unitaires pour la fonctionnalité P1 :
 * filtre par type de lait et mise à jour des stocks.
 */
public class TestP1 {

    private static Fromages base;
    private Panier panier;

    @Responsable("Zeineb ESSEGHAIER")
    @BeforeClass
    public static void setUpClass() {
        base = OutilsBaseDonneesFromages.générationBaseDeFromages(
                OutilsBaseDonneesFromages.CHEMIN_FICHIER);
    }

    @Responsable("Zeineb ESSEGHAIER")
    @Before
    public void setUpTest() {
        base = OutilsBaseDonneesFromages.générationBaseDeFromages(
                OutilsBaseDonneesFromages.CHEMIN_FICHIER);
        panier = new Panier();
    }

    // =============================================
    // Tests filtre par type de lait
    // =============================================

    @Responsable("Zeineb ESSEGHAIER")
    @Test
    public void filtreVacheRetourneSeulementVache() {
        List<Fromage> résultat = base.fromagesAuLaitDe(TypeLait.VACHE);
        assertNotNull(résultat);
        for (Fromage f : résultat) {
            assertEquals(TypeLait.VACHE, f.getTypeFromage());
        }
    }

    @Responsable("Zeineb ESSEGHAIER")
    @Test
    public void filtreBrebisRetourneSeulementBrebis() {
        List<Fromage> résultat = base.fromagesAuLaitDe(TypeLait.BREBIS);
        assertNotNull(résultat);
        assertFalse("Il doit y avoir des fromages au lait de brebis",
                résultat.isEmpty());
        for (Fromage f : résultat) {
            assertEquals(TypeLait.BREBIS, f.getTypeFromage());
        }
    }

    @Responsable("Zeineb ESSEGHAIER")
    @Test
    public void filtreChèvreRetourneSeulementChèvre() {
        List<Fromage> résultat = base.fromagesAuLaitDe(TypeLait.CHEVRE);
        assertNotNull(résultat);
        assertFalse("Il doit y avoir des fromages au lait de chèvre",
                résultat.isEmpty());
        for (Fromage f : résultat) {
            assertEquals(TypeLait.CHEVRE, f.getTypeFromage());
        }
    }

    @Responsable("Zeineb ESSEGHAIER")
    @Test
    public void sommeDesFiltresEgaleTotalFromages() {
        int nbVache  = base.fromagesAuLaitDe(TypeLait.VACHE).size();
        int nbChèvre = base.fromagesAuLaitDe(TypeLait.CHEVRE).size();
        int nbBrebis = base.fromagesAuLaitDe(TypeLait.BREBIS).size();
        int total    = base.getFromages().size();
        assertEquals("La somme des filtres doit égaler le total",
                total, nbVache + nbChèvre + nbBrebis);
    }

    // =============================================
    // Tests gestion des stocks
    // =============================================

    @Responsable("Zeineb ESSEGHAIER")
    @Test
    public void stockDiminueAprèsAjoutAuPanier() {
        Article article = base.getFromage("Abondance").getArticles().get(0);
        int stockAvant = article.getQuantitéEnStock();
        panier.ajouterArticle(article, 3);
        assertEquals("Le stock doit diminuer de 3",
                stockAvant - 3, article.getQuantitéEnStock());
    }

    @Responsable("Zeineb ESSEGHAIER")
    @Test
    public void stockPositifOuNulAprèsCommande() {
        Article article = base.getFromage("Abondance").getArticles().get(0);
        panier.ajouterArticle(article, 1);
        assertTrue("Le stock ne doit pas être négatif",
                article.getQuantitéEnStock() >= 0);
    }

    @Responsable("Zeineb ESSEGHAIER")
    @Test
    public void ajoutMêmeArticleMiseÀJourQuantité() {
        Article article = base.getFromage("Abondance").getArticles().get(0);
        int stockAvant = article.getQuantitéEnStock();
        panier.ajouterArticle(article, 2);
        panier.ajouterArticle(article, 4);
        assertEquals("Stock diminue de 4 au total",
                stockAvant - 4, article.getQuantitéEnStock());
    }
}