/**
 * Created by qianzise on 2016/10/9 0009.
 */
public class Main {

    public static void main(String args[]){

        DBhelper.Init();//初始化数据库

        Spider spider=new Spider();
        spider.init();
     //   spider.crawl();

        spider.crawl(523526);



        DBhelper.getInstance().finish();
        return;
    }
}
