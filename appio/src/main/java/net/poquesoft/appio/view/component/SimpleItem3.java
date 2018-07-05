package net.poquesoft.appio.view.component;

import android.view.View;
import android.widget.TextView;

import net.poquesoft.appio.R;
import net.poquesoft.appio.view.listeners.ActionListener;

/**
 * Standard Button component
 */

public class SimpleItem3 extends Component {

    String text1 = "";
    String text2 = "";
    String text3 = "";
    ActionListener actionListener;
    int clickAction = Component.NONE;
    View clickableView;

    public SimpleItem3(String t1, String t2, String t3) {
        text1 = t1;
        text2 = t2;
        text3 = t3;
    }

    @Override
    public int getLayout() {
        return R.layout.item_simple3;
    }

    @Override
    public void initView(final View v) {
        clickableView = v.findViewById(R.id.content);
        clickableView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (actionListener!= null) actionListener.onAction(clickAction);
            }
        });
        TextView textView1 = v.findViewById(R.id.text1);
        textView1.setText(text1);
        TextView textView2 = v.findViewById(R.id.text2);
        textView2.setText(text2);
        TextView textView3 = v.findViewById(R.id.text3);
        textView3.setText(text3);
    }

    public SimpleItem3 setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
        return this;
    }

    public SimpleItem3 setClickAction(int clickAction) {
        this.clickAction = clickAction;
        return this;
    }
}
