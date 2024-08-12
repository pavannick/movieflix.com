package com.movieflix.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.movieflix.entities.Movie;
import com.movieflix.entities.User;
import com.movieflix.services.MovieService;
import com.movieflix.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	@Autowired
	UserService userv;
	
	@Autowired
	MovieService movserv;
	
	@PostMapping("register")
	public String addUsers(@ModelAttribute User usr) {
		boolean status = userv.emailExits(usr.getEmail());
		if(status == true) {
			return "regfail";
		} else {
			userv.addUsers(usr);
			return "registersuccess";
		}
	}


	@PostMapping("login")
	public String validateUser(@RequestParam String email,
			@RequestParam String password, HttpSession session) {
		boolean loginStatus = userv.checkUser(email, password);
		if(loginStatus == true)  {

			session.setAttribute("email", email);

			if(email.equals("admin@gmail.com")) {
				return "adminhome";
			}
			else {
				return "home";
			}
		}
		else   {
			return "login";
		}
	}

	@GetMapping("viewuser")
	public String viewUser(Model model) {
		List<User> userList = userv.viewUser();
		model.addAttribute("user", userList);
		return "viewuser";
	}

	@GetMapping("exploremovie")
	public String exploreMovie(Model model, HttpSession session) {
		
		String email = (String)session.getAttribute("email");

		User usr=userv.getUser(email);
		boolean status = usr.isPremium();
		
		if(status == true)
		{
			List<Movie> movieList = movserv.viewMovie();
			model.addAttribute("movie", movieList);
			return "viewmovieuser";
		}
		else
		{
			return "payment";
		}
	}
	
	
	@GetMapping("logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "login";
	}
	





}
