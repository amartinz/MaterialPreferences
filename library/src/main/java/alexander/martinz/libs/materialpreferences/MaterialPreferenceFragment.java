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

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MaterialPreferenceFragment extends Fragment {

    protected int getLayoutResourceId() {
        return -1;
    }

    public MaterialPreferenceFragment() { }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.material_prefs_fragment_preference, container, false);
        LinearLayout v = (LinearLayout) rootView.findViewById(R.id.fragment_preference_container);

        int layoutResId = getLayoutResourceId();
        if (layoutResId != -1) {
            View child = inflater.inflate(layoutResId, v, false);
            v.addView(child);
        }

        return rootView;
    }

}
