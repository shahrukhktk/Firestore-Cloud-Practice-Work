package com.ir_tech.firestorecloudexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity
{

    private static final String TAG = "MainActivity";

    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";

    private EditText ed_title, ed_description;
    private TextView loadData;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private DocumentReference dbReference = firebaseFirestore.document("Notebook/My First Note");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed_title = (EditText)findViewById(R.id.ed_title);
        ed_description = (EditText)findViewById(R.id.ed_description);
        loadData = (TextView)findViewById(R.id.text_view_data);

    }

    public void saveNote(View view)
    {
        final String title = ed_title.getText().toString();
        String description = ed_description.getText().toString();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(KEY_TITLE, title);
        hashMap.put(KEY_DESCRIPTION, description);

        //short way to reference
        //firebaseFirestore.collection("Notebook/My First Note");

        //reference to firestore
        dbReference.set(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        Toast.makeText(MainActivity.this, "Successfully Saved", Toast.LENGTH_SHORT).show();
                        ed_title.setText("");
                        ed_description.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(MainActivity.this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void loadNote(View view)
    {
        dbReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                if(documentSnapshot.exists())
                {
                    String title = documentSnapshot.getString(KEY_TITLE);
                    String description = documentSnapshot.getString(KEY_DESCRIPTION);
                    loadData.setText("Title: "+title + " \n Description: "+description);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Document Not Exits!", Toast.LENGTH_SHORT).show();
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}
