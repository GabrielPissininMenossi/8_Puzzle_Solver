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
    private float lado;

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



        int i = 0;
        Estado estado;
        while (i < MainActivity.estadoList.size())
        {
            estado = MainActivity.estadoList.get(i);
            if (estado.getNum() != 9)
            {
                canvas.drawRect(estado.getX(), estado.getY(), estado.getX() + lado, estado.getY() + lado, paint);
                canvas.drawRect(estado.getX(), estado.getY(), estado.getX() + lado, estado.getY() + lado, paintBorda);
                canvas.drawText("" + estado.getNum(), estado.getX() + lado/2, estado.getY()+ lado/2 + 50, textPaint);
            }
            i++;

        }
    }
    public void inicializarEstados()
    {
        MainActivity.estadoList.clear();
        int num;
        Estado estado;
        float largura, altura, grade, x, y;
        grade = Math.min(getWidth(), getHeight()) - 200; // tamanho da grade
        lado = grade / 3f;
        largura = (getWidth() - grade) /2f;
        altura = (getHeight() - grade) /2f;
        List<Integer> auxList = new ArrayList<>();
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                num = (int)(Math.random() * 9) + 1;
                while (auxList.contains(num))
                    num = (int)(Math.random() * 9) + 1;
                auxList.add(num);
                x = largura + (j * lado); // coluna
                y = altura + (i * lado); // linha
                estado = new Estado(x,y, num);
                MainActivity.estadoList.add(estado);
            }
        }
        invalidate();
    }
}
