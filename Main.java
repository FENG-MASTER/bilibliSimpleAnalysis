/**
 * Created by qianzise on 2016/10/9 0009.
 */
public class Main {

    public static void main(String args[]){

        DBhelper.Init();

        Spider spider=new Spider();
        spider.init();
        spider.crawl();
    //    spider.crawl(769);

        return;
    }
}
