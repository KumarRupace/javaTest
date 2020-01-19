package com.java.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * CustomerUserDetailsService provide the service to customized password for user. 
 * 
 * @author Kimi Qian Min
 *
 */
@Component
public class CustomerUserDetailsService implements UserDetailsService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * 123456 will new password to login the system instead of auto generation during server starting
	 * 
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		String encodedPassword = passwordEncoder.encode("123456");
		logger.info("encodedPassword - {}", encodedPassword);
		User admin = new User(username, encodedPassword, AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
		return admin;
	}

}
