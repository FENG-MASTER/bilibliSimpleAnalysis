import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


/**
 *  单个爬虫任务,负责将特定的AV号的视频信息趴取下来
 */
public class CrawlTask implements Runnable {

    private int num;//要处理的AV号

    private DBhelper dBhelper;//数据库

    private AtomicInteger numOfThreads;//当前使用线程数(活动的爬虫数目)

    public CrawlTask(AtomicInteger numOfThreads, int num) {
        this.num = num;
        this.numOfThreads=numOfThreads;
        numOfThreads.addAndGet(1);
        dBhelper = DBhelper.getInstance();
    }


    @Override
    public void run() {

        VideoInfo info=getInfo(num);
        if (info!=null){
            dBhelper.addDate(info);
            System.out.println(info.AV);
        }
        numOfThreads.addAndGet(-1);
    }


    private VideoInfo getInfo(int AVNum){

        VideoApiModel videoApiModel=formatJson(getJsonData(Util.API_BASE_URL+num));
        if (!videoApiModel.hasData()){
            //如果API返回的数据全部为0.那认为没有这个视频,不进行更深入的数据获取
            return null;
        }

        VideoInfo info=getVideoInfo();

        //重新包装更多的数据来源
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
            reader=new BufferedReader(new InputStreamReader(url.openStream()));//耗时部分,读取网页内容

            String temp = reader.readLine();
            while (temp != null) {
                builder.append(temp);
                temp = reader.readLine();
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return builder.toString();

    }



    /*
    * 获取更详细的信息
    *
    * */
    private VideoInfo getVideoInfo() {

        VideoInfo info=new VideoInfo();

        String title;
        String author=null;
        String mainArea;
        String subArea;
        List<String> tags = new ArrayList<>();



        Document document = null;

        boolean isGet=false;

        while (!isGet){
            //这个循环是为了连接超时,无限重试
            try {
                document = Jsoup.connect(Util.WEB_BASE_URL + num).get();//耗时部分,读取网页内容
                isGet=true;
            } catch (IOException e) {
              isGet=false;
            }

        }



        if(document.getElementsByClass("tminfo").size()<=0){
            //能进入到这个if里,证明页面上没有这个标签,也就是找不到视频或者视频被删除或者仅会员才能看的视频等,,,
            // 这边我统一处理,先把API给的数据留下,其他空
            VideoInfo emptyInfo=new VideoInfo();
            emptyInfo.AV=num;//记得把AV号存一下
            return emptyInfo;
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
                author=metaElements.get(i).attr("content");//这个是检测网页头的meta元素里的author,这个我看了下,是UP主的名字,我就把这个当作UP主名字存起来
                break;
            }
        }


        int tagsSize = tagsElements.size();
        for (int i = 0; i < tagsSize; i++) {
            tags.add(tagsElements.get(i).child(0).html());//标签列表
        }

        info.name=title;
        info.author=author;
        info.mainArea=mainArea;
        info.subArea=subArea;
        info.setTags(tags);

        return info;

    }



}
