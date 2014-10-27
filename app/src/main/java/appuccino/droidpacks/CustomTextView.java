package appuccino.droidpacks;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextView extends TextView {

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public CustomTextView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        if (attrs!=null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTextView);
            String fontName = a.getString(R.styleable.CustomTextView_typeface);
            if (fontName!=null) {
                Typeface tf = null;
                if(fontName.equals("italic")){
                    tf = FontManager.italic;
                } else if (fontName.equals("medium")){
                    tf = FontManager.medium;
                } else if (fontName.equals("bold")){
                    tf = FontManager.bold;
                } else if (fontName.equals("thin")){
                    tf = FontManager.thin;
                } else if (fontName.equals("icon_pack")){
                    tf = FontManager.icon_pack;
                } else {
                    //this is the fallback font if none is defined
                    tf = FontManager.light;
                }
                setTypeface(tf);
            } else {
                Typeface tf = FontManager.light;
                setTypeface(tf);
            }
            a.recycle();
        }
    }

}
