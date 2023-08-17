package com.example.notes1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.ItemTouchHelper;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.notes1.Database.Note;
import com.example.notes1.ViewModel.NoteViewModel;
import com.example.notes1.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    NoteViewModel noteViewModel;
    NoteAdapter noteAdapter;
    List<Note> allNote;
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setFilter();

        binding.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditNote.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });
        binding.rcyclerNotes.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        noteAdapter = new NoteAdapter();
        binding.rcyclerNotes.setAdapter(noteAdapter);
        noteViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance((Application) this.getApplicationContext()))
                .get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                noteAdapter.setNotes(notes);
                allNote = notes;
                Log.d("set adapter", "set adapter");

            }
        });


        noteAdapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
//                since we are in anonymous inner class we need to specify Main Activity
                Intent intent = new Intent(MainActivity.this, AddEditNote.class);
//                we need to send our title and other info for update
                intent.putExtra(AddEditNote.EXTRA_ID, note.getId());
                intent.putExtra(AddEditNote.EXTRA_TITLE, note.getTitle());
                intent.putExtra(AddEditNote.EXTRA_SUBTITLE, note.getSubtitle());
                intent.putExtra(AddEditNote.EXTRA_NOTE, note.getNote());
                intent.putExtra(AddEditNote.EXTRA_PRIORITY, note.getPriority());
                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                Collections.swap(allNote, fromPosition, toPosition);
                recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
                return false;
            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                final Note itemDeleted = noteAdapter.getNoteAt(viewHolder.getAdapterPosition());
                noteViewModel.delete(noteAdapter.getNoteAt(viewHolder.getAdapterPosition()));


                Snackbar.make(binding.rcyclerNotes, "Note  deleted", BaseTransientBottomBar.LENGTH_SHORT)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                noteViewModel.insert(itemDeleted);
                                binding.rcyclerNotes.getAdapter().notifyDataSetChanged();
                                binding.rcyclerNotes.scrollToPosition(viewHolder.getAdapterPosition());

                            }
                        })
                        .setActionTextColor(getResources().getColor(R.color.colorAccent))
                        .show();

            }
        }).attachToRecyclerView(binding.rcyclerNotes);


    }

    private void setFilter() {
        binding.txtNoFilter.setBackgroundResource(R.drawable.filter_selected_shape);
        binding.txtNoFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData(0);
                binding.txtNoFilter.setBackgroundResource(R.drawable.filter_selected_shape);
                binding.txtLowtoHigh.setBackgroundResource(R.drawable.filter_un_shape);
                binding.txtHightoLow.setBackgroundResource(R.drawable.filter_un_shape);
            }


        });
        binding.txtLowtoHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData(1);
                binding.txtLowtoHigh.setBackgroundResource(R.drawable.filter_selected_shape);
                binding.txtNoFilter.setBackgroundResource(R.drawable.filter_un_shape);
                binding.txtHightoLow.setBackgroundResource(R.drawable.filter_un_shape);

            }


        });
        binding.txtHightoLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData(2);
                binding.txtHightoLow.setBackgroundResource(R.drawable.filter_selected_shape);
                binding.txtLowtoHigh.setBackgroundResource(R.drawable.filter_un_shape);
                binding.txtNoFilter.setBackgroundResource(R.drawable.filter_un_shape);
            }
        });

    }

    private void loadData(int i) {
        if (i == 0) {
            noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
                @Override
                public void onChanged(List<Note> notes) {
                    noteAdapter.setNotes(notes);
                    allNote = notes;
                    Log.d("set adapter", "set adapter");

                }
            });
        } else if (i == 1) {
            noteViewModel.getLowtoHigh().observe(this, new Observer<List<Note>>() {
                @Override
                public void onChanged(List<Note> notes) {
                    noteAdapter.setNotes(notes);
                    allNote = notes;
                    Log.d("set adapter", "set adapter");

                }
            });
        } else {
            noteViewModel.getHightoLow().observe(this, new Observer<List<Note>>() {
                @Override
                public void onChanged(List<Note> notes) {
                    noteAdapter.setNotes(notes);
                    allNote = notes;
                    Log.d("set adapter", "set adapter");

                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditNote.EXTRA_TITLE);
            String subtitle = data.getStringExtra(AddEditNote.EXTRA_SUBTITLE);
            String note = data.getStringExtra(AddEditNote.EXTRA_NOTE);
            String priority = data.getStringExtra(AddEditNote.EXTRA_PRIORITY);
            String date = new SimpleDateFormat("EEEE, dd-MMM-yyyy", Locale.getDefault()).format(new Date());
            Note note1 = new Note(title, subtitle, date, note, priority);
            noteViewModel.insert(note1);
            Toast.makeText(this, "note saved", Toast.LENGTH_SHORT).show();

        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditNote.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String title = data.getStringExtra(AddEditNote.EXTRA_TITLE);
            String subtitle = data.getStringExtra(AddEditNote.EXTRA_SUBTITLE);
            String note = data.getStringExtra(AddEditNote.EXTRA_NOTE);
            String priority = data.getStringExtra(AddEditNote.EXTRA_PRIORITY);
            String date = new SimpleDateFormat("EEEE, dd-MM-yyyy", Locale.getDefault()).format(new Date());
            Note note1 = new Note(title, subtitle, date, note, priority);
            note1.setId(id);
            noteViewModel.update(note1);
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();


        } else {
            Toast.makeText(this, "No changes made", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_notes);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search your note here...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                NotesSearch(newText);
                Log.d("text changed", "changed");
                return true;
            }
        });
        return true;
    }

    private void NotesSearch(String newText) {
        Log.d("Search", "NotesSearch: " + newText);

        ArrayList<Note> filterNotes = new ArrayList<>();
        for (Note items : allNote) {
            if (items.getTitle().toLowerCase().contains((newText.toLowerCase())) || items.getSubtitle().toLowerCase().contains((newText.toLowerCase()))) {
                Log.d("search", "NotesSearch: ");
                filterNotes.add(items);
            }

        }
        noteAdapter.filteredList(filterNotes);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteAll:
                noteViewModel.deleteAllNotes();
                return true;

            case R.id.search_notes:

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}