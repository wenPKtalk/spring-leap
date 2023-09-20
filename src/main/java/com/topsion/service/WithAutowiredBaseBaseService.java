package com.topsion.service;

public class WithAutowiredBaseBaseService {
	private AServiceImpl as;

	public AServiceImpl getAs() {
		return as;
	}
	public void setAs(AServiceImpl as) {
		this.as = as;
	}
	public WithAutowiredBaseBaseService() {
	}
	public void sayHello() {
		System.out.println("Base Base Service says hello");

	}
	public void init() {
		System.out.println("Base Base Service init method.");

	}
}
