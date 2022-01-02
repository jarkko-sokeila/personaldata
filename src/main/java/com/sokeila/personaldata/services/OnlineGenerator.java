package com.sokeila.personaldata.services;

import com.sokeila.personaldata.model.Online;
import com.sokeila.personaldata.utils.RandomUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Service
public class OnlineGenerator extends RandomGenerator {

    public Online generateOnline(String firstName, String lastName) {
        Online online = new Online();
        online.setUsername(generateUsername(firstName, lastName));
        online.setPassword(generatePassword());
        online.setPasswordHashMd5(getMd5Hash(online.getPassword()));
        online.setPasswordHashSha1(getSha1Hash(online.getPassword()));
        online.setIpAddress(getRandomIpAddress());
        online.setLastLoginIpAddress(getRandomIpAddress());

        return online;
    }

    private String generateUsername(String firstName, String lastName) {
        String username = "";
        if(firstName.length() > 3) {
            username += firstName.substring(0, 3);
        } else {
            username += firstName;
        }

        if(lastName.length() > 3) {
            username += lastName.substring(0, 3);
        } else {
            username += lastName;
        }

        return username.toLowerCase();
    }

    private String generatePassword() {
        int leftLimit = 48; // number '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = getRandomGenerator();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String password = buffer.toString();

        return password;
    }

    private String getMd5Hash(String password) {
        String md5Hex = DigestUtils.md5DigestAsHex(password.getBytes());

        return md5Hex;
    }

    private String getSha1Hash(String password) {
        try {
            // getInstance() method is called with algorithm SHA-1
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(password.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String getRandomIpAddress() {
        StringBuilder ipAddress = new StringBuilder();
        ipAddress.append(RandomUtils.getRandomNumber(100, 800)).append(".");
        ipAddress.append(RandomUtils.getRandomNumber(1, 200)).append(".");
        ipAddress.append(RandomUtils.getRandomNumber(1, 200)).append(".");
        ipAddress.append(RandomUtils.getRandomNumber(100, 800));

        return ipAddress.toString();
    }
}
