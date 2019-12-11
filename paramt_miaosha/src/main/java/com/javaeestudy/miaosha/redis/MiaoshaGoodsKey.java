package com.javaeestudy.miaosha.redis;

public class MiaoshaGoodsKey extends BasePrefix {

    private MiaoshaGoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    public static MiaoshaGoodsKey getGoodsListKey = new MiaoshaGoodsKey(60, "goodsList");
    public static MiaoshaGoodsKey getGoodsKey = new MiaoshaGoodsKey(60, "goodsList");
    public static MiaoshaGoodsKey getGoodsStockKey = new MiaoshaGoodsKey(0, "goodsStockKey");
    public static MiaoshaGoodsKey getOrderKey = new MiaoshaGoodsKey(0, "orderKey");

}
