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
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import alexander.martinz.libs.materialpreferences.views.AwesomeSpinner;

public class MaterialListPreference extends MaterialPreference implements View.OnClickListener, AwesomeSpinner.OnValueSelectedListener {
    private boolean mInit;

    private ArrayAdapter<CharSequence> spinnerAdapter;
    private AwesomeSpinner awesomeSpinner;

    private String mDefaultValue;
    private int mDefaultIndex;

    private int mEntriesResId;
    private int mEntryValuesResId;

    private CharSequence[] mEntries;
    private CharSequence[] mEntryValues;

    private boolean mPlaceOnBottom;

    private String mValue;

    protected int getSpinnerItemResId() {
        return R.layout.material_prefs_item_spinner_content;
    }

    public MaterialListPreference(Context context) {
        super(context);
        mDefaultIndex = -1;
        mEntriesResId = -1;
        mEntryValuesResId = -1;
    }

    public MaterialListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaterialListPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MaterialListPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override public boolean init(Context context, AttributeSet attrs) {
        if (mInit) {
            return false;
        }
        mInit = true;

        if (!super.init(context, attrs)) {
            return false;
        }

        if (spinnerAdapter == null) {
            if (mEntriesResId != -1) {
                final CharSequence[] entries = getResources().getTextArray(mEntriesResId);
                spinnerAdapter = createAdapter(entries);
            } else if (mEntries != null) {
                spinnerAdapter = createAdapter(mEntries, mEntryValues);
            }
        }
        if (awesomeSpinner == null) {
            awesomeSpinner = (AwesomeSpinner) getLayoutInflater().inflate(R.layout.material_prefs_item_spinner, this, false);
            awesomeSpinner.setAdapter(spinnerAdapter);
            if (spinnerAdapter != null) {
                if (!TextUtils.isEmpty(mDefaultValue)) {
                    awesomeSpinner.setText(spinnerAdapter.getPosition(mDefaultValue));
                } else if (mDefaultIndex != -1) {
                    awesomeSpinner.setText(spinnerAdapter.getItem(mDefaultIndex));
                }
            }
            if (mPlaceOnBottom) {
                addToWidgetFrameBottom(awesomeSpinner);
            } else {
                addToWidgetFrame(awesomeSpinner);
            }

            // spinners are ... weird, to prevent the listener from getting called we have to set it
            // after the spinner is fully initialized
            awesomeSpinner.post(new Runnable() {
                @Override public void run() {
                    awesomeSpinner.setOnValueSelectedListener(MaterialListPreference.this);
                }
            });
        }

        setOnClickListener(this);
        return true;
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

        mPlaceOnBottom = a.getBoolean(R.styleable.MaterialPreference_prefPlaceOnBottom, false);

        return a;
    }

    public void setAdapter(ArrayAdapter<CharSequence> adapter) {
        spinnerAdapter = adapter;
        if (awesomeSpinner.isPopupShowing()) {
            awesomeSpinner.dismissDropDown();
        }
        awesomeSpinner.setAdapter(spinnerAdapter);
    }

    @NonNull public AwesomeSpinner getSpinner() {
        return awesomeSpinner;
    }

    @Nullable public ArrayAdapter<CharSequence> getSpinnerAdapter() {
        return spinnerAdapter;
    }

    public <T extends MaterialPreference> T setSpinnerTextViewColor(@ColorInt int color) {
        awesomeSpinner.setTextColor(color);
        return (T) this;
    }

    @Nullable public String getValue() {
        return mValue;
    }

    public <T extends MaterialPreference> T setValue(String value) {
        mValue = value;
        int position = -1;
        if (mEntryValues != null) {
            for (int i = 0; i < mEntryValues.length; i++) {
                String tmp = String.valueOf(mEntryValues[i]);
                if (value.equals(tmp)) {
                    position = i;
                    break;
                }
            }
        }

        if (position != -1 && position < mEntries.length) {
            awesomeSpinner.setText(mEntries[position]);
        }
        return (T) this;
    }

    public <T extends MaterialPreference> T setValueIndex(int index) {
        final String value = String.valueOf(spinnerAdapter.getItem(index));
        if (!TextUtils.isEmpty(value)) {
            mValue = value;
            if (index != -1 && index < mEntries.length) {
                awesomeSpinner.setText(mEntries[index]);
            }
        }
        return (T) this;
    }

    public CharSequence[] getEntries() {
        return mEntries;
    }

    public <T extends MaterialPreference> T setEntries(CharSequence[] entries) {
        mEntries = entries;
        return (T) this;
    }

    public CharSequence[] getEntryValues() {
        return mEntryValues;
    }

    public <T extends MaterialPreference> T setEntryValues(CharSequence[] entryValues) {
        mEntryValues = entryValues;
        return (T) this;
    }

    public ArrayAdapter<CharSequence> createAdapter(CharSequence[] entries) {
        return createAdapter(entries, null);
    }

    public ArrayAdapter<CharSequence> createAdapter(CharSequence[] entries, CharSequence[] values) {
        mEntries = entries;
        mEntryValues = values;
        return new ArrayAdapter<>(getContext(), getSpinnerItemResId(), mEntries);
    }

    @Override public void onClick(View v) {
        super.onClick(v);
        awesomeSpinner.performClick();
    }

    @Override public void onValueSelected(Object value) {
        final String stringValue = String.valueOf(value);
        String realValue = stringValue;
        if (mEntries != null && mEntryValues != null && mEntries.length == mEntryValues.length) {
            for (int i = 0; i < mEntries.length; i++) {
                if (TextUtils.equals(mEntries[i], stringValue)) {
                    realValue = String.valueOf(mEntryValues[i]);
                }
            }
        }

        if (mListener != null) {
            if (mListener.onPreferenceChanged(this, realValue)) {
                mValue = realValue;
            }
        }
    }
}
