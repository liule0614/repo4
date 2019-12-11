package com.javaeestudy.miaosha.mapper;

import com.javaeestudy.miaosha.dimain.MiaoshaOrder;
import com.javaeestudy.miaosha.dimain.OrderInfo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface MiaoshaOrderMapper {


    @Select("select * from miaosha_order where user_id = #{id} and goods_id = #{goodsId}")
    MiaoshaOrder queryGoodsByUserIdAndGoodsId(@Param("id")Long id, @Param("goodsId")long goodsId);

    @Select("select * from order_info where id = #{orderId} ")
    OrderInfo queryOrderByOrderId(Long orderId);

    @Delete("delete from order_info")
    void deleteOrders();
    @Delete("delete from miaosha_order")
    void deleteMiaoshaOrders();

    @Update("update order_info set status = 1 where order_number=#{orderNumber}")
    int updateOrderState(String orderNumber);
}
