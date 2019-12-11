package com.javaeestudy.miaosha.redis;

public class MiaoshaKey extends BasePrefix {


    public MiaoshaKey(int expireSeconds, String prefix){
        super(expireSeconds,prefix);
    }

    public static MiaoshaKey getIsGoodsOver = new MiaoshaKey(0,"isGoodsOver");

    public static MiaoshaKey getPath = new MiaoshaKey(20,"path");

    public static MiaoshaKey getMiaoshaVerifyCode = new MiaoshaKey(60,"miaoshaVerifyCode");
}
