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
    private String subtext = "";
    private int size = NORMAL;
    private int style = NORMAL;

    public LabelTextComponent() {
    }

    public LabelTextComponent(String label) {
        this.label = label;
    }

    public LabelTextComponent(String label, String text) {
        this.label = label;
        this.text = text;
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
        if (text != null)
            textView.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

        TextView subtextView = v.findViewById(R.id.subtext);
        if (subtext != null && !"".equals(subtext)) {
            subtextView.setText(Html.fromHtml(subtext), TextView.BufferType.SPANNABLE);
            subtextView.setVisibility(View.VISIBLE);
        } else {
            subtextView.setVisibility(View.GONE);
        }
    }

    public LabelTextComponent setLabel(String label) {
        this.label = label;
        return this;
    }

    public LabelTextComponent setText(String text) {
        this.text = text;
        return this;
    }

    public LabelTextComponent setSubtext(String text) {
        this.subtext = text;
        return this;
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
