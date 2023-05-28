package com.glsm.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static com.glsm.security.ApplicationUserPermission.COURSE_WRITE;
import static com.glsm.security.ApplicationUserRole.*;


// let's change form "based auth" to basic auth
// specify username and password for every single request

@Configuration
@EnableWebSecurity // Spring Security yapılandırmasını etkinleştirir.
//@EnableGlobalMethodSecurity(prePostEnabled = true)// anotasyonu ise metod düzeyinde yetkilendirme işlemlerini etkinleştirir.
                                                    // Bu anotasyon, Spring Security ile yöntem düzeyinde yetkilendirme yapmayı sağlar.
                                                    // Bu, @PreAuthorize ve @PostAuthorize
                                                    // gibi anotasyonları metodlarda kullanmanıza olanak tanir
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {


    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/","index","/css/*","/js/*")
                .permitAll()//hangi URL'lerin hangi roller veya yetkilendirmelerle erişilebilir olacağını belirlemek için kullanılır.
                .antMatchers("/api/**").hasRole(STUDENT.name())
//                .antMatchers(HttpMethod.DELETE,"management/api/**").hasAuthority(COURSE_WRITE.getPermission())
//                .antMatchers(HttpMethod.POST,"management/api/**").hasAuthority(COURSE_WRITE.getPermission())
//                .antMatchers(HttpMethod.PUT,"management/api/**").hasAuthority(COURSE_WRITE.getPermission())
//                //Yani, yukarıdaki ifade, HTTP PUT metoduyla "management/api/" ile başlayan herhangi bir URL'ye sadece COURSE_WRITE
//                // yetkisine sahip kullanıcıların erişebileceğini belirtir.
//
//                .antMatchers("management/api/**").hasAnyRole(ADMIN.name(),ADMINTRAINEE.name())

                //HTTP GET metoduyla "management/api/" ile başlayan herhangi bir URL'ye sadece
                // ADMIN veya ADMINTRAINEE rollerinden herhangi birine sahip olan kullanıcıların erişebileceğini belirtir.

                .anyRequest()//tüm URL'lerin yetkilendirme kuralına tabi olacağını ifade eder.
                .authenticated()//yetkilendirilmemiş isteklerin reddedileceğini ve sadece kimlik doğrulama geçmişi olan kullanıcılara izin verileceğini belirtir.
                .and()//
                .httpBasic(); //HTTP temel kimlik doğrulama yöntemini kullanarak kimlik doğrulama yapılacağını belirtir

    }


    @Override
    @Bean
    protected UserDetailsService userDetailsService() {

        UserDetails annaSmithUser = User.builder()
                                        .username("annasmith")
                                        .password(passwordEncoder.encode("password")) // password must be encoded
//                                        .roles(STUDENT.name()) // ROLE_STUDENT -->
                .authorities(STUDENT.grantedAuthoritySet())
                                        .build();

        UserDetails lindaUser = User.builder()
                                        .username("linda")
                                        .password(passwordEncoder.encode("password123"))
//                                        .roles(ADMIN.name()) //ROLE_ADMIN
                .authorities(ADMIN.grantedAuthoritySet())
                                        .build();

        UserDetails tomUser = User.builder()
                                        .username("tom")
                                        .password(passwordEncoder.encode("password123"))
//                                        .roles(ADMINTRAINEE.name()) //ROLE_ADMINTRAINEE
                .authorities(ADMINTRAINEE.grantedAuthoritySet())
                                        .build();

    /*
    InMemoryUserDetailsManager sınıfı
    *  kullanıcı ayrıntılarını geçici olarak bellekte depolamak için kullanılan bir uygulama türüdür ve
    *  genellikle test veya basit senaryolar için kullanılır.*/

        return new  InMemoryUserDetailsManager(
                annaSmithUser,
                lindaUser,
                tomUser
        );

    }







}
