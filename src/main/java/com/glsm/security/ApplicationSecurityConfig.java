package com.glsm.security;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


// let's change form "based auth" to basic auth
// specify username and password for every single request

@Configuration
@EnableWebSecurity // Spring Security yapılandırmasını etkinleştirir.
//@EnableGlobalMethodSecurity(prePostEnabled = true)// anotasyonu ise metod düzeyinde yetkilendirme işlemlerini etkinleştirir.
                                                    // Bu anotasyon, Spring Security ile yöntem düzeyinde yetkilendirme yapmayı sağlar.
                                                    // Bu, @PreAuthorize ve @PostAuthorize
                                                    // gibi anotasyonları metodlarda kullanmanıza olanak tanir
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                .antMatchers("/","index","/css/*","/js/*")
                .permitAll()//hangi URL'lerin hangi roller veya yetkilendirmelerle erişilebilir olacağını belirlemek için kullanılır.
                .anyRequest()//tüm URL'lerin yetkilendirme kuralına tabi olacağını ifade eder.
                .authenticated()//yetkilendirilmemiş isteklerin reddedileceğini ve sadece kimlik doğrulama geçmişi olan kullanıcılara izin verileceğini belirtir.
                .and()//
                .httpBasic(); //HTTP temel kimlik doğrulama yöntemini kullanarak kimlik doğrulama yapılacağını belirtir

    }






}
