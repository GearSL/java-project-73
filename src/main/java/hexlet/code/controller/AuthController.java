package hexlet.code.controller;

import hexlet.code.dto.JwtRequestDTO;
import hexlet.code.service.UserDetailsServiceImpl;
import hexlet.code.utils.JwtTokenUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
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

    @Operation(summary = "Login")
    @PostMapping("/login")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Authorized"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public String createUserToken(@Parameter(description = "User credentials")
                                                 @RequestBody JwtRequestDTO authRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(),
                authRequest.getPassword()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());

        return jwtTokenUtils.generateToken(userDetails);
    }
}
