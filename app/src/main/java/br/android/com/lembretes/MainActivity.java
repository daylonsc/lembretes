package br.android.com.lembretes;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.android.com.lembretes.adapter.LembreteAdapter;
import br.android.com.lembretes.data.LembreteDbHelper;
import br.android.com.lembretes.data.model.Lembrete;
import br.android.com.lembretes.uteis.RecyclerViewOnClickListenerHack;

public class MainActivity extends AppCompatActivity implements RecyclerViewOnClickListenerHack {

    private RecyclerView mRecyclerView;
    private LembreteAdapter mLembreteAdapter;
    private LembreteDbHelper mDb;
    private List<Lembrete> lembreteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDb = new LembreteDbHelper(this);

        lembreteList.addAll(mDb.getAllLembretes());

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_lembretes);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setHasFixedSize(true);

        mLembreteAdapter = new LembreteAdapter( lembreteList);
        mLembreteAdapter.setRecyclerViewOnClickListenerHack(this);
        mRecyclerView.setAdapter(mLembreteAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLembreteDialog(false, null, -1);
            }
        });
    }

    private void inserirLembrete(String note) {
        long id = mDb.inserirLembrete(note);

        Lembrete lembrete = mDb.getLembrete(id);

        if (lembrete != null) {
            lembreteList.add(0, lembrete);
            mLembreteAdapter.notifyDataSetChanged();
        }
    }

    private void atualizarLembrete(String lembrete, int position) {
        Lembrete l = lembreteList.get(position);
        l.setLembrete(lembrete);
        mDb.editarLembrete(l);
        mLembreteAdapter.notifyDataSetChanged();

    }

    private void deletarLembrete(Lembrete lembrete, int position) {
        lembreteList.remove(position);
        mDb.deletarLembrete(lembrete);
        mLembreteAdapter.notifyDataSetChanged();
    }

    private void showLembreteDialog(final boolean shouldUpdate, final Lembrete lembrete, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.lembrete_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputNote = view.findViewById(R.id.note);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.label_novo_lembrete) : getString(R.string.label_editar_lembrete));

        if (shouldUpdate && lembrete != null) {
            inputNote.setText(lembrete.getLembrete());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "Editar" : "Salvar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(inputNote.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Digite um lembrete!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                if (shouldUpdate && lembrete != null) {
                    atualizarLembrete(inputNote.getText().toString(), position);
                } else {
                    inserirLembrete(inputNote.getText().toString());
                }
            }
        });
    }

    private void showActionsDialog(final Lembrete lembrete, final int position) {
        CharSequence colors[] = new CharSequence[]{"Editar", "Deletar"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Escolha uma opção");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int acao) {
                if (acao == 0) {
                    showLembreteDialog(true, lembrete, position);
                } else {
                    deletarLembrete(lembrete, position);
                }
            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickListener(View view, int position) {
        showActionsDialog(lembreteList.get(position), position);
    }

    @Override
    public void onLongPressClickListener(View view, int position) {
    }
}
