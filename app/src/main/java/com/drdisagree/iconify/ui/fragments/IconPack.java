package com.drdisagree.iconify.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.drdisagree.iconify.Iconify;
import com.drdisagree.iconify.R;
import com.drdisagree.iconify.databinding.FragmentIconPackBinding;
import com.drdisagree.iconify.ui.adapters.IconPackAdapter;
import com.drdisagree.iconify.ui.adapters.MenuAdapter;
import com.drdisagree.iconify.ui.adapters.SectionTitleAdapter;
import com.drdisagree.iconify.ui.base.BaseFragment;
import com.drdisagree.iconify.ui.dialogs.LoadingDialog;
import com.drdisagree.iconify.ui.models.IconPackModel;
import com.drdisagree.iconify.ui.models.MenuModel;
import com.drdisagree.iconify.ui.utils.ViewHelper;

import java.util.ArrayList;

public class IconPack extends BaseFragment {

    private FragmentIconPackBinding binding;
    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentIconPackBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Header
        ViewHelper.setHeader(requireContext(), getParentFragmentManager(), binding.header.toolbar, R.string.activity_title_icon_pack);

        // Loading dialog while enabling or disabling pack
        loadingDialog = new LoadingDialog(requireContext());

        // RecyclerView
        binding.iconPackContainer.setLayoutManager(new LinearLayoutManager(requireContext()));
        ConcatAdapter adapter = new ConcatAdapter(initActivityItems(), new SectionTitleAdapter(requireContext(), R.layout.view_section_title, R.string.icon_pack_styles), initIconPackItems());
        binding.iconPackContainer.setAdapter(adapter);
        binding.iconPackContainer.setHasFixedSize(true);

        return view;
    }

    private MenuAdapter initActivityItems() {
        ArrayList<MenuModel> iconpack_activity_list = new ArrayList<>();

        iconpack_activity_list.add(new MenuModel(R.id.action_iconPack_to_coloredBattery, getResources().getString(R.string.activity_title_colored_battery), getResources().getString(R.string.activity_desc_colored_battery), R.drawable.ic_colored_battery));
        iconpack_activity_list.add(new MenuModel(R.id.action_iconPack_to_mediaIcons, getResources().getString(R.string.activity_title_media_icons), getResources().getString(R.string.activity_desc_media_icons), R.drawable.ic_media_player_icon));
        iconpack_activity_list.add(new MenuModel(R.id.action_iconPack_to_settingsIcons, getResources().getString(R.string.activity_title_settings_icons), getResources().getString(R.string.activity_desc_settings_icons), R.drawable.ic_settings_icon_pack));

        return new MenuAdapter(requireContext(), iconpack_activity_list);
    }

    private IconPackAdapter initIconPackItems() {
        ArrayList<IconPackModel> iconpack_list = new ArrayList<>();

        iconpack_list.add(new IconPackModel("Aurora", R.string.iconpack_aurora_desc, R.drawable.preview_aurora_wifi, R.drawable.preview_aurora_signal, R.drawable.preview_aurora_airplane, R.drawable.preview_aurora_location));
        iconpack_list.add(new IconPackModel("Gradicon", R.string.iconpack_gradicon_desc, R.drawable.preview_gradicon_wifi, R.drawable.preview_gradicon_signal, R.drawable.preview_gradicon_airplane, R.drawable.preview_gradicon_location));
        iconpack_list.add(new IconPackModel("Lorn", R.string.iconpack_lorn_desc, R.drawable.preview_lorn_wifi, R.drawable.preview_lorn_signal, R.drawable.preview_lorn_airplane, R.drawable.preview_lorn_location));
        iconpack_list.add(new IconPackModel("Plumpy", R.string.iconpack_plumpy_desc, R.drawable.preview_plumpy_wifi, R.drawable.preview_plumpy_signal, R.drawable.preview_plumpy_airplane, R.drawable.preview_plumpy_location));
        iconpack_list.add(new IconPackModel("Acherus", R.string.iconpack_acherus_desc, R.drawable.preview_acherus_wifi, R.drawable.preview_acherus_signal, R.drawable.preview_acherus_airplane, R.drawable.preview_acherus_location));
        iconpack_list.add(new IconPackModel("Circular", R.string.iconpack_circular_desc, R.drawable.preview_circular_wifi, R.drawable.preview_circular_signal, R.drawable.preview_circular_airplane, R.drawable.preview_circular_location));
        iconpack_list.add(new IconPackModel("Filled", R.string.iconpack_filled_desc, R.drawable.preview_filled_wifi, R.drawable.preview_filled_signal, R.drawable.preview_filled_airplane, R.drawable.preview_filled_location));
        iconpack_list.add(new IconPackModel("Kai", R.string.iconpack_kai_desc, R.drawable.preview_kai_wifi, R.drawable.preview_kai_signal, R.drawable.preview_kai_airplane, R.drawable.preview_kai_location));
        iconpack_list.add(new IconPackModel("OOS", R.string.iconpack_oos_desc, R.drawable.preview_oos_wifi, R.drawable.preview_oos_signal, R.drawable.preview_oos_airplane, R.drawable.preview_oos_location));
        iconpack_list.add(new IconPackModel("Outline", R.string.iconpack_outline_desc, R.drawable.preview_outline_wifi, R.drawable.preview_outline_signal, R.drawable.preview_outline_airplane, R.drawable.preview_outline_location));
        iconpack_list.add(new IconPackModel("PUI", R.string.iconpack_pui_desc, R.drawable.preview_pui_wifi, R.drawable.preview_pui_signal, R.drawable.preview_pui_airplane, R.drawable.preview_pui_location));
        iconpack_list.add(new IconPackModel("Rounded", R.string.iconpack_rounded_desc, R.drawable.preview_rounded_wifi, R.drawable.preview_rounded_signal, R.drawable.preview_rounded_airplane, R.drawable.preview_rounded_location));
        iconpack_list.add(new IconPackModel("Sam", R.string.iconpack_sam_desc, R.drawable.preview_sam_wifi, R.drawable.preview_sam_signal, R.drawable.preview_sam_airplane, R.drawable.preview_sam_location));
        iconpack_list.add(new IconPackModel("Victor", R.string.iconpack_victor_desc, R.drawable.preview_victor_wifi, R.drawable.preview_victor_signal, R.drawable.preview_victor_airplane, R.drawable.preview_victor_location));

        return new IconPackAdapter(requireContext(), iconpack_list, loadingDialog);
    }

    @Override
    public void onDestroy() {
        loadingDialog.dismiss();
        super.onDestroy();
    }
}