import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by qianzise on 2016/10/10 0010.
 */
public class CrawlTask implements Runnable {

    private int num;
    private String ApiUrl;
    private DBhelper dBhelper;



    public CrawlTask(int num) {
        this.num = num;
        this.ApiUrl = Util.API_BASE_URL+num;
        dBhelper = DBhelper.getInstance();
    }


    @Override
    public void run() {

        VideoInfo info=getInfo(num);
        dBhelper.addDate(info);
    }


    private VideoInfo getInfo(int AVNum){

        VideoApiModel videoApiModel=formatJson(getJsonData(ApiUrl));
        VideoInfo info=getVideoInfo();

        info.AV=AVNum;
        info.coin=videoApiModel.getData().getCoin();
        info.danmuku=videoApiModel.getData().getDanmuku();
        info.favorite=videoApiModel.getData().getFavorite();
        info.his_rank=videoApiModel.getData().getHis_rank();
        info.now_rank=videoApiModel.getData().getNow_rank();
        info.reply=videoApiModel.getData().getReply();
        info.share=videoApiModel.getData().getShare();
        info.view=videoApiModel.getData().getView();
        info.message=videoApiModel.getData().getMessage();

        return info;
    }

    private VideoApiModel formatJson(String json) {
        Gson gson = new Gson();
        VideoApiModel video = gson.fromJson(json, VideoApiModel.class);//部分信息
        return video;
    }

    private String getJsonData(String sUrl){

        URL url=null;
        BufferedReader reader=null;

        StringBuilder builder = new StringBuilder();

        try {
            url=new URL(sUrl);
            reader=new BufferedReader(new InputStreamReader(url.openStream()));

            String temp = reader.readLine();
            while (temp != null) {
                builder.append(temp);
                temp = reader.readLine();
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.toString();

    }




    private VideoInfo getVideoInfo() {

        VideoInfo info=new VideoInfo();

        String title;
        String author=null;
        String mainArea;
        String subArea;
        List<String> tags = new ArrayList<>();



        Document document = null;
        try {
            document = Jsoup.connect(Util.WEB_BASE_URL + num).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements areaElements = document.getElementsByClass("tminfo").get(0).children();
        Elements tagsElements = document.getElementsByClass("tag-list").get(0).children();
        Elements metaElements=document.getElementsByTag("meta");

        title = document.getElementsByClass("v-title").get(0).getElementsByTag("h1").get(0).html();
        mainArea = areaElements.get(1).child(0).html();
        subArea = areaElements.get(2).child(0).html();

        int metaNum=metaElements.size();
        for (int i =0;i<metaNum;i++){
            if(metaElements.get(i).attr("name").equals("author")){
                author=metaElements.get(i).attr("content");
                break;
            }
        }


        int tagsSize = tagsElements.size();
        for (int i = 0; i < tagsSize; i++) {
            tags.add(tagsElements.get(i).child(0).html());
        }

        info.name=title;
        info.author=author;
        info.mainArea=mainArea;
        info.subArea=subArea;
        info.setTags(tags);


        return info;


    }





}
