/**
 * 模型类,这个是根据b站给的api的返回json格式,对应的模型类,包含了一些视频相关的数据
 */
public class VideoApiModel {


    public int code;//不知道是干嘛的数据,好像一直是0
    public data data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public VideoApiModel.data getData() {
        return data;
    }

    public void setData(VideoApiModel.data data) {
        this.data = data;
    }


    public class data{
        public int view;//播放量
        public int danmuku;//弹幕数
        public int reply;//回复数
        public int favorite;//收藏数
        public int coin;//硬币数
        public int share;//分享数
        public int now_rank;///目前站内排名
        public int his_rank;//历史站内最高排名
        public String message;//也不知道这个参数是什么...好像一直是空的

        public int getView() {
            return view;
        }

        public void setView(int view) {
            this.view = view;
        }

        public int getDanmuku() {
            return danmuku;
        }

        public void setDanmuku(int danmuku) {
            this.danmuku = danmuku;
        }

        public int getReply() {
            return reply;
        }

        public void setReply(int reply) {
            this.reply = reply;
        }

        public int getFavorite() {
            return favorite;
        }

        public void setFavorite(int favorite) {
            this.favorite = favorite;
        }

        public int getCoin() {
            return coin;
        }

        public void setCoin(int coin) {
            this.coin = coin;
        }

        public int getShare() {
            return share;
        }

        public void setShare(int share) {
            this.share = share;
        }

        public int getNow_rank() {
            return now_rank;
        }

        public void setNow_rank(int now_rank) {
            this.now_rank = now_rank;
        }

        public int getHis_rank() {
            return his_rank;
        }

        public void setHis_rank(int his_rank) {
            this.his_rank = his_rank;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public boolean hasData(){
        return !(data.share==0&&data.view==0&&
                data.reply==0&& data.his_rank==0&&
                data.now_rank==0&& data.coin==0&&
                data.danmuku==0&&data.favorite==0);
    }


}
