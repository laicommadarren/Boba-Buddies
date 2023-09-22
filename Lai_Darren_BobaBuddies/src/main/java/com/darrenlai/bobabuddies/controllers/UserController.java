package com.darrenlai.bobabuddies.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.darrenlai.bobabuddies.models.LoginUser;
import com.darrenlai.bobabuddies.models.User;
import com.darrenlai.bobabuddies.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class UserController {
	@Autowired
	private UserService userService;
	
	@GetMapping("/")
    public String index(Model model) {
        model.addAttribute("newUser", new User());
        model.addAttribute("newLogin", new LoginUser());
        return "index";
    }
	
	@PostMapping("/register")
    public String register(@Valid @ModelAttribute("newUser") User newUser, 
            BindingResult result, Model model, HttpSession session) {
		if(result.hasErrors()) {
	        model.addAttribute("newLogin", new LoginUser());
	        return "register";
	    }
		
		User user = userService.register(newUser, result);
        if(result.hasErrors()) {
            model.addAttribute("newLogin", new LoginUser());
            return "register";
        }
        session.setAttribute("userId", user.getId());
    
        return "redirect:/dashboard";
    }
    
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("newLogin") LoginUser newLogin, 
            BindingResult result, Model model, HttpSession session) {
    	if(result.hasErrors()) {
            model.addAttribute("newUser", new User());
            return "login";
        }
        // Add once service is implemented:
        User user = userService.login(newLogin, result);
    
        if(result.hasErrors()) {
            model.addAttribute("newUser", new User());
            return "login";
        }
        session.setAttribute("userId", user.getId());
        
    
        return "redirect:/dashboard";
    }
    
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
    	session.setAttribute("userId", null);
    	session.setAttribute("userName", null);
    	session.setAttribute("userFirstName", null);
    	return "redirect:/";
    }
    
    @GetMapping("/user/{id}")
    public String profile(@PathVariable("id") Long id, Model model) {
    	model.addAttribute("thisUser", userService.findUser(id));
    	model.addAttribute("userService", userService);
    	return "userProfile";
    }
    @PostMapping("/user/{id}/follow")
    public String followUser(@PathVariable("id") Long id, HttpSession session) {
    	userService.followUser((Long) session.getAttribute("userId"), id);
    	return String.format("redirect:/user/%d", (Long) session.getAttribute("userId"));
    }
    // unfollow selected user
    @DeleteMapping("/user/{friendId}/unfollow")
    public String unfollowFriend(@PathVariable("friendId") Long friendId, HttpSession session) {
    	Long userId = (Long) session.getAttribute("userId");
    	userService.unfollowUser(userId, friendId);
    	return String.format("redirect:/user/%d", userId);
    	
    }
}