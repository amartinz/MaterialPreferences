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
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import alexander.martinz.libs.materialpreferences.utils.Helper;

public class MaterialEditTextPreference extends MaterialPreference implements View.OnClickListener {
    private EditText mEditTextValue;

    private String mDefaultValue;
    private int mPrefTextColor;
    private int mPrefTextSize;
    private int mPrefTextMaxLength;

    private String mValue;

    public MaterialEditTextPreference(Context context) {
        super(context);
        mPrefTextColor = -1;
        mPrefTextSize = -1;
        mPrefTextMaxLength = 25;
    }

    public MaterialEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaterialEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MaterialEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override public boolean init(Context context, AttributeSet attrs) {
        if (!super.init(context, attrs)) {
            return false;
        }

        if (mEditTextValue == null) {
            mEditTextValue = new EditText(context);
            mEditTextValue.setText(mDefaultValue);
            if (mPrefTextColor != -1) {
                if (isInEditMode()) {
                    mEditTextValue.setTextColor(Color.parseColor("#009688"));
                } else {
                    mEditTextValue.setTextColor(context.getResources().getColor(mPrefTextColor));
                }
            }
            if (mPrefTextSize != -1) {
                mEditTextValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, mPrefTextSize);
            }
            mEditTextValue.setInputType(InputType.TYPE_NULL);
            mEditTextValue.setEllipsize(TextUtils.TruncateAt.END);
            mEditTextValue.setOnClickListener(this);
            addToWidgetFrame(mEditTextValue);
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
        mPrefTextColor = a.getResourceId(R.styleable.MaterialPreference_prefTextColor, -1);
        mPrefTextSize = a.getInt(R.styleable.MaterialPreference_prefTextSize, -1);
        mPrefTextMaxLength = a.getInt(R.styleable.MaterialPreference_prefTextMaxLength, 25);

        return a;
    }

    @NonNull public EditText getEditTextValue() {
        return mEditTextValue;
    }

    @Override public void onClick(View v) {
        super.onClick(v);
        final AlertDialog dialog = createAlertDialog();
        dialog.show();
    }

    protected AlertDialog createAlertDialog() {
        final Context context = getContext();
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getTitle());

        // create the wrapper layout to apply margins to the edit text
        final LinearLayout wrapper = new LinearLayout(context);
        wrapper.setOrientation(LinearLayout.VERTICAL);
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int margin = (int) Helper.convertDpToPixels(10);
        layoutParams.setMargins(margin, 0, margin, 0);

        // create the edittext and add it to the wrapper layout
        final EditText editText = new EditText(context);
        editText.setText(mValue);
        wrapper.addView(editText, layoutParams);

        // set our wrapper as view
        builder.setView(wrapper);

        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                final String value = editText.getText().toString();
                if (TextUtils.equals(value, mValue)) {
                    // the value did not change, so lets end here
                    return;
                }

                mValue = value;
                final MaterialEditTextPreference preference = MaterialEditTextPreference.this;

                if (mListener != null) {
                    if (mListener.onPreferenceChanged(preference, mValue)) {
                        setText(mValue);
                    }
                } else {
                    setText(mValue);
                }
            }
        });

        return builder.create();
    }

    public <T extends MaterialPreference> T setValue(String value) {
        mValue = value;
        setText(value);

        return (T) this;
    }

    public <T extends MaterialPreference> T setText(String text) {
        if (mPrefTextMaxLength != -1 && text.length() > mPrefTextMaxLength) {
            text = String.format("%s%s", text.substring(0, mPrefTextMaxLength), "…");
        }
        mEditTextValue.setText(text);

        return (T) this;
    }

    public <T extends MaterialPreference> T setTextColor(int color) {
        mEditTextValue.setTextColor(color);

        return (T) this;
    }

}
