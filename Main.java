import Helper.DBhelper;

/**
 * bilibili爬虫程序
 * 作者:FENG-MASTER
 * 爬取b站所有视频的基础信息,包括
 * 名字,时间,播放量,收藏量,硬币数,分享量,评论数量,上传时间,标签,up主
 *
 */
public class Main {

    public static void main(String args[]) {

        DBhelper.Init();//初始化数据库

        Spider spider = new Spider();
        spider.init();
        spider.crawl();


        DBhelper.getInstance().finish();

    }
}
