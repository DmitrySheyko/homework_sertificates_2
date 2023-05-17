package com.example.homework_sertificates_2.metric;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.security.KeyStore;

import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.*;

@Service
@RequiredArgsConstructor
@PropertySource(value = "classpath:certStore.properties")
public class CertificateServiceImpl {

    @Value("${KEYSTORE_TYPE}")
    private String KEYSTORE_TYPE;

    @Value("${KEYSTORE_PASSWORD}")
    private String KEYSTORE_PASSWORD;

    @Value("${KEYSTORE_PATH}")
    private String KEYSTORE_PATH;

    @SneakyThrows
    public List<CertificateInfo> getCertificates() {
        Map<String, X509Certificate> certMap = new HashMap<>();
        List<CertificateInfo> certInfonfoList = new ArrayList<>();

        KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
        char[] pwdArray = KEYSTORE_PASSWORD.toCharArray();
        keyStore.load(new FileInputStream(KEYSTORE_PATH), pwdArray);

        Enumeration<String> aliases = keyStore.aliases();
        while (aliases.hasMoreElements()) {
            String certificateName = aliases.nextElement();
            certMap.put(certificateName, (X509Certificate) keyStore.getCertificate(certificateName));
        }

        for (String name : certMap.keySet()) {
            Date notBefore = certMap.get(name).getNotBefore();
            Date notAfter = certMap.get(name).getNotAfter();
            Duration duration = Duration.between(notBefore.toInstant(), notAfter.toInstant());
            certInfonfoList.add(new CertificateInfo(name, duration.toDays()));
        }
        return certInfonfoList;
    }

}