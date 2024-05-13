package com.example.movieboxoffice.spider.detail;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.movieboxoffice.entity.DoubanSuggest;
import com.example.movieboxoffice.entity.MovieDetail;
import com.example.movieboxoffice.entity.MovieDo;
import com.example.movieboxoffice.service.impl.MovieDetailServiceImpl;
import com.example.movieboxoffice.service.impl.MovieDoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.selector.Html;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class MovieDetailDoubanService {

    private static final String BASE_URL = "https://movie.douban.com/j/subject_suggest?q=";

    private String movieName ;
    private Long movieCode ;

    @Autowired
    private MovieDetailServiceImpl movieDetailService;
    @Autowired
    private MovieDoServiceImpl movieDoService;


    public void getMovieDetail(MovieDo movieDo) {
        String url = movieDetail(movieDo);
        getSuggestMovieDetail(url);
    }

    private String  movieDetail(MovieDo movieDo) {
        movieName = movieDo.getMovieName();
        movieCode = movieDo.getMovieCode();
        String requestUrl = BASE_URL + (encodeQuery(movieDo.getMovieName()));
        DoubanSuggest movie = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(requestUrl).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36");

            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                // 解析JSON响应
//                JSONObject jsonObject = JSONObject.parseObject(response.toString());
                JSONArray subjects = JSON.parseArray(response.toString());
                movie = subjects.getObject(0, DoubanSuggest.class);
                System.out.println(movie);
                return movie.getUrl();

            } else {
                System.err.println("请求失败，状态码：" + connection.getResponseCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private  void getSuggestMovieDetail(String suggestUrl) {
        if (suggestUrl == null) {
            System.out.println("电影详情获取失败");
            return;
        }
        Html html = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(suggestUrl).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36");

            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                // 解析响应
                html = new Html(response.toString(), suggestUrl);

            } else {
                System.err.println("请求失败，状态码：" + connection.getResponseCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (html != null) {
            List<String> persons = html.xpath("//*[@id='info']/span/span[@class='attrs']").all();
            List<String> directors = new Html(persons.get(0)).xpath("//a/text()").all();
            StringBuilder sb = new StringBuilder();
            for (String s : directors) {
                sb.append(s);
                sb.append(" ");
            }
            String director = sb.toString();

            List<String> scripters = new Html(persons.get(1)).xpath("//a/text()").all();
            sb = new StringBuilder();
            for (String s : scripters) {
                sb.append(s);
                sb.append(" ");
            }
            String scripter = sb.toString();

            List<String> actors = new Html(persons.get(2)).xpath("//a/text()").all();
            sb = new StringBuilder();
            for (String s : actors) {
                sb.append(s);
                sb.append(" ");
            }
            String actor = sb.toString();

            List<String> types = html.xpath("//*[@id='info']/span[@property='v:genre']/text()").all();
            sb = new StringBuilder();
            for (String s : types) {
                sb.append(s);
                sb.append(" ");
            }
            String type = sb.toString();
            String area = html.xpath("//*[@id='info']//text()").get().replace("/","").trim().split(" ")[0];
            String time = html.xpath("//*[@id='info']/span[@property='v:runtime']/text()").get();
            String introduction = html.xpath("//span[@property='v:summary']/text()").get();
            String poster = html.xpath("//*[@id='mainpic']/a/img").get();
            poster = poster.substring(poster.indexOf("src=")+5, poster.indexOf("title=")-2);
            MovieDetail movieDetail = new MovieDetail();
            movieDetail.setMovieName(movieName);
            movieDetail.setDirector(director);
            movieDetail.setScripter(scripter);
            movieDetail.setActor(actor);
            movieDetail.setType(type);
            movieDetail.setArea(area);
            movieDetail.setLength(time);
            if (introduction.length() > 1024){
                movieDetail.setIntroduction(introduction.substring(0,1024));
            }else {
                movieDetail.setIntroduction(introduction);
            }
            movieDetail.setPoster(poster);
            movieDetail.setMovieCode(movieCode);
            movieDetailService.save(movieDetail);
//            movieDoService.doMovie(movieCode);
        }

    }

    private static String encodeQuery(String query) {
        try {
            return URLEncoder.encode(query, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("Error encoding query", e);
        }
    }

}
