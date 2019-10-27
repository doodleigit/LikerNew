package com.liker.android.Setting.service;

//import com.doodle.Setting.model.Email;

import com.liker.android.Setting.model.Email;

public interface EmailModificationListener {

    void onEmailRemove(Email email, int position);
    void onEmailResendVerification(Email email);

}
