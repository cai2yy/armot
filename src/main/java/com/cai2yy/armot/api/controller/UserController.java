package com.cai2yy.armot.api.controller;

import com.cai2yy.armot.utils.Session;
import lib.cjhttp.demo.User;
import lib.cjhttp.demo.UserDB;
import lib.cjhttp.server.Controller;
import lib.cjhttp.server.HttpContext;
import lib.cjhttp.server.HttpRequest;
import lib.cjhttp.server.Router;
import com.cai2yy.armot.api.service.UserServer;

import javax.inject.Inject;
import java.util.UUID;

/**
 * @author Cai2yy
 * @date 2020/2/21 18:06
 */

public class UserController implements Controller {

    @Inject
    UserServer userServer;

    @Inject
    Session session;

    @Inject
    UserDB userDB;

    @Override
    public Router route() {
        return new Router()
                .handler("/login", "GET", this::getLoginPage)
                .handler("/login", "POST", this::login);
    }

    public void getLoginPage(HttpContext ctx, HttpRequest req) {
        ctx.render("login.ftl");
    }

    public void login(HttpContext ctx, HttpRequest req) {
        String username = req.mixedParam("username");
        String password = req.mixedParam("password");

        if (!userDB.checkAccess(username, password)) {
            ctx.abort(401, "用户名密码错误");
        }

        User user = new User(username);
        String sid = UUID.randomUUID().toString();
        session.setUser(sid, user);
        ctx.redirect("/user");

    }


}
