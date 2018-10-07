package net.poquesoft.appio.view.component;

import android.graphics.Typeface;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import net.poquesoft.appio.R;

/**
 * Standard Text component
 */

public class TextComponent extends Component {

    //Flags
    public static final int NORMAL = 0;
    public static final int EXTRA_SMALL = 1;
    public static final int BOLD = 2;

    private String text = "";
    private int size = NORMAL;
    private int style = NORMAL;

    public TextComponent() {
        text = "Text";
    }

    public TextComponent(String s) {
        text = s;
    }

    @Override
    public int getLayout() {
        return R.layout.component_text;
    }

    @Override
    public void initView(View v) {
        TextView textView = v.findViewById(R.id.text);
        if (size == EXTRA_SMALL)
            textView.setTextSize(R.dimen.text_extra_small_size);
        if (style == BOLD)
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        textView.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
    }

    public TextComponent setSize(int size) {
        // No acaba de funcionar pero aun no se usa
        this.size = size;
        return this;
    }

    public TextComponent setStyle(int style) {
        this.style = style;
        return this;
    }

    public void setText(String text) {
        this.text = text;
        update();
    }
}
