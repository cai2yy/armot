package com.cai2yy.armot.utils;

import lib.cjhttp.demo.User;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
@Singleton
public class Session {

	private Map<String, User> users = new HashMap<>();

	public User getUser(String sid) {
		return users.get(sid);
	}

	public void setUser(String sid, User user) {
		this.users.put(sid, user);
	}

}
