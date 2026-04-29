package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.myapplication.databinding.Fragment2Binding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment2 extends Fragment {

    public static Fragment2Binding fragment2Binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public interface OnFragment2DataListener {
        void onDataReceived(String name, String email, String gender, String age, String university, String phone);
    }

    private OnFragment2DataListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragment2DataListener) {
            listener = (OnFragment2DataListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragment2DataListener");
        }
    }

    public Fragment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment2 newInstance(String param1, String param2) {
        Fragment2 fragment = new Fragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragment2Binding = Fragment2Binding.inflate(inflater, container, false);
        View view = fragment2Binding.getRoot();


        EditText nameField = fragment2Binding.editTextName;
        EditText emailField = fragment2Binding.editTextEmail;
        RadioGroup genderGroup = fragment2Binding.radioGroupGender;
        EditText ageField = fragment2Binding.editTextAge;
        EditText universityField = fragment2Binding.editTextUniversity;
        EditText phoneField = fragment2Binding.editTextPhone;
        Button continueButton = fragment2Binding.buttoncontinue;


        continueButton.setOnClickListener(v -> {
            String name = nameField.getText().toString();
            String email = emailField.getText().toString();
            String age = ageField.getText().toString();
            String university = universityField.getText().toString();
            String phone = phoneField.getText().toString();

            int selectedGenderId = genderGroup.getCheckedRadioButtonId();
            String gender = "";
            if (selectedGenderId != -1) {
                RadioButton selectedGender = view.findViewById(selectedGenderId);
                gender = selectedGender.getText().toString();
            }

            listener.onDataReceived(name, email, gender, age, university, phone);

            Toast.makeText(getActivity(),
                    "Name: " + name + "\nEmail: " + email +
                            "\nGender: " + gender + "\nAge: " + age +
                            "\nUniversity: " + university + "\nPhone: " + phone,
                    Toast.LENGTH_LONG).show();
        });

        return view;
    }
}
