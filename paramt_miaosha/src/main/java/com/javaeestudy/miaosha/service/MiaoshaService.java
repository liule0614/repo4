package com.javaeestudy.miaosha.service;

import com.javaeestudy.miaosha.dimain.MiaoshaOrder;
import com.javaeestudy.miaosha.dimain.MiaoshaUser;
import com.javaeestudy.miaosha.dimain.OrderInfo;
import com.javaeestudy.miaosha.redis.MiaoshaKey;
import com.javaeestudy.miaosha.redis.RedisService;
import com.javaeestudy.miaosha.util.MD5Util;
import com.javaeestudy.miaosha.util.UUIDUtil;
import com.javaeestudy.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

@Service
public class MiaoshaService {

    @Autowired
    private MiaoshaGoodsService goodsService;
    @Autowired
    private MiaoshaOrderService orderService;
    @Autowired
    private RedisService redisService;

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {

        int i = this.goodsService.reduceCount(goods);

        if (i > 0) {
            OrderInfo orderInfo = this.goodsService.insertOrder(user, goods);

            return orderInfo;
        }else {

            setGoodsOver(goods.getId());

        }
        return null;
    }

    public long getMiaoshaResult(Long userId, long goodsId) {
        MiaoshaOrder order = this.orderService.queryGoodsByUserIdAndGoodsId(userId, goodsId);
        //三种情况
        if (order != null) {
            return order.getOrderId();
        } else {
            Boolean isOver = getGoodsOver(goodsId);
            if (isOver) {  //秒杀失败
                return -1;
            } else {  //
                return 0; //继续轮询
            }
        }
    }

    private void setGoodsOver(Long goodsId) {
        this.redisService.set(MiaoshaKey.getIsGoodsOver, "" + goodsId, true);  //true 是失败

    }

    private Boolean getGoodsOver(Long goodsId) {
        return this.redisService.exists(MiaoshaKey.getIsGoodsOver, "" + goodsId);

    }

    public void reset(List<GoodsVo> goodsList) {
        goodsService.resetStock(goodsList);
        orderService.deleteOrders();

    }

    public String createPath(Long id, long goodsId) {
        String str = MD5Util.md5(UUIDUtil.uuid());
        String key = id + "_" + goodsId;
        redisService.set(MiaoshaKey.getPath,key,str);
        return str;
    }

    public BufferedImage createVerifyCode(MiaoshaUser user, long goodsId) {
        if(user == null || goodsId <=0) {
            return null;
        }
        int width = 90;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis中
        int rnd = calc(verifyCode);
        redisService.set(MiaoshaKey.getMiaoshaVerifyCode, user.getId()+","+goodsId, rnd);
        //输出图片
        return image;
    }

    public boolean checkVerifyCode(MiaoshaUser user, long goodsId, int verifyCode) {
        if(user == null || goodsId <=0) {
            return false;
        }
        Integer codeOld = redisService.get(MiaoshaKey.getMiaoshaVerifyCode, user.getId()+","+goodsId, Integer.class);
        if(codeOld == null || codeOld - verifyCode != 0 ) {
            return false;
        }
        redisService.delete(MiaoshaKey.getMiaoshaVerifyCode, user.getId()+","+goodsId);
        return true;
    }

    private static int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer)engine.eval(exp);  // 1*5+2
        }catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static char[] ops = new char[] {'+', '-', '*'};
    /**
     * + - *
     * */
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        int num4 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        char op3 = ops[rdm.nextInt(3)];
        String exp = ""+ num1 + op1 + num2 + op2 + num3+ op3 + num4;
        return exp;
    }
}