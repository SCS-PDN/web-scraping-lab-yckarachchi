import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class WebScraper {
    public static void main(String[] args) {
        try {
            String url = "https://www.bbc.com";
            Document doc = Jsoup.connect(url).get();

            // Title and headings
            System.out.println("Title: " + doc.title());
            for (int i = 1; i <= 6; i++) {
                Elements headings = doc.select("h" + i);
                for (Element heading : headings) {
                    System.out.println("Heading (h" + i + "): " + heading.text());
                }
            }

            // Links
            Elements links = doc.select("a[href]");
            for (Element link : links) {
                System.out.println("Link: " + link.attr("abs:href"));
            }
            
            Elements articles = doc.select("article");
                       
            class NewsItem {
                String headline;
                String publicationDate;
                String author;

                public NewsItem(String headline, String publicationDate, String author) {
                    this.headline = headline;
                    this.publicationDate = publicationDate;
                    this.author = author;
                }

                @Override
                public String toString() {
                    return "Headline: " + headline + "\nDate: " + publicationDate + "\nAuthor: " + author + "\n";
                }
            }
            
            List<NewsItem> newsList = new ArrayList<>();
            
            for (Element article : articles) {
                String headline = article.select("h1, h2, h3").text();
                String date = article.select("time").attr("datetime");
                String author = article.select("[class*=author]").text(); // may vary by site

                if (!headline.isEmpty()) {
                    newsList.add(new NewsItem(headline, date, author));
                }
            }

            System.out.println("\n--- Extracted News ---");
            for (NewsItem item : newsList) {
                System.out.println(item);
            }
            
          
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
