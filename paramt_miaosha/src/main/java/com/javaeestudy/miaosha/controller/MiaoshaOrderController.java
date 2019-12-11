package com.javaeestudy.miaosha.controller;

import com.javaeestudy.miaosha.dimain.MiaoshaUser;
import com.javaeestudy.miaosha.dimain.OrderInfo;
import com.javaeestudy.miaosha.result.CodeMsg;
import com.javaeestudy.miaosha.result.Result;
import com.javaeestudy.miaosha.service.MiaoshaGoodsService;
import com.javaeestudy.miaosha.service.MiaoshaOrderService;
import com.javaeestudy.miaosha.vo.GoodsVo;
import com.javaeestudy.miaosha.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/order")
public class MiaoshaOrderController {

    @Autowired
    private MiaoshaGoodsService goodsService;
    @Autowired
    private MiaoshaOrderService orderService;

    @RequestMapping("/detail")
    @ResponseBody
    public  Result<OrderDetailVo> detail(@RequestParam("orderId") Long orderId, MiaoshaUser user) {
           //1首先判断用户是否登录，如果未登录，则返回
           if(user==null) {
               return Result.error(CodeMsg.SESSION_ERROR);
           }
            //2查询 订单OrderInfo
           OrderInfo order = this.orderService.queryOrderByOrderId(orderId);
           if(order==null) {
               return Result.error(CodeMsg.ORDER_NOT_FIND);
           }
            //3查询 goodsVo
           GoodsVo goods = this.goodsService.queryGoodsByGoodsId(order.getGoodsId());
           OrderDetailVo vo = new OrderDetailVo();
           vo.setGoods(goods);
           vo.setOrder(order);
           return Result.success(vo);
       }

}
