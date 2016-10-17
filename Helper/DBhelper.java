package Helper;

import Model.VideoInfo;
import org.apache.commons.lang3.StringEscapeUtils;

import java.sql.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 数据库工具类
 */
public class DBhelper {
    private static final String USER = "root"; //用户名
    private static final String PASSWORD = "1237891379"; //密码
    private static final String HOST = "localhost"; //IP
    private static final String PORT = "3306";  //端口

    private CopyOnWriteArrayList<VideoInfo> videoInfos = new CopyOnWriteArrayList<>();//采用缓存的机制,把读取到的数据都存list,统一写入

    private static final int MAX_QUEUE_LEN = 150;//最大缓存大小.


    private static DBhelper instance;
    private Connection connection;
    private Statement statement;

    public static DBhelper getInstance() {
        return instance;
    }

    public static void Init() {
        instance = new DBhelper();
    }

    private DBhelper() {

        String url = "jdbc:mysql://" + HOST + ":" + PORT + "/bilibili?"
                + "user=" + USER + "&password=" + PASSWORD + "&useUnicode=true&characterEncoding=UTF8";

        try {
            Class.forName("com.mysql.jdbc.Driver");

            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();

            if (!hasTable()) {
                createTable();
            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private void createTable() {
        String sql = "CREATE TABLE `videos` ( " +
                "`AV` INT(15) NOT NULL  PRIMARY KEY ," +
                " `name` TEXT NULL DEFAULT NULL , " +
                "`datetime` DATETIME NULL DEFAULT NULL," +
                "`view` INT NOT NULL , " +
                "`danmaku` INT NOT NULL , " +
                "`reply` INT NOT NULL , " +
                "`favorite` INT NOT NULL , " +
                "`coin` INT NOT NULL ," +
                " `share` INT NOT NULL ," +
                " `now_rank` INT NOT NULL ," +
                " `his_rank` INT NOT NULL , " +
                " `tags` TEXT NULL," +
                "`message` TEXT  NULL ," +
                " `author` CHAR(30) NULL DEFAULT NULL , " +
                "`mainArea` CHAR(30) NULL DEFAULT NULL , " +
                "`subArea` CHAR(30) NULL DEFAULT NULL ) ";
        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("数据库已经存在");
        }
    }

    private boolean hasTable() {
        String sql = "SELECT * FROM `videos`";
        try {
            statement.executeQuery(sql);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Deprecated
    private boolean addDate(int AV,
                            String name,
                            int view,
                            int danmuku,
                            int reply,
                            int favorite,
                            int coin,
                            int share,
                            int now_rank,
                            int his_rank,
                            String message,
                            String autour,
                            String mainArea,
                            String subArea,
                            String tags,
                            String datetime) {


        String sql = "INSERT INTO `videos` " +
                "(`AV`, `name`, " + "`datetime`," +
                "`view`, `danmaku`," +
                " `reply`, `favorite`," +
                " `coin`, `share`, `now_rank`, " +
                "`his_rank`, `message`," +
                " `author`, `mainArea`, `subArea`,`tags`)" +
                " VALUES (" + AV + ", '" + name + "','" + datetime + "'," +
                view + "," + danmuku + ", " +
                reply + ", " + favorite + ", " +
                coin + ", " + share + ", " + now_rank + ", " +
                his_rank + ", '" + message + "', '" +
                autour + "', '" + mainArea + "', '" + subArea + "','" + tags + "')";


        int res = 0;
        try {


            res = statement.executeUpdate(sql);


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(AV);
            System.out.println(sql);
        }


        return res > 0;

    }

    private String getSQL(VideoInfo info) {
        return "REPLACE INTO `videos` " +
                "(`AV`, `name`,`datetime`, " +
                "`view`, `danmaku`," +
                " `reply`, `favorite`," +
                " `coin`, `share`, `now_rank`, " +
                "`his_rank`, `message`," +
                " `author`, `mainArea`, `subArea`,`tags`)" +
                " VALUES (" + info.AV + ", '" + info.name + "','" + info.getDate() + "'," +
                info.view + "," + info.danmuku + ", " +
                info.reply + ", " + info.favorite + ", " +
                info.coin + ", " + info.share + ", " + info.now_rank + ", " +
                info.his_rank + ", '" + info.message + "', '" +
                info.author + "', '" + info.mainArea + "', '" + info.subArea + "','" + info.getTags() + "')";

    }

    /**
     * 统一把缓存的数据写入数据库
     * 这样做可以加快速度
     *
     * @param info 要存的晚会信息
     * @return 是否存储成功
     */
    public synchronized boolean addDate(VideoInfo info) {

        checkInfo(info);

        boolean res = false;

        if (videoInfos.size() >= MAX_QUEUE_LEN) {
            res = addDates(videoInfos);
            videoInfos.clear();
        } else {
            videoInfos.add(info);
        }

        return res;
    }

    /**
     * 用于检查并处理视频信息中是否有需要转义的字符,
     * 防止sql注入,和报错
     *
     * @param info 晚会信息
     */
    private void checkInfo(VideoInfo info) {

        info.name = StringEscapeUtils.escapeXml10(info.name);
        info.author = StringEscapeUtils.escapeXml10(info.author);
        info.setTags(StringEscapeUtils.escapeXml10(info.getTags()));
    }


    private boolean addDates(List<VideoInfo> infoList) {


        if (infoList.size() <= 0) {
            return false;
        }

        int count = infoList.size();
        StringBuilder builder = new StringBuilder();
        VideoInfo firstInfo = infoList.get(0);
        checkInfo(firstInfo);

        builder.append(getSQL(firstInfo));

        VideoInfo info ;

        for (int i = 1; i < count; i++) {
            info = infoList.get(i);

            checkInfo(info);

            builder.append(",(").append(info.AV).append(", '").append(info.name).append("','").append(info.getDate()).append("', ").append(info.view).append(",").append(info.danmuku).append(", ").append(info.reply).append(", ").append(info.favorite).append(", ").append(info.coin).append(", ").append(info.share).append(", ").append(info.now_rank).append(", ").append(info.his_rank).append(", '").append(info.message).append("', '").append(info.author).append("', '").append(info.mainArea).append("', '").append(info.subArea).append("','").append(info.getTags()).append("')");

        }

        int res = 0;
        try {
            res = statement.executeUpdate(builder.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return res >= infoList.size();

    }

    public void finish() {

        if (!videoInfos.isEmpty()) {
            addDates(videoInfos);
        }
    }

    /**
     * 获取数据库中最大的ID
     *
     * @return 返回最大ID
     */
    private int getMaxAID() {
        String sql = "SELECT MAX(`AV`) AS `MAXAV` FROM `videos` ";

        try {
            ResultSet set = statement.executeQuery(sql);
            if (set.next()) {
                return set.getInt("MAXAV");
            } else {
                return 1;
            }
        } catch (SQLException e) {

            return 1;
        }

    }

    /**
     * 获取这次爬虫开始的ID位置
     * 用了最无赖的方法,正确方式应该是在每次关闭后要确认所有线程数据都写进的数据库的时候,然后获取最大ID才是下次运行的ID
     * 但不想又写个线程去控制,所以采取这种方式,直接获取最大ID-1000作为下次的起始ID
     *
     * @return 返回这次开始爬虫的起始ID
     */
    public int getLastTimeID() {
        int lastID = getMaxAID();
        if (lastID < 1100) {
            return 1;
        } else {
            return lastID - 1000;
        }
    }


}
