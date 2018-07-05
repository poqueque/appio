package net.poquesoft.appio.presenter;

import net.poquesoft.appio.view.BaseView;

/**
 * Created by edi on 18/06/18.
 */

public class AppioPresenter implements BasePresenter {

    public BaseView view;

    public AppioPresenter(BaseView view) {
        attach(view);
    }

    @Override
    public void attach(BaseView v) {
        this.view = v;
    }

    @Override
    public void detach() {

    }
}
