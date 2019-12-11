package com.javaeestudy.miaosha.mapper;

import com.javaeestudy.miaosha.dimain.MiaoshaGoods;
import com.javaeestudy.miaosha.dimain.MiaoshaOrder;
import com.javaeestudy.miaosha.dimain.OrderInfo;
import com.javaeestudy.miaosha.vo.GoodsVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MiaoshaGoodsMapper {


    @Select("select g.*,mg.miaosha_price,mg.stock_count,mg.start_date,mg.end_date from goods g left join miaosha_goods mg on g.id=mg.goods_id")
    public List<GoodsVo> queryGoodsList();

    @Select("select g.*,mg.miaosha_price,mg.stock_count,mg.start_date,mg.end_date from goods g left join miaosha_goods mg on g.id=mg.goods_id where g.id = #{goodsId}")
    GoodsVo queryGoodsByGoodsId(Long goodsId);

    @Update("update miaosha_goods set stock_count=stock_count-1 where goods_id = #{id} and stock_count>0")
    int reduceCount(GoodsVo goods);

    @Insert("insert into order_info(user_id, goods_id, goods_name, goods_count, goods_price, goods_channel, status, create_date, order_number)values("
            + "#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{goodsChannel},#{status},#{createDate},#{orderNumber} )")
    @SelectKey(keyColumn="id", keyProperty="id", resultType=long.class, before=false, statement="select last_insert_id()")
    int insertOrderInfo(OrderInfo orderInfo);

    @Insert("insert into miaosha_order (user_id, goods_id, order_id)values(#{userId}, #{goodsId}, #{orderId})")
    int insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);

    @Update("update miaosha_goods set stock_count = #{stockCount} where goods_id = #{goodsId}")
    void resetStock(MiaoshaGoods g);
}
