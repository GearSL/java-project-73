package hexlet.code.controllers;

import hexlet.code.dto.JwtRequestDTO;
import hexlet.code.dto.JwtResponseDTO;
import hexlet.code.exceptions.ErrorResponse;
import hexlet.code.service.UserDetailsServiceImpl;
import hexlet.code.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${base-url}")
public class AuthController {
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> createUserToken(@RequestBody JwtRequestDTO authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(),
                    authRequest.getPassword()));
        } catch (BadCredentialsException ex) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
                    "Not correct login or password"), HttpStatus.UNAUTHORIZED);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
        String token = jwtTokenUtils.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponseDTO(token));
    }
}
