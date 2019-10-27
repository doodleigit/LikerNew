package com.liker.android.Profile.service;

//import com.doodle.Profile.model.AdvanceSuggestion;

import com.liker.android.Profile.model.AdvanceSuggestion;

public interface SuggestionClickListener {

    void onSuggestionClick(String suggestion);
    void onSuggestionClick(AdvanceSuggestion advanceSuggestion);

}
