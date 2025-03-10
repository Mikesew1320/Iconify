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
import com.drdisagree.iconify.databinding.FragmentBrightnessBarBinding;
import com.drdisagree.iconify.ui.adapters.BrightnessBarAdapter;
import com.drdisagree.iconify.ui.adapters.MenuAdapter;
import com.drdisagree.iconify.ui.adapters.SectionTitleAdapter;
import com.drdisagree.iconify.ui.base.BaseFragment;
import com.drdisagree.iconify.ui.dialogs.LoadingDialog;
import com.drdisagree.iconify.ui.models.BrightnessBarModel;
import com.drdisagree.iconify.ui.models.MenuModel;
import com.drdisagree.iconify.ui.utils.ViewHelper;

import java.util.ArrayList;

public class BrightnessBar extends BaseFragment {

    private FragmentBrightnessBarBinding binding;
    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBrightnessBarBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Header
        ViewHelper.setHeader(requireContext(), getParentFragmentManager(), binding.header.toolbar, R.string.activity_title_brightness_bar);

        // Loading dialog while enabling or disabling pack
        loadingDialog = new LoadingDialog(requireContext());

        // RecyclerView
        binding.brightnessBarContainer.setLayoutManager(new LinearLayoutManager(requireContext()));
        ConcatAdapter adapter = new ConcatAdapter(initActivityItems(), new SectionTitleAdapter(requireContext(), R.layout.view_section_title, R.string.brightness_bar_styles), initBrightnessBarItems());
        binding.brightnessBarContainer.setAdapter(adapter);
        binding.brightnessBarContainer.setHasFixedSize(true);

        return view;
    }

    private MenuAdapter initActivityItems() {
        ArrayList<MenuModel> brightnessbar_activity_list = new ArrayList<>();

        brightnessbar_activity_list.add(new MenuModel(R.id.action_brightnessBar_to_brightnessBarPixel, getResources().getString(R.string.activity_title_pixel_variant), getResources().getString(R.string.activity_desc_pixel_variant), R.drawable.ic_pixel_device));

        return new MenuAdapter(requireContext(), brightnessbar_activity_list);
    }

    private BrightnessBarAdapter initBrightnessBarItems() {
        ArrayList<BrightnessBarModel> bb_list = new ArrayList<>();

        bb_list.add(new BrightnessBarModel("Rounded Clip", R.drawable.bb_roundedclip, R.drawable.auto_bb_roundedclip, false));
        bb_list.add(new BrightnessBarModel("Rounded Bar", R.drawable.bb_rounded, R.drawable.auto_bb_rounded, false));
        bb_list.add(new BrightnessBarModel("Double Layer", R.drawable.bb_double_layer, R.drawable.auto_bb_double_layer, false));
        bb_list.add(new BrightnessBarModel("Shaded Layer", R.drawable.bb_shaded_layer, R.drawable.auto_bb_shaded_layer, false));
        bb_list.add(new BrightnessBarModel("Outline", R.drawable.bb_outline, R.drawable.auto_bb_outline, true));
        bb_list.add(new BrightnessBarModel("Leafy Outline", R.drawable.bb_leafy_outline, R.drawable.auto_bb_leafy_outline, true));
        bb_list.add(new BrightnessBarModel("Neumorph", R.drawable.bb_neumorph, R.drawable.auto_bb_neumorph, false));
        bb_list.add(new BrightnessBarModel("Inline", R.drawable.bb_inline, R.drawable.auto_bb_rounded, false));
        bb_list.add(new BrightnessBarModel("Neumorph Outline", R.drawable.bb_neumorph_outline, R.drawable.auto_bb_neumorph_outline, true));
        bb_list.add(new BrightnessBarModel("Neumorph Thumb", R.drawable.bb_neumorph_thumb, R.drawable.auto_bb_neumorph_thumb, false));
        bb_list.add(new BrightnessBarModel("Blocky Thumb", R.drawable.bb_blocky_thumb, R.drawable.auto_bb_blocky_thumb, false));
        bb_list.add(new BrightnessBarModel("Comet Thumb", R.drawable.bb_comet_thumb, R.drawable.auto_bb_comet_thumb, false));
        bb_list.add(new BrightnessBarModel("Minimal Thumb", R.drawable.bb_minimal_thumb, R.drawable.auto_bb_minimal_thumb, false));
        bb_list.add(new BrightnessBarModel("Old School Thumb", R.drawable.bb_oldschool_thumb, R.drawable.auto_bb_oldschool_thumb, false));
        bb_list.add(new BrightnessBarModel("Gradient Thumb", R.drawable.bb_gradient_thumb, R.drawable.auto_bb_gradient_thumb, false));
        bb_list.add(new BrightnessBarModel("Lighty", R.drawable.bb_lighty, R.drawable.auto_bb_lighty, true));
        bb_list.add(new BrightnessBarModel("Semi Transparent", R.drawable.bb_semi_transparent, R.drawable.auto_bb_semi_transparent, false));
        bb_list.add(new BrightnessBarModel("Thin Outline", R.drawable.bb_thin_outline, R.drawable.auto_bb_thin_outline, true));
        bb_list.add(new BrightnessBarModel("Purfect", R.drawable.bb_purfect, R.drawable.auto_bb_purfect, false));
        bb_list.add(new BrightnessBarModel("Translucent Outline", R.drawable.bb_translucent_outline, R.drawable.auto_bb_translucent_outline, true));

        return new BrightnessBarAdapter(requireContext(), bb_list, loadingDialog, "BBN");
    }

    @Override
    public void onDestroy() {
        loadingDialog.dismiss();
        super.onDestroy();
    }
}