package ru.nngasu.finalqualifyingproject.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import ru.nngasu.finalqualifyingproject.config.security.AuthFailureHandler
import ru.nngasu.finalqualifyingproject.config.security.AuthSuccessHandler
import ru.nngasu.finalqualifyingproject.service.UserService

@Configuration
@EnableWebSecurity
class SecurityConfig(
    val userService: UserService,
    val authSuccessHandler: AuthSuccessHandler,
    val authFailureHandler: AuthFailureHandler
): WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http
            .cors()
                .configurationSource(corsConfigurationSource())
            .and()
            .authorizeRequests()
                .antMatchers("/", "/users/**").permitAll()
                .anyRequest().authenticated()
                .and()
            .csrf().disable()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder())
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("*");
        configuration.allowedHeaders = listOf("*");
        configuration.allowedMethods = listOf("*");
        configuration.allowCredentials = true;
        configuration.maxAge = 86400L;

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

}