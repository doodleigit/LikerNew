package com.liker.android.Profile.service;

//import com.doodle.Profile.model.Story;

import com.liker.android.Profile.model.Story;

public interface StoryModificationListener {

    void onStoryEdit(Story story, int position);

}
