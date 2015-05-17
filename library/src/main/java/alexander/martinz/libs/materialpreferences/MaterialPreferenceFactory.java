package alexander.martinz.libs.materialpreferences;

import android.content.Context;
import android.text.TextUtils;

public class MaterialPreferenceFactory {

    public static MaterialPreference createPreference(Context context,
            boolean isCard, String key, String title, String summary, String unknown) {
        final MaterialPreference preference = new MaterialPreference(context);
        preference.setAsCard(isCard);
        preference.init(context);
        preference.setKey(key);
        preference.setTitle(title);
        preference.setSummary(TextUtils.isEmpty(summary) ? unknown : summary);
        return preference;
    }

    public static MaterialEditTextPreference createEditTextPreference(Context context,
            boolean isCard, String key, String title, String summary, String unknown,
            String value) {
        final MaterialEditTextPreference preference = new MaterialEditTextPreference(context);
        preference.setAsCard(isCard);
        preference.init(context);
        preference.setKey(key);
        preference.setTitle(title);
        preference.setSummary(TextUtils.isEmpty(summary) ? unknown : summary);
        preference.setValue(value);
        return preference;
    }

    public static MaterialListPreference createListPreference(Context context,
            boolean isCard, String key, String title, String summary, String unknown) {
        final MaterialListPreference preference = new MaterialListPreference(context);
        preference.setAsCard(isCard);
        preference.init(context);
        preference.setKey(key);
        preference.setTitle(title);
        preference.setSummary(TextUtils.isEmpty(summary) ? unknown : summary);
        return preference;
    }

    public static MaterialSwitchPreference createSwitchPreference(Context context, boolean isCard,
            String key, String title, String summary, String unknown, boolean isChecked) {
        final MaterialSwitchPreference preference = new MaterialSwitchPreference(context);
        preference.setAsCard(isCard);
        preference.init(context);
        preference.setKey(key);
        preference.setTitle(title);
        preference.setSummary(TextUtils.isEmpty(summary) ? unknown : summary);
        preference.setChecked(isChecked);
        return preference;
    }

}
