package com.javaeestudy.miaosha.vo;

import com.javaeestudy.miaosha.dimain.MiaoshaUser;

public class GoodsDetailVo {

    private MiaoshaUser user;
    private GoodsVo goods;

    private long miaoshaStatus ;
    private long remainSeconds ;

    public MiaoshaUser getUser() {
        return user;
    }

    public void setUser(MiaoshaUser user) {
        this.user = user;
    }


    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }

    public long getMiaoshaStatus() {
        return miaoshaStatus;
    }

    public void setMiaoshaStatus(long miaoshaStatus) {
        this.miaoshaStatus = miaoshaStatus;
    }

    public long getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(long remainSeconds) {
        this.remainSeconds = remainSeconds;
    }
}
