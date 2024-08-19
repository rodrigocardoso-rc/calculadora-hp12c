package com.example.calculadora;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.BiFunction;

enum FinanceOperation {
    pv,
    fv,
    pmt,
    n,
    i
}

public class Calculator {
    public static final int MODO_EDITANDO = 0;
    public static final int MODO_EXIBINDO = 1;
    public static final int MODO_ERRO = 2;
    private double numero;
    private Deque<Double> operandos;
    private int modo = MODO_EXIBINDO;
    private Double n;
    private Double i;
    private Double pv;
    private Double pmt;
    private Double fv;


    public Calculator() {
        numero = 0;
        operandos = new LinkedList<>();
    }

    public void setNumero(double numero) {
        this.numero = numero;
        modo = MODO_EDITANDO;
    }

    public void reset() {
        numero = 0;
        operandos = new LinkedList<>();

        n = null;
        i = null;
        pv = null;
        fv = null;
        pmt = null;
    }

    public double getNumero() {
        return numero;
    }

    public int getModo() {
        return modo;
    }

    public void enter() {
        if (modo == MODO_ERRO) {
            modo = MODO_EXIBINDO;
        }
        if (modo == MODO_EDITANDO) {
            operandos.push(numero);
            modo = MODO_EXIBINDO;
        }
    }

    protected void executarOperacao(BiFunction<Double, Double, Double> operacao) {
        if (modo == MODO_EDITANDO || modo == MODO_ERRO) {
            enter();
        }
        double op1 = Optional.ofNullable(operandos.pollFirst()).orElse(0.0);
        double op2 = Optional.ofNullable(operandos.pollFirst()).orElse(0.0);
        numero = operacao.apply(op1, op2);
        operandos.push(numero);
    }

    public void soma() {
        executarOperacao((op1, op2) -> op1 + op2);
    }

    public void subtracao() {
        executarOperacao((op1, op2) -> op1 - op2);
    }

    public void multiplicacao() {
        executarOperacao((op1, op2) -> op1 * op2);
    }

    public void divisao() {
        if (modo == MODO_EDITANDO) {
            enter();
        }
        double denominador = Optional.ofNullable(operandos.peek()).orElse(0.0);
        if (denominador == 0) {
            modo = MODO_ERRO;
            return;
        }
        executarOperacao((op1, op2) -> op2 / op1);
    }

    public void pressN() {
        calculateItem(() -> n = numero, FinanceOperation.n);
    }

    public void pressI() {
        calculateItem(() -> i = numero/100, FinanceOperation.i);
    }

    public void pressPv() {
        calculateItem(() -> pv = numero, FinanceOperation.pv);
    }

    public void pressPmt() {
        calculateItem(() -> pmt = numero, FinanceOperation.pmt);
    }

    public void pressFv() {
        calculateItem(() -> fv = numero, FinanceOperation.fv);
    }

    public void calculateItem(Runnable setValue, FinanceOperation operation) {
        if (operation == FinanceOperation.pv &&pv == null && fv != null && i != null) {
            numero = calculatePv();
        } else if (operation == FinanceOperation.fv && fv == null && pv != null && i != null && n != null) {
            numero = calculateFv();
        } else if (operation == FinanceOperation.i && i == null && pv != null && fv != null && n != null) {
            numero = calculateI();
        } else if (operation == FinanceOperation.n && n == null && pv != null && fv != null && i != null) {
            numero = calculateN();
        } else if (operation == FinanceOperation.pmt && pmt == null && pv != null && i != null && n != null) {
            numero = calculatePmt();
        } else {
            setValue.run();
        }

        enter();
    }

    private double calculatePv() {
        double denominator = 1 + i;
        return fv / Math.pow(denominator, n);
    }

    private double calculateFv() {
        double tax = Math.pow(1 + i, n);
        return pv * tax;
    }

    private double calculateI() {
        double value = Math.pow(fv / pv, 1 / n);
        return value - 1;
    }

    private double calculateN() {
        double numerator = Math.log(fv/pv);
        double denominator = Math.log(1 + i);

        return numerator/denominator;
    }

    private double calculatePmt() {
        double tax = 1 + i;

        double numerator = Math.pow(tax, n) * i;
        double denominator = Math.pow(tax, n) - 1;

        return pv * (numerator/denominator);
    }
}
