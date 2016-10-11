import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by qianzise on 2016/10/10 0010.
 */
public class Spider {


    private ThreadPoolExecutor executor
            =new ThreadPoolExecutor(8,10,3, TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>());


    private AtomicInteger crawlNum=new AtomicInteger();

    public void crawl(){

        init();
        executor.execute(new CrawlTask(2333));

    }

    private void init(){
        crawlNum.set(1);
    }

}
