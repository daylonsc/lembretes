package br.android.com.lembretes.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.android.com.lembretes.data.model.Lembrete;
import br.android.com.lembretes.uteis.Util;

public class LembreteDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "lembrete.db";

    private static final int DATABASE_VERSION = 1;

    // Constructor
    public LembreteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE "
                + Lembrete.LembreteEntry.TABLE_NAME + " (" +
                Lembrete.LembreteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Lembrete.LembreteEntry.COLUNA_DESCRICAO_LEMBRETE + " TEXT NOT NULL, " +
                Lembrete.LembreteEntry.COLUNA_DATA_HORA + " DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_WAITLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Lembrete.LembreteEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public long inserirLembrete(String lembrete) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Lembrete.LembreteEntry.COLUNA_DESCRICAO_LEMBRETE, lembrete);
        long id = db.insert(Lembrete.LembreteEntry.TABLE_NAME, null, values);
        db.close();

        return id;
    }

    public int editarLembrete(Lembrete lembrete) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Lembrete.LembreteEntry.COLUNA_DESCRICAO_LEMBRETE, lembrete.getLembrete());

        return db.update(Lembrete.LembreteEntry.TABLE_NAME, values, Lembrete.LembreteEntry._ID + " = ?",
                new String[]{String.valueOf(lembrete.getId())});
    }

    public void deletarLembrete(Lembrete lembrete) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Lembrete.LembreteEntry.TABLE_NAME, Lembrete.LembreteEntry._ID + " = ?",
                new String[]{String.valueOf(lembrete.getId())});
        db.close();
    }

    public Lembrete getLembrete(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Lembrete.LembreteEntry.TABLE_NAME,
                new String[]{Lembrete.LembreteEntry._ID, Lembrete.LembreteEntry.COLUNA_DESCRICAO_LEMBRETE,
                        Lembrete.LembreteEntry.COLUNA_DATA_HORA},
                Lembrete.LembreteEntry._ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Lembrete lembrete = new Lembrete();
        lembrete.setId(cursor.getInt(cursor.getColumnIndex(Lembrete.LembreteEntry._ID)));
        lembrete.setLembrete(cursor.getString(cursor.getColumnIndex(Lembrete.LembreteEntry.COLUNA_DESCRICAO_LEMBRETE)));
        lembrete.setDataHora(Util.strToDateTime(cursor.getString(cursor.getColumnIndex(Lembrete.LembreteEntry.COLUNA_DATA_HORA))));

        cursor.close();

        return lembrete;
    }

    public List<Lembrete> getAllLembretes() {
        List<Lembrete> lembretes = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + Lembrete.LembreteEntry.TABLE_NAME + " ORDER BY " +
                Lembrete.LembreteEntry.COLUNA_DATA_HORA + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Lembrete lembrete = new Lembrete();
                lembrete.setId(cursor.getInt(cursor.getColumnIndex(Lembrete.LembreteEntry._ID)));
                lembrete.setLembrete(cursor.getString(cursor.getColumnIndex(Lembrete.LembreteEntry.COLUNA_DESCRICAO_LEMBRETE)));
                lembrete.setDataHora(Util.strToDateTime(cursor.getString(cursor.getColumnIndex(Lembrete.LembreteEntry.COLUNA_DATA_HORA))));
                lembretes.add(lembrete);
            } while (cursor.moveToNext());
        }

        db.close();

        return lembretes;
    }
}
