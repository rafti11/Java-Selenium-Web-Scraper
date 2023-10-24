package org.example;

import org.example.model.Book;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.ArrayList;
import java.util.List;

public class WebScraper {

    public Book getBookFromURL(String url) {

        Book book = new Book();

        WebDriver webDriver = new FirefoxDriver();
        webDriver.get(url);

        String jsonCSS = "script[type=\"application/ld+json\"]";
        String synopsisCSS = "div.d-none.d-md-inline > div > div > div.formated-text > p";
        String synopsisCSS2 = ".d-none.d-md-inline .formated-text";
        String publisherCSS = "div.d-none.d-md-inline > div > span";
        String genresCSS = "div.d-none.d-md-inline > div > a > span.v-chip__content";

        WebElement synopsis;


        if (!webDriver.findElements(By.cssSelector(synopsisCSS)).isEmpty()) {
            synopsis = webDriver.findElement(By.cssSelector(synopsisCSS));
            book.setSynopsis(synopsis.getText());
        } else {
            if (!webDriver.findElements(By.cssSelector(synopsisCSS2)).isEmpty()) {
                synopsis = webDriver.findElement(By.cssSelector(synopsisCSS2));
                book.setSynopsis(synopsis.getText());
            }
            book.setSynopsis("");
        }

        WebElement jsonWE = webDriver.findElement(By.cssSelector(jsonCSS));

        WebElement publisher = webDriver.findElement(By.cssSelector(publisherCSS));
        List<WebElement> genresWE = webDriver.findElements(By.cssSelector(genresCSS));
        book.setPublisher(publisher.getText());

        ArrayList<String> genresList = new ArrayList<>();
        for (WebElement we : genresWE) {
            genresList.add(we.getText());
        }
        book.setGenres(genresList);

        JSONArray jsonArray = new JSONArray(jsonWE.getAttribute("innerHTML"));

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject jsonObject = jsonArray.getJSONObject(i);

            if (i == 1) {

                try {
                    book.setTitle(jsonObject.getString("name"));
                }catch (JSONException e) {
                    System.err.println(e.getMessage());
                }

                try {
                    book.setImage(jsonObject.getString("image"));
                }catch (JSONException e) {
                    System.err.println(e.getMessage());
                }

                try {
                    JSONObject authorJS = jsonObject.getJSONObject("author");
                    book.setAuthor(authorJS.getString("name"));
                }catch (JSONException e) {
                    System.err.println(e.getMessage());
                }


                try {
                    JSONArray workJS = jsonObject.getJSONArray("workExample");

                    for (int a = 0; a < workJS.length(); a++) {

                        if (a == 0) {
                            JSONObject workJSob = workJS.getJSONObject(0);
                            book.setIsbn(workJSob.getString("isbn"));
                            book.setPages(workJSob.getInt("numberOfPages"));
                            book.setLanguage(workJSob.getString("inLanguage"));
                            book.setYear(workJSob.getString("datePublished"));
                        }
                    }

                }catch (JSONException e) {

                    System.err.println(e.getMessage());
                }

            }

        }

        webDriver.close();

        String path = "/home/usuario/dls/" + book.getIsbn() + ".jpg";
        Tools.downloadImageFromUrl(book.getImage(), path);

        book.setImage(book.getIsbn() + ".jpg");

        return book;
    }
}
