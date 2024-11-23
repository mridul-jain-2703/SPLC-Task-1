import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.sql.PreparedStatement;
import java.sql.SQLException;
//This will be our BOT

public class Web_Crawler implements Runnable{
    private static final int MAX_DEPTH = 3;
    private Thread thread;
    private String firstLink;
    private ArrayList<String> visitedLinks = new ArrayList<>();
    private int ID;

    public Web_Crawler(String link, int num){
        System.out.println("WebCrawler created\n");
        firstLink = link;
        ID = num;

        //Creating a new thread for the bot to run
        thread = new Thread(this);
        thread.start();
    }

    private void saveToDatabase(String url, String title) {
        String sql = "INSERT INTO crawled_data (url, title) VALUES (?, ?)";

        try (java.sql.Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, url);
            pstmt.setString(2, title);
            pstmt.executeUpdate();

            System.out.println("Saved to database: " + url + " | " + title);

        } catch (SQLException e) {
            Logger.getGlobal().log(Level.WARNING, "Failed to insert data into database", e);
        }
    }

    @Override
    public void run() {
        crawl(1, firstLink);
    }

    private void crawl(int level, String url){
        if(level <= MAX_DEPTH){
            Document doc = request(url);
            if(doc != null){
                //Element is similar to an arrayList in java
                for(Element link: doc.select("a[href]")){
                    String nextLink = link.absUrl("href");

                    if(!visitedLinks.contains(nextLink)){
                        crawl(++level, nextLink);
                    }
                }
            }
        }
    }
    private Document request(String url){
        try{
            //'con' returns url in the form of an object
            Connection con = Jsoup.connect(url);
            Document doc = con.get();

            if(con.response().statusCode() == 200){
                System.out.println("\nBOT ID:"+ID+" Received webpage at:" +url);
                String title = doc.title();
                System.out.println(title);
                visitedLinks.add(url);
                saveToDatabase(url, title);

                return doc;
            }
        }catch(IOException e){
            Logger.getGlobal().log(Level.WARNING,"Failure to complete action");
        }return null;
    }

    public Thread getThread() {
        return thread;
    }
}
