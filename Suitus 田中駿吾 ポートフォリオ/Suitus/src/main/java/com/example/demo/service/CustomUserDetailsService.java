package com.example.demo.service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.LoginUserDetails;
import com.example.demo.model.Users;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service  // サービスクラスであることを示す@Serviceアノテーション
@RequiredArgsConstructor  // コンストラクタインジェクションのためのアノテーション
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Users> _user  = userRepository.findByUsername(username);
		if (_user == null) {
            throw new UsernameNotFoundException("ユーザーが見つかりません: " + username);
        }
		
			return _user.map(user -> new LoginUserDetails(user))
		      .orElseThrow(() -> new UsernameNotFoundException("not found username=" + username));
		// TODO Auto-generated method stub
	}

    
}