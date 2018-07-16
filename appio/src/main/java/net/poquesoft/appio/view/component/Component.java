package net.poquesoft.appio.view.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Component on screen. All components (Buttons, Panels, ComponentLists, ...) will implement this class
 */

public abstract class Component {

    public static final int NONE = 0;
    public static final int HEADER = 1;
    public static final int ITEM = 2;
    public String tag = null;
    public View view;
    int type = ITEM;

    public abstract int getLayout();

    public abstract void initView(View v);

    public void setTag(String tag){
        this.tag = tag;
    };

    public String getTag(){
        return tag;
    };

    public View getView(Context context , ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        view = inflater.inflate(getLayout(), parent, false);
        initView(view);
        return view;
    };

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }

    public void update() {
        if (view != null) initView(view);
    }
}
