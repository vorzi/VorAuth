package com.vorauth.session;

import org.mindrot.jbcrypt.BCrypt;

public class HashCrypt {
    public static String hash(String passwd, Integer roundSalt) {
        String hash = BCrypt.hashpw(passwd, BCrypt.gensalt(roundSalt));

        return hash;
    };

    public static Boolean check(String passwd, String hash) {
        boolean isSame = BCrypt.checkpw(passwd, hash);

        return isSame;
    };
}
