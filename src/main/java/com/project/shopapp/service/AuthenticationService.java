package com.project.shopapp.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.project.shopapp.dto.*;
import com.project.shopapp.entity.Role;
import com.project.shopapp.entity.Token;
import com.project.shopapp.entity.User;
import com.project.shopapp.enums.ROLE;
import com.project.shopapp.exception.AppException;
import com.project.shopapp.exception.ErrorCode;
import com.project.shopapp.repository.httpclient.OutboundClient;
import com.project.shopapp.repository.TokenRepository;
import com.project.shopapp.repository.UserRepository;
import com.project.shopapp.repository.httpclient.OutboundUserClient;
import com.project.shopapp.response.AuthenticationResponse;
import com.project.shopapp.response.IntrospectResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    TokenRepository tokenRepository;
    OutboundClient outboundClient;
    OutboundUserClient outboundUserClient;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;
    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    @NonFinal
    @Value("${outbound.identity.client-id}")
    protected String CLIENT_ID;

    @NonFinal
    @Value("${outbound.identity.client-secret}")
    protected String CLIENT_SECRET;

    @NonFinal
    @Value("${outbound.identity.redirect-uri}")
    protected String REDIRECT_URI;

    @NonFinal
    protected final String GRANT_TYPE = "authorization_code";


    public AuthenticationResponse authenticate(AuthenticationDTO request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        boolean authentication = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!authentication){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        boolean exists = "ADMIN".equals(user.getRole().getName());
        var token = generateToken(user);
//        log.info("Check role isADMIN : {}", exists);
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(authentication)
                .isAdmin(exists)
                .build();
    }

    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid= true;
        try {
            verifyToken(token,false);
        }catch (AppException exception){
            isValid=false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)
                .build();

    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {

        try{
            var signToken = verifyToken(request.getToken(),true);

            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expirationDate = signToken.getJWTClaimsSet().getExpirationTime();
            Token invalidatedToken = Token.builder()
                    .id(jit)
                    .expiryTime(expirationDate)
                    .build();
            tokenRepository.save(invalidatedToken);
            log.info(jit);
            log.info(expirationDate.toString());
            log.info(signToken.toString());
        }catch (AppException e){
            log.info("Token already expired");
        }

    }

    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var signToken = verifyToken(request.getToken(),true);

        String jit = signToken.getJWTClaimsSet().getJWTID();
        Date expirationDate = signToken.getJWTClaimsSet().getExpirationTime();
        Token invalidatedToken = Token.builder()
                .id(jit)
                .expiryTime(expirationDate)
                .build();
        tokenRepository.save(invalidatedToken);
        var username = signToken.getJWTClaimsSet().getSubject();
        var user = userRepository.findByEmail(username)
                .orElseThrow(()->new AppException(ErrorCode.UNAUTHENTICATED));
        var token = generateToken(user);
        boolean exists = "ADMIN".equals(user.getRole().getName());

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .isAdmin(exists)
                .build();
    }

    public AuthenticationResponse outboundAuthenticate(String code){
        var response = outboundClient.exchangeToken(ExchangeTokenRequest.builder()
                .code(code)
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .redirectUri(REDIRECT_URI)
                .grantType(GRANT_TYPE)
                .build());

        log.info("TOKEN RESPONSE {}", response);

        var userInfo = outboundUserClient.getUserInfo("json", response.getAccessToken());

        var user = userRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> userRepository.save(User.builder()
                                .email(userInfo.getEmail())
                                .fullName(userInfo.getName())
                                .role(Role.builder()
                                        .name(ROLE.USER.name())
                                        .build())
                                .googleAccountId(userInfo.getId())
                                .password("")
                                .active(true)
                        .build()));

        return AuthenticationResponse.builder()
                .token(response.getAccessToken())
                .build();
    }

    //------------------------------------------------------------------------------------------------------

// Tạo ra token

    private String generateToken(User user){
        // Tạo header mã hóa token với thuật toán HS512
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        // Body token
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("chien")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope",buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        // Kí token
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize(); //=> String
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }

    }

//    private String  buildScope(User user){
//        StringJoiner stringJoiner = new StringJoiner(" ");
//        if(!CollectionUtils.isEmpty(user.getRoles()))
//            user.getRoles().forEach(role -> {
//                stringJoiner.add("ROLE_"+role.getName());
////                if(!CollectionUtils.isEmpty(role.getPermissions()))
////                    role.getPermissions().forEach(permission -> {
////                        stringJoiner.add(permission.getName());
////                    });
//            });
//        return stringJoiner.toString();
//    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        Role role = user.getRole(); //User có một Role duy nhất.
        if (role != null) {
            stringJoiner.add("ROLE_" + role.getName());
        }
        return stringJoiner.toString();
    }

    //verify token

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh) ? new Date(signedJWT.getJWTClaimsSet().getIssueTime().
                toInstant().plus(REFRESHABLE_DURATION,ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);
        if(!(verified && expiryTime.after(new Date())))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (tokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        return signedJWT;
    }

}
