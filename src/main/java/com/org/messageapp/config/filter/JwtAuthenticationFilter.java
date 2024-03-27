package com.org.messageapp.config.filter;

import com.org.messageapp.entity.User;
import com.org.messageapp.repository.UserRepository;
import com.org.messageapp.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String userId;
        final String jwt = request.getHeader("x-auth-token");
             if(StringUtils.isBlank(jwt )){
                 filterChain.doFilter(request,response);
                 return;
             }
             userId = jwtService.extractUserId(jwt);
             if(userId!=null && SecurityContextHolder.getContext().getAuthentication() == null){
                 Optional<User> userOpt = userRepository.findById(userId);
                 if(userOpt.isPresent() && jwtService.isTokenValid(jwt,userOpt.get())){
                     User user = userOpt.get();
                     request.setAttribute("userId",user.getId());
                     UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                             user,null,user.getAuthorities()
                     );
                     authToken.setDetails(
                             new WebAuthenticationDetailsSource().buildDetails(request)
                     );
                     SecurityContextHolder.getContext().setAuthentication(authToken);
                 }
             }
             filterChain.doFilter(request,response);
    }
}
