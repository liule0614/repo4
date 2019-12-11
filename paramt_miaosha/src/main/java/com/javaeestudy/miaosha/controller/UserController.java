package com.javaeestudy.miaosha.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.javaeestudy.miaosha.dimain.MiaoshaUser;
import com.javaeestudy.miaosha.result.Result;

@Controller
@RequestMapping("/user")
public class UserController {

	/**
	 * qps 512  
	 * @param miaoshaUser
	 * @return
	 */
   @RequestMapping("/user_info")
   @ResponseBody
   public Result<MiaoshaUser> list(MiaoshaUser miaoshaUser ) {
	   System.out.println("**"+miaoshaUser);
	   return Result.success(miaoshaUser);
   }
  
	 	
}
