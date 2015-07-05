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
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MaterialPreference extends LinearLayout implements View.OnClickListener, View.OnTouchListener {
    private boolean mInit;

    protected View mView;
    protected MaterialPreferenceChangeListener mListener;
    protected MaterialPreferenceClickListener mClickListener;

    protected ImageView mIcon;
    protected TextView mTitle;
    protected TextView mSummary;
    protected LinearLayout mWidgetFrame;
    protected LinearLayout mWidgetFrameBottom;

    protected String mPrefKey;

    // only useful to modify if the general constructor is used and manually init'ed
    protected boolean mPrefAsCard;
    protected int mResIdIcon;
    protected int mResIdTitle;
    protected int mResIdSummary;

    protected boolean mSelectable;

    public interface MaterialPreferenceChangeListener {
        boolean onPreferenceChanged(MaterialPreference preference, Object newValue);
    }

    public interface MaterialPreferenceClickListener {
        boolean onPreferenceClicked(MaterialPreference preference);
    }

    /**
     * Creates a new instance with empty key.
     *
     * @see MaterialPreference#MaterialPreference(Context)
     */
    public MaterialPreference(Context context) {
        this(context, "");
    }

    /**
     * General constructor, where you need to set up your values and call {@link #init(Context)} yourself!
     *
     * @param context Your activities context
     * @param key     The key for the preference
     */
    public MaterialPreference(Context context, String key) {
        super(context, null);
        mPrefKey = key;
        mPrefAsCard = false;
        mResIdIcon = -1;
        mResIdTitle = -1;
        mResIdSummary = -1;
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

    public LayoutInflater getCustomLayoutInflater() {
        return null;
    }

    public boolean init(Context context) {
        return init(context, null);
    }

    public boolean init(Context context, AttributeSet attrs) {
        if (mInit) {
            return false;
        }
        mInit = true;

        if (attrs != null) {
            TypedArray typedArray = parseAttrs(context, attrs);
            recycleTypedArray(typedArray);
        }

        final int layoutResId = mPrefAsCard
                ? R.layout.material_prefs_card_preference : R.layout.material_prefs_preference;
        mView = getLayoutInflater().inflate(layoutResId, this, false);
        super.addView(mView);

        mIcon = (ImageView) mView.findViewById(android.R.id.icon);
        mTitle = (TextView) mView.findViewById(android.R.id.title);
        mSummary = (TextView) mView.findViewById(android.R.id.summary);
        mWidgetFrame = (LinearLayout) mView.findViewById(android.R.id.widget_frame);
        mWidgetFrameBottom = (LinearLayout) mView.findViewById(R.id.widget_frame_bottom);

        if (mResIdIcon != -1 && mIcon != null) {
            mIcon.setImageResource(mResIdIcon);
            mIcon.setVisibility(View.VISIBLE);
        }
        if (mResIdTitle != -1) {
            mTitle.setText(mResIdTitle);
        }
        if (mResIdSummary != -1 && mSummary != null) {
            mSummary.setText(mResIdSummary);
            mSummary.setVisibility(View.VISIBLE);
        }

        setSelectable(true);
        setOnClickListener(this);
        setOnTouchListener(this);

        setOrientation(LinearLayout.VERTICAL);

        return true;
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

    @Override public void onClick(View v) {
        if (mSelectable && mClickListener != null) {
            mClickListener.onPreferenceClicked(this);
        }
    }

    @Override public boolean onTouch(View v, MotionEvent event) {
        if (!mSelectable) {
            return true;
        }
        return false;
    }

    public LayoutInflater getLayoutInflater() {
        LayoutInflater inflater = getCustomLayoutInflater();
        if (inflater == null) {
            inflater = LayoutInflater.from(getContext());
        }
        return inflater;
    }

    public void addToWidgetFrame(int layoutResId) {
        View view = getLayoutInflater().inflate(layoutResId, mWidgetFrame, false);
        addToWidgetFrame(view);
    }

    public void addToWidgetFrame(View view) {
        mWidgetFrame.addView(view);
    }

    public void addToWidgetFrameBottom(int layoutResId) {
        View view = getLayoutInflater().inflate(layoutResId, mWidgetFrameBottom, false);
        addToWidgetFrameBottom(view);
    }

    public void addToWidgetFrameBottom(View view) {
        mWidgetFrameBottom.addView(view);
    }

    public <T extends MaterialPreference> T setOnPreferenceChangeListener(
            MaterialPreferenceChangeListener listener) {
        mListener = listener;
        return (T) this;
    }

    public <T extends MaterialPreference> T setOnPreferenceClickListener(
            MaterialPreferenceClickListener listener) {
        mClickListener = listener;
        return (T) this;
    }

    @Override public void setBackgroundColor(int color) {
        if (mView instanceof CardView) {
            ((CardView) mView).setCardBackgroundColor(color);
        } else {
            super.setBackgroundColor(color);
        }
    }

    @Nullable public MaterialPreferenceChangeListener getOnPreferenceChangeListener() {
        return mListener;
    }

    public boolean isSelectable() {
        return mSelectable;
    }

    public <T extends MaterialPreference> T setSelectable(boolean isSelectable) {
        mSelectable = isSelectable;
        setClickable(mSelectable);
        return (T) this;
    }

    @Nullable public ImageView getIconView() {
        return mIcon;
    }

    @NonNull public TextView getTitleView() {
        return mTitle;
    }

    @Nullable public TextView getSummaryView() {
        return mSummary;
    }

    @Nullable public LinearLayout getWidgetFrame() {
        return mWidgetFrame;
    }

    @NonNull public String getKey() {
        if (TextUtils.isEmpty(mPrefKey)) {
            return "";
        }
        return mPrefKey;
    }

    public <T extends MaterialPreference> T setKey(String key) {
        mPrefKey = key;
        return (T) this;
    }

    @Nullable public String getTitle() {
        if (mTitle == null) {
            return null;
        }
        return String.valueOf(mTitle.getText());
    }

    public <T extends MaterialPreference> T setTitle(String title) {
        mTitle.setText(title);
        return (T) this;
    }

    @Nullable public String getSummary() {
        if (mSummary == null) {
            return null;
        }
        return String.valueOf(mSummary.getText());
    }

    public <T extends MaterialPreference> T setSummary(@Nullable String summary) {
        if (mSummary != null) {
            if (TextUtils.isEmpty(summary)) {
                mSummary.setVisibility(View.GONE);
            } else {
                mSummary.setText(summary);
                mSummary.setVisibility(View.VISIBLE);
            }
        }
        return (T) this;
    }

    public boolean isCard() {
        return mPrefAsCard;
    }

    public <T extends MaterialPreference> T setAsCard(boolean asCard) {
        mPrefAsCard = asCard;
        return (T) this;
    }

    public int getResIdIcon() {
        return mResIdIcon;
    }

    public <T extends MaterialPreference> T setResIdIcon(int resIdIcon) {
        mResIdIcon = resIdIcon;
        return (T) this;
    }

    public int getmResIdTitle() {
        return mResIdTitle;
    }

    public <T extends MaterialPreference> T setResIdTitle(int resIdTitle) {
        mResIdTitle = resIdTitle;
        return (T) this;
    }

    public int getResIdSummary() {
        return mResIdSummary;
    }

    public <T extends MaterialPreference> T setResIdSummary(int resIdSummary) {
        mResIdSummary = resIdSummary;
        return (T) this;
    }
}
