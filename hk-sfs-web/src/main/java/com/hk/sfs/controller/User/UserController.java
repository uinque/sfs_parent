package com.hk.sfs.controller.User;

import com.hk.sfs.controller.DataTableController;
import com.hk.sfs.metadata.entity.UserEntity;
import com.hk.sfs.metadata.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Administrator
 * @date 2017/4/22
 */
@Controller
@RequestMapping("/user")
public class UserController extends DataTableController<UserEntity> {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Override
    protected String toIndex(Model model, HttpServletRequest request, HttpServletResponse response, String defaultView) {
        //userService.findOne();
        model.addAttribute("name",getAsString("a","aaa"));
        return "/user/index";
    }

}
