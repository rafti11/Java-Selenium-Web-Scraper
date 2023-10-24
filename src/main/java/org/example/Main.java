package org.example;

import org.example.model.Book;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {


        String file = "/home/usuario/dls/links.txt";
        List<String> urlList = Tools.fileToList(file);


        String[] info = {"ISBN","TITLE", "LANGUAGE", "PAGES", "PUBLISHER", "AUTHOR", "YEAR", "IMAGE", "SYNOPSIS", "GENRES"};
        ArrayList<String[]> bookList = new ArrayList<>();
        bookList.add(info);


        for (String url: urlList) {

            WebScraper wp = new WebScraper();
            Book book = wp.getBookFromURL(url);
            bookList.add(book.bookToString());

        }

        String file2 = "/home/usuario/dls/book.csv";
        Tools.createCVSFileFromList(file2, bookList);

    }
}