package ru.forxy.auth.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSASSASigner;
import org.springframework.beans.factory.InitializingBean;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * JSON Web Token Manager
 */
public class JWTManager implements InitializingBean {

    private String privateKey;

    private String clientID;

    private String publicKey;

    RSAPublicKey rsaPublicKey;

    RSAPrivateKey rsaPrivateKey;

    private KeyPairGenerator keyGenerator;

    public String toJWT(String json) throws JOSEException {
        JWSSigner signer = new RSASSASigner(rsaPrivateKey);
        JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.RS256), new Payload(json));
        jwsObject.sign(signer);
        return jwsObject.serialize();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        KeyPairGenerator.getInstance("RSA");
        keyGenerator.initialize(1024);

        KeyPair kp = keyGenerator.genKeyPair();
        rsaPublicKey = (RSAPublicKey) kp.getPublic();
        rsaPrivateKey = (RSAPrivateKey) kp.getPrivate();
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
