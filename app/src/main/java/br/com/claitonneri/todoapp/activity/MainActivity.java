package br.com.claitonneri.todoapp.activity;

import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.claitonneri.todoapp.R;
import br.com.claitonneri.todoapp.helper.DbHelper;

public class MainActivity extends AppCompatActivity {

    ListView listviewTarefas;

    DbHelper dbHelper;
    ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(this);

        listviewTarefas = findViewById(R.id.list_tarefa);
        
        carregarListaTarefas();
    }

    private void carregarListaTarefas() {

        ArrayList<String> listaTarefas = dbHelper.getListarTarefas();

        if(mAdapter == null){
            mAdapter = new ArrayAdapter<String>(this, R.layout.item_lista, R.id.txt_tarefa, listaTarefas);
            listviewTarefas.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(listaTarefas);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.opcao_add_tarefa:
                final EditText edtTarefa = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Nova tarefa")
                        .setMessage("Adicione uma nova tarefa.")
                        .setView(edtTarefa)
                        .setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String tarefa = String.valueOf(edtTarefa.getText());
                                dbHelper.inserirTarefa(tarefa);
                                carregarListaTarefas();
                            }
                        }).setNegativeButton("Cancelar", null).create();
                dialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deletaTarefa(View view) {
        View parent = (View) view.getParent();
        TextView txtTarefa = findViewById(R.id.txt_tarefa);
        String tarefa = String.valueOf(txtTarefa.getText());
        dbHelper.deletarTarefa(tarefa);
        carregarListaTarefas();
    }
}
