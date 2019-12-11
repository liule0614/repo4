package com.javaeestudy.miaosha.service;

import com.javaeestudy.miaosha.dimain.MiaoshaOrder;
import com.javaeestudy.miaosha.dimain.MiaoshaUser;
import com.javaeestudy.miaosha.dimain.OrderInfo;
import com.javaeestudy.miaosha.mapper.MiaoshaOrderMapper;
import com.javaeestudy.miaosha.redis.MiaoshaGoodsKey;
import com.javaeestudy.miaosha.redis.RedisService;
import com.javaeestudy.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MiaoshaOrderService {

    @Autowired
    private MiaoshaOrderMapper orderMapper;
    @Autowired
    private RedisService redisService;

    public MiaoshaOrder queryGoodsByUserIdAndGoodsId(long id, long goodsId) {
        MiaoshaOrder order = this.redisService.get(MiaoshaGoodsKey.getOrderKey, "" + id + "" + goodsId, MiaoshaOrder.class);
        if(order!=null){
            return order;
        }
        return orderMapper.queryGoodsByUserIdAndGoodsId(id,goodsId);
    }


    public OrderInfo queryOrderByOrderId(Long orderId) {
        return orderMapper.queryOrderByOrderId(orderId);
    }

    public void deleteOrders() {
        orderMapper.deleteOrders();
        orderMapper.deleteMiaoshaOrders();

    }

    public Boolean updateOrderState(String orderNumber) {
        int i = this.orderMapper.updateOrderState(orderNumber);
        return i>0;
    }
}
