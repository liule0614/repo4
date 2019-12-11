package com.javaeestudy.miaosha.service;

import com.javaeestudy.miaosha.dimain.MiaoshaGoods;
import com.javaeestudy.miaosha.dimain.MiaoshaOrder;
import com.javaeestudy.miaosha.dimain.MiaoshaUser;
import com.javaeestudy.miaosha.dimain.OrderInfo;
import com.javaeestudy.miaosha.mapper.MiaoshaGoodsMapper;
import com.javaeestudy.miaosha.redis.MiaoshaGoodsKey;
import com.javaeestudy.miaosha.redis.RedisService;
import com.javaeestudy.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class MiaoshaGoodsService {


    @Autowired
    private MiaoshaGoodsMapper goodsMapper;
    @Autowired
    private RedisService redisService;

    public List<GoodsVo> queryGoodsList() {

        return this.goodsMapper.queryGoodsList();
    }

    public GoodsVo queryGoodsByGoodsId(Long goodsId) {
        return this.goodsMapper.queryGoodsByGoodsId(goodsId);
    }

    public int reduceCount(GoodsVo goods) {
        return this.goodsMapper.reduceCount(goods);
    }

    @Transactional
    public OrderInfo insertOrder(MiaoshaUser user, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setGoodsChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderInfo.setOrderNumber(MiaoshaGoodsService.getOrderId(user.getId(),goods.getId()));
        int i = this.goodsMapper.insertOrderInfo(orderInfo);

        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(goods.getId());
        miaoshaOrder.setOrderId(orderInfo.getId());
        miaoshaOrder.setUserId(user.getId());
        int i1 = this.goodsMapper.insertMiaoshaOrder(miaoshaOrder);
        if(i>0 && i1>0){
            this.redisService.set(MiaoshaGoodsKey.getOrderKey,""+user.getId()+""+goods.getId(),miaoshaOrder);
            return orderInfo;
        }
        return null;
    }

    public void resetStock(List<GoodsVo> goodsList) {
        for(GoodsVo goods : goodsList ) {
            MiaoshaGoods g = new MiaoshaGoods();
            g.setGoodsId(goods.getId());
            g.setStockCount(goods.getStockCount());
            goodsMapper.resetStock(g);
        }
    }

    public static String getOrderId(Long userId,Long goodsId){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(new Date());
        String result="";
        Random random = new Random();
        for (int i = 0;i<3;i++){
            result+=random.nextInt(10);
        }
        String s = userId.toString();
        String s1 = goodsId.toString();
        return format+s+s1+result;
    }


}
