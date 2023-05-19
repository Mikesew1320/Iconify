package com.drdisagree.iconify.xposed.mods;

/* Modified from AOSPMods
 * https://github.com/siavash79/AOSPMods/blob/canary/app/src/main/java/sh/siava/AOSPMods/systemui/QSThemeManagerA12.java
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see [http://www.gnu.org/licenses/].
 */

import static com.drdisagree.iconify.common.Const.SYSTEMUI_PACKAGE;
import static com.drdisagree.iconify.common.Preferences.DUALTONE_QSPANEL;
import static com.drdisagree.iconify.common.Preferences.LIGHT_QSPANEL;
import static com.drdisagree.iconify.config.XPrefs.Xprefs;
import static de.robv.android.xposed.XposedBridge.hookAllConstructors;
import static de.robv.android.xposed.XposedBridge.hookAllMethods;
import static de.robv.android.xposed.XposedBridge.hookMethod;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.findMethodExact;
import static de.robv.android.xposed.XposedHelpers.findMethodExactIfExists;
import static de.robv.android.xposed.XposedHelpers.getObjectField;
import static de.robv.android.xposed.XposedHelpers.setObjectField;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;

import androidx.core.graphics.ColorUtils;

import com.drdisagree.iconify.xposed.HookEntry;
import com.drdisagree.iconify.xposed.ModPack;
import com.drdisagree.iconify.xposed.utils.Helpers;

import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

@SuppressWarnings("RedundantThrows")
public class QSLightThemeA12 extends ModPack {

    public static final String listenPackage = SYSTEMUI_PACKAGE;
    private static final String TAG = "Iconify - QSLightThemeA12: ";
    private static boolean lightQSHeaderEnabled = false;
    private static boolean dualToneQSEnabled = false;
    private Object mBehindColors;

    public QSLightThemeA12(Context context) {
        super(context);
    }

    @Override
    public void updatePrefs(String... Key) {
        if (Xprefs == null) return;

        dualToneQSEnabled = Xprefs.getBoolean(DUALTONE_QSPANEL, false);
        if (dualToneQSEnabled) Helpers.enableOverlay("IconifyComponentQSDT.overlay");
        else Helpers.disableOverlay("IconifyComponentQSDT.overlay");

        boolean lightQSEnabled = Xprefs.getBoolean(LIGHT_QSPANEL, false);
        if (lightQSEnabled) Helpers.enableOverlay("IconifyComponentQSLT.overlay");
        else Helpers.disableOverlay("IconifyComponentQSLT.overlay");

        setLightQSHeader(lightQSEnabled);
    }

    public void setLightQSHeader(boolean state) {
        if (lightQSHeaderEnabled != state) {
            lightQSHeaderEnabled = state;

            try {
                applyOverlays();
            } catch (Throwable ignored) {
            }
        }
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (!lpparam.packageName.equals(listenPackage)) return;

        Class<?> UtilsClass = findClass("com.android.settingslib.Utils", lpparam.classLoader);
        Class<?> OngoingPrivacyChipClass = findClass("com.android.systemui.privacy.OngoingPrivacyChip", lpparam.classLoader);
        Class<?> FragmentHostManagerClass = findClass("com.android.systemui.fragments.FragmentHostManager", lpparam.classLoader);
        Class<?> ScrimControllerClass = findClass("com.android.systemui.statusbar.phone.ScrimController", lpparam.classLoader);
        Class<?> GradientColorsClass = findClass("com.android.internal.colorextraction.ColorExtractor.GradientColors", lpparam.classLoader);
        Class<?> StatusbarClass = findClass("com.android.systemui.statusbar.phone.StatusBar", lpparam.classLoader);
        Class<?> InterestingConfigChangesClass = findClass("com.android.settingslib.applications.InterestingConfigChanges", lpparam.classLoader);

        Method applyStateMethod = findMethodExactIfExists(ScrimControllerClass, "applyStateToAlpha");
        if (applyStateMethod == null) {
            applyStateMethod = findMethodExact(ScrimControllerClass, "applyState");
        }

        try {
            mBehindColors = GradientColorsClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        hookAllMethods(ScrimControllerClass, "onUiModeChanged", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                mBehindColors = GradientColorsClass.newInstance();
            }
        });

        hookAllMethods(ScrimControllerClass, "updateScrims", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (!dualToneQSEnabled) return;

                Object mScrimBehind = getObjectField(param.thisObject, "mScrimBehind");
                boolean mBlankScreen = (boolean) getObjectField(param.thisObject, "mBlankScreen");
                float alpha = (float) callMethod(mScrimBehind, "getViewAlpha");
                boolean animateBehindScrim = alpha != 0 && !mBlankScreen;

                callMethod(mScrimBehind, "setColors", mBehindColors, animateBehindScrim);
            }
        });

        hookAllMethods(ScrimControllerClass, "updateThemeColors", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (!dualToneQSEnabled) return;

                @SuppressLint("DiscouragedApi") ColorStateList states = (ColorStateList) callStaticMethod(UtilsClass, "getColorAttr", mContext, mContext.getResources().getIdentifier("android:attr/colorSurfaceHeader", "attr", listenPackage));
                int surfaceBackground = states.getDefaultColor();

                ColorStateList accentStates = (ColorStateList) callStaticMethod(UtilsClass, "getColorAccent", mContext);
                int accent = accentStates.getDefaultColor();

                callMethod(mBehindColors, "setMainColor", surfaceBackground);
                callMethod(mBehindColors, "setSecondaryColor", accent);

                double contrast = ColorUtils.calculateContrast((int) callMethod(mBehindColors, "getMainColor"), Color.WHITE);

                callMethod(mBehindColors, "setSupportsDarkText", contrast > 4.5);
            }
        });


        findAndHookMethod(OngoingPrivacyChipClass, "updateResources", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (!lightQSHeaderEnabled) return;
                Resources res = mContext.getResources();

                @SuppressLint("DiscouragedApi") int iconColor = mContext.getColor(res.getIdentifier("android:color/system_neutral1_900", "color", mContext.getPackageName()));
                setObjectField(param.thisObject, "iconColor", iconColor);
            }
        });

        hookMethod(applyStateMethod, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (!lightQSHeaderEnabled) return;

                boolean mClipsQsScrim = (boolean) getObjectField(param.thisObject, "mClipsQsScrim");
                if (mClipsQsScrim) {
                    setObjectField(param.thisObject, "mBehindTint", Color.TRANSPARENT);
                }
            }
        });

        Class<?> ScrimStateEnum = findClass("com.android.systemui.statusbar.phone.ScrimState", lpparam.classLoader);

        Object[] constants = ScrimStateEnum.getEnumConstants();
        for (Object constant : constants) {
            String enumVal = constant.toString();
            switch (enumVal) {
                case "KEYGUARD":
                    findAndHookMethod(constant.getClass(), "prepare", ScrimStateEnum, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            if (!lightQSHeaderEnabled) return;

                            boolean mClipQsScrim = (boolean) getObjectField(param.thisObject, "mClipQsScrim");
                            if (mClipQsScrim) {
                                callMethod(param.thisObject, "updateScrimColor", getObjectField(param.thisObject, "mScrimBehind"), 1f, Color.TRANSPARENT);
                            }
                        }
                    });
                    break;
                case "BOUNCER":
                    findAndHookMethod(constant.getClass(), "prepare", ScrimStateEnum, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            if (!lightQSHeaderEnabled) return;

                            setObjectField(param.thisObject, "mBehindTint", Color.TRANSPARENT);
                        }
                    });
                    break;
                case "SHADE_LOCKED":
                    hookAllMethods(constant.getClass(), "prepare", new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            if (!lightQSHeaderEnabled) return;

                            setObjectField(param.thisObject, "mBehindTint", Color.TRANSPARENT);

                            boolean mClipQsScrim = (boolean) getObjectField(param.thisObject, "mClipQsScrim");
                            if (mClipQsScrim) {
                                callMethod(param.thisObject, "updateScrimColor", getObjectField(param.thisObject, "mScrimBehind"), 1f, Color.TRANSPARENT);
                            }
                        }
                    });
                    hookAllMethods(constant.getClass(), "getBehindTint", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            if (!lightQSHeaderEnabled) return;
                            param.setResult(Color.TRANSPARENT);
                        }
                    });
                    break;

                case "UNLOCKED":
                    findAndHookMethod(constant.getClass(), "prepare", ScrimStateEnum, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            if (!lightQSHeaderEnabled) return;

                            setObjectField(param.thisObject, "mBehindTint", Color.TRANSPARENT);

                            callMethod(param.thisObject, "updateScrimColor", getObjectField(param.thisObject, "mScrimBehind"), 1f, Color.TRANSPARENT);
                        }
                    });
                    break;
            }
        }

        hookAllConstructors(FragmentHostManagerClass, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                setObjectField(param.thisObject, "mConfigChanges", InterestingConfigChangesClass.getDeclaredConstructor(int.class).newInstance(0x40000000 | 0x0004 | 0x0100 | 0x80000000 | 0x0200));
            }
        });

        hookAllConstructors(StatusbarClass, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                hookAllMethods(getObjectField(param.thisObject, "mOnColorsChangedListener").getClass(), "onColorsChanged", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        applyOverlays();
                    }
                });
            }
        });
    }

    private void applyOverlays() throws Throwable {
        boolean isDark = getIsDark();

        Helpers.disableOverlay("IconifyComponentQSLT.overlay");

        Thread.sleep(50);

        if (lightQSHeaderEnabled && !isDark) {
            Helpers.enableOverlay("IconifyComponentQSLT.overlay");
        }
    }

    private boolean getIsDark() {
        return (mContext.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_YES) == Configuration.UI_MODE_NIGHT_YES;
    }

    @Override
    public boolean listensTo(String packageName) {
        return listenPackage.equals(packageName) && !HookEntry.isChildProcess && Build.VERSION.SDK_INT < 33;
    }
}