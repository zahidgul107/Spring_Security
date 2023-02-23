package com.security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.config.JwtUtils;
import com.security.dao.UserDao;
import com.security.dto.AuthenticationRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@PostMapping("/authenticate")
	public ResponseEntity<String> authentication(@RequestBody AuthenticationRequest request) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
				);
		final UserDetails user = userDao.findUserByEmail(request.getEmail());
		if (user != null) {
			return ResponseEntity.ok(jwtUtils.generateToken(user));
		}
		return ResponseEntity.status(400).body("Some error has occurred");
	}
}
