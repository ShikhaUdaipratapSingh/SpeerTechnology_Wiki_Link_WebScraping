import org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class uniqWikiLink {
    private static Set<String> allUniqueSet = new HashSet<>();
    private static List<String> uniqueList = new ArrayList<>();
    private  static List<String> allLinks = new ArrayList<>();

    private static final int MAX_getWikiURLsS = 3;
    static String startURL = "https://en.wikipedia.org/wiki/";


    public static void main(String[] args) throws IOException {


        int maxgetWikiURLs = 3;  //this could be taken from arg[0]

        if (!isValidURL(startURL)) {
            System.out.println("Error: Not a valid Wikipedia link.");
            return;
        }

        for (int i = 0; i < maxgetWikiURLs; i++) {
            int uniqItemCount = 10 * (i + 1);
            System.out.println("Cycle number " + i);
            getWikiURLs(i, uniqItemCount);
        }

        File obj = new File("C:\\intellij\\Speer_Task_WebScraping\\link.csv");
        if (obj.createNewFile()) {
            System.out.println("File created : " + obj.getName());
        } else {
            System.out.println("File already exits");
        }

        FileWriter writer = new FileWriter("link.csv");

        Iterator<String> iter = uniqueList.iterator();
        int urlNumberItr= 1;

        while (iter.hasNext()) {

            String urlLine = iter.next();
            try {
                writer.write("Link number " + urlNumberItr + " | URL : " + urlLine + "\n");
            } catch (IOException e) {
                System.out.println("Error occured");
                e.printStackTrace();

            }

           urlNumberItr++;
        }
        writer.write("Total count of Wiki Links: " + allLinks.size() + " | Unique link count :" + uniqueList.size());
        writer.close();
    }

    private static boolean isValidURL(String link) {
        // Wikipedia links validation
        return ((link).toLowerCase()).startsWith("https://en.wikipedia.org/wiki/");
    }

    private static void getWikiURLs(int cycle, int uniqItemCount) throws IOException {

        List<String> newLinks = new ArrayList<>();

        if (cycle == 0) {
            readWikiLinks();    // function to calculate total count and unique count
        }

        int endIndex = uniqItemCount - 1;
        int startIndex = uniqItemCount - 10;   // start index should be 10 behind to read 10 item in each iteration

        newLinks.addAll(allUniqueSet); //change hashset to arraylist to use subList
        uniqueList.addAll(newLinks.subList(startIndex, endIndex + 1));
    }

    private static void readWikiLinks(){

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run in headless mode to avoid opening a visible browser window.

        WebDriver driver = new ChromeDriver(options);
        try {

            driver.get(startURL);

            List<WebElement> links = driver.findElements(By.xpath("//a[@href]"));

            for (WebElement link : links) {
                String href = link.getAttribute("href");
                if (isValidURL(href) && !allUniqueSet.contains(href)) {
                    System.out.println("Here are the href links :" + href);
                    allUniqueSet.add(href);
                }
                if ( isValidURL(href) ) {
                    allLinks.add(href);
                     }
            }
            System.out.println("Total found unique links" + allUniqueSet.size());
        } finally {
            driver.quit();
        }

    }
}
