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
        Liste<T> liste = new DobbeltLenketListe<>();
        try {
            fratilKontroll(antall,fra,til);
            //for løkke for å loope fra indeks "fra" til indeks "til"
            //bruk en peker..
            int peker = 0;
            Node<T> current = hode;
            for (int i = 0; i < antall; i++) {
                //fra når peker == fra => begynn å lage nye noder
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
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw e;
        }
        return liste;
        /*throw new UnsupportedOperationException();*/
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
            endringer++;
            return true;
        }

        /*throw new UnsupportedOperationException();*/
    }

    @Override
    public void leggInn(int indeks, T verdi) {

        //handler for null verdi
        if(verdi == null) {
            throw new NullPointerException("Ikke tillatt med nullverdier i metoden!");
        }
        //handler for out of bounds indeks
        if(indeks < 0 || indeks > antall) {
            throw new IndexOutOfBoundsException(indeks);
        }
        else {
            if(antall == 0 || indeks == antall) { //sjekk hvis listen er tom eller hvis nye node skal legges bakerst
                leggInn(verdi);                 //leggInn() metode legger nye noder bakerst eller først hvis listen er tom
            }
            else {
                Node<T> r;                      //blir høyre node
                r = finnNode(indeks);           //bruk finnNode(indeks) til å finne noden som skal erstattes??
                Node<T> q = new Node<>(verdi);  //ny node
                Node<T> p = r.forrige;         //venstre node

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

        /*throw new UnsupportedOperationException();*/
    }

    @Override
    public boolean inneholder(T verdi) {
        //kall indeksTil() metode
        //hvis den returnerer verdi > -1 => return true, hvis ikke return false
        boolean inneHolder = false;
        if (indeksTil(verdi) > -1) {
            inneHolder = true;
        }
        return inneHolder;
        /*throw new UnsupportedOperationException();*/
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
        //en node peker for sammenligning med verdi
        //peker starter fra "hode" og går til neste node på hver iterasjon i en løkke
        Node<T> peker = hode;

        //for løkke for som skal kjøre til verdiet er funnet
        for(int i = 0; i < antall; i++) {
            if(peker.verdi.equals(verdi)) { //sammenlign peker verdi mot verdi fra metodens parameter
                return i;                   //retur "i" fra for løkke hvis verdiet finnes
            }
            peker = peker.neste;            //retur "-1" hvis det ikke finnes
        }
        return -1;
        /*throw new UnsupportedOperationException();*/
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
        int v = 0;          //venstre peker
        int h = antall;   //høyre peker
        Node<T> vPeker = hode;
        Node<T> p,r;

        //[]
        if(antall == 0) {
            return false;
        }
        //[1]
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
                //[1,2] remove 1
                if(v == 0) {
                    r = vPeker.neste;
                    r.forrige = null;
                    hode = r;
                    antall--;
                    endringer++;
                    return true;
                }
                //[1,2] remove 2
                if(v == antall-1) {
                    p = vPeker.forrige;
                    p.neste = null;
                    hale = p;
                    antall--;
                    endringer++;
                    return true;
                }
                //[1,2,3] remove 2
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
        /*throw new UnsupportedOperationException();*/
    }

    @Override
    public T fjern(int indeks) {
        //finn node, bruk finnNode(int indeks)
        try {
            indeksKontroll(indeks, false);
            //3 variabler, q = noden som skal fjernes, p=venstre node, r=høyre node
            Node<T> p, q, r;
            //oppdater peker til venstre og høyre node
            if(indeks == 0 && antall == 1) {
                hode = null;
                hale = null;
                antall--;
                endringer++;
                return null;
            }
            else if(indeks == 0 && antall > 1) { //hvis hode skal fjernes
                q =  hode;
                r = hode.neste;
                r.forrige = null;
                hode = r;
                antall--;
                endringer++;
            }
            else if(indeks == antall - 1 && antall > 1) { //hvis hale skal fjernes
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

        /*throw new UnsupportedOperationException();*/
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
        Iterator<T> i = new DobbeltLenketListeIterator();
        return i;
        /*throw new UnsupportedOperationException();*/
    }

    public Iterator<T> iterator(int indeks) { throw new UnsupportedOperationException();
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
            /*throw new UnsupportedOperationException();*/
        }

        @Override
        public boolean hasNext(){
            return denne != null;
        }

        @Override
        public T next(){
            T verdi;
            // iteratorendringer == endringer
            if(iteratorendringer != endringer) {
                throw new ConcurrentModificationException("iteratorendringer er ikke like endringer");
            }

            //tom liste
            if(denne != null) {
                verdi = denne.verdi;
                //hasNext() != true
                if(hasNext()) {
                    denne = denne.neste;
                } else {
                    throw new NoSuchElementException("Finnes ikke flere element i liste");
                }
            } else {
                throw new NoSuchElementException("Tom liste!");
            }

            return verdi;
           /* throw new UnsupportedOperationException();*/
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


