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
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MaterialPreferenceCategory extends MaterialPreference {
    private boolean mInit;

    protected CardView mCardView;
    protected LinearLayout mCardContainer;

    public MaterialPreferenceCategory(Context context) {
        super(context);
    }

    public MaterialPreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaterialPreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MaterialPreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override public boolean init(Context context, AttributeSet attrs) {
        if (mInit) {
            return false;
        }
        mInit = true;

        if (attrs != null) {
            super.parseAttrs(context, attrs);
        }

        mView = getLayoutInflater()
                .inflate(R.layout.material_prefs_card_preference_category, this, false);
        super.addView(mView);

        mCardView = (CardView) mView.findViewById(R.id.card_preference_root);

        mIcon = (ImageView) mView.findViewById(android.R.id.icon);
        mTitle = (TextView) mView.findViewById(android.R.id.title);
        mCardContainer = (LinearLayout) mView.findViewById(R.id.card_preference_container);
        mWidgetFrame = (LinearLayout) mView.findViewById(android.R.id.widget_frame);

        if (mResIdIcon != -1 && mIcon != null) {
            mIcon.setImageResource(mResIdIcon);
            mIcon.setVisibility(View.VISIBLE);
        }
        if (mResIdTitle != -1) {
            mTitle.setText(mResIdTitle);
        }

        setSelectable(false);
        setOrientation(LinearLayout.VERTICAL);

        return true;
    }

    @NonNull public CardView getCardView() {
        return mCardView;
    }

    @NonNull public LinearLayout getPreferenceContainer() {
        return mCardContainer;
    }

    public MaterialPreferenceCategory addPreference(MaterialPreference preference) {
        addView(preference);
        return this;
    }

    public MaterialPreferenceCategory addPreference(MaterialPreference preference, int index) {
        addView(preference, index);
        return this;
    }

    @Override public void setBackgroundColor(int color) {
        mCardView.setCardBackgroundColor(color);
    }

    @Override public void addView(@NonNull View child) {
        if (mCardContainer != null) {
            mCardContainer.addView(child);
            return;
        }
        super.addView(child);
    }

    @Override public void addView(@NonNull View child, int index) {
        if (mCardContainer != null) {
            mCardContainer.addView(child, index);
            return;
        }
        super.addView(child, index);
    }

    @Override public void addView(@NonNull View child, int width, int height) {
        if (mCardContainer != null) {
            mCardContainer.addView(child, width, height);
            return;
        }
        super.addView(child, width, height);
    }

    @Override public void addView(@NonNull View child, ViewGroup.LayoutParams params) {
        if (mCardContainer != null) {
            mCardContainer.addView(child, params);
            return;
        }
        super.addView(child, params);
    }

    @Override public void addView(@NonNull View child, int index, ViewGroup.LayoutParams params) {
        if (mCardContainer != null) {
            mCardContainer.addView(child, index, params);
            return;
        }
        super.addView(child, index, params);
    }

}
