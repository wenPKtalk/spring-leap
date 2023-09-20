package com.topsion.service;


import com.topsion.framework.beans.factory.annotation.Autowired;

public class WithAutowiredBaseService {
	@Autowired
	private WithAutowiredBaseBaseService bbs;

	public WithAutowiredBaseBaseService getBbs() {
		return bbs;
	}
	public void setBbs(WithAutowiredBaseBaseService bbs) {
		this.bbs = bbs;
	}
	public WithAutowiredBaseService() {
	}
	public void sayHello() {
		System.out.print("Base Service says hello");
		bbs.sayHello();
	}
	public void init() {
		System.out.print("Base Service init method.");
	}
}
