package net.poquesoft.appio.presenter;

import net.poquesoft.appio.view.BaseView;

/**
 * Created by edi on 18/06/18.
 */

interface BasePresenter {

    void attach(BaseView v);

    void detach();
}
