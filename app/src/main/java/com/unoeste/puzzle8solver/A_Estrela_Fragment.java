package com.unoeste.puzzle8solver;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link A_Estrela_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class A_Estrela_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    static List<Estado> estadoList = new ArrayList<>();
    static List<Integer> estadoAtual = new ArrayList<>();
    static List<Integer> estadoFinal = new ArrayList<>(Arrays.asList(0,1,2,3,4,5,6,7,8)); // estado final padrão
    private LinkedList<Tabuleiro> fila = new LinkedList();
    private LinkedList<Tabuleiro> caminho = new LinkedList<>();
    private Tabuleiro ultimoEstado;
    private Puzzle8View puzzle8View;
    private TextView tvEstadoFinal;
    private Button btEmbaralhar, btBusca_Estrela;
    private Long startTime, endTime;
    private int qtdePassos = 0, tamanhoCaminho = 0;
    private TextView tvTempoGasto, tvCaminhoSolucao, tvQtdePassos;
    private final int TEMPO = 500;
    MainActivity mainActivity;

    public A_Estrela_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment A_Estrela_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static A_Estrela_Fragment newInstance(String param1, String param2) {
        A_Estrela_Fragment fragment = new A_Estrela_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_opcao, menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.it_final)
        {
            estadoFinal();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // diz ao sistema que esse fragmento tem menu
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public void onAttach(@NonNull Context context) { // nos permite alternar de um fragmento para a main activity
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_a__estrela_, container, false);
        puzzle8View = view.findViewById(R.id.puzzle8View);
        tvEstadoFinal = view.findViewById(R.id.tvEstadoFinal);
        btEmbaralhar = view.findViewById(R.id.btEmbaralhar);
        btBusca_Estrela = view.findViewById(R.id.btBusca_Estrela);
        tvCaminhoSolucao = view.findViewById(R.id.tvCaminhoSolucao);
        tvTempoGasto = view.findViewById(R.id.tvTempoGasto);
        tvQtdePassos = view.findViewById(R.id.tvQtdePassos);
        tvEstadoFinal.setText(estadoFinal.toString());
        btEmbaralhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                puzzle8View.inicializarEstados();
            }
        });
        btBusca_Estrela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fila.clear();
                estadoAtual.clear();
                caminho.clear();
                ultimoEstado = null;
                qtdePassos = 0;
                tamanhoCaminho = 0;
                inicializarEstadoInicial();
                if (isSolucionavel(estadoAtual))
                {
                    btBusca_Estrela.setVisibility(View.INVISIBLE);
                    btEmbaralhar.setVisibility(View.INVISIBLE);
                    Toast.makeText(v.getContext(), "Searching Solution...", Toast.LENGTH_SHORT).show();
                    new Thread(() -> {

                        buscaA_Estrela();
                        mainActivity.runOnUiThread(() -> {
                            if (ultimoEstado != null) // solucao encontrada
                            {
                                Toast.makeText(v.getContext(), "Solution Exists", Toast.LENGTH_SHORT).show();

                                caminhoSolucao();
                                tvTempoGasto.setText(String.format("%.3f",(endTime - startTime)/1000f)+" sec");
                                tvQtdePassos.setText(""+qtdePassos);
                                tvCaminhoSolucao.setText(""+tamanhoCaminho);
                                animarSolucao();
                            }
                            else
                            {
                                Toast.makeText(v.getContext(), "No Solution", Toast.LENGTH_SHORT).show();
                                btBusca_Estrela.setVisibility(View.VISIBLE);
                            }

                        });
                    }).start();

                }
                else
                {
                    Toast.makeText(v.getContext(), "No Solution", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private void estadoFinal()
    {
        EditText input = new EditText(getContext());
        new AlertDialog.Builder(getContext())
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
                        estadoFinal = new ArrayList<>(Arrays.asList(0,1,2,3,4,5,6,7,8));
                        Toast.makeText(getContext(), "Input Not Allowed", Toast.LENGTH_SHORT).show();
                    }
                    else
                    if (!estadoFinal.contains(0) || !estadoFinal.contains(1) || !estadoFinal.contains(2) || !estadoFinal.contains(3) || !estadoFinal.contains(4) || !estadoFinal.contains(5) || !estadoFinal.contains(6) || !estadoFinal.contains(7) || !estadoFinal.contains(8))
                    {
                        estadoFinal = new ArrayList<>(Arrays.asList(0,1,2,3,4,5,6,7,8));
                        Toast.makeText(getContext(), "Input Not Allowed", Toast.LENGTH_SHORT).show();
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
        return (num >= 0 && num <= 8) && (!estadoFinal.contains(num));
    }
    private int contarPecasFora(List<Integer> estadoAtual)
    {
        int cont = 0;
        for (int i = 0; i < estadoFinal.size(); i++)
        {
            if (estadoFinal.get(i) != estadoAtual.get(i))
                cont++;

        }
        return cont;
    }
    private void inicializarEstadoInicial()
    {
        estadoAtual.clear();
        int i = 0;
        while (i < estadoList.size())
        {
            estadoAtual.add(estadoList.get(i).getNum());
            i++;
        }
    }
    private void ordenarFila()
    {
        Tabuleiro estadoAux;
        List<Integer> aux;
        for (int i = 0; i < fila.size(); i++)
        {
            for (int j = i + 1; j < fila.size(); j++)
            {
                if (fila.get(i).getF() > fila.get(j).getF())
                {
                    estadoAux = fila.get(i);
                    fila.set(i, fila.get(j));
                    fila.set(j, estadoAux);

                }

            }
        }

    }
    private void insercaoDiretaPrioridade(Tabuleiro novo)
    {
        fila.add(novo);
        int i = fila.size() - 1;
        Tabuleiro aux;
        while (i > 0 && fila.get(i).getF() < fila.get(i - 1).getF())
        {
            aux = fila.get(i);
            fila.set(i, fila.get(i-1));
            fila.set(i - 1, aux);
            i--;

        }
    }
    private int buscarIndice0(List<Integer> lista)
    {
        int i =0;
        while (i < lista.size() && lista.get(i) != 0)
            i++;
        if (i < lista.size()) // achou
            return i;
        else
            return -1;
    }
    private boolean foiVisitado(List<Tabuleiro> visitados, Tabuleiro novoTabuleiro)
    {
        int i = 0;
        while (i < visitados.size())
        {
            if (visitados.get(i).getEstado().equals(novoTabuleiro.getEstado()))
                return true;
            i++;
        }
        return false;
    }
    private void caminhoSolucao()
    {
        Tabuleiro aux;
        aux = ultimoEstado;
        while (aux != null)
        {
            caminho.addFirst(aux);
            tamanhoCaminho++;
            aux = aux.getPai();
        }

    }
    private void animarSolucao()
    {
        int i =0;
        android.os.Handler handler = new android.os.Handler(mainActivity.getMainLooper());
        while (i < caminho.size())
        {
            final int passo = i;
            final Tabuleiro aux = caminho.get(i);
            handler.postDelayed(() -> {

                Tabuleiro tabuleiro = aux;
                atualizarEstados(tabuleiro.getEstado());
                if (passo == caminho.size() - 1)
                {
                    btBusca_Estrela.setVisibility(View.VISIBLE);
                    btEmbaralhar.setVisibility(View.VISIBLE);
                }
            }, (long) i * TEMPO);
            i++;
        }
    }
    private void expandirLista(List<Integer> novaLista, Tabuleiro pai, List<Tabuleiro> visitado)
    {
        Tabuleiro novoEstadoTabuleiro = new Tabuleiro(novaLista);
        if (!foiVisitado(visitado, novoEstadoTabuleiro))
        {
            novoEstadoTabuleiro.setPai(pai);
            novoEstadoTabuleiro.setG(pai.getG() + 1);
            novoEstadoTabuleiro.setH(calcularDistanciaManhattan(novoEstadoTabuleiro.getEstado()));
            novoEstadoTabuleiro.setF(novoEstadoTabuleiro.getG() + novoEstadoTabuleiro.getH());
            insercaoDiretaPrioridade(novoEstadoTabuleiro);
            qtdePassos++;
        }

    }

    private boolean isSolucionavel(List<Integer> estado)
    {
        int inversoes = 0;

        List<Integer> estadoSemVazio = new ArrayList<>();
        for (Integer num : estado) {
            if (num != 0) {
                estadoSemVazio.add(num);
            }
        }

        for (int i = 0; i < estadoSemVazio.size() - 1; i++) {
            for (int j = i + 1; j < estadoSemVazio.size(); j++) {
                if (estadoSemVazio.get(i) > estadoSemVazio.get(j)) {
                    inversoes++;
                }
            }
        }

        return (inversoes % 2 == 0);
    }
    private int buscaPonto(int numero)
    {
        int i = 0;
        while (i < estadoFinal.size() && numero != estadoFinal.get(i))
            i++;
        return i;
    }
    private int calcularDistanciaManhattan(List<Integer> lista)
    {
        int i = 0;
        int total =0;
        int dist, linhaAtual, colunaAtual, linhaFinal, colunaFinal;
        while (i < lista.size())
        {
            if (lista.get(i) != 0)
            {
                linhaAtual = i / 3;
                colunaAtual = i % 3;
                dist = buscaPonto(lista.get(i));
                linhaFinal = dist / 3;
                colunaFinal = dist % 3;

                total = total + Math.abs(linhaAtual - linhaFinal) + Math.abs(colunaAtual - colunaFinal);
            }

            i++;
        }
        return total;
    }
    private void buscaA_Estrela()
    {
        startTime = System.currentTimeMillis();
        List<Tabuleiro> visitados = new ArrayList<>();
        int flagD = 0, flagE = 0, flagB = 0, flagC = 0, flagFim = 0;
        Tabuleiro estadoTabuleiro = new Tabuleiro(estadoAtual);
        estadoTabuleiro.setH(calcularDistanciaManhattan(estadoTabuleiro.getEstado()));
        estadoTabuleiro.setG(0);
        estadoTabuleiro.setF(estadoTabuleiro.getG() + estadoTabuleiro.getH());
        estadoTabuleiro.setPai(null);
        insercaoDiretaPrioridade(estadoTabuleiro);
        qtdePassos++;
        while (!fila.isEmpty() && flagFim != 1)
        {
            //Toast.makeText(this, "entrei", Toast.LENGTH_SHORT).show();
            estadoTabuleiro = fila.removeFirst();
            if (!foiVisitado(visitados, estadoTabuleiro))
            {
                visitados.add(estadoTabuleiro);
                //Toast.makeText(this, "entrei", Toast.LENGTH_SHORT).show();
                if (!estadoTabuleiro.getEstado().equals(estadoFinal))
                {
                    //Toast.makeText(this, "entrei", Toast.LENGTH_SHORT).show();
                    int i = 0,flag, pos;
                    flagD = 0; flagE = 0; flagB = 0; flagC = 0; flag = 0;
                    pos = buscarIndice0(estadoTabuleiro.getEstado());
                    if (pos != -1)
                    {
                        int auxNum;

                        if (pos >= 3) // existe posica para cima
                        {
                            List<Integer> aux = new ArrayList<>(estadoTabuleiro.getEstado());
                            auxNum = aux.get(pos - 3);
                            aux.set(pos - 3, 0);
                            aux.set(pos, auxNum);
                            expandirLista(aux, estadoTabuleiro, visitados);
                        }
                        if (pos <= 5) // existe posicao para baixo
                        {
                            List<Integer> aux = new ArrayList<>(estadoTabuleiro.getEstado());
                            auxNum = aux.get(pos + 3);
                            aux.set(pos + 3, 0);
                            aux.set(pos, auxNum);
                            expandirLista(aux, estadoTabuleiro, visitados);
                        }
                        if ((pos + 1) % 3 != 0 )
                        {
                            List<Integer> aux = new ArrayList<>(estadoTabuleiro.getEstado());
                            auxNum = aux.get(pos + 1);
                            aux.set(pos + 1, 0);
                            aux.set(pos, auxNum);
                            expandirLista(aux, estadoTabuleiro, visitados);
                        }
                        if(pos % 3 != 0 )
                        {
                            List<Integer> aux = new ArrayList<>(estadoTabuleiro.getEstado());
                            auxNum = aux.get(pos - 1);
                            aux.set(pos - 1, 0);
                            aux.set(pos, auxNum);
                            expandirLista(aux, estadoTabuleiro, visitados);
                        }

                    }
                }
                else
                {
                    flagFim = 1;
                    ultimoEstado = estadoTabuleiro;
                }
            }

        }
        endTime = System.currentTimeMillis();

    }
    private void atualizarEstados(List<Integer> lista)
    {
        int i = 0;
        while (i < lista.size())
        {
            estadoAtual.set(i, lista.get(i));
            i++;
        }
        puzzle8View.trocarEstados();
    }
}