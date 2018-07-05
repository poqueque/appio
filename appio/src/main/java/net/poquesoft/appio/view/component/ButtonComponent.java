package net.poquesoft.appio.view.component;

import android.view.View;
import android.widget.Button;

import net.poquesoft.appio.R;
import net.poquesoft.appio.view.listeners.ActionListener;

/**
 * Standard Button component
 */

public class ButtonComponent extends Component {

    String text = "";
    int clickAction = Component.NONE;
    ActionListener actionListener;
    Button button;

    public ButtonComponent(String s) {
        text = s;
    }

    @Override
    public int getLayout() {
        return R.layout.component_button;
    }

    @Override
    public void initView(final View v) {
        button = v.findViewById(R.id.button);
        button.setText(text);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (actionListener!= null) actionListener.onAction(clickAction);
            }
        });
    }

    public ButtonComponent setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
        return this;
    }

    public ButtonComponent setClickAction(int clickAction) {
        this.clickAction = clickAction;
        return this;
    }
}
