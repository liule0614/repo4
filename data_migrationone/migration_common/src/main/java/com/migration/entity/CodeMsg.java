package com.migration.entity;

public class CodeMsg {
	private int code;
	private String msg;
	
	//通用异常
	public static CodeMsg SUCCESS = new CodeMsg(0, "success");
	public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
	public static CodeMsg SESSION_ERROR = new CodeMsg(500120, "登录超时");
	//登录错误信息
	public static CodeMsg PASSWORD_ISNULL = new CodeMsg(500210, "密码为空");
	public static CodeMsg USER_ERROR = new CodeMsg(500220, "账号错误");
	public static CodeMsg USERNOTFOUND = new CodeMsg(500230, "账号不存在");
	public static CodeMsg PASSWORD_ERROR = new CodeMsg(500240, "密码错误");
	public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500260, "账号不能为空");
	public static CodeMsg USER_EMPTY = new CodeMsg(500270, "密码不能为空");
	public static CodeMsg NO_LOGIN = new CodeMsg(500280, "请先登录");

	public static CodeMsg SUBMIT_ERROR = new CodeMsg(500300, "提交任务失败");

	private CodeMsg(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	public int getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}

	@Override
	public String toString() {
		return "CodeMsg [code=" + code + ", msg=" + msg + "]";
	}
	
	
}
