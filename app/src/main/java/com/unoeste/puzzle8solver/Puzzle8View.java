package com.unoeste.puzzle8solver;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Puzzle8View extends View
{
    private float lado, grade, largura, altura;

    public Puzzle8View(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public Puzzle8View(Context context)
    {
        super(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        inicializarEstados();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#2f2c79"));
        paint.setStyle(Paint.Style.FILL);

        Paint paintBorda = new Paint();
        paintBorda.setColor(Color.WHITE);
        paintBorda.setStyle(Paint.Style.STROKE);
        paintBorda.setStrokeWidth(5);

        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(150);
        textPaint.setTextAlign(Paint.Align.CENTER);



        int i = 0, linha, coluna;
        float x, y;
        Estado estado;
        while (i < A_Estrela_Fragment.estadoList.size())
        {
            linha = i / 3;
            coluna = i % 3;
            x = largura + (coluna * lado);
            y = altura + (linha * lado);
            estado = A_Estrela_Fragment.estadoList.get(i);
            if (estado.getNum() != 0)
            {
                canvas.drawRect(x, y, x + lado, y + lado, paint);
                canvas.drawRect(x, y, x + lado, y + lado, paintBorda);
                canvas.drawText("" + estado.getNum(), x + lado/2, y + lado/2 + 50, textPaint);
            }
            i++;

        }
    }
    public void inicializarEstados()
    {
        A_Estrela_Fragment.estadoList.clear();
        int num;
        Estado estado;
        float x, y;
        grade = Math.min(getWidth(), getHeight()) - 200; // tamanho da grade
        lado = grade / 3;
        largura = (getWidth() - grade) /2f;
        altura = (getHeight() - grade) /2f;
        List<Integer> auxList = new ArrayList<>();
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                num = (int)(Math.random() * 9);
                while (auxList.contains(num))
                    num = (int)(Math.random() * 9);
                auxList.add(num);
                x = largura + (j * lado); // coluna
                y = altura + (i * lado); // linha
                estado = new Estado(x,y, num);
                A_Estrela_Fragment.estadoList.add(estado);
            }
        }
        invalidate();
    }
    private Estado buscarEstado(int num)
    {
        int i = 0;
        while (i < A_Estrela_Fragment.estadoList.size() && A_Estrela_Fragment.estadoList.get(i).getNum() != num)
            i++;
        if (i < A_Estrela_Fragment.estadoList.size()) // achou
            return A_Estrela_Fragment.estadoList.get(i);
        else
            return null;
    }
    public void trocarEstados()
    {
        int i = 0, pos;
        while (i < A_Estrela_Fragment.estadoAtual.size())
        {
            if (A_Estrela_Fragment.estadoList.get(i).getNum() != A_Estrela_Fragment.estadoAtual.get(i)) // se for diferente, preciso trocar
            {
                Estado aux = buscarEstado(A_Estrela_Fragment.estadoAtual.get(i));

                if (aux != null)
                {
                    pos = A_Estrela_Fragment.estadoList.indexOf(aux);
                    A_Estrela_Fragment.estadoList.set(pos, A_Estrela_Fragment.estadoList.get(i));
                    A_Estrela_Fragment.estadoList.set(i, aux);
                }
            }
            i++;
        }
        invalidate();
    }

}
