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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MaterialPreference extends FrameLayout {
    protected View mView;
    protected MaterialPreferenceChangeListener mListener;

    protected ImageView mIcon;
    protected TextView mTitle;
    protected TextView mSummary;
    protected LinearLayout mWidgetFrame;

    protected String mPrefKey;
    protected boolean mPrefAsCard;

    protected int mResIdIcon;
    protected int mResIdTitle;
    protected int mResIdSummary;

    public interface MaterialPreferenceChangeListener {
        void onPreferenceChanged(MaterialPreference preference, Object newValue);
    }

    public MaterialPreference(Context context) {
        super(context);
        init(context, null);
    }

    public MaterialPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MaterialPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MaterialPreference(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = parseAttrs(context, attrs);
        recycleTypedArray(typedArray);

        int layoutResId = mPrefAsCard ? R.layout.card_preference : R.layout.preference;

        mView = getLayoutInflater().inflate(layoutResId, this, true);

        mIcon = (ImageView) mView.findViewById(android.R.id.icon);
        mTitle = (TextView) mView.findViewById(android.R.id.title);
        mSummary = (TextView) mView.findViewById(android.R.id.summary);
        mWidgetFrame = (LinearLayout) mView.findViewById(android.R.id.widget_frame);

        if (mResIdIcon != -1) {
            mIcon.setImageResource(mResIdIcon);
        } else {
            mIcon.setVisibility(View.GONE);
        }
        if (mResIdTitle != -1) {
            mTitle.setText(mResIdTitle);
        } else {
            mTitle.setVisibility(View.GONE);
        }
        if (mResIdSummary != -1) {
            mSummary.setText(mResIdSummary);
        } else {
            mSummary.setVisibility(View.GONE);
        }
    }

    protected TypedArray parseAttrs(Context context, AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaterialPreference);

        if (a == null) {
            Log.e(this.getClass().getSimpleName(), "Could not obtain typed array!");
            return null;
        }

        mPrefKey = a.getString(R.styleable.MaterialPreference_prefKey);
        mPrefAsCard = a.getBoolean(R.styleable.MaterialPreference_prefAsCard, false);
        mResIdIcon = a.getResourceId(R.styleable.MaterialPreference_prefIcon, -1);
        mResIdTitle = a.getResourceId(R.styleable.MaterialPreference_prefTitle, -1);
        mResIdSummary = a.getResourceId(R.styleable.MaterialPreference_prefSummary, -1);

        return a;
    }

    protected void recycleTypedArray(TypedArray typedArray) {
        if (typedArray != null) {
            typedArray.recycle();
        }
    }

    public LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(getContext());
    }

    public void addToWidgetFrame(int layoutResId) {
        View view = getLayoutInflater().inflate(layoutResId, mWidgetFrame, false);
        addToWidgetFrame(view);
    }

    public void addToWidgetFrame(View view) {
        mWidgetFrame.addView(view);
    }

    public <T extends MaterialPreference> T setOnPreferenceChangeListener(
            MaterialPreferenceChangeListener listener) {
        mListener = listener;
        return (T) this;
    }

    @Nullable public View getRootView() {
        return mView;
    }

    @NonNull public String getKey() {
        if (TextUtils.isEmpty(mPrefKey)) {
            return "";
        }
        return mPrefKey;
    }

}
