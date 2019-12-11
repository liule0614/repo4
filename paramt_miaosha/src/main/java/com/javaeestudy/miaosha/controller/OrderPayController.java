package com.javaeestudy.miaosha.controller;

import com.javaeestudy.miaosha.service.MiaoshaOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/alipay")
public class OrderPayController {

    @Autowired
    private MiaoshaOrderService orderService;

    @RequestMapping("/updateOrderState")
    @ResponseBody
    public String updateOrderState(HttpServletRequest request){
        try {
            String orderNumber = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

            Boolean flag = this.orderService.updateOrderState(orderNumber);

            if(flag){
                return "success";
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

}
