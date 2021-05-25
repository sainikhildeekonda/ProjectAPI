package com.example.projectapi;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrationFragment extends Fragment {

    EditText edt_email,edt_pass,edt_cpass,edt_name,edt_location, edt_dob, edt_gender;
    Button btn_register;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
//    private NavController navController;

    public RegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edt_email = view.findViewById(R.id.edt_register_email);
        edt_pass = view.findViewById(R.id.edt_register_password);
        edt_cpass = view.findViewById(R.id.edt_register_CPassword);
        edt_name = view.findViewById(R.id.edt_register_name);
        edt_location = view.findViewById(R.id.edt_register_location);
        edt_dob = view.findViewById(R.id.edt_register_dob);
        edt_gender = view.findViewById(R.id.edt_register_gender);
        btn_register = view.findViewById(R.id.btn_register);

//        navController = Navigation.findNavController(requireActivity(),R.id.host_fragment);

        btn_register.setOnClickListener(view1 -> {

            if (!checkEmptyFields())
            {
                if (edt_pass.getText().length()<6)
                {
                    edt_pass.setError("Invalid Password, Password Should be at least 6 characters");
                    edt_pass.requestFocus();
                }else {
                    if (!edt_pass.getText().toString().equals(edt_cpass.getText().toString()))
                    {
                        edt_cpass.setError("Password not match!");
                        edt_cpass.requestFocus();
                    }else
                    {
                        String email = edt_email.getText().toString();
                        String pass = edt_pass.getText().toString();
                        String name = edt_name.getText().toString();
                        String location = edt_location.getText().toString();
                        String dob = edt_dob.getText().toString();
                        String gender = edt_gender.getText().toString();
                        Person person = new Person(email,pass,name,location,dob,gender);

                        createUser(person);

                    }
                }

            }

        });

    }

    public boolean checkEmptyFields()
    {
        if(TextUtils.isEmpty(edt_email.getText().toString()))
        {
            edt_email.setError("Email cannot be empty!");
            edt_email.requestFocus();
            return true;
        }else if (TextUtils.isEmpty(edt_pass.getText().toString()))
        {
            edt_pass.setError("Password cannot be empty!");
            edt_pass.requestFocus();
            return true;
        }else if (TextUtils.isEmpty(edt_cpass.getText().toString()))
        {
            edt_cpass.setError("Confirm Password cannot be empty!");
            edt_cpass.requestFocus();
            return true;
        }else if (TextUtils.isEmpty(edt_name.getText().toString()))
        {
            edt_name.setError("Name cannot be empty!");
            edt_name.requestFocus();
            return true;
        }else if (TextUtils.isEmpty(edt_location.getText().toString()))
        {
            edt_location.setError("Location cannot be empty!");
            edt_location.requestFocus();
            return true;
        }else if (TextUtils.isEmpty(edt_dob.getText().toString()))
        {
            edt_dob.setError("dob cannot be empty!");
            edt_dob.requestFocus();
            return true;
        }else if (TextUtils.isEmpty(edt_gender.getText().toString()))
        {
            edt_gender.setError("gender cannot be empty!");
            edt_gender.requestFocus();
            return true;
        }

        return false;
    }

    public void createUser(Person person)
    {
        auth.createUserWithEmailAndPassword(person.getEmail(),person.getPassword())
                .addOnCompleteListener(requireActivity(), task ->
                {

                    if (task.isSuccessful())
                    {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        writeFireStore(person, firebaseUser);
                    }
                    else {
                        Toast.makeText(requireContext(),"Registration Error!",Toast.LENGTH_LONG).show();
                    }

                });
    }

    public void writeFireStore(Person person, FirebaseUser firebaseUser)
    {
        Map<String,Object> userMap = new HashMap<>();
        userMap.put("Name",person.getName());
        userMap.put("Email",person.getEmail());
        userMap.put("Location",person.getLocation());
        userMap.put("dob",person.getDob());
        userMap.put("gender",person.getGender());
        DocumentReference documentReference = firestore.collection("User").document(firebaseUser.getUid());
        documentReference.set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(requireContext(),"Registration Success!",Toast.LENGTH_LONG).show();
                FirebaseAuth.getInstance().signOut();
                requireActivity().onBackPressed();
//                launchLogin();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireContext(),"FireStore Error!",Toast.LENGTH_LONG).show();

            }
        });

    }

    private void launchLogin(){
        Fragment loginFragment = new LoginFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, loginFragment );
        transaction.addToBackStack(null);
        transaction.commit();
    }
}