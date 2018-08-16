package br.com.claitonneri.todoapp.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

//
// Created by Claiton Neri on 16/08/18.
//

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "todo.db";
    private static final int DB_VERSION = 1;
    public static final String DB_TABLE = "tb_tarefa";
    public static final String DB_COLUNA = "nome";

    // Construtor
    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("CREATE TABLE %s (id INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL);", DB_TABLE, DB_COLUNA);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql = String.format("DROP TABLE IF EXISTS %s", DB_TABLE);
        db.execSQL(sql);
        onCreate(db);
    }

    public void inserirTarefa(String tarefa) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DB_COLUNA, tarefa);
        db.insertWithOnConflict(DB_TABLE, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void deletarTarefa(String tarefa) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(DB_TABLE, DB_COLUNA + " = ?", new String[]{ tarefa });
        db.close();
    }

    public ArrayList<String> getListarTarefas() {
        ArrayList<String> listaTarefas = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(DB_TABLE, new String[]{ DB_COLUNA }, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int id = cursor.getColumnIndex(DB_COLUNA);
            listaTarefas.add(cursor.getString(id));
        }
        cursor.close();
        db.close();

        return listaTarefas;
    }
}
