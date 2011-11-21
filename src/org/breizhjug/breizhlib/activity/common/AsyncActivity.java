package org.breizhjug.breizhlib.activity.common;


public interface AsyncActivity {

    void showLoadingProgressDialog();

    void showProgressDialog(CharSequence message);

    void dismissProgressDialog();
}
