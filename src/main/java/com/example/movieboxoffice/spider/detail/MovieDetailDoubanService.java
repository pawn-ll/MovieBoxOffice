package com.example.movieboxoffice.spider.detail;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.movieboxoffice.entity.DoubanSuggest;
import com.example.movieboxoffice.entity.MovieDetail;
import com.example.movieboxoffice.entity.SecondDo;
import com.example.movieboxoffice.enums.MovieDetailLength;
import com.example.movieboxoffice.service.impl.MovieDetailServiceImpl;
import com.example.movieboxoffice.service.impl.MovieDoServiceImpl;
import com.example.movieboxoffice.service.impl.SecondDoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.selector.Html;

import java.io.BufferedReader;
import java.io.IOException;
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
    @Autowired
    private SecondDoServiceImpl secondDoService;


    public void getMovieDetail(String movieName ,Long movieCode , boolean second) {
        try {
            DoubanSuggest suggest = movieDetail(movieName,movieCode);
            if (suggest !=null) {
                getSuggestMovieDetail(suggest.getUrl(), suggest.getImg());
                if (second)
                    secondDoService.doMovie(movieCode);
            }else {
                System.out.println("------------"+movieName+" 未匹配成功");
                if (!second ) {
                    SecondDo secondDo = new SecondDo();
                    secondDo.setMovieCode(movieCode);
                    secondDo.setMovieName(movieName);
                    secondDoService.save(secondDo);
                    movieDoService.doMovie(movieCode);
                }

            }
        }catch (Exception e){
            System.out.println(movieName+"  爬取失败");
            System.out.println(e);
            if (!second ) {
                SecondDo secondDo = new SecondDo();
                secondDo.setMovieCode(movieCode);
                secondDo.setMovieName(movieName);
                secondDoService.save(secondDo);
                movieDoService.doMovie(movieCode);
            }
        }

    }

    private DoubanSuggest  movieDetail(String movieName ,Long movieCode) throws IOException {
        this.movieCode = movieCode;
        this.movieName = movieName;
        String requestUrl = BASE_URL + (encodeQuery(movieName));
        DoubanSuggest movie = null;
        BufferedReader reader = null;
        StringBuilder response = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(requestUrl).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36");

            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                // 解析JSON响应
                JSONArray subjects = JSON.parseArray(response.toString());
                if (subjects.size() < 1) return null;
                movie = getDoubanSuggest(subjects,movieName);
                System.out.println(movie);
            } else {
                System.err.println("请求失败，状态码：" + connection.getResponseCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return movie;
    }

    private  void getSuggestMovieDetail(String suggestUrl,String poster) {
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
            String director = getValidString(directors, MovieDetailLength.DIRECTOR.getLength());
            String scripter = "";
            if (persons.size()>2) {
                List<String> scripters = new Html(persons.get(1)).xpath("//a/text()").all();
                scripter = getValidString(scripters, MovieDetailLength.SCIPTER.getLength());
            }
            String actor = "";
            List<String> actors = null ;
            if (persons.size()==2) {
                actors = new Html(persons.get(1)).xpath("//a/text()").all();

            }else if (persons.size()>2){
                actors = new Html(persons.get(2)).xpath("//a/text()").all();
            }
            if (actors.size()>0)
                actor = getValidString(actors, MovieDetailLength.ACTOR.getLength());

            List<String> types = html.xpath("//*[@id='info']/span[@property='v:genre']/text()").all();
            String type = getValidString(types, MovieDetailLength.TYPE.getLength());

            List<String> releaseDates = html.xpath("//*[@id='info']/span[@property='v:initialReleaseDate']/text()").all();
            String releaseDate = getValidString(releaseDates, MovieDetailLength.TYPE.getLength());

            String area = html.xpath("//*[@id='info']//text()").get().replace("/","").trim().split(" ")[0];
            String time = html.xpath("//*[@id='info']/span[@property='v:runtime']/text()").get();
            String introduction = html.xpath("//span[@property='v:summary']/text()").get();
            MovieDetail movieDetail = new MovieDetail();
            movieDetail.setMovieName(movieName);
            movieDetail.setDirector(director.trim());
            movieDetail.setScripter(scripter.trim());
            movieDetail.setActor(actor.trim());
            movieDetail.setReleaseDate(releaseDate.trim());
            movieDetail.setType(type.trim());
            movieDetail.setArea(area.trim());
            if (time != null) {
                movieDetail.setLength(time.trim());
            }
            if (introduction.length() > 1024){
                movieDetail.setIntroduction(introduction.substring(0,1024));
            }else {
                movieDetail.setIntroduction(introduction);
            }
            movieDetail.setPoster(poster);
            movieDetail.setMovieCode(movieCode);
            movieDetailService.save(movieDetail);
            movieDoService.doMovie(movieCode);
        }

    }

    private static String encodeQuery(String query) {
        try {
            return URLEncoder.encode(query, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("Error encoding query", e);
        }
    }

    private DoubanSuggest getDoubanSuggest(JSONArray subjects, String movieName){
        List<DoubanSuggest> doubanSuggests = JSONArray.parseArray(subjects.toString(), DoubanSuggest.class);
        DoubanSuggest res = null;
        for (DoubanSuggest doubanSuggest : doubanSuggests){
            if (!doubanSuggest.getEpisode().equals("")){
                continue;
            }
            if (!doubanSuggest.getTitle().equals(movieName)){
                continue;
            }
            res = doubanSuggest;
        }
        return res;
    }

    private String getValidString(List<String> list,Integer length){
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            if ((sb.length()+s.length()) > length)
                break;
            sb.append(s);
            sb.append(" ");
        }
        return sb.toString();
    }

}
