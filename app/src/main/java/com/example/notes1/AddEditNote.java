package com.example.notes1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.notes1.databinding.ActivityAddEditNoteBinding;

public class AddEditNote extends AppCompatActivity {
    public static final String EXTRA_ID = "com.example.notes1.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.notes1.EXTRA_TITLE";
    public static final String EXTRA_SUBTITLE = "com.example.notes1.EXTRA_DESCRIPTION";
    public static final String EXTRA_NOTE = "com.example.notes1.EXTRA_NOTE";
    public static final String EXTRA_PRIORITY = "com.example.notes1.EXTRA_PRIORITY";

    ActivityAddEditNoteBinding addEditNoteBinding;
    String priority = "";
    boolean priorityButtonClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_edit_note);
        addEditNoteBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_edit_note);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            getSupportActionBar().setTitle("Edit Note");
//            set the edit text from the previous
            addEditNoteBinding.edtTxtTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            addEditNoteBinding.edtTxtSubTitle.setText(intent.getStringExtra(EXTRA_SUBTITLE));
            addEditNoteBinding.edtTxtNote.setText(intent.getStringExtra(EXTRA_NOTE));
            priority = intent.getStringExtra(EXTRA_PRIORITY);

            switch (priority) {
                case "3":
                    addEditNoteBinding.imgRed.setImageResource(R.drawable.ic_done_black_24dp);
                    addEditNoteBinding.imgGreen.setImageResource(0);
                    addEditNoteBinding.imgYellow.setImageResource(0);
                    break;
                case "2":
                    addEditNoteBinding.imgRed.setImageResource(0);
                    addEditNoteBinding.imgGreen.setImageResource(R.drawable.ic_done_black_24dp);
                    addEditNoteBinding.imgYellow.setImageResource(0);
                    break;
                case "1":
                    addEditNoteBinding.imgRed.setImageResource(0);
                    addEditNoteBinding.imgGreen.setImageResource(0);
                    addEditNoteBinding.imgYellow.setImageResource(R.drawable.ic_done_black_24dp);
                    break;

            }


        } else {

            getSupportActionBar().setTitle(R.string.AddNoteTitle);
        }

        addEditNoteBinding.imgRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEditNoteBinding.imgRed.setImageResource(R.drawable.ic_done_black_24dp);
                addEditNoteBinding.imgGreen.setImageResource(0);
                addEditNoteBinding.imgYellow.setImageResource(0);
                priority = "3";
                priorityButtonClicked = true;
            }
        });
        addEditNoteBinding.imgGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEditNoteBinding.imgGreen.setImageResource(R.drawable.ic_done_black_24dp);
                addEditNoteBinding.imgYellow.setImageResource(0);
                addEditNoteBinding.imgRed.setImageResource(0);
                priority = "2";
                priorityButtonClicked = true;
            }
        });
        addEditNoteBinding.imgYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEditNoteBinding.imgYellow.setImageResource(R.drawable.ic_done_black_24dp);
                addEditNoteBinding.imgRed.setImageResource(0);
                addEditNoteBinding.imgGreen.setImageResource(0);
                priority = "1";
                priorityButtonClicked = true;

            }
        });

        addEditNoteBinding.fabSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                only update the priority if a priority button is clicked
//               if(!priorityButtonClicked){
//                   retrieve the existing priority from intent if not changed
//                   priority=getIntent().getStringExtra(EXTRA_PRIORITY);
//               }
                saveNote();
            }
        });

    }



    private void saveNote() {
        String title = addEditNoteBinding.edtTxtTitle.getText().toString();
        String subtitle = addEditNoteBinding.edtTxtSubTitle.getText().toString();
        String note = addEditNoteBinding.edtTxtNote.getText().toString();
        if (!priorityButtonClicked) {
            priority = "3";
        }
//
        if (title.trim().isEmpty() || note.trim().isEmpty()) {
            Toast.makeText(this, "Please insert title and note", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_SUBTITLE, subtitle);
        data.putExtra(EXTRA_NOTE, note);
        data.putExtra(EXTRA_PRIORITY, priority);
//        this in case of Editing the note
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
//     id can never be negative
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK, data);
        finish();
    }
}
