package com.economigos.economigosfiles.utils;

public class PilhaObj<T> {
    private T[] pilha;
    private Integer topo;

    public PilhaObj(Integer size) {
        this.pilha = (T[]) new Object[size];
        this.topo = -1;
    }

    public boolean isEmpty(){
        return topo.equals(-1);
    }

    public boolean isFull(){
        return topo.equals(pilha.length -1);
    }

    public void push (T i){
        if (isFull()){
            System.out.println("A pilha está cheia");
        } else {
            pilha[++topo] = i;
        }
    }

    public T pop () {
        if (isEmpty()){
            return null;
        } else {
            return pilha[topo--];
        }
    }

    public T peek(){
        if (isEmpty()){
            return null;
        } else {
            return pilha[topo];
        }
    }

    public void exibe(){
        if (isEmpty()){
            System.out.println("A lista está vazia");
        } else {
            for (int i = 0; i <= topo; i++){
                System.out.println(pilha[i]);
            }
        }
    }

    public PilhaObj<T> multiPop(int n){
        if (n >= pilha.length){
            return null;
        } else{
            PilhaObj<T> aux = new PilhaObj<T>(n);
            for (int i = 0; i < n; i++){
                aux.push(pop());
            }
            return aux;
        }
    }

    public void multiPush(PilhaObj<T> aux){
        while (!aux.isEmpty()){
            push(aux.pop());
        }
    }
}