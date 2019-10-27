package com.liker.android.Profile.service;

//import com.doodle.Profile.model.Phone;

import com.liker.android.Profile.model.Phone;

public interface PhoneModificationListener {

    void onPhoneEdit(Phone phone);
    void onPhoneRemove(Phone phone, int position);

}
