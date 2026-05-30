package modele;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import fr.iut.annotations.Responsable;

public class TestMiseEnPlaceBDD {

    private static Fromages mesArticles;

    @Responsable("Hervé LEBLANC")
    @BeforeClass
    public static void setuP() {
        mesArticles = OutilsBaseDonneesFromages.générationBaseDeFromages(
                OutilsBaseDonneesFromages.CHEMIN_FICHIER);
    }

    @Responsable("Hervé LEBLANC")
    @Test
    public void pasDeFromageSansDésignation() {
        List<Fromage> fromages = mesArticles.getFromages();
        for (var f : fromages) {
            assertNotNull(f.getDésignation());
            assertNotEquals("", f.getDésignation());
        }
    }

    @Responsable("Hervé LEBLANC")
    @Test
    public void AuMoinsUnArticleParFromage() {
        List<Fromage> fromages = mesArticles.getFromages();
        for (var f : fromages) {
            assertTrue(f.getArticles().size() > 0);
        }
    }

    @Responsable("Hervé LEBLANC")
    @Test
    public void chaqueCléArticleEstNonNull() {
        List<Fromage> fromages = mesArticles.getFromages();
        for (var f : fromages) {
            List<Article> articles = f.getArticles();
            for (var a : articles) {
                assertNotNull(a.getClé());
            }
        }
    }

    @Responsable("Hervé LEBLANC")
    @Test
    public void chaqueArticleRéfèreSonFRomage() {
        List<Fromage> fromages = mesArticles.getFromages();
        for (var f : fromages) {
            List<Article> articles = f.getArticles();
            for (var a : articles) {
                assertSame(f, a.getFromage());
            }
        }
    }

}
