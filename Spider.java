

import Helper.DBhelper;
import util.Util;
import task.CrawlTask;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 爬虫主程序
 */
 class Spider {


    private ThreadPoolExecutor executor = null;//线程池

    private AtomicInteger crawlNum = new AtomicInteger(1);//线程安全的整数,这个是AV号

    private AtomicInteger numOfThreads = new AtomicInteger(0);

    private static final int MAX_AV = 8999999;


    public void crawl() {

        long startTime = System.currentTimeMillis();

        crawlNum.set(DBhelper.getInstance().getLastTimeID());

        System.out.println("起始ID:" + crawlNum.intValue());

        while (crawlNum.intValue() < MAX_AV || numOfThreads.intValue() != 0) {

            if (crawlNum.intValue() % 1000 == 0) {
                System.out.println("进度:" + crawlNum);
            }

            if (crawlNum.intValue() < MAX_AV) {
                executor.execute(new CrawlTask(numOfThreads, crawlNum.intValue()));
                crawlNum.addAndGet(1);//++

                if (numOfThreads.intValue() >= Util.MAX_THREADS) {

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        executor.shutdown();

        long useTime = System.currentTimeMillis() - startTime;
        System.out.println("历时:" + useTime / 1000 + "秒");

    }

    /*
    * 用于测试,单个AV号信息获取
    * */
    public void crawl(int num) {
        executor.execute(new CrawlTask(numOfThreads, num));
    }

    public void init() {
        executor = new ThreadPoolExecutor(Util.MAX_THREADS, Util.MAX_THREADS + 10, 3, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());

    }


}
