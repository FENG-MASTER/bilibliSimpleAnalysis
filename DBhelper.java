import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 数据库工具类
 */
public class DBhelper {
    public static final String USER = "root"; //用户名
    public static final String PASSWORD = "1237891379"; //密码
    public static final String HOST = "localhost"; //IP
    public static final String PORT = "3306";  //端口

    private CopyOnWriteArrayList<VideoInfo> videoInfos=new CopyOnWriteArrayList<>();
    public static final int MAX_QUEUE_LEN=100;


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
                "`datetime` DATETIME NULL DATETIME NULL,"+
                "`view` INT NOT NULL , " +
                "`danmaku` INT NOT NULL , " +
                "`reply` INT NOT NULL , " +
                "`favorite` INT NOT NULL , " +
                "`coin` INT NOT NULL ," +
                " `share` INT NOT NULL ," +
                " `now_rank` INT NOT NULL ," +
                " `his_rank` INT NOT NULL , " +
                " `tags` TEXT NULL,"+
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
                            String tags) {

        String sql="INSERT INTO `videos` " +
                "(`AV`, `name`, " +
                "`view`, `danmaku`," +
                " `reply`, `favorite`," +
                " `coin`, `share`, `now_rank`, " +
                "`his_rank`, `message`," +
                " `author`, `mainArea`, `subArea`,`tags`)" +
                " VALUES (" + AV + ", '" + name + "'," +
                view + "," + danmuku + ", " +
                reply + ", " + favorite + ", " +
                coin + ", " + share + ", " + now_rank + ", " +
                his_rank + ", '" + message + "', '" +
                autour + "', '" + mainArea + "', '" + subArea + "','"+tags+"')";


        int res=0;
        try {
            res = statement.executeUpdate(sql);


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(AV);
            System.out.println(sql);
        }

        return res>0;

    }

    private String getSQL(VideoInfo info){
        return "INSERT INTO `videos` " +
                "(`AV`, `name`,`datetime`, " +
                "`view`, `danmaku`," +
                " `reply`, `favorite`," +
                " `coin`, `share`, `now_rank`, " +
                "`his_rank`, `message`," +
                " `author`, `mainArea`, `subArea`,`tags`)" +
                " VALUES (" + info.AV + ", '" + info.name + "'," +info.getDate()+","+
                info.view + "," + info.danmuku + ", " +
                info.reply + ", " + info.favorite + ", " +
                info.coin + ", " + info.share + ", " + info.now_rank + ", " +
                info.his_rank + ", '" + info.message + "', '" +
                info.author + "', '" + info.mainArea + "', '" + info.subArea + "','"+info.getTags()+"')";

    }

    public synchronized boolean addDate(VideoInfo info ){

        checkInfo(info);

        if (videoInfos.size()>=MAX_QUEUE_LEN){
            addDates(videoInfos);
            videoInfos.clear();
        }else {
            videoInfos.add(info);
        }


//       return addDate(info.AV,info.name,info.view,
//               info.danmuku,info.reply,info.favorite,
//               info.coin,info.share,info.now_rank,info.his_rank,
//               info.message,info.author,info.mainArea,info.subArea,info.getTags());

        return true;
    }

    private void checkInfo(VideoInfo info){

        if (info.name!=null&&info.name.contains("'")){
            info.name= info.name.replace("'","''");
        }

        if (info.author!=null&&info.author.contains("'")){
            info.author= info.author.replace("'","''");
        }

        if (info.getTags()!=null&&!info.getTags().contains("\\'")&&info.getTags().contains("'")){
            info.setTags(info.getTags().replace("'","''"));
        }
    }


    private boolean addDates(List<VideoInfo> infoList){
        if (infoList.size()<=0){
            return false;
        }

        int count=infoList.size();
        StringBuilder builder=new StringBuilder();
        VideoInfo firstInfo=infoList.get(0);
        checkInfo(firstInfo);

        builder.append(getSQL(firstInfo));

        VideoInfo info=null;
        for (int i =1;i<count;i++){
            info=infoList.get(i);

            checkInfo(info);

            builder.append(",(" + info.AV + ", '" + info.name + "'," +info.getDate()+", "+
            info.view + "," + info.danmuku + ", " +
                    info.reply + ", " + info.favorite + ", " +
                    info.coin + ", " + info.share + ", " + info.now_rank + ", " +
                    info.his_rank + ", '" + info.message + "', '" +
                    info.author + "', '" + info.mainArea + "', '" + info.subArea + "','"+info.getTags()+"')");

        }



        int res=0;
        try {
            res = statement.executeUpdate(builder.toString());


        } catch (SQLException e) {
            e.printStackTrace();

        }
        return res>0;

    }

    public void finish(){
        if (!videoInfos.isEmpty()){
            addDates(videoInfos);
        }
    }

}
