package com.me.esztertoth.vetclinicapp.views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.me.esztertoth.vetclinicapp.utils.FontCache;

public class LogoTextView extends AppCompatTextView {

    public LogoTextView(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public LogoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public LogoTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("Pacifico-Regular.ttf", context);
        setTypeface(customFont);
    }


}
