package net.poquesoft.appio.view.component;

import android.graphics.Typeface;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import net.poquesoft.appio.R;

/**
 * Standard Text component
 */

public class LabelTextComponent extends Component {

    //Flags
    public static final int NORMAL = 0;
    public static final int EXTRA_SMALL = 1;
    public static final int BOLD = 2;

    private String label = "";
    private String text = "";
    private int size = NORMAL;
    private int style = NORMAL;

    public LabelTextComponent(String label, String s) {
        this.label = label;
        text = s;
    }

    @Override
    public int getLayout() {
        return R.layout.component_label_text;
    }

    @Override
    public void initView(View v) {
        TextView labelTextView = v.findViewById(R.id.label);
        if (size == EXTRA_SMALL)
            labelTextView.setTextSize(R.dimen.text_extra_small_size);
        if (style == BOLD)
            labelTextView.setTypeface(labelTextView.getTypeface(), Typeface.BOLD);
        labelTextView.setText(Html.fromHtml(label), TextView.BufferType.SPANNABLE);
        TextView textView = v.findViewById(R.id.text);
        if (size == EXTRA_SMALL)
            textView.setTextSize(R.dimen.text_extra_small_size);
        if (style == BOLD)
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        textView.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
    }

    public LabelTextComponent setSize(int size) {
        // No acaba de funcionar pero aun no se usa
        this.size = size;
        return this;
    }

    public LabelTextComponent setStyle(int style) {
        this.style = style;
        return this;
    }
}
