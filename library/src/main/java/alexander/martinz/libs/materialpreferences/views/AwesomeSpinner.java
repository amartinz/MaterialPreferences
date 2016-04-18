package alexander.martinz.libs.materialpreferences.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by amartinz on 08.04.16.
 */
public class AwesomeSpinner extends AppCompatAutoCompleteTextView implements AdapterView.OnItemClickListener, View.OnClickListener {
    private OnValueSelectedListener onValueSelectedListener;

    public interface OnValueSelectedListener {
        void onValueSelected(Object value);
    }

    public AwesomeSpinner(Context context) {
        super(context);
        init();
    }

    public AwesomeSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AwesomeSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override public boolean enoughToFilter() {
        return true;
    }

    protected void init() {
        setTextIsSelectable(false);
        setFocusable(false);
        setKeyListener(null);
        setOnClickListener(this);
        setOnItemClickListener(this);
    }

    public void setOnValueSelectedListener(@Nullable OnValueSelectedListener onValueSelectedListener) {
        this.onValueSelectedListener = onValueSelectedListener;
    }

    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (onValueSelectedListener != null) {
            final Object value = parent.getAdapter().getItem(position);
            onValueSelectedListener.onValueSelected(value);
        }
    }

    @Override public void onClick(View v) {
        if (isPopupShowing()) {
            dismissDropDown();
        } else {
            if (getFilter() != null) {
                performFiltering("", 0);
            }
            showDropDown();
        }
    }
}
