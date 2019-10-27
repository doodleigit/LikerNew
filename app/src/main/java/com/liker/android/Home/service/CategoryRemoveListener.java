package com.liker.android.Home.service;

//import com.doodle.Home.model.CommonCategory;

import com.liker.android.Home.model.CommonCategory;

public interface CategoryRemoveListener {

    void onCategoryRemove(CommonCategory commonCategory);
    void onCategorySelect(CommonCategory commonCategory);
    void onCategoryDeSelect();
    void onCategorySelectChange(CommonCategory oldCommonCategory, CommonCategory newCommonCategory);

}
