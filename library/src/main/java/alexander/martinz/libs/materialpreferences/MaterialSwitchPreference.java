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
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

public class MaterialSwitchPreference extends MaterialPreference implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private SwitchCompat mSwitch;

    private boolean mDefaultValue;

    public MaterialSwitchPreference(Context context) {
        super(context);
    }

    public MaterialSwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaterialSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MaterialSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override public boolean init(Context context, AttributeSet attrs) {
        if (!super.init(context, attrs)) {
            return false;
        }

        if (mSwitch == null) {
            mSwitch = new SwitchCompat(context);
            mSwitch.setChecked(mDefaultValue);
            addToWidgetFrame(mSwitch);
            mSwitch.setOnCheckedChangeListener(this);
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

        mDefaultValue = a.getBoolean(R.styleable.MaterialPreference_prefDefaultValue, false);

        return a;
    }

    @NonNull public SwitchCompat getSwitch() {
        return mSwitch;
    }

    public boolean isChecked() {
        return mSwitch.isChecked();
    }

    public <T extends MaterialPreference> T setChecked(boolean isChecked) {
        mSwitch.setChecked(isChecked);
        return (T) this;
    }

    @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (mListener != null) {
            mListener.onPreferenceChanged(this, isChecked);
        }
    }

    @Override public void onClick(View v) {
        super.onClick(v);
        mSwitch.toggle();
    }

    @Override public <T extends MaterialPreference> T setSelectable(boolean isSelectable) {
        super.setSelectable(isSelectable);
        if (mSwitch != null) {
            mSwitch.setClickable(isSelectable);
        }
        return (T) this;
    }
}
