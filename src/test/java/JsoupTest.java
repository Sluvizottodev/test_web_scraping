import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JsoupTest {
    public static void main(String[] args) throws Exception {
        Document doc = JsoupTest.connect("https://example.com").get();
        System.out.println("Título: " + doc.title());
    }
}