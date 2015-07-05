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
import android.widget.TextView;

public class MaterialListPreference extends MaterialPreference implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private ArrayAdapter<CharSequence> mSpinnerAdapter;
    private Spinner mSpinner;

    private TextView mSpinnerTextView;
    private int mSpinnerTextViewColor = -1;

    private String mDefaultValue;
    private int mDefaultIndex;

    private int mEntriesResId;
    private int mEntryValuesResId;

    private CharSequence[] mEntries;
    private CharSequence[] mEntryValues;

    private boolean mPlaceOnBottom;

    private String mValue;

    protected int getSpinnerItemResId() {
        return R.layout.material_item_spinner_content;
    }

    protected int getSpinnerDropdownItemResId() {
        return android.R.layout.simple_spinner_dropdown_item;
    }

    public MaterialListPreference(Context context) {
        super(context);
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
    public MaterialListPreference(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override public boolean init(Context context, AttributeSet attrs) {
        if (!super.init(context, attrs)) {
            return false;
        }

        if (mSpinnerAdapter == null) {
            if (mEntriesResId != -1) {
                mSpinnerAdapter = ArrayAdapter.createFromResource(context,
                        mEntriesResId, getSpinnerItemResId());
                mSpinnerAdapter.setDropDownViewResource(getSpinnerDropdownItemResId());
            } else if (mEntries != null) {
                mSpinnerAdapter = createAdapter(mEntries, mEntryValues);
            }
        }
        if (mSpinner == null) {
            mSpinner = (Spinner) getLayoutInflater()
                    .inflate(R.layout.material_item_spinner, this, false);
            mSpinner.setAdapter(mSpinnerAdapter);
            if (!TextUtils.isEmpty(mDefaultValue)) {
                mSpinner.setSelection(mSpinnerAdapter.getPosition(mDefaultValue));
            } else if (mDefaultIndex != -1) {
                mSpinner.setSelection(mDefaultIndex);
            }
            if (mPlaceOnBottom) {
                addToWidgetFrameBottom(mSpinner);
            } else {
                addToWidgetFrame(mSpinner);
            }

            // spinners are ... weird, to prevent the listener from getting called we have to set it
            // after the spinner is fully initialized
            mSpinner.post(new Runnable() {
                @Override public void run() {
                    mSpinnerTextView = null;
                    mSpinner.setOnItemSelectedListener(MaterialListPreference.this);
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
        mSpinnerAdapter = adapter;
        mSpinner.setAdapter(mSpinnerAdapter);
    }

    @NonNull public Spinner getSpinner() {
        return mSpinner;
    }

    @Nullable public ArrayAdapter<CharSequence> getSpinnerAdapter() {
        return mSpinnerAdapter;
    }

    @Nullable public TextView getSpinnerTextView() {
        return mSpinnerTextView;
    }

    public <T extends MaterialPreference> T setSpinnerTextViewColor(int color) {
        mSpinnerTextViewColor = color;
        if (mSpinnerTextView != null && color != -1) {
            mSpinnerTextView.setTextColor(mSpinnerTextViewColor);
        }
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
        if (position == -1) {
            position = mSpinnerAdapter.getPosition(mValue);
        }
        if (position != -1) {
            // set a tag to prevent the onitemselected listener from triggering
            mSpinner.setTag(position);
            mSpinner.setSelection(position);
        }
        return (T) this;
    }

    public <T extends MaterialPreference> T setValueIndex(int index) {
        String value = String.valueOf(mSpinnerAdapter.getItem(index));
        if (!TextUtils.isEmpty(value)) {
            mValue = value;
            mSpinner.setSelection(index);
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

        final ArrayAdapter<CharSequence> arrayAdapter = new ArrayAdapter<>(getContext(),
                getSpinnerItemResId(), entries);
        arrayAdapter.setDropDownViewResource(getSpinnerDropdownItemResId());
        return arrayAdapter;
    }

    @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // we have set a value, do not fire the listener
        if (mSpinner.getTag() == position) {
            return;
        }

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

        mSpinner.setTag(position);
    }

    @Override public void onNothingSelected(AdapterView<?> parent) {
        // nothing to be done?
    }

    @Override public void onClick(View v) {
        super.onClick(v);
        mSpinner.performClick();
    }
}
