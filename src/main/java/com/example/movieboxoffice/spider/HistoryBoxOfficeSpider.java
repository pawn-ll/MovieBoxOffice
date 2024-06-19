package com.example.movieboxoffice.spider;

import com.example.movieboxoffice.entity.MovieBoxoffice;
import com.example.movieboxoffice.entity.MovieDetail;
import com.example.movieboxoffice.entity.SecondDo;
import com.example.movieboxoffice.enums.MovieDetailLength;
import com.example.movieboxoffice.service.impl.MovieBoxofficeServiceImpl;
import com.example.movieboxoffice.service.impl.MovieDetailServiceImpl;
import com.example.movieboxoffice.service.impl.SecondDoServiceImpl;
import com.github.yitter.idgen.YitIdHelper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.selector.Html;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Log4j2
public class HistoryBoxOfficeSpider {

    @Autowired
    private SecondDoServiceImpl secondDoService;
    @Autowired
    private MovieDetailServiceImpl movieDetailService;
    @Autowired
    private MovieBoxofficeServiceImpl movieBoxofficeService;


    public static final String url = "http://www.boxofficecn.com/boxoffice";

    public void dateSpider(Integer year)  {
        String res = null;
        try {
            String newUrl = url + year;
            HttpURLConnection connection = (HttpURLConnection) new URL(newUrl).openConnection();
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
                res = response.toString();
//                System.out.println(res);
            } else {
                System.err.println("请求失败，状态码：" + connection.getResponseCode());
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (res != null) {
            Html html = new Html(res);
            List<String> all = html.xpath("//*[@class='entry-content']/table/tbody/tr").all();

            List<MovieBoxoffice> existsMovies = movieBoxofficeService.getExistList();
            Set<String> set = existsMovies.stream().map(MovieBoxoffice::getMovieName).collect(Collectors.toSet());

            MovieBoxoffice movieBoxoffice = null ;
            System.out.println(all.size());
            for (int i = 1; i < all.size(); i++){
                System.out.println(i);
                String[] split = all.get(i).split(" <td>");
                int length = split.length;

                //处理电影名称
                String movieName = split[length-2].replace("</td>", "").replace("\n","");
                int hrefIndex = split[length - 2].indexOf("href=");
                Long movieCode = null;
                //处理详情
                if (hrefIndex != -1){
                    String name = movieName.replace("</a>","");
                    int index = name.lastIndexOf(">");
                    movieName = name.substring(index+1);
                    if (set.contains(movieName)) continue;

                    movieCode = YitIdHelper.nextId();
                    int targetIndex = split[length - 2].indexOf("target");
                    String href = split[length - 2].substring(hrefIndex + 6,targetIndex-2);
                    String https = href.replace("http", "https");
                    try {
                        getMovieDetail(https, movieName,  movieCode);
                    }catch ( Exception e){
                        log.error("电影详情爬取失败："+movieName);
                        SecondDo secondDo = new SecondDo();
                        secondDo.setMovieName(movieName);
                        secondDo.setMovieCode(movieCode);
                        secondDoService.save(secondDo);
                    }

                } else {
                    if (set.contains(movieName)) continue;

                    movieCode = YitIdHelper.nextId();
                    SecondDo secondDo = new SecondDo();
                    secondDo.setMovieName(movieName);
                    secondDo.setMovieCode(movieCode);
                    secondDoService.save(secondDo);

                }

                //处理票房
                String boxOffice = split[length-1].replace("</td>", "")
                        .replace("\n","").replace("</tr>","");
                BigDecimal boxOfficeBigDecimal = null;
                if (boxOffice.equals("-")){
                    boxOfficeBigDecimal = BigDecimal.ZERO;
                }else {
                    boxOfficeBigDecimal = parseBigDecimal(boxOffice);
                }

                movieBoxoffice = new MovieBoxoffice();
                movieBoxoffice.setMovieCode(movieCode);
                movieBoxoffice.setSumBoxoffice(boxOfficeBigDecimal);
                movieBoxoffice.setSumSplitBoxoffice(BigDecimal.ZERO);
                movieBoxoffice.setMovieName(movieName);
                movieBoxofficeService.save(movieBoxoffice);
            }

        }

    }

    private void getMovieDetail(String url ,String movieName,Long movieCode ) throws InterruptedException {
        Thread.sleep(1000);
        if (url == null) {
            System.out.println("电影详情获取失败");
            return;
        }
        Html html = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
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
                html = new Html(response.toString(), url);

            } else {
                System.err.println("请求失败，状态码：" + connection.getResponseCode());
                throw new RuntimeException("电影详情获取失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("电影详情获取失败");
        }
        if (html != null) {
            List<String> persons = html.xpath("//*[@id='info']/span/span[@class='attrs']").all();
            List<String> directors = new ArrayList<>();
            if (persons.size()>0) {
                directors = new Html(persons.get(0)).xpath("//a/text()").all();
            }
            String director = getValidString(directors, MovieDetailLength.DIRECTOR.getLength());
            String scripter = null;
            if (persons.size()>2) {
                List<String> scripters = new Html(persons.get(1)).xpath("//a/text()").all();
                scripter = getValidString(scripters, MovieDetailLength.SCIPTER.getLength());
            }
            String actor = null;
            List<String> actors = new ArrayList<>();
            if (persons.size()==2) {
                actors = new Html(persons.get(1)).xpath("//a/text()").all();

            }else if (persons.size()>2){
                actors = new Html(persons.get(2)).xpath("//a/text()").all();
            }
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
            if (scripter != null) {
                movieDetail.setScripter(scripter.trim());
            }
            movieDetail.setActor(actor.trim());
            movieDetail.setReleaseDate(releaseDate.trim());
            movieDetail.setType(type.trim());
            if (area != null) {
                movieDetail.setArea(area.trim());
            }
            if (time != null) {
                movieDetail.setLength(time.trim());
            }
            if (introduction != null) {
                if (introduction.length() > 1024) {
                    movieDetail.setIntroduction(introduction.substring(0, 1024));
                } else {
                    movieDetail.setIntroduction(introduction);
                }
            }

            String poster = html.xpath("//*[@id='mainpic']/a/img").get();
            int start = poster.indexOf("http");
            int end = poster.indexOf("title");
            poster = poster.substring(start, end-2);

            movieDetail.setPoster(poster);
            movieDetail.setMovieCode(movieCode);
            movieDetailService.save(movieDetail);
        }
    }

    private BigDecimal parseBigDecimal(String s){
        BigDecimal bigDecimal;
        try {
            bigDecimal = new BigDecimal(s);
        }catch (Exception e){
            bigDecimal = BigDecimal.ZERO;
        }
        return bigDecimal;

    }

    private String getValidString(List<String> list,Integer length){
        StringBuilder sb = new StringBuilder();
        if (list.size() == 0){
            return "";
        }
        for (String s : list) {
            if ((sb.length()+s.length()) > length)
                break;
            sb.append(s);
            sb.append(" ");
        }
        return sb.toString();
    }



}
