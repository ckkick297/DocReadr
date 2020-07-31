package com.example.keerat666.listviewpdfui;

import android.view.Menu;

interface OnPageChangeListener {

    boolean onCreateOptionsLanguage(Menu menu);

    /**
     * Called when the user use swipe to change page
     *
     * @param page      the new page displayed, starting from 0
     * @param pageCount the total page count
     */
    void onPageChanged(int page, int pageCount);

}