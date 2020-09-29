package no.oslomet.cs.algdat;


////////////////// class DobbeltLenketListe //////////////////////////////


import java.sql.SQLOutput;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;



public class DobbeltLenketListe<T> implements Liste<T> {

    /**
     * Node class
     * @param <T>
     */
    private static final class Node<T> {
        private T verdi;                   // nodens verdi
        private Node<T> forrige, neste;    // pekere

        private Node(T verdi, Node<T> forrige, Node<T> neste) {
            this.verdi = verdi;
            this.forrige = forrige;
            this.neste = neste;
        }

        private Node(T verdi) {
            this(verdi, null, null);
        }
    }

    // instansvariabler
    private Node<T> hode;          // peker til den første i listen
    private Node<T> hale;          // peker til den siste i listen
    private int antall;            // antall noder i listen
    private int endringer;         // antall endringer i listen

    public DobbeltLenketListe() {
        this.antall = 0;
        this.endringer = 0;
        hode = hale = null;
        /*throw new UnsupportedOperationException();*/
    }

    private T[] arr;

    @SuppressWarnings("unchecked")
    public DobbeltLenketListe(T[] a) {
        if(a == null) {
            throw new NullPointerException("Tabellen er null");
        }
        arr = (T[]) new Object[a.length];
        for(T verdi : a) {
            if(verdi != null) {
                arr[antall] = verdi;
                antall++;
            }
        }
        int lengde = arr.length;
        if(lengde == 0) {
            hode = null;
            hale = null;
        }

        for(int i = 0; i < lengde; i++) {
            if(a[i] == null) {
                continue;
            }
            Node<T> ny = new Node(a[i]);
            if(hode == null) {
               hode = ny;
               hale = ny;
               hode.forrige = null;
               hale.neste = null;
            } else {
               hale.neste = ny;
               ny.forrige = hale;
               hale = ny;
               hale.neste = null;
            }
        }
        /*throw new UnsupportedOperationException();*/
    }

    public Liste<T> subliste(int fra, int til){
        throw new UnsupportedOperationException();
    }

    @Override
    public int antall() {
        return antall;
        /*throw new UnsupportedOperationException();*/
    }

    @Override
    public boolean tom() {
        return antall == 0;
        /*throw new UnsupportedOperationException();*/
    }

    @Override
    public boolean leggInn(T verdi) {
        if(verdi == null) {
           throw new NullPointerException("Null verdier ikke tillat!");
        } else {
            Node<T> ny = new Node(verdi);
            antall++;
            if(hode == null) {
                hode = ny;
                hale = ny;
                hode.forrige = null;
                hale.neste = null;
            } else {
                hale.neste = ny;
                ny.forrige = hale;
                hale = ny;
                hale.neste = null;
            }
            return true;
        }

        /*throw new UnsupportedOperationException();*/
    }

    @Override
    public void leggInn(int indeks, T verdi) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean inneholder(T verdi) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T hent(int indeks) {
        T verdi;
        if(antall == 0) {
            throw new IndexOutOfBoundsException("Tom liste");
        }
        try {
            indeksKontroll(indeks, false);
            verdi = finnNode(indeks).verdi;
        } catch (IndexOutOfBoundsException e) {
            throw e;
        }
        return verdi;
    }

    private Node<T> finnNode(int indeks) {
        Node<T> current = null;
        if(antall == 1 && indeks == 0) {
            current = hode;
            return current;
        }
        else if(indeks == antall-1) {
             return hale;
        }
        else if(indeks <= (antall/2)) {
            current = hode;
            for(int i = 0; i < indeks; i++)  {
                current = current.neste;
            }
        }
        else if(indeks > (antall/2)) {
            current = hale;
            for(int i = antall-1; i > indeks; i--)  {
                current = current.forrige;
            }
        }

        return current;
    }

    @Override
    public int indeksTil(T verdi) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T oppdater(int indeks, T nyverdi) {
        T returVerdi;
        if(nyverdi == null) {
            throw new NullPointerException("Ikke tillate med nullverdier i oppdater metoden!");
        }
        try {
            indeksKontroll(indeks, false);
            Node<T> r = finnNode(indeks);
            returVerdi = r.verdi;
            r.verdi = nyverdi;
            endringer++;
        } catch (IndexOutOfBoundsException e) {
            throw e;
        }

        return returVerdi;
        /*throw new UnsupportedOperationException();*/
    }

    @Override
    public boolean fjern(T verdi) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T fjern(int indeks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void nullstill() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("[");
        Node current = hode;
        if(current == null) {
            str.append("]");
        }
        else if(hode == hale) {
            str.append(current.verdi).append("]");
        }
        else {
            str.append(current.verdi).append(",");
            while(current.neste != hale) {
                current = current.neste;
                str.append(" ").append(current.verdi).append(",");
            }
            str.append(" ").append(hale.verdi).append("]");
        }

        return str.toString();
        /*throw new UnsupportedOperationException();*/
    }

    public String omvendtString() {
        StringBuilder str = new StringBuilder("[");
        Node current = hale;
        if(current == null) {
            str.append("]");
        }
        else if(hode == hale) {
            str.append(current.verdi).append("]");
        }
        else {
            str.append(current.verdi).append(",");
            while(current.forrige != hode) {
                current = current.forrige;
                str.append(" ").append(current.verdi).append(",");
            }
            str.append(" ").append(hode.verdi).append("]");
        }

        return str.toString();
        /*throw new UnsupportedOperationException();*/
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    public Iterator<T> iterator(int indeks) {
        throw new UnsupportedOperationException();
    }

    private class DobbeltLenketListeIterator implements Iterator<T>
    {
        private Node<T> denne;
        private boolean fjernOK;
        private int iteratorendringer;

        private DobbeltLenketListeIterator(){
            denne = hode;     // p starter på den første i listen
            fjernOK = false;  // blir sann når next() kalles
            iteratorendringer = endringer;  // teller endringer
        }

        private DobbeltLenketListeIterator(int indeks){
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext(){
            return denne != null;
        }

        @Override
        public T next(){
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove(){
            throw new UnsupportedOperationException();
        }

    } // class DobbeltLenketListeIterator

    public static <T> void sorter(Liste<T> liste, Comparator<? super T> c) {
        throw new UnsupportedOperationException();
    }

} // class DobbeltLenketListe


