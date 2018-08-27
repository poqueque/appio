package net.poquesoft.appio.view.component;

import android.view.View;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import net.poquesoft.appio.R;


/**
 * Percentage Bar component
 */

public class PercentageBarComponent extends Component {

    private String percentageText = "";

    TextView labelText;
    RoundCornerProgressBar percentageBar;
    float max = 100;
    float progress = 0;

    public PercentageBarComponent() {
    }

    @Override
    public int getLayout() {
        return R.layout.component_percentagebar;
    }

    @Override
    public void initView(View v) {
        percentageBar = v.findViewById(R.id.percentageBar);
        percentageBar.setMax(max);
        percentageBar.setProgress(progress);
        labelText = v.findViewById(R.id.label);
        if (percentageText != null) {
            labelText.setText(percentageText);
        }
    }

    public PercentageBarComponent setText(String percentageText) {
        this.percentageText = percentageText;
        return this;
    }

    public PercentageBarComponent setMax(float max) {
        this.max = max;
        return this;
    }

    public PercentageBarComponent setProgress(float progress) {
        this.progress = progress;
        return this;
    }
}
