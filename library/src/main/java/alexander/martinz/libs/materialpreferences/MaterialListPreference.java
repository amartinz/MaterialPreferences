/*
 * Copyright 2015 Alexander Martinz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package alexander.martinz.libs.materialpreferences;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MaterialListPreference extends MaterialPreference implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private Spinner mSpinner;
    private ArrayAdapter<CharSequence> mSpinnerAdapter;

    private String mDefaultValue;
    private int mDefaultIndex;

    private int mEntriesResId;
    private int mEntryValuesResId;

    private CharSequence[] mEntries;
    private CharSequence[] mEntryValues;

    private String mValue;

    public MaterialListPreference(Context context) {
        super(context);
        init(context, null);
    }

    public MaterialListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MaterialListPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MaterialListPreference(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @Override public void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);

        if (mSpinnerAdapter == null) {
            mSpinnerAdapter = ArrayAdapter.createFromResource(context,
                    mEntriesResId, android.R.layout.simple_spinner_item);
            mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }
        if (mSpinner == null) {
            mSpinner = new Spinner(context);
            mSpinner.setAdapter(mSpinnerAdapter);
            if (!TextUtils.isEmpty(mDefaultValue)) {
                mSpinner.setSelection(mSpinnerAdapter.getPosition(mDefaultValue));
            } else if (mDefaultIndex != -1) {
                mSpinner.setSelection(mDefaultIndex);
            }
            addToWidgetFrame(mSpinner);

            // spinners are ... weird, to prevent the listener from getting called we have to set it
            // after the spinner is fully initialized
            mSpinner.post(new Runnable() {
                @Override public void run() {
                    mSpinner.setOnItemSelectedListener(MaterialListPreference.this);
                }
            });
        }

        this.setOnClickListener(this);
    }

    @Override protected TypedArray parseAttrs(Context context, AttributeSet attrs) {
        final TypedArray a = super.parseAttrs(context, attrs);

        if (a == null) {
            Log.e(this.getClass().getSimpleName(), "Could not obtain typed array!");
            return null;
        }

        mDefaultValue = a.getString(R.styleable.MaterialPreference_prefDefaultValue);
        mValue = mDefaultValue;

        mDefaultIndex = a.getInt(R.styleable.MaterialPreference_prefDefaultIndex, -1);

        mEntriesResId = a.getResourceId(R.styleable.MaterialPreference_prefEntries, -1);
        if (mEntriesResId != -1) {
            if (isInEditMode()) {
                mEntries = new CharSequence[]{ "Coffee", "Chocolate" };
            } else {
                mEntries = context.getResources().getStringArray(mEntriesResId);
            }
        }

        mEntryValuesResId = a.getResourceId(R.styleable.MaterialPreference_prefEntryValues, -1);
        if (mEntryValuesResId != -1) {
            if (isInEditMode()) {
                mEntryValues = new CharSequence[]{ "0", "1" };
            } else {
                mEntryValues = context.getResources().getStringArray(mEntryValuesResId);
            }
        }

        return a;
    }

    public void setAdapter(ArrayAdapter<CharSequence> adapter) {
        mSpinner.setAdapter(adapter);
    }

    @NonNull public Spinner getSpinner() {
        return mSpinner;
    }

    @Nullable public ArrayAdapter<CharSequence> getSpinnerAdapter() {
        return mSpinnerAdapter;
    }

    @Nullable public String getValue() {
        return mValue;
    }

    @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        final String value;
        if (mEntryValues != null && position < mEntryValues.length) {
            value = String.valueOf(mEntryValues[position]);
        } else {
            value = String.valueOf(mSpinnerAdapter.getItem(position));
        }
        if (mListener != null) {
            if (mListener.onPreferenceChanged(this, value)) {
                mValue = value;
            }
        }
    }

    @Override public void onNothingSelected(AdapterView<?> parent) {
        // nothing to be done?
    }

    @Override public void onClick(View v) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            mSpinner.performClick();
        } else {
            mSpinner.callOnClick();
        }
    }
}
