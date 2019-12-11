package com.javaeestudy.miaosha.controller;

import com.javaeestudy.miaosha.service.MiaoshaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.javaeestudy.miaosha.rabbit.MQSender;
import com.javaeestudy.miaosha.redis.RedisService;
import com.javaeestudy.miaosha.redis.UserKey;
import com.javaeestudy.miaosha.result.CodeMsg;
import com.javaeestudy.miaosha.result.Result;

@Controller
public class DemoCotroller {

	@Autowired
	private MiaoshaUserService userService;
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	MQSender sender;
	
	@RequestMapping("/")
	@ResponseBody
	public String home() {
		return "helloWorld";
	}
	
	//json api 接口数据
	
	
	@RequestMapping("/hello")
	@ResponseBody
	public Result<CodeMsg>hello() {
		return Result.success(CodeMsg.SUCCESS);
	}
	
	
	

	//4
	@RequestMapping("/mq/header")
    @ResponseBody
    public Result<String> header() {
		sender.sendHeader("helloWorld");
        return Result.success("Hello，world");
    }
	//3
	@RequestMapping("/mq/fanout")
    @ResponseBody
    public Result<String> fanout() {
		sender.sendFanout("helloWorld");
        return Result.success("Hello，world");
    }
	//2
	@RequestMapping("/mq/topic")
    @ResponseBody
    public Result<String> topic() {
		sender.sendTopic("helloWorld");
        return Result.success("Hello，world");
    }
	//1
	@RequestMapping("/mq")
    @ResponseBody
    public Result<String> mq() {
		sender.send("helloWorld");
        return Result.success("Hello，world");
    }
	
	
	
	@RequestMapping("/helloerror")
	@ResponseBody
	public Result<CodeMsg>helloeroor() {
		
		return Result.error(CodeMsg.SERVER_ERROR);
	}
	
	//thymeleaf
	@RequestMapping("/thymeleaf")
	public String abc(Model model) {
		model.addAttribute("name", "wenjy");
		return "hello";
	}
	

	/*
	@RequestMapping("/testmap")
	@ResponseBody
	public Map<String,Object> testMap() {
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("name", "zs");
		map.put("age", 18);
		return map;
	}
	
	
	@RequestMapping("/testuser")
	@ResponseBody
	public User testUser() {
		 User user = new User();
		 user.setAge(18);
		 user.setName("ls");
		 
		return user;
	}
	
	
	@RequestMapping("/testlist")
	@ResponseBody
	public List<User> testlist() {
		List<User> list = new ArrayList<User>();
		for(int i=0;i<10;i++) {
			User user = new User();
			user.setAge(18+i);
			user.setName("ls"+i);
			list.add(user);
		}
		
		return list;
	}
	
	*/
	
	
	
}
