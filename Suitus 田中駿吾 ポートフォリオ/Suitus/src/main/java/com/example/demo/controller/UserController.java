package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Users;
import com.example.demo.repository.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    PasswordEncoder encoder = new BCryptPasswordEncoder();
    @GetMapping("/signin")
    public String signin(Model model, @ModelAttribute("error") String error) {
    	Users user = new Users();
    	model.addAttribute("user", user);
    	model.addAttribute("error", error);
    	return "user/signin";
    }
    @GetMapping("/success")
    public String success(Model model) {

    	return "user/success";
    }
    @PostMapping("/register")
    public String registerUser(@ModelAttribute Users user,RedirectAttributes redirectError) {

    	//検索したユーザー情報が空になってない（存在している）場合重複があるとみなす。
        if(userRepository.findByUsername(user.getUsername()).isPresent()) {
        	System.out.println(userRepository.findByUsername(user.getUsername()));
        	redirectError.addFlashAttribute("error", "エラー：\n" + "ユーザー名が既に存在しています。");
        	return "redirect:/user/signin";
        }
    	if(user.getPassword().trim()!="" && user.getUsername().trim()!="" && user.getEmail().trim()!="")
        {
    	user.setPassword(encoder.encode(user.getPassword()));
    	//user.setRoles("USER")はUSERの権限を付与している
    	//絶対にいじってはいけない
    	user.setRoles("USER");
    	
    	userRepository.save(user);
    	return "redirect:/user/success";
        }
    	redirectError.addFlashAttribute("error", "エラー：\n" + "ユーザー名またはパスワード,\nメールアドレスが入力されていません。");
    	System.out.println("エラー：\n" + "ユーザー名またはパスワード,メールアドレスが入力されていません。");
    	return "redirect:/user/signin";
        
    }
    
}