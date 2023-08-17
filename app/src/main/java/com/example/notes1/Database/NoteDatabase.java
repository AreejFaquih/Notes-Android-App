package com.example.notes1.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class}, version = 2)
public abstract class NoteDatabase extends RoomDatabase {
    public abstract NoteDao noteDao();
   public static NoteDatabase INSTANCE;

   public static synchronized NoteDatabase getInstance(Context context){
       if(INSTANCE== null){
           INSTANCE= Room.databaseBuilder(context.getApplicationContext(),NoteDatabase.class,"note_database")
                   .fallbackToDestructiveMigration()
                   .addCallback(roomCallback)
                   .allowMainThreadQueries()
                   .build();
       }
           return INSTANCE;


   }

   private  static RoomDatabase.Callback roomCallback= new RoomDatabase.Callback(){
       @Override
       public void onCreate(@NonNull SupportSQLiteDatabase db) {
           super.onCreate(db);
           new PopulateDbAsyncTask(INSTANCE).execute();
       }
   };

   private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void>{
       private NoteDao noteDao;

       public PopulateDbAsyncTask(NoteDatabase db) {
           noteDao=db.noteDao();
       }

       @Override
       protected Void doInBackground(Void... voids) {
           noteDao.insert(new Note("title","subtitel","56","fesjhj","4"));
           return null;
       }
   }
}

