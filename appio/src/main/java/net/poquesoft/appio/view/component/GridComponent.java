package net.poquesoft.appio.view.component;

import android.content.Context;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;

import net.poquesoft.appio.R;

/**
 * Standard Text component
 */

public class GridComponent extends Component {

    Component[] components;
    int columns;

    public GridComponent(Component[] components, int columns) {
        this.components = components;
        this.columns = columns;
    }

    @Override
    public int getLayout() {
        return R.layout.component_grid;
    }

    @Override
    public void initView(View v) {
        TableLayout tableView = v.findViewById(R.id.table);

        TableRow tableRow = getNewTableRow(v.getContext());

        for (int i=0; i<components.length; i++){
            View childView = components[i].getView(v.getContext(),tableRow);
            tableRow.addView(childView);

            if (tableRow.getChildCount() >= columns){
                tableView.addView(tableRow);
                tableRow = getNewTableRow(v.getContext());
            }
        }
        if (tableRow.getChildCount() > 0){
            while (tableRow.getChildCount()<columns){
                EmptyComponent emptyComponent = new EmptyComponent();
                View childView = emptyComponent.getView(v.getContext(),tableRow);
                tableRow.addView(childView);
            }
            tableView.addView(tableRow);
        }
    }

    public TableRow getNewTableRow(Context context) {
        TableRow newTableRow= new TableRow(context);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT);
        newTableRow.setLayoutParams(lp);
        return newTableRow;
    }
}
