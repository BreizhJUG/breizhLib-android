package org.breizhjug.breizhlib.activity;


public interface AsyncActivity {

    void showLoadingProgressDialog();

	void showProgressDialog(CharSequence message);

	void dismissProgressDialog();
}
