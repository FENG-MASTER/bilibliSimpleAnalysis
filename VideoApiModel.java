/**
 * Created by qianzise on 2016/10/10 0010.
 */
public class VideoApiModel {


    public int code;
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
        public int view;
        public int danmuku;
        public int reply;
        public int favorite;
        public int coin;
        public int share;
        public int now_rank;
        public int his_rank;
        public String message;

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


}
