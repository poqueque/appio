package net.poquesoft.appio.view.component;

import android.support.v7.widget.SearchView;
import android.view.View;

import net.poquesoft.appio.R;

/**
 * Standard Text component
 */

public class SearchComponent extends Component {

    String hint = "";

    public SearchComponent(String hint) {
        this.hint = hint;
    }

    @Override
    public int getLayout() {
        return R.layout.component_search;
    }

    @Override
    public void initView(View v) {
        SearchView searchView = v.findViewById(R.id.search);
        searchView.setQueryHint(hint);
    }
}
