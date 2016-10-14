/**
 * Created by qianzise on 2016/10/11 0011.
 */
public class Util {
    public static final String WEB_BASE_URL="http://www.bilibili.com/video/av";  //视频网页
    public static final String API_BASE_URL="http://api.bilibili.com/archive_stat/stat?aid=";  //API网页

    public static final int MAX_THREADS=15;

    public static class Timer{

        private long startTime;
        private String title;

        public Timer(String string) {
            this.startTime = System.currentTimeMillis();
            this.title=string;
        }

        public void end(){
            System.out.println(title+"运行时间:"+(System.currentTimeMillis()-startTime) );
        }


    }

}
