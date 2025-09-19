package com.unoeste.puzzle8solver;

import java.util.ArrayList;
import java.util.List;

public class Tabuleiro
{
    private List <Integer> estado = new ArrayList<>();
    private int h;
    private int g;
    private int f;
    private Tabuleiro pai;

    public Tabuleiro(List<Integer> estado) {
        this.estado = estado;
    }

    public List<Integer> getEstado() {
        return estado;
    }

    public void setEstado(List<Integer> estado) {
        this.estado = estado;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public Tabuleiro getPai() {
        return pai;
    }

    public void setPai(Tabuleiro pai) {
        this.pai = pai;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

}
