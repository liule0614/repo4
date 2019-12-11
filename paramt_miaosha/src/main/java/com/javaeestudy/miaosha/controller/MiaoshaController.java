package com.javaeestudy.miaosha.controller;

import com.javaeestudy.miaosha.access.AccessLimit;
import com.javaeestudy.miaosha.dimain.MiaoshaOrder;
import com.javaeestudy.miaosha.dimain.MiaoshaUser;
import com.javaeestudy.miaosha.dimain.OrderInfo;
import com.javaeestudy.miaosha.rabbit.MQSender;
import com.javaeestudy.miaosha.rabbit.MiaoshaMessage;
import com.javaeestudy.miaosha.redis.AccessKey;
import com.javaeestudy.miaosha.redis.MiaoshaGoodsKey;
import com.javaeestudy.miaosha.redis.MiaoshaKey;
import com.javaeestudy.miaosha.redis.RedisService;
import com.javaeestudy.miaosha.result.CodeMsg;
import com.javaeestudy.miaosha.result.Result;
import com.javaeestudy.miaosha.service.MiaoshaGoodsService;
import com.javaeestudy.miaosha.service.MiaoshaOrderService;
import com.javaeestudy.miaosha.service.MiaoshaService;
import com.javaeestudy.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {

    @Autowired
    private MiaoshaGoodsService goodsService;
    @Autowired
    private MiaoshaOrderService orderService;
    @Autowired
    private MiaoshaService miaoshaService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private MQSender sender;

    private Map<Long,Boolean> localOverMap = new HashMap<Long,Boolean>();

    /**
     *
     * 优化加解决超卖之后
     *  qps 2330
     * */
    @RequestMapping(value = "/{path}/do_miaosha",method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> miaosha(HttpServletRequest request,ModelMap map, MiaoshaUser user, Long goodsId, @PathVariable("path")String path){
        String s = this.redisService.get(MiaoshaKey.getPath, user.getId() + "_" + goodsId, String.class);
        if(null==s){
            return Result.error(CodeMsg.PATH_ERROR);
        }

        if(user==null){
            return Result.error(CodeMsg.NO_LOGIN);
        }

        Boolean flag = localOverMap.get(goodsId);
        if(flag){
            return Result.error(CodeMsg.STOCK_NULL);
        }
        //缓存里预检库存
        Long decr = this.redisService.decr(MiaoshaGoodsKey.getGoodsStockKey, "" + goodsId);
        if(decr<=-1){
            localOverMap.put(goodsId,true);
            return Result.error(CodeMsg.STOCK_NULL);
        }
        //判断是否秒杀
        MiaoshaOrder order = this.orderService.queryGoodsByUserIdAndGoodsId(user.getId(), goodsId);
        if(order!=null){
            return Result.error(CodeMsg.GOODS_ISREPEAT);
        }
        //准备实体类封装秒杀用户信息和商品id
        MiaoshaMessage message = new MiaoshaMessage();
        message.setGoodsId(goodsId);
        message.setUser(user);

        //进入简单队列模式
        this.sender.sendMiaoshaMessage(message);


        return Result.success(0);
        /*GoodsVo goods = this.goodsService.queryGoodsByGoodsId(goodsId);
        if(goods.getStockCount()<=0){
            return Result.error(CodeMsg.STOCK_NULL);
        }
        MiaoshaOrder order = this.orderService.queryGoodsByUserIdAndGoodsId(user.getId(),goodsId);
        if(order!=null){
            return Result.error(CodeMsg.GOODS_ISREPEAT);
        }
        OrderInfo orderInfo = this.miaoshaService.miaosha(user,goods);

        return Result.success(orderInfo);*/
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //吧商品库存放入redis中
        List<GoodsVo> goodsList = this.goodsService.queryGoodsList();
        for (GoodsVo goods : goodsList){
            this.redisService.set(MiaoshaGoodsKey.getGoodsStockKey,""+goods.getId(),goods.getStockCount());
            localOverMap.put(goods.getId(),false);
        }
    }




    @RequestMapping(value="/result",method=RequestMethod.GET)
    @ResponseBody
    public Result<Long> result(MiaoshaUser miaoshaUser , Model model, @RequestParam(value="goodsId") long goodsId ) {
        //如果没有登录跳转到登录页面
        if(miaoshaUser==null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        model.addAttribute("user", miaoshaUser);
        long result = this.miaoshaService.getMiaoshaResult(miaoshaUser.getId(),goodsId);

        return Result.success(result);
    }

    @AccessLimit(seconds=5,maxCount=5,needLogin=true)
    @RequestMapping(value="/path",method=RequestMethod.GET)
    @ResponseBody
    public Result<String> getPath(HttpServletRequest request,MiaoshaUser miaoshaUser , Model model, @RequestParam(value="goodsId") long goodsId,@RequestParam(value="verifyCode", defaultValue="0")int verifyCode) {
        //如果没有登录跳转到登录页面
       /* String uri = request.getRequestURI();
        String key = uri + "_" + miaoshaUser.getId() + "_" + goodsId;
        Integer count = this.redisService.get(AccessKey.getAccessKey, key, Integer.class);
        if(count==null){
            this.redisService.set(AccessKey.getAccessKey, key, 1);
        }else if(count<5){
            this.redisService.incr(AccessKey.getAccessKey, key);
        }else{
            return Result.error(CodeMsg.ACCESS_LIMIT_REACHED);
        }*/

        if(miaoshaUser==null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        boolean check = miaoshaService.checkVerifyCode(miaoshaUser, goodsId, verifyCode);
        if(!check){
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        model.addAttribute("user", miaoshaUser);
        String path = this.miaoshaService.createPath(miaoshaUser.getId(),goodsId);

        return Result.success(path);
    }

    @RequestMapping(value = "/verifyCode",method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaVerifyCode(HttpServletResponse response
    ,MiaoshaUser user
    ,@RequestParam("goodsId")long goodsId){
        if(user==null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        try {
            BufferedImage image = miaoshaService.createVerifyCode(user,goodsId);
            ServletOutputStream out = response.getOutputStream();
            ImageIO.write(image,"JPEG",out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error(CodeMsg.MIAOSHA_FALL);
        }

        return null;
    }


}
