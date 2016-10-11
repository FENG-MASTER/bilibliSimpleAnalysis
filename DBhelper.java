import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by qianzise on 2016/10/9 0009.
 */
public class DBhelper {
    public static final String USER = "root";
    public static final String PASSWORD = "1237891379";
    public static final String HOST = "localhost";
    public static final String PORT = "3306";


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
                " `name` CHAR(40) NULL DEFAULT NULL , " +
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
                " `author` CHAR(20) NULL DEFAULT NULL , " +
                "`mainArea` CHAR(10) NULL DEFAULT NULL , " +
                "`subArea` CHAR(10) NULL DEFAULT NULL ) ";
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

    public boolean addDate(int AV,
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
        }

        return res>0;

    }

    public boolean addDate(VideoInfo info ){
       return addDate(info.AV,info.name,info.view,
               info.danmuku,info.reply,info.favorite,
               info.coin,info.share,info.now_rank,info.his_rank,
               info.message,info.author,info.mainArea,info.subArea,info.getTags());
    }

}