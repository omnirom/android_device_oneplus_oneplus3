/*
* Copyright (C) 2016 The OmniROM Project
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*
*/
package org.omnirom.device;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.TwoStatePreference;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.util.Log;

public class DeviceSettings extends PreferenceActivity implements
        Preference.OnPreferenceChangeListener {

    public static final String KEY_CAMERA_SWITCH = "camera";
    public static final String KEY_TORCH_SWITCH = "torch";
    public static final String KEY_VIBSTRENGTH = "vib_strength";
    public static final String KEY_MUSIC_SWITCH = "music";
    public static final String KEY_UP_ARROW_SWITCH ="up_arrow";
    public static final String KEY_LEFT_ARROW_SWITCH ="left_arrow";
    public static final String KEY_RIGHT_ARROW_SWITCH ="right_arrow";

    private static final String KEY_SLIDER_MODE_TOP = "slider_mode_top";
    private static final String KEY_SLIDER_MODE_CENTER = "slider_mode_center";
    private static final String KEY_SLIDER_MODE_BOTTOM = "slider_mode_bottom";
    private static final String KEY_SWAP_BACK_RECENTS = "swap_back_recents";
    public static final String KEY_SRGB_SWITCH = "srgb";
    public static final String KEY_HBM_SWITCH = "hbm";
    public static final String KEY_PROXI_SWITCH = "proxi";
    private static final String KEY_MUSIC_APP = "music_gesture_app";
    private static final String KEY_MUSIC_CONTROL = "music_gesture_control";
    private static final String KEY_CAMERA_APP = "camera_gesture_app";
    private static final String KEY_CAMERA_CONTROL = "camera_gesture_control";
    private static final String KEY_TORCH_APP = "torch_gesture_app";
    private static final String KEY_TORCH_CONTROL = "torch_gesture_control";
    private static final String KEY_UP_ARROW_APP = "up_arrow_gesture_app";
    private static final String KEY_LEFT_ARROW_APP = "left_arrow_gesture_app";
    private static final String KEY_RIGHT_ARROW_APP = "right_arrow_gesture_app";

    public static final String DEVICE_GESTURE_MAPPING_0 = "device_gesture_mapping_0";
    public static final String DEVICE_GESTURE_MAPPING_1 = "device_gesture_mapping_1";
    public static final String DEVICE_GESTURE_MAPPING_2 = "device_gesture_mapping_2";
    public static final String DEVICE_GESTURE_MAPPING_3 = "device_gesture_mapping_3";
    public static final String DEVICE_GESTURE_MAPPING_4 = "device_gesture_mapping_4";
    public static final String DEVICE_GESTURE_MAPPING_5 = "device_gesture_mapping_5";

    private TwoStatePreference mTorchSwitch;
    private TwoStatePreference mCameraSwitch;
    private VibratorStrengthPreference mVibratorStrength;
    private TwoStatePreference mMusicSwitch;
    private ListPreference mSliderModeTop;
    private ListPreference mSliderModeCenter;
    private ListPreference mSliderModeBottom;
    private TwoStatePreference mSwapBackRecents;
    private TwoStatePreference mSRGBModeSwitch;
    private TwoStatePreference mHBMModeSwitch;
    private TwoStatePreference mProxiSwitch;
    private AppSelectListPreference mMusicApp;
    private TwoStatePreference mMusicControl;
    private AppSelectListPreference mCameraApp;
    private TwoStatePreference mCameraControl;
    private AppSelectListPreference mTorchApp;
    private TwoStatePreference mTorchControl;
    private TwoStatePreference mUpArrowSwitch;
    private AppSelectListPreference mUpArrowApp;
    private TwoStatePreference mLeftArrowSwitch;
    private AppSelectListPreference mLeftArrowApp;
    private TwoStatePreference mRightArrowSwitch;
    private AppSelectListPreference mRightArrowApp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        addPreferencesFromResource(R.xml.main);

        mTorchSwitch = (TwoStatePreference) findPreference(KEY_TORCH_SWITCH);
        mTorchSwitch.setEnabled(TorchGestureSwitch.isSupported());
        mTorchSwitch.setChecked(TorchGestureSwitch.isCurrentlyEnabled(this));
        mTorchSwitch.setOnPreferenceChangeListener(new TorchGestureSwitch());

        mCameraSwitch = (TwoStatePreference) findPreference(KEY_CAMERA_SWITCH);
        mCameraSwitch.setEnabled(CameraGestureSwitch.isSupported());
        mCameraSwitch.setChecked(CameraGestureSwitch.isCurrentlyEnabled(this));
        mCameraSwitch.setOnPreferenceChangeListener(new CameraGestureSwitch());

        mVibratorStrength = (VibratorStrengthPreference) findPreference(KEY_VIBSTRENGTH);
        if (mVibratorStrength != null) {
            mVibratorStrength.setEnabled(VibratorStrengthPreference.isSupported());
        }

        mMusicSwitch = (TwoStatePreference) findPreference(KEY_MUSIC_SWITCH);
        mMusicSwitch.setEnabled(MusicGestureSwitch.isSupported());
        mMusicSwitch.setChecked(MusicGestureSwitch.isCurrentlyEnabled(this));
        mMusicSwitch.setOnPreferenceChangeListener(new MusicGestureSwitch());

        mSliderModeTop = (ListPreference) findPreference(KEY_SLIDER_MODE_TOP);
        mSliderModeTop.setOnPreferenceChangeListener(this);
        int sliderModeTop = getSliderAction(0);
        int valueIndex = mSliderModeTop.findIndexOfValue(String.valueOf(sliderModeTop));
        mSliderModeTop.setValueIndex(valueIndex);
        mSliderModeTop.setSummary(mSliderModeTop.getEntries()[valueIndex]);

        mSliderModeCenter = (ListPreference) findPreference(KEY_SLIDER_MODE_CENTER);
        mSliderModeCenter.setOnPreferenceChangeListener(this);
        int sliderModeCenter = getSliderAction(1);
        valueIndex = mSliderModeCenter.findIndexOfValue(String.valueOf(sliderModeCenter));
        mSliderModeCenter.setValueIndex(valueIndex);
        mSliderModeCenter.setSummary(mSliderModeCenter.getEntries()[valueIndex]);

        mSliderModeBottom = (ListPreference) findPreference(KEY_SLIDER_MODE_BOTTOM);
        mSliderModeBottom.setOnPreferenceChangeListener(this);
        int sliderModeBottom = getSliderAction(2);
        valueIndex = mSliderModeBottom.findIndexOfValue(String.valueOf(sliderModeBottom));
        mSliderModeBottom.setValueIndex(valueIndex);
        mSliderModeBottom.setSummary(mSliderModeBottom.getEntries()[valueIndex]);

        mSwapBackRecents = (TwoStatePreference) findPreference(KEY_SWAP_BACK_RECENTS);
        mSwapBackRecents.setChecked(Settings.System.getInt(getContentResolver(),
                    Settings.System.BUTTON_SWAP_BACK_RECENTS, 0) != 0);

        mSRGBModeSwitch = (TwoStatePreference) findPreference(KEY_SRGB_SWITCH);
        mSRGBModeSwitch.setEnabled(SRGBModeSwitch.isSupported());
        mSRGBModeSwitch.setChecked(SRGBModeSwitch.isCurrentlyEnabled(this));
        mSRGBModeSwitch.setOnPreferenceChangeListener(new SRGBModeSwitch());

        mHBMModeSwitch = (TwoStatePreference) findPreference(KEY_HBM_SWITCH);
        mHBMModeSwitch.setEnabled(HBMModeSwitch.isSupported());
        mHBMModeSwitch.setChecked(HBMModeSwitch.isCurrentlyEnabled(this));
        mHBMModeSwitch.setOnPreferenceChangeListener(new HBMModeSwitch());

        mProxiSwitch = (TwoStatePreference) findPreference(KEY_PROXI_SWITCH);
        mProxiSwitch.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.DEVICE_PROXI_CHECK_ENABLED, 1) != 0);

        mMusicApp = (AppSelectListPreference) findPreference(KEY_MUSIC_APP);
        mMusicApp.setOnPreferenceChangeListener(this);

        mMusicControl = (TwoStatePreference) findPreference(KEY_MUSIC_CONTROL);
        mMusicControl.setOnPreferenceChangeListener(this);

        updateGestureConfig(DEVICE_GESTURE_MAPPING_0, mMusicApp, mMusicControl);

        mCameraApp = (AppSelectListPreference) findPreference(KEY_CAMERA_APP);
        mCameraApp.setOnPreferenceChangeListener(this);

        mCameraControl = (TwoStatePreference) findPreference(KEY_CAMERA_CONTROL);
        mCameraControl.setOnPreferenceChangeListener(this);

        updateGestureConfig(DEVICE_GESTURE_MAPPING_1, mCameraApp, mCameraControl);

        mTorchApp = (AppSelectListPreference) findPreference(KEY_TORCH_APP);
        mTorchApp.setOnPreferenceChangeListener(this);

        mTorchControl = (TwoStatePreference) findPreference(KEY_TORCH_CONTROL);
        mTorchControl.setOnPreferenceChangeListener(this);

        updateGestureConfig(DEVICE_GESTURE_MAPPING_2, mTorchApp, mTorchControl);

        mUpArrowSwitch = (TwoStatePreference) findPreference(KEY_UP_ARROW_SWITCH);
        mUpArrowSwitch.setEnabled(UpArrowGestureSwitch.isSupported());
        mUpArrowSwitch.setChecked(UpArrowGestureSwitch.isCurrentlyEnabled(this));
        mUpArrowSwitch.setOnPreferenceChangeListener(new UpArrowGestureSwitch());

        mUpArrowApp = (AppSelectListPreference) findPreference(KEY_UP_ARROW_APP);
        String value = Settings.System.getString(getContentResolver(), DEVICE_GESTURE_MAPPING_3);
        mUpArrowApp.setValue(value);
        mUpArrowApp.setOnPreferenceChangeListener(this);

        mLeftArrowSwitch = (TwoStatePreference) findPreference(KEY_LEFT_ARROW_SWITCH);
        mLeftArrowSwitch.setEnabled(LeftArrowGestureSwitch.isSupported());
        mLeftArrowSwitch.setChecked(LeftArrowGestureSwitch.isCurrentlyEnabled(this));
        mLeftArrowSwitch.setOnPreferenceChangeListener(new LeftArrowGestureSwitch());

        mLeftArrowApp = (AppSelectListPreference) findPreference(KEY_LEFT_ARROW_APP);
        value = Settings.System.getString(getContentResolver(), DEVICE_GESTURE_MAPPING_4);
        mLeftArrowApp.setValue(value);
        mLeftArrowApp.setOnPreferenceChangeListener(this);

        mRightArrowSwitch = (TwoStatePreference) findPreference(KEY_RIGHT_ARROW_SWITCH);
        mRightArrowSwitch.setEnabled(RightArrowGestureSwitch.isSupported());
        mRightArrowSwitch.setChecked(RightArrowGestureSwitch.isCurrentlyEnabled(this));
        mRightArrowSwitch.setOnPreferenceChangeListener(new RightArrowGestureSwitch());

        mRightArrowApp = (AppSelectListPreference) findPreference(KEY_RIGHT_ARROW_APP);
        value = Settings.System.getString(getContentResolver(), DEVICE_GESTURE_MAPPING_5);
        mRightArrowApp.setValue(value);
        mRightArrowApp.setOnPreferenceChangeListener(this);

        mRightArrowApp.setEnabled(!mMusicControl.isChecked());
        mLeftArrowApp.setEnabled(!mMusicControl.isChecked());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            finish();
            return true;
        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == mSwapBackRecents) {
            Settings.System.putInt(getContentResolver(),
                    Settings.System.BUTTON_SWAP_BACK_RECENTS, mSwapBackRecents.isChecked() ? 1 : 0);
            return true;
        } else if (preference == mProxiSwitch) {
            Settings.System.putInt(getContentResolver(),
                    Settings.System.DEVICE_PROXI_CHECK_ENABLED, mProxiSwitch.isChecked() ? 1 : 0);
            return true;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mSliderModeTop) {
            String value = (String) newValue;
            int sliderMode = Integer.valueOf(value);
            setSliderAction(0, sliderMode);
            int valueIndex = mSliderModeTop.findIndexOfValue(value);
            mSliderModeTop.setSummary(mSliderModeTop.getEntries()[valueIndex]);
        } else if (preference == mSliderModeCenter) {
            String value = (String) newValue;
            int sliderMode = Integer.valueOf(value);
            setSliderAction(1, sliderMode);
            int valueIndex = mSliderModeCenter.findIndexOfValue(value);
            mSliderModeCenter.setSummary(mSliderModeCenter.getEntries()[valueIndex]);
        } else if (preference == mSliderModeBottom) {
            String value = (String) newValue;
            int sliderMode = Integer.valueOf(value);
            setSliderAction(2, sliderMode);
            int valueIndex = mSliderModeBottom.findIndexOfValue(value);
            mSliderModeBottom.setSummary(mSliderModeBottom.getEntries()[valueIndex]);
        } else if (preference == mMusicApp) {
            String value = (String) newValue;
            Settings.System.putString(getContentResolver(), DEVICE_GESTURE_MAPPING_0, value);
        } else if (preference == mMusicControl) {
            Boolean value = (Boolean) newValue;
            updateGestureConfig(DEVICE_GESTURE_MAPPING_0, mMusicApp, value);
            mRightArrowApp.setEnabled(!value);
            mLeftArrowApp.setEnabled(!value);
        } else if (preference == mCameraApp) {
            String value = (String) newValue;
            Settings.System.putString(getContentResolver(), DEVICE_GESTURE_MAPPING_1, value);
        } else if (preference == mCameraControl) {
            Boolean value = (Boolean) newValue;
            updateGestureConfig(DEVICE_GESTURE_MAPPING_1, mCameraApp, value);
        } else if (preference == mTorchApp) {
            String value = (String) newValue;
            Settings.System.putString(getContentResolver(), DEVICE_GESTURE_MAPPING_2, value);
        } else if (preference == mTorchControl) {
            Boolean value = (Boolean) newValue;
            updateGestureConfig(DEVICE_GESTURE_MAPPING_2, mTorchApp, value);
        } else if (preference == mUpArrowApp) {
            String value = (String) newValue;
            Settings.System.putString(getContentResolver(), DEVICE_GESTURE_MAPPING_3, value);
        } else if (preference == mLeftArrowApp) {
            String value = (String) newValue;
            Settings.System.putString(getContentResolver(), DEVICE_GESTURE_MAPPING_4, value);
        } else if (preference == mRightArrowApp) {
            String value = (String) newValue;
            Settings.System.putString(getContentResolver(), DEVICE_GESTURE_MAPPING_5, value);
        }
        return true;
    }

    private int getSliderAction(int position) {
        String value = Settings.System.getString(getContentResolver(),
                    Settings.System.BUTTON_EXTRA_KEY_MAPPING);
        final String defaultValue = "5,3,0";

        if (value == null) {
            value = defaultValue;
        } else if (value.indexOf(",") == -1) {
            value = defaultValue;
        }
        try {
            String[] parts = value.split(",");
            return Integer.valueOf(parts[position]);
        } catch (Exception e) {
        }
        return 0;
    }

    private void setSliderAction(int position, int action) {
        String value = Settings.System.getString(getContentResolver(),
                    Settings.System.BUTTON_EXTRA_KEY_MAPPING);
        final String defaultValue = "5,3,0";

        if (value == null) {
            value = defaultValue;
        } else if (value.indexOf(",") == -1) {
            value = defaultValue;
        }
        try {
            String[] parts = value.split(",");
            parts[position] = String.valueOf(action);
            String newValue = TextUtils.join(",", parts);
            Settings.System.putString(getContentResolver(),
                    Settings.System.BUTTON_EXTRA_KEY_MAPPING, newValue);
        } catch (Exception e) {
        }
    }

    private void updateGestureConfig(String prefKey, AppSelectListPreference appPref, TwoStatePreference controlPref) {
        String value = Settings.System.getString(getContentResolver(), prefKey);
        boolean defaultValue = false;
        if (TextUtils.isEmpty(value) || value.equals("default#")) {
            defaultValue = true;
            value = "";
        } else if (value.startsWith("default#")) {
            defaultValue = true;
            value = value.substring("default#".length(), value.length());
        }
        if (defaultValue) {
            appPref.setEnabled(false);
            controlPref.setChecked(true);
        } else {
            appPref.setValue(value);
            controlPref.setChecked(false);
        }
    }

    private void updateGestureConfig(String prefKey, AppSelectListPreference appPref, boolean defaultValue) {
        String oldValue = Settings.System.getString(getContentResolver(), prefKey);
        if (TextUtils.isEmpty(oldValue) || oldValue.equals("default#")) {
            oldValue = "";
        } else if (oldValue.startsWith("default#")) {
            oldValue = oldValue.substring("default#".length(), oldValue.length());
        }
        if (defaultValue) {
            appPref.setEnabled(false);
            Settings.System.putString(getContentResolver(), prefKey, "default#" + oldValue);
        } else {
            appPref.setEnabled(true);
            appPref.setValue(oldValue);
            Settings.System.putString(getContentResolver(), prefKey, oldValue);
        }
    }
}
