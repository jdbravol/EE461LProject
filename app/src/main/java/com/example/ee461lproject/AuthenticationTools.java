package com.example.ee461lproject;

import android.text.TextUtils;
import java.util.ArrayList;

/**
 * Created by Aaron on 4/25/17.
 */

public class AuthenticationTools {

    public static boolean textFieldsEmpty(ArrayList<String> textFields) {
        for (String field : textFields) {
            if (TextUtils.isEmpty(field)) {
                return true;
            }
        }

        return false;
    }

    public static boolean validateEmailPassword(String email, String password) {

        // email tests
        if(email.isEmpty()){
            return false;
        }
        if(!email.contains("@")){
            return false;
        }
        if(!email.contains(".")){
            return false;
        }

        // password tests
        if(password.isEmpty()){
            return false;
        }
        if(password.equals("password") ||
                password.equals("abc123")){
            return false;
        }

        return true;
    }

    public static boolean validateEvent(Event e){

        // TODO determine what will passed in from an event and determine if it is valid

        if(e.getUniqueID().isEmpty()){
            return false;
        }
        if(e.getEventName().isEmpty()){
            return false;
        }
        if(e.getOrganizer().isEmpty()){
            return false;
        }

        // TODO include check for Date

        if(e.getLocation().isEmpty()){
            return false;
        }
        if(e.getDescription().isEmpty()){
            return false;
        }

        // TODO include check for freeFood

        if(e.getCategory().isEmpty()){
            return false;
        }

        return true;
    }
}
