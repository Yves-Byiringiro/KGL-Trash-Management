package com.kgltrash.view;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.kgltrash.R;

/**
 * Author: Aanuoluwapo Orioke
 */
public class ReceiptImageFragment extends Fragment {

    private static final String ARG_RECEIPT_IMAGE_PATH = "receiptImagePath";

    private String mReceiptImagePath;

    public ReceiptImageFragment() {
    }

    public static ReceiptImageFragment newInstance(String receiptImagePath) {
        ReceiptImageFragment fragment = new ReceiptImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RECEIPT_IMAGE_PATH, receiptImagePath);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mReceiptImagePath = getArguments().getString(ARG_RECEIPT_IMAGE_PATH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_receipt_image_fragment, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.receipt_image);
        Uri imageUri = Uri.parse(mReceiptImagePath);
        imageView.setImageURI(imageUri);
        return view;
    }
}