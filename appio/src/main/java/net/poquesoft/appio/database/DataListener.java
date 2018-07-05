package net.poquesoft.appio.database;

/**
 * Interface to define a Listener for data changes recived from Firebase
 *
 * Created by edi on 12/01/17.
 */
public interface DataListener {

    /**
     * Event fired when some data has changed
     *
     */
    void onDataChange();

}
