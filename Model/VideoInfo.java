package Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by qianzise on 2016/10/10 0010.
 */
public class VideoInfo {

    public int AV;//AV号
    public String name;//标题

    private Date date;

    public int view;//播放量
    public int danmuku;//弹幕数
    public int reply;//回复数
    public int favorite;//收藏数
    public int coin;//硬币数
    public int share;//分享数
    public int now_rank;//现在的排名
    public int his_rank;//历史最高排名
    public String message;// 目前不知道用处
    public String author;//UP主
    public String mainArea;//分区(主)
    public String subArea;//分区(子)

    private String tags;//标签


    private   SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public void setDate(String dateString){

        try {
            date= simpleDateFormat.parse(dateString.replace("T"," "));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getDate() {
        if (null==date){
            return "0000-00-00 00:00:00";
        }
        return simpleDateFormat.format(date)+":00";
    }



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

        if (list.size()==0){
            return;
        }
        Iterator<String> iterator=list.iterator();
        StringBuilder builder=new StringBuilder();

        while (iterator.hasNext()){
            builder.append(iterator.next());
            builder.append(",");
        }

        builder.deleteCharAt(builder.length()-1);

        setTags(builder.toString());
    }


    @Override
    public String toString() {
        return "AV:"+AV+" name:"+name;
    }
}
