package com.example.notes1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes1.Database.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private OnItemClickListener listener;
    private List<Note> note = new ArrayList<>();


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note currentNote = note.get(position);
        if (currentNote.getPriority() != null) {
        if (currentNote.getPriority().equals("3")) {
            holder.imgPriority.setBackgroundResource(R.drawable.red_shape);
        } else if (currentNote.getPriority().equals("2")) {
            holder.imgPriority.setBackgroundResource(R.drawable.green_shape);
        } else if (currentNote.getPriority().equals("1")) {
            holder.imgPriority.setBackgroundResource(R.drawable.yellow_shape);
        }
            else {
            holder.imgPriority.setBackgroundResource(R.drawable.red_shape);
            }
    }


        holder.txtTitle.setText(currentNote.getTitle());
        holder.txtSubtitle.setText(currentNote.getSubtitle());
        holder.txtDate.setText(currentNote.getDate());


    }

    public void setNotes(List<Note> note) {
        this.note = note;
        notifyDataSetChanged();
    }
    public Note getNoteAt(int position) {
        return note.get(position);
    }

    @Override
    public int getItemCount() {
        return note.size();

    }

    public void filteredList(List<Note> filterNotes) {
      this.note=filterNotes;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtSubtitle, txtDate;
        ImageView imgPriority;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtViewTitle);
            txtSubtitle = itemView.findViewById(R.id.txtViewSubTitle);
            txtDate = itemView.findViewById(R.id.txtDate);
            imgPriority = itemView.findViewById(R.id.viewPriority);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(note.get(position));
                    }
                }
            });
        }
    }


    public interface OnItemClickListener {
        void onItemClick(Note note);
    }
//to call onItemClick method from our adapter we need a reference to it so we create a method
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}


