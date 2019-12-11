package com.javaeestudy.miaosha.controller;

import com.javaeestudy.miaosha.dimain.MiaoshaUser;
import com.javaeestudy.miaosha.redis.MiaoshaGoodsKey;
import com.javaeestudy.miaosha.redis.RedisService;
import com.javaeestudy.miaosha.result.CodeMsg;
import com.javaeestudy.miaosha.result.Result;
import com.javaeestudy.miaosha.service.MiaoshaGoodsService;
import com.javaeestudy.miaosha.service.MiaoshaUserService;
import com.javaeestudy.miaosha.vo.GoodsDetailVo;
import com.javaeestudy.miaosha.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class MiaoshaGoodsController {

    @Autowired
    private MiaoshaUserService userService;
    @Autowired
    RedisService redisService;
    @Autowired
    private MiaoshaGoodsService goodsService;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    //商品页面
    /**
    *  线程5000
    *   循环5
     *   qps 449
     *
     *   优化后
     *   qps638
    * */
    @RequestMapping(value = "/to_list",produces = "text/html")
    @ResponseBody
    public String list(ModelMap map, MiaoshaUser user,Model model, HttpServletResponse response, HttpServletRequest request){
        if(user==null){
            return "login";
        }
        map.put("user",user);

        //从缓存里面取出页面数据
        String html = this.redisService.get(MiaoshaGoodsKey.getGoodsListKey, "", String.class);
        //判断有无数据有直接返回
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        //没有从数据库查询商品列表
        List<GoodsVo> goodsList = this.goodsService.queryGoodsList();
        map.put("goodsList",goodsList);

        //吧查询出来的数据渲染成html字符串
        SpringWebContext ctx = new SpringWebContext(request, response,
                request.getServletContext(), request.getLocale(), model.asMap(), applicationContext);
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list",ctx);
        //如果html不为空吧html保存到redis里面，保存时间为60秒
        if(!StringUtils.isEmpty(html)){
            this.redisService.set(MiaoshaGoodsKey.getGoodsListKey,"",html);
        }

        return html;
    }


    //详情页面
    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(ModelMap map, MiaoshaUser user, @PathVariable("goodsId")Long goodsId, HttpServletRequest request, HttpServletResponse response, Model model){
        if(user==null){
            return Result.error(CodeMsg.NO_LOGIN);
        }

        /*String html = this.redisService.get(MiaoshaGoodsKey.getGoodsKey, "" + goodsId, String.class);

        if(!StringUtils.isEmpty(html)){
            return html;
        }*/

        GoodsVo goods = this.goodsService.queryGoodsByGoodsId(goodsId);

        long miaoshaStatus = 0; //状态   0未开始  1一开始  2  结束
        long remainSeconds = 0;//开始
        long start = goods.getStartDate().getTime();
        long end = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        if(now<start){
            miaoshaStatus=0;
            remainSeconds=(start-now)/1000;
        }else if(now>end){
            miaoshaStatus=2;
            remainSeconds=-1;

        }else{
            miaoshaStatus=1;
            remainSeconds=0;
        }
        map.put("miaoshaStatus",miaoshaStatus);
        map.put("remainSeconds",remainSeconds);

        /*//渲染数据
        SpringWebContext ctx = new SpringWebContext(request, response,
                request.getServletContext(), request.getLocale(), model.asMap(), applicationContext);
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail",ctx);
        //保存到redis
        if(!StringUtils.isEmpty(html)){
            this.redisService.set(MiaoshaGoodsKey.getGoodsKey,""+goodsId,html);
        }*/
        GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
        goodsDetailVo.setGoods(goods);
        goodsDetailVo.setUser(user);
        goodsDetailVo.setMiaoshaStatus(miaoshaStatus);
        goodsDetailVo.setRemainSeconds(remainSeconds);

        return Result.success(goodsDetailVo);
    }


}
