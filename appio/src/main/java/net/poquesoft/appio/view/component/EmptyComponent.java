package net.poquesoft.appio.view.component;

import android.view.View;
import android.widget.Button;

import net.poquesoft.appio.R;
import net.poquesoft.appio.view.listeners.ActionListener;

/**
 * Standard Button component
 */

public class EmptyComponent extends Component {

    String text = "";
    int clickAction = Component.NONE;
    ActionListener actionListener;
    Button button;

    public EmptyComponent() {

    }

    @Override
    public int getLayout() {
        return R.layout.component_empty;
    }

    @Override
    public void initView(final View v) {
    }
}
