package moqim.me.facelook.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moqim.me.facelook.services.AuthentificationService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthentificationFilter extends OncePerRequestFilter {

    private final AuthentificationService authentificationService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try{
            String token = extractToken(request);
            if (null != token) {
                UserDetails userDetails = authentificationService.validateToken(token);
                UsernamePasswordAuthenticationToken authentication  =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);

                if(userDetails instanceof FBUserDetails) {
                    request.setAttribute("userId", ((FBUserDetails) userDetails).getId());
                }


            }
        } catch (Exception e){
            // Do not throw exception, just don't auth the user
            log.warn("Received invalid token");
        }

        filterChain.doFilter(request, response);

    }
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
