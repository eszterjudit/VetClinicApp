package com.me.esztertoth.vetclinicapp.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.me.esztertoth.vetclinicapp.utils.FontCache;

public class LogoTextView extends TextView {

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
