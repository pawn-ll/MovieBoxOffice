package com.example.movieboxoffice.utils;

import org.apache.commons.codec.binary.Base64;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Img2Base64Utils {

    private final static String IMG_PATH = "D:\\Projects\\MovieBoxOffice\\tmpImg\\image.jpg";


    public static void main(String[] args)  {
        String s = "";
        try {
            s = imgToBase64("https://img3.doubanio.com/view/photo/s_ratio_poster/public/p2880944813.jpg");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(s);
    }

    public static String imgToBase64(String imgUrl) throws Exception {
        URL url = new URL(imgUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36");
        connection.setDoOutput(true); // Triggers GET request.

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
        }

        try (FileOutputStream fos = new FileOutputStream(IMG_PATH)) {
            try (InputStream is = connection.getInputStream()) {
                byte[] buffer = new byte[1024];
                int read;
                while ((read = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, read);
                }
                fos.flush();
                System.out.println("Image saved successfully.");
            }
        } catch (IOException e) {
            System.err.println("Error saving image: " + e.getMessage());
        } finally {
            connection.disconnect();
        }

//         Encode the bytes as Base64.
        try {
            byte[] imageBytes = Files.readAllBytes(Paths.get(IMG_PATH));
            String base64ImageString = new String(Base64.encodeBase64(imageBytes));
            Files.deleteIfExists(Paths.get(IMG_PATH)); // 删除本地图片文件
            return base64ImageString;
        } catch (IOException e) {
            System.err.println("Error reading image file or deleting it: " + e.getMessage());
            return null;
        }
    }
}
