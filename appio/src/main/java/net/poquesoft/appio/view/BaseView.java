package net.poquesoft.appio.view;

/**
 * Generic View for MVP model
 */

public interface BaseView<T> {

    void setPresenter(T presenter);

    void onReceiveData(String key, String data);

    void showProgress();

    void hideProgress();
}
