package com.example.movieboxoffice.spider.daily;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.movieboxoffice.entity.BoxOfficeWeb;
import com.example.movieboxoffice.entity.DailyBoxoffice;
import com.example.movieboxoffice.entity.DailySumBoxoffice;
import com.example.movieboxoffice.entity.MovieDo;
import com.example.movieboxoffice.service.RedisService;
import com.example.movieboxoffice.service.impl.DailyBoxofficeServiceImpl;
import com.example.movieboxoffice.service.impl.DailySumBoxofficeServiceImpl;
import com.example.movieboxoffice.service.impl.MovieDoServiceImpl;
import com.example.movieboxoffice.utils.MyConstant;
import com.example.movieboxoffice.utils.MyDateUtils;
import com.github.yitter.idgen.YitIdHelper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Log4j2
public class DailyBoxOfficeSpider {

//    @Autowired
//    private DailySpiderPageProcessor spiderPageProcessor;
//    @Autowired
//    private DailyBoxOfficePipeline boxOfficePipeline;
//    @Autowired
//    private DefaultDailyBoxOfficePipeline defaultDailyBoxOfficePipeline;
    @Autowired
    private RedisService redisService;
    @Autowired
    private MovieDoServiceImpl movieDoService;
    @Autowired
    private DailyBoxofficeServiceImpl dailyBoxofficeService;
    @Autowired
    private DailySumBoxofficeServiceImpl dailySumBoxofficeService;


//    public  Spider getDefaultSpider(){
//        String date = MyDateUtils.getNowStringDate(MyDateUtils.YYMMDD);
//        String url = "https://zgdypf.zgdypw.cn/getDayData?date="+date+"&withSvcFee=true";
//        return Spider.create(spiderPageProcessor).addPipeline(defaultDailyBoxOfficePipeline).addUrl(url).thread(3);
//    }
//
//    public Spider getDateSpider(String dates){
//
//        String urls = "https://zgdypf.zgdypw.cn/getDayData?withSvcFee=true&date="+dates+"&dateType=0";
//        return Spider.create(spiderPageProcessor).addPipeline(boxOfficePipeline).addUrl(urls).thread(3);
//    }

    public  void defaultSpider(){
        String date = MyDateUtils.getNowStringDate(MyDateUtils.YYMMDD);
        String url = "https://zgdypf.zgdypw.cn/getDayData?date="+date+"&withSvcFee=true";
        defaultSpiderCrawl(url,date);
    }

    public void dateSpider(String dates){
        String urls = "https://zgdypf.zgdypw.cn/getDayData?withSvcFee=true&date="+dates+"&dateType=0";
        dateSpider(urls ,dates);
    }


    private void defaultSpiderCrawl(String url,String date ){
        String res = null;
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
                res = response.toString();

            } else {
                System.err.println("请求失败，状态码：" + connection.getResponseCode());
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (res != null) {
            JSONObject jsonObject = JSON.parseObject(res);
            String str = jsonObject.getString("list");
            List<BoxOfficeWeb> list = JSON.parseArray(str, BoxOfficeWeb.class);
            log.error("------------size:"+list.size());
            List<DailyBoxoffice> dailyBoxoffices = new ArrayList<>();
            List<MovieDo> existsMovies = movieDoService.selectExistList();
            Map<String, Long> map = existsMovies.stream().collect(Collectors.toMap(MovieDo::getMovieName, MovieDo::getMovieCode));
            DailyBoxoffice dailyBoxoffice = null;
            for (BoxOfficeWeb boxOffice :list ){
                dailyBoxoffice = new DailyBoxoffice();
                dailyBoxoffice.setRecordDate(date);
                dailyBoxoffice.setId(null);
                dailyBoxoffice.setDayBoxoffice(parseBigDecimal(boxOffice.getSalesInWanDesc()));
                if (dailyBoxoffice.getDayBoxoffice().equals(BigDecimal.ZERO)) break;
                dailyBoxoffice.setDaySplitBoxoffice(parseBigDecimal(boxOffice.getSplitSalesInWanDesc()));
                dailyBoxoffice.setMovieName(boxOffice.getName());
                dailyBoxoffice.setSumBoxoffice(boxOffice.getSumSalesDesc());
                dailyBoxoffice.setSumSplitBoxoffice(boxOffice.getSumSplitSalesDesc());
                dailyBoxoffice.setReleaseDays(Integer.parseInt(boxOffice.getReleaseDays()));
                long movieCode ;
                if (map.get(dailyBoxoffice.getMovieName())!=null) {
                    movieCode =  map.get(dailyBoxoffice.getMovieName());
                }else {
                    MovieDo movieDo = new MovieDo();
                    movieCode= YitIdHelper.nextId();
                    movieDo.setMovieCode(movieCode);
                    movieDo.setMovieName(dailyBoxoffice.getMovieName());
                    movieDo.setMovieDate(date);
                    movieDo.setMovieYear(Integer.parseInt(date.substring(0,4)));
                    movieDoService.save(movieDo);
                }
                dailyBoxoffice.setMovieCode(movieCode);
                dailyBoxoffice.setDayBoxofficeRate(boxOffice.getSalesRateDesc());
                dailyBoxoffice.setDaySplitBoxofficeRate(boxOffice.getSplitSalesRateDesc());
                dailyBoxoffice.setDayArrangeRate(boxOffice.getSessionRateDesc());
                dailyBoxoffice.setDaySeatRate(boxOffice.getSeatRateDesc());
                dailyBoxoffices.add(dailyBoxoffice);

            }
            redisService.set(MyConstant.TODAY_DAILY_BOXOFFICELIST, JSONArray.toJSONString(dailyBoxoffices));

            JSONObject nationalSales = (JSONObject) jsonObject.get("nationalSales");
            JSONObject salesDesc = (JSONObject)nationalSales.get("salesDesc");
            JSONObject splitSalesDesc = (JSONObject)nationalSales.get("splitSalesDesc");
            String sales = salesDesc.get("value").toString()+salesDesc.get("unit").toString();
            String splitSales = splitSalesDesc.get("value").toString()+splitSalesDesc.get("unit").toString();
            DailySumBoxoffice dailySumBoxoffice = new DailySumBoxoffice();
            dailySumBoxoffice.setSumBoxoffice(sales);
            dailySumBoxoffice.setSumSplitBoxoffice(splitSales);
            dailySumBoxoffice.setDate(date);
            redisService.set(MyConstant.TODAY_DAILY_SUMBOXOFFICE, JSONObject.toJSONString(dailySumBoxoffice));

        }


    }

    private void dateSpider(String url , String date){
        String res = null;
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
                res = response.toString();

            } else {
                System.err.println("请求失败，状态码：" + connection.getResponseCode());
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (res != null) {
            JSONObject jsonObject = JSON.parseObject(res);
            String str = jsonObject.getString("list");
            List<BoxOfficeWeb> list = JSON.parseArray(str, BoxOfficeWeb.class);
            log.error("------------size:"+list.size());
            List<MovieDo> existsMovies = movieDoService.selectExistList();
            Map<String, Long> map = existsMovies.stream().collect(Collectors.toMap(MovieDo::getMovieName, MovieDo::getMovieCode));
            DailyBoxoffice dailyBoxoffice = null;

            for (BoxOfficeWeb boxOffice :list ){
                dailyBoxoffice = new DailyBoxoffice();
                dailyBoxoffice.setRecordDate(date);
                dailyBoxoffice.setId(null);
                dailyBoxoffice.setDayBoxoffice(parseBigDecimal(boxOffice.getSalesInWanDesc()));
                if (dailyBoxoffice.getDayBoxoffice().equals(BigDecimal.ZERO)) break;
                dailyBoxoffice.setDaySplitBoxoffice(parseBigDecimal(boxOffice.getSplitSalesInWanDesc()));
                dailyBoxoffice.setMovieName(boxOffice.getName());
                dailyBoxoffice.setSumBoxoffice(boxOffice.getSumSalesDesc());
                dailyBoxoffice.setSumSplitBoxoffice(boxOffice.getSumSplitSalesDesc());
                dailyBoxoffice.setReleaseDays(Integer.parseInt(boxOffice.getReleaseDays()));
                long movieCode ;
                if (map.get(dailyBoxoffice.getMovieName())!=null) {
                    movieCode =  map.get(dailyBoxoffice.getMovieName());
                }else {
                    MovieDo movieDo = new MovieDo();
                    movieCode= YitIdHelper.nextId();
                    movieDo.setMovieCode(movieCode);
                    movieDo.setMovieName(dailyBoxoffice.getMovieName());
                    movieDo.setMovieDate(date);
                    movieDo.setMovieYear(Integer.parseInt(date.substring(0,4)));
                    movieDoService.save(movieDo);
                }
                dailyBoxoffice.setMovieCode(movieCode);
                dailyBoxoffice.setDayBoxofficeRate(boxOffice.getSalesRateDesc());
                dailyBoxoffice.setDaySplitBoxofficeRate(boxOffice.getSplitSalesRateDesc());
                dailyBoxoffice.setDayArrangeRate(boxOffice.getSessionRateDesc());
                dailyBoxoffice.setDaySeatRate(boxOffice.getSeatRateDesc());

                dailyBoxofficeService.save(dailyBoxoffice);
            }

            JSONObject nationalSales = (JSONObject) jsonObject.get("nationalSales");
            JSONObject salesDesc = (JSONObject)nationalSales.get("salesDesc");
            JSONObject splitSalesDesc = (JSONObject)nationalSales.get("splitSalesDesc");
            String sales = salesDesc.get("value").toString()+salesDesc.get("unit").toString();
            String splitSales = splitSalesDesc.get("value").toString()+splitSalesDesc.get("unit").toString();
            DailySumBoxoffice dailySumBoxoffice = new DailySumBoxoffice();
            dailySumBoxoffice.setSumBoxoffice(sales);
            dailySumBoxoffice.setSumSplitBoxoffice(splitSales);
            dailySumBoxoffice.setDate(date);
            dailySumBoxofficeService.save(dailySumBoxoffice);

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



}
