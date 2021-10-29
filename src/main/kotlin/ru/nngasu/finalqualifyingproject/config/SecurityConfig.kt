package ru.nngasu.finalqualifyingproject.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import ru.nngasu.finalqualifyingproject.config.security.*
import ru.nngasu.finalqualifyingproject.service.UserService


/**
@author Peshekhonov Maksim
 */
@Configuration
@EnableWebSecurity
class SecurityConfig(
    val userService: UserService,
    val authSuccessHandler: AuthSuccessHandler,
    val authFailureHandler: AuthFailureHandler,
    val logoutSuccessHandler: LogoutSuccessHandler
): WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http
            .cors()
                .configurationSource(corsConfigurationSource())
                .and()
            .authorizeRequests()
                .antMatchers("/", "/users/**", "/signup").permitAll()
                .anyRequest().authenticated()
                .and()
            .apply(AuthFilterConfigurer(AuthFilter(), "/login"))
                .successHandler(authSuccessHandler)
                .failureHandler(authFailureHandler)
                .and()
            .logout()
                .logoutSuccessHandler(logoutSuccessHandler)
                .invalidateHttpSession(true)
                .permitAll()
                .and()
            .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(authEntryPoint())
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
    fun accessDeniedHandler(): AccessDeniedHandler? {
        return RestAccessDeniedHandler()
    }

    @Bean
    fun authEntryPoint(): AuthenticationEntryPoint? {
        return AuthRestEntryPoint()
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