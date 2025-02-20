package com.minhhieu.identity_service.service;

import com.minhhieu.identity_service.dto.request.AuthenticationRequest;
import com.minhhieu.identity_service.dto.request.IntrospectRequest;
import com.minhhieu.identity_service.dto.request.LogoutRequest;
import com.minhhieu.identity_service.dto.request.RefreshTokenRequest;
import com.minhhieu.identity_service.dto.response.ApiResponse;
import com.minhhieu.identity_service.dto.response.AuthenticationResponse;
import com.minhhieu.identity_service.dto.response.IntrospectResponse;
import com.minhhieu.identity_service.entity.InvalidatedToken;
import com.minhhieu.identity_service.entity.Users;
import com.minhhieu.identity_service.exception.AppException;
import com.minhhieu.identity_service.exception.ErrorCode;
import com.minhhieu.identity_service.repository.InvalidateTokenRepository;
import com.minhhieu.identity_service.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.crypto.impl.HMAC;
import com.nimbusds.jose.jwk.JWKException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.type.TrueFalseConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AuthenticationService {
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGN_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    private long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    private long REFRESHABLE_DURATION;

    UserRepository userRepository;
    InvalidateTokenRepository invalidateTokenRepository;

    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {
        var token = request.getToken();
        SignedJWT signedJWT = verifyToken(token,false);
        return IntrospectResponse.builder()
                .valid(true)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USERNAME_INVALID));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!authenticated){
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        }
        var token = generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(authenticated)
                .build();
    }

    private String generateToken(Users user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("minhhieu.vn")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope",buildScope(user))
                .build();
        Payload payload = new Payload(jwtClaimSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try{
            jwsObject.sign(new MACSigner(SIGN_KEY.getBytes()));
                return jwsObject.serialize();
        }catch (JOSEException e){
            log.error("Cannot create token",e);
            throw new RuntimeException(e);
        }
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {
        var signedJWT  = verifyToken(request.getToken(), true);
        String jit = signedJWT.getJWTClaimsSet().getJWTID();
        Date experyTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        var userName = signedJWT.getJWTClaimsSet().getSubject();
        var user = userRepository.findByUsername(userName).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        String token = generateToken(user);
        invalidateTokenRepository.save(InvalidatedToken.builder()
                .id(jit)
                .expiryTime(experyTime)
                .build());
        return AuthenticationResponse.builder().token(token).authenticated(true).build();

    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try{
            var signedToken = verifyToken(request.getToken(), true);
            String jit = signedToken.getJWTClaimsSet().getJWTID();
            Date experyTime = signedToken.getJWTClaimsSet().getExpirationTime();
            invalidateTokenRepository.save(InvalidatedToken.builder()
                    .id(jit)
                    .expiryTime(experyTime)
                    .build());
        }
        catch (AppException e){
            log.info("Logout Success, Token already expired");
        }
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws ParseException, JOSEException {
        JWSVerifier verifier = new MACVerifier(SIGN_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date experyTime = isRefresh
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                :signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);
        if(!verified && experyTime.after(new Date())){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        var id = signedJWT.getJWTClaimsSet().getJWTID();
        if(invalidateTokenRepository.existsById(id)){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return signedJWT;
    }

    private String buildScope(Users user){
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRoles())){
            user.getRoles().forEach(role -> {
                stringJoiner.add(role.getName());
                if(!CollectionUtils.isEmpty(role.getPermissions())){
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
                }
            });
        }
        return stringJoiner.toString();
    }
}
