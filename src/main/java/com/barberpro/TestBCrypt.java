package com.barberpro;

import org.mindrot.jbcrypt.BCrypt;

public class TestBCrypt {

    public static void main(String[] args) {

        String password = "barber2";

        String hash = BCrypt.hashpw(
                password,
                BCrypt.gensalt(10)
        );

        System.out.println("HASH:");
        System.out.println(hash);

        boolean match = BCrypt.checkpw(
                "barber2",
                hash
        );

        System.out.println("MATCH: " + match);
    }
}