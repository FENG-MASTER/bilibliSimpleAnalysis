import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by qianzise on 2016/10/10 0010.
 */
public class VideoInfo {

    public int AV;
    public String name;
    public int view;
    public int danmuku;
    public int reply;
    public int favorite;
    public int coin;
    public int share;
    public int now_rank;
    public int his_rank;
    public String message;
    public String author;
    public String mainArea;
    public String subArea;


    private String tags;

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public List<String> getTagsList(){
        return Arrays.asList(tags.split(","));
    }

    public void setTags(List<String> list){
        Iterator<String> iterator=list.iterator();
        StringBuilder builder=new StringBuilder();

        while (iterator.hasNext()){
            builder.append(iterator.next());
            builder.append(",");
        }

        builder.deleteCharAt(builder.length()-1);

        setTags(builder.toString());
    }



}
