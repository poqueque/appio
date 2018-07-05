package net.poquesoft.appio.view.component;

import android.view.View;
import android.widget.TextView;

import net.poquesoft.appio.R;

/**
 * Standard Text component
 */

public class TitleComponent extends Component {

    String text = "";

    public TitleComponent(String s) {
        text = s;
    }

    @Override
    public int getLayout() {
        return R.layout.component_title;
    }

    @Override
    public void initView(View v) {
        TextView textView = v.findViewById(R.id.title);
        textView.setText(text);
    }
}
