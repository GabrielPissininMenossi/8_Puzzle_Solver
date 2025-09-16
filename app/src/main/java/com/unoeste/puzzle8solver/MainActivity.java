package com.unoeste.puzzle8solver;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static List<Estado> estadoList = new ArrayList<>();
    static List<Integer> estadoFinal = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8)); // estado final padrão
    Puzzle8View puzzle8View;
    private TextView tvEstadoFinal;
    private Button btEmbaralhar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.FrameLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        puzzle8View = findViewById(R.id.puzzle8View);
        tvEstadoFinal = findViewById(R.id.tvEstadoFinal);
        btEmbaralhar = findViewById(R.id.btEmbaralhar);

        tvEstadoFinal.setText(estadoFinal.toString());

        btEmbaralhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                puzzle8View.inicializarEstados();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_opcao,menu);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()== R.id.it_final){

            estadoFinal();
        }

        return super.onOptionsItemSelected(item);
    }

    private void estadoFinal()
    {
        EditText input = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("Input Final State")
                .setView(input)
                .setPositiveButton("Ok", (dialog, which) -> {
                    int i = 0, flag = 0;
                    estadoFinal.clear();
                    while(i < input.getText().toString().length() && flag != 1)
                    {
                        if (!verificarEntrada(Integer.parseInt(String.valueOf(input.getText().toString().charAt(i)))))
                            flag = 1;
                        else
                            estadoFinal.add(Integer.parseInt(String.valueOf(input.getText().toString().charAt(i))));
                        i++;
                    }
                    if (flag == 1)
                    {
                        estadoFinal = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8));
                        Toast.makeText(this, "Input Not Allowed", Toast.LENGTH_SHORT).show();
                    }
                    else
                    if (!estadoFinal.contains(1) || !estadoFinal.contains(2) || !estadoFinal.contains(3) || !estadoFinal.contains(4) || !estadoFinal.contains(5) || !estadoFinal.contains(6) || !estadoFinal.contains(7) || !estadoFinal.contains(8))
                    {
                        estadoFinal = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8));
                        Toast.makeText(this, "Input Not Allowed", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        tvEstadoFinal.setText(estadoFinal.toString());
                    }
                    //Toast.makeText(this, estadoFinal.toString(), Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    private boolean verificarEntrada(int num)
    {
        return (num >= 1 && num <= 8) && (!estadoFinal.contains(num));
    }

}