package com.example.movieboxoffice.spider.detail;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.movieboxoffice.entity.Actor;
import com.example.movieboxoffice.entity.DoubanSuggest;
import com.example.movieboxoffice.enums.Gender;
import com.example.movieboxoffice.service.impl.ActorServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.selector.Html;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Log4j2
public class ActorDetailSpider {


    private static final String BASE_URL = "https://movie.douban.com/j/subject_suggest?q=";
    @Autowired
    private ActorServiceImpl actorService;


    public void actorDetailSpider() throws InterruptedException {
        List<Actor> actorNames = actorService.getDoList().getRecords();
        for (Actor actor : actorNames) {
            DoubanSuggest doubanSuggest = actorDetail(actor.getName());
            if (doubanSuggest != null) {
                getSuggestMovieDetail(doubanSuggest.getUrl(), doubanSuggest.getImg(), actor);
            }
            Thread.sleep(3000);

        }
//        Actor actor = new Actor();
//        actor.setName("成龙");
//        DoubanSuggest doubanSuggest = actorDetail(actor.getName());
//        if (doubanSuggest != null) {
//            getSuggestMovieDetail(doubanSuggest.getUrl(), doubanSuggest.getImg(), actor);
//        }
    }

    public void getSuggestMovieDetail(String suggestUrl,String poster,Actor actor ) {
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
            List<String> infosLabel = html.xpath("//div[@class='article']//div[@class='right']/ul/li/span[@class='label']/text()").all();
            List<String> infosValue = html.xpath("//div[@class='article']//div[@class='right']/ul/li/span[@class='value']/text()").all();
            int size = infosLabel.size();
            for ( int i = 0; i < size; i++){
                if (infosLabel.get(i).trim().equals("性别:")){
                    if (infosValue.get(i).trim().equals("男"))
                        actor.setGender(Gender.MALE.getGender());
                    if (infosValue.get(i).trim().equals("女"))
                        actor.setGender(Gender.FEMALE.getGender());
                }
                if (infosLabel.get(i).trim().equals("出生日期:")){
                    actor.setBirthday(infosValue.get(i).trim());
                }
                if (infosLabel.get(i).trim().equals("出生地:")){
                    actor.setBirthpalce(infosValue.get(i).trim());
                }
                if (infosLabel.get(i).trim().equals("更多中文名:")){
                    actor.setAlias(infosValue.get(i).trim());
                }
                if (infosLabel.get(i).trim().equals("职业:")){
                    actor.setCareer(infosValue.get(i).trim());
                }
            }
            StringBuilder  instructions = new StringBuilder();
            List<String> all = html.xpath("//div[@class='article']//div[@class='content']/p/text()").all();
            for (String s : all) {
                instructions.append(s);
            }
            actor.setIntroduction(instructions.toString());
            if(poster==null){
                poster = html.xpath("//div[@class='article']//div[@class='avatar-container']/img/@src").get();
            }
            actor.setPoster(poster);
            actor.setDbUrl(suggestUrl);
            actorService.updateById(actor);
        }

    }

    private DoubanSuggest actorDetail(String name )  {
        String requestUrl = BASE_URL + (encodeQuery(name));
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
                movie = getDoubanSuggest(subjects,name);
                System.out.println(movie);
            } else {
                System.err.println("请求失败，状态码：" + connection.getResponseCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return movie;
    }

    private DoubanSuggest getDoubanSuggest(JSONArray subjects, String name){
        List<DoubanSuggest> doubanSuggests = JSONArray.parseArray(subjects.toString(), DoubanSuggest.class);
        DoubanSuggest res = null;
        for (DoubanSuggest doubanSuggest : doubanSuggests){
            if (doubanSuggest.getTitle().equals(name)){
                return doubanSuggest;
            }
        }
        return res;
    }

    private static String encodeQuery(String query) {
        try {
            return URLEncoder.encode(query, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("Error encoding query", e);
        }
    }

}
