package no.oslomet.cs.algdat;


////////////////// class DobbeltLenketListe //////////////////////////////


import java.util.Comparator;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;


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

    @SuppressWarnings("unchecked")
    public DobbeltLenketListe(T[] a) {
        if(a == null) {
            throw new NullPointerException("Tabellen er null");
        }
        T[] arr = (T[]) new Object[a.length];
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
            } else {
               hale.neste = ny;
               ny.forrige = hale;
               hale = ny;
            }
            hale.neste = null;
        }

    }

    public Liste<T> subliste(int fra, int til){
        Liste<T> liste = new DobbeltLenketListe<>();
        fratilKontroll(antall,fra,til);
        int peker = 0;
        Node<T> current = hode;
        for (int i = 0; i < antall; i++) {
            if(peker == fra) {
                while(peker != til ) {
                    liste.leggInn(current.verdi);
                    current = current.neste;
                    peker++;
                }
                break;
            }
            peker++;
            current = current.neste;
        }
        return liste;

    }

    private static void fratilKontroll(int antall, int fra, int til)
    {
        if (fra < 0)
            throw new IndexOutOfBoundsException
                    ("fra(" + fra + ") er negativ!");

        if (til > antall)
            throw new IndexOutOfBoundsException
                    ("til(" + til + ") > antall(" + antall + ")");

        if (fra > til)
            throw new IllegalArgumentException
                    ("fra(" + fra + ") > til(" + til + ") - illegalt intervall!");
    }



    @Override
    public int antall() {
        return antall;
    }

    @Override
    public boolean tom() {
        return antall == 0;
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
            } else {
                hale.neste = ny;
                ny.forrige = hale;
                hale = ny;
            }
            hale.neste = null;
            endringer++;
            return true;
        }
    }

    @Override
    public void leggInn(int indeks, T verdi) {


        if(verdi == null) {
            throw new NullPointerException("Ikke tillatt med nullverdier i metoden!");
        }

        if(indeks < 0 || indeks > antall) {
            throw new IndexOutOfBoundsException(indeks);
        }
        else {
            if(antall == 0 || indeks == antall) {
                leggInn(verdi);
            }
            else {
                Node<T> r;
                r = finnNode(indeks);
                Node<T> q = new Node<>(verdi);
                Node<T> p = r.forrige;

                //opdater node pekere
                if(indeks == 0) {
                    q.forrige = null;
                    q.neste = r;
                    r.forrige = q;
                    hode = q;
                }
                else {
                    q.forrige = p;
                    q.neste = r;
                    p.neste = q;
                    r.forrige = q;
                }
                antall++;
                endringer++;
            }
        }

    }

    @Override
    public boolean inneholder(T verdi) {
        boolean inneHolder = false;
        if (indeksTil(verdi) > -1) {
            inneHolder = true;
        }
        return inneHolder;
    }

    @Override
    public T hent(int indeks) {
        T verdi;
        try {
            indeksKontroll(indeks, false);
            verdi = finnNode(indeks).verdi;
        } catch (IndexOutOfBoundsException e) {
            throw e;
        }
        return verdi;
    }

    public Node<T> finnNode(int indeks) {
        Node<T> current = null;

        if(antall == 0) {
            throw new IndexOutOfBoundsException("Tom liste");
        }
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
        Node<T> peker = hode;

        for(int i = 0; i < antall; i++) {
            if(peker.verdi.equals(verdi)) {
                return i;
            }
            peker = peker.neste;
        }
        return -1;

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

    }



    @Override
    public boolean fjern(T verdi) {
        int v = 0;
        int h = antall;
        Node<T> vPeker = hode;
        Node<T> p,r;

        if(antall == 0) {
            return false;
        }
        if(antall == 1) {
            if(vPeker.verdi.equals(verdi)) {
                hode = null;
                hale = null;
                antall--;
                endringer++;
                return true;
            }
            return false;
        }

        while(v != h) {
            if(vPeker.verdi.equals(verdi)) {
                if(v == 0) {
                    r = vPeker.neste;
                    r.forrige = null;
                    hode = r;
                    antall--;
                    endringer++;
                    return true;
                }
                if(v == antall-1) {
                    p = vPeker.forrige;
                    p.neste = null;
                    hale = p;
                    antall--;
                    endringer++;
                    return true;
                }
                p = vPeker.forrige;
                r = vPeker.neste;
                p.neste = r;
                r.forrige = p;
                antall--;
                endringer++;
                return true;
            }
            v++;
            vPeker = vPeker.neste;
        }
        return false;
    }

    @Override
    public T fjern(int indeks) {

        try {
            indeksKontroll(indeks, false);
            Node<T> p, q, r;
            if(indeks == 0 && antall == 1) {
                hode = null;
                hale = null;
                antall--;
                endringer++;
                return null;
            }
            else if(indeks == 0 && antall > 1) {
                q =  hode;
                r = hode.neste;
                r.forrige = null;
                hode = r;
                antall--;
                endringer++;
            }
            else if(indeks == antall - 1 && antall > 1) {
                q = hale;
                p = hale.forrige;
                p.neste = null;
                hale = p;
                antall--;
                endringer++;
            }
            else {
                q = finnNode(indeks);
                p = q.forrige;
                r = q.neste;
                p.neste = r;
                r.forrige = p;
                antall--;
                endringer++;
            }
            return q.verdi;
        } catch (IndexOutOfBoundsException e) {
            throw e;
        }

    }

    @Override
    public void nullstill() {
        Node<T> current = hode;
        while(current != null) {
            fjern(0);
            current = current.neste;
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("[");
        Node<T> current = hode;
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
    }

    public String omvendtString() {
        StringBuilder str = new StringBuilder("[");
        Node<T> current = hale;
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
    }

    @Override
    public Iterator<T> iterator() {
        Iterator<T> i = new DobbeltLenketListeIterator();
        return i;
    }

    public Iterator<T> iterator(int indeks) {
        try {
            indeksKontroll(indeks, false);
            Iterator<T> i = new DobbeltLenketListeIterator(indeks);
            return i;
        } catch (IndexOutOfBoundsException e) {
            throw e;
        }
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
            denne = finnNode(indeks);
            fjernOK = false;
            iteratorendringer = endringer;
        }

        @Override
        public boolean hasNext(){
            return denne != null;
        }

        @Override
        public T next(){
            T verdi;
            if(iteratorendringer != endringer) {
                throw new ConcurrentModificationException("iteratorendringer er ikke like endringer");
            }
            if(denne != null) {
                verdi = denne.verdi;
                if(hasNext()) {
                    denne = denne.neste;
                } else {
                    throw new NoSuchElementException("Finnes ikke flere element i liste");
                }
            } else {
                throw new NoSuchElementException("Tom liste!");
            }

            return verdi;
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


