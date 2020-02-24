package com.cai2yy.armot.api.bean;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

@Data
public class User {

	private String name;

	public User(String name) {
		this.name = name;
	}

	public static void main(String[] args) {
		String s = "112";
		System.out.println(Pattern.compile("^[-\\+]?[\\d]*$").matcher(s).matches());
	}

}
