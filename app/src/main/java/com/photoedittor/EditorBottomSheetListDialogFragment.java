package com.photoedittor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EditorBottomSheetListDialogFragment extends BottomSheetDialogFragment {


    public static EditorBottomSheetListDialogFragment newInstance(int itemCount) {
        final EditorBottomSheetListDialogFragment fragment = new EditorBottomSheetListDialogFragment();
        final Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edittorbottomsheet_list_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) view;

    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


}
