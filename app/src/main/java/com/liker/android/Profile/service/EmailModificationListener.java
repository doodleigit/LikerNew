package com.liker.android.Profile.service;

//import com.doodle.Profile.model.Email;

import com.liker.android.Profile.model.Email;

public interface EmailModificationListener {

    void onEmailRemove(Email email, int position);
    void onEmailResendVerification(Email email);

}
