

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by qianzise on 2016/10/10 0010.
 */
public class Spider {


    private ThreadPoolExecutor executor=null;


    private AtomicInteger crawlNum=new AtomicInteger();

    public static final int MAX_AV=7099999;


    public void crawl(){

        long startTime=System.currentTimeMillis();

        while (crawlNum.intValue()<MAX_AV){

            executor.execute(new CrawlTask(crawlNum.intValue()));
            crawlNum.addAndGet(1);
        }

        executor.shutdown();

        long useTime=System.currentTimeMillis()-startTime;
        System.out.println("历时:"+   useTime/1000/60 +"分钟");

    }

    public void crawl(int num){
        executor.execute(new CrawlTask(num));
    }

    public void init(){
        executor =new ThreadPoolExecutor(8,10,3, TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>());
        crawlNum.set(1);
    }

}
