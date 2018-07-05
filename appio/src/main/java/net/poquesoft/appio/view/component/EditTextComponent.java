package net.poquesoft.appio.view.component;

import android.text.InputType;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import net.poquesoft.appio.R;

/**
 * Standard Text component
 */

public class EditTextComponent extends Component {

    private String hint = "";
    private int maxLines = 1;
    private String label = null;
    private String description = null;
    private String initialText = null;

    TextView labelText;
    AutoCompleteTextView editText;
    TextView descriptionText;

    public EditTextComponent(String hint) {
        this.hint = hint;
    }

    @Override
    public int getLayout() {
        return R.layout.component_edittext;
    }

    @Override
    public void initView(View v) {
        editText = v.findViewById(R.id.input);
        labelText = v.findViewById(R.id.label);
        descriptionText = v.findViewById(R.id.description);
        if (hint != null) {
            editText.setHint(hint);
        }
        if (maxLines > 1){
            editText.setMaxLines(maxLines);
            editText.setLines(maxLines);
            editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE|InputType.TYPE_CLASS_TEXT);
        }
        if (label == null){
            labelText.setVisibility(View.GONE);
        } else {
            labelText.setText(label);
            labelText.setVisibility(View.VISIBLE);
        }
        if (description == null){
            descriptionText.setVisibility(View.GONE);
        } else {
            descriptionText.setText(description);
            descriptionText.setVisibility(View.VISIBLE);
        }
        if (initialText != null){
            editText.setText(initialText);
        }
    }

    public EditTextComponent setLabel(String label) {
        this.label = label;
        return this;
    }

    public EditTextComponent setDescription(String description) {
        this.description = description;
        return this;
    }

    public EditTextComponent setMaxLines(int maxLines) {
        this.maxLines = maxLines;
        return this;
    }

    public EditTextComponent setInitialText(String initialText) {
        this.initialText = initialText;
        return this;
    }

    public String getText(){
        return editText.getText().toString();
    }
}
