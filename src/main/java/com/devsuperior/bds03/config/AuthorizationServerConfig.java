package com.devsuperior.bds03.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration //Define que será uma classe de configuração
@EnableAuthorizationServer //Define que será essa classe que fará os processamentos em segundo plano representando o AuthorizationServer do OAuth2 
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	//Será preciso esses 4 Beans que foi definido anteriormente:
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtAccessTokenConverter accessTokenConverter;
	
	@Autowired
	private JwtTokenStore jwtTokenStore;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		
		endpoints.authenticationManager(authenticationManager)
		.tokenStore(jwtTokenStore) 
		.accessTokenConverter(accessTokenConverter);
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory() 
		.withClient("dscatalog") 
		.secret(passwordEncoder.encode("dscatalog1234")) 
		.scopes("read", "write")
		.authorizedGrantTypes("password") 
		.accessTokenValiditySeconds(86400);
	}
	
}
