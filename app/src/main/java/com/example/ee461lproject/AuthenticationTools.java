package com.example.ee461lproject;

import android.text.TextUtils;
import java.util.ArrayList;

/**
 * Created by Aaron on 4/25/17.
 */

public class AuthenticationTools {

    public static boolean validateForm(ArrayList<String> fieldsToCheck) {

        // TODO: Finish code for field validation
        for (String field : fieldsToCheck) {
            if (TextUtils.isEmpty(field)) {
                return false;
            }
        }

        return true;
    }

}
