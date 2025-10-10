package com.example.demo.security;

import com.example.demo.exceptions.InvalidTokenException;
import com.example.demo.model.user.UserEntity;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.time.Instant;

@Slf4j
@Service
public class JwtService {

    private final Long expiration;
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    @Autowired
    public JwtService(KeyPair keyPair, JwtPropertiesHolder props) {
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
        this.expiration = props.getExpiration();
    }


    @SneakyThrows
    public String generateToken(UserEntity user) {
        Instant now = Instant.now();
        Date issueDate = Date.from(now);
        Date expirationDate = Date.from(now.plusMillis(expiration));

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issueTime(issueDate)
                .expirationTime(expirationDate)
                .claim("role", user.getRole())
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.RS256),
                claimsSet
        );

        JWSSigner signer = new RSASSASigner(privateKey);
        signedJWT.sign(signer);

        //log.info(Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        //log.info(Base64.getEncoder().encodeToString(privateKey.getEncoded()));

        return signedJWT.serialize();
    }

    @SneakyThrows
    public String validateTokenAndGetSubject(String token) throws InvalidTokenException {
        SignedJWT signedJWT = SignedJWT.parse(token);

        JWSVerifier verifier = new RSASSAVerifier((RSAPublicKey) publicKey);

        if (!signedJWT.verify(verifier)) {
            throw new InvalidTokenException("Invalid token signature");
        }

        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

        Date now = new Date();
        if (claims.getExpirationTime() == null || now.after(claims.getExpirationTime())) {
            throw new InvalidTokenException("Token expired");
        }

        return claims.getSubject();
    }

}