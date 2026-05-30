package modele;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import fr.iut.annotations.Responsable;

/**
 * La classe OutilsBaseFromages fournit des méthodes utilitaires pour la gestion
 * d'une base de données de fromages à partir de fichiers JSON.
 */
public class OutilsBaseDonneesFromages {

    /**
     * Le chemin relatif du fichier json depuis la racine du projet java
     */
    public static final String CHEMIN_FICHIER = "src/main/resources/data/fromages.json";

    /**
     * Génère une base de données de fromages à partir d'un fichier JSON.
     *
     * @param cheminFichier le chemin du fichier JSON contenant les données des
     *                      tomates
     * @return une instance de Fromages représentant la base de données de
     *         fromages en mémoire
     */
    @Responsable({ "Hervé LEBLANC", "Christine JULIEN" })
    public static Fromages générationBaseDeFromages(String cheminFichier) {

        List<Fromage> fromages = new ArrayList<>();
        try {
            fromages = lectureFromagesDepuisJson(cheminFichier);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Fromages base = new Fromages();
        base.addFromages(fromages);
        return base;
    }

    /**
     * Sauvegarde la base de données de tomates dans un fichier JSON.
     *
     * @param base          la base de données de tomates à sauvegarder
     * @param cheminFichier le chemin du fichier JSON où sauvegarder les données
     */
    @Responsable({ "Hervé LEBLANC", "Christine JULIEN" })
    public static void sauvegarderBaseDeFromages(Fromages base,
            String cheminFichier) {
        List<Fromage> fromages = base.getFromages();
        try {
            écritureFromagesVersJson(fromages, cheminFichier);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Responsable({ "Hervé LEBLANC", "Christine JULIEN" })
    private static List<Fromage> lectureFromagesDepuisJson(String cheminFichier)
            throws Exception {
        // Lecture du fichier
        String contenu = new String(
                Files.readAllBytes(Paths.get(cheminFichier)));

        JSONArray jsonArray = new JSONArray(contenu);
        List<Fromage> fromages = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonFromage = jsonArray.getJSONObject(i);

            String désignation = jsonFromage.getString("désignation");
            String nomImage = jsonFromage.getString("nomImage");

            Fromage fromage = new Fromage(désignation, nomImage);
            fromage.updateTypeFromage(
                    TypeLait.getTypeLait(jsonFromage.getString("typeFromage")));
            fromage.addDescription(jsonFromage.getString("description"));

            // Lecture du tableau d’articles
            JSONArray jsonArticles = jsonFromage.getJSONArray("articles");

            for (int j = 0; j < jsonArticles.length(); j++) {
                JSONObject jsonArticle = jsonArticles.getJSONObject(j);

                String clé = jsonArticle.getString("clé");
                float prixTTC = (float) jsonArticle.getDouble("prixTTC");

                int quantitéEnStock = jsonArticle.getInt("quantitéEnStock");

                fromage.addArticle(clé, prixTTC, quantitéEnStock);
            }

            fromages.add(fromage);
        }

        return fromages;
    }

    @Responsable({ "Hervé LEBLANC", "Christine JULIEN" })
    private static void écritureFromagesVersJson(List<Fromage> fromages,
            String cheminFichier) throws Exception {

        JSONArray jsonArray = new JSONArray();

        for (Fromage fromage : fromages) {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("typeFromage",
                    fromage.getTypeFromage().getTypeDeLait());
            jsonObject.put("désignation", fromage.getDésignation());
            jsonObject.put("nomImage", fromage.getNomImage());
            jsonObject.put("description", fromage.getDescription());

            // Tableau d’articles
            JSONArray articlesArray = new JSONArray();

            List<Article> articles = fromage.getArticles();
            for (Article article : articles) {
                JSONObject articleJson = new JSONObject();
                articleJson.put("clé", article.getClé());
                articleJson.put("prixTTC", article.getPrixTTC());
                articleJson.put("quantitéEnStock",
                        article.getQuantitéEnStock());

                articlesArray.put(articleJson);
            }

            // Ajout du tableau d’articles au fromage
            jsonObject.put("articles", articlesArray);

            jsonArray.put(jsonObject);
        }

        Files.write(Paths.get(cheminFichier),
                jsonArray.toString(4).getBytes());
        System.out.println("sauvegarde faite");

    }
}
