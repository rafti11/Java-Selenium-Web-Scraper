package org.example;

import com.opencsv.CSVWriter;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Tools {

    // Read a file and return a list with all the links
    public static List<String> fileToList(String filePatch) {

        ArrayList<String> list = new ArrayList<>();

        File file = new File(filePatch);
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                list.add(data);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public static void downloadImageFromUrl(String urlLink, String filePatch)  {

        URL url;
        try {
            url = URI.create(urlLink).toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        try (InputStream inputStream = url.openStream()) {

            try (OutputStream outputStream = new FileOutputStream(filePatch)) {

                byte[] bt = new byte[2048];
                int position;

                while ((position = inputStream.read(bt)) != -1) {
                    outputStream.write(bt, 0, position);
                }

            } catch (FileNotFoundException e) {
                System.err.println(e.getLocalizedMessage());
            }

        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }

    }


    public static void createCVSFileFromList(String filePatch, List<String[]> list) {

        try (CSVWriter writer = new CSVWriter(new FileWriter(filePatch))) {
            writer.writeAll(list);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
