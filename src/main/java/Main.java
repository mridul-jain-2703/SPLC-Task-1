import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<Web_Crawler> bot = new ArrayList<>();
        bot.add(new Web_Crawler("https://www.indiatoday.in/",1));
        bot.add(new Web_Crawler("https://indianexpress.com/",2));
        bot.add(new Web_Crawler("https://www.livemint.com/news",3));

        for(Web_Crawler w: bot){
            try{
                w.getThread().join();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }

    }
}
