package net.poquesoft.appio.view.component;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import net.poquesoft.appio.R;

/**
 * Standard Text component
 */

public class ImageComponent extends Component {

    Drawable drawable;
    String imageUrl;

    public ImageComponent() {
    }

    @Override
    public int getLayout() {
        return R.layout.component_image;
    }

    @Override
    public void initView(View v) {
        ImageView imageView = v.findViewById(R.id.image);
        if (drawable != null)
            imageView.setImageDrawable(drawable);
        if (imageUrl != null)
            Picasso.get().load(imageUrl).placeholder(drawable).into(imageView);
    }

    public ImageComponent setDrawable(Drawable drawable) {
        this.drawable = drawable;
        return this;
    }

    public ImageComponent setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }
}
