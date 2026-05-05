package analisis.algoritmos.MochilaEjercicio;

import java.util.ArrayList;
import java.util.List;

public class Mochila {

    private int pesoMaximo;
    private List<ObjetoCantidad> objetos; 

    public Mochila(int pesoMaximo){
        this.pesoMaximo = pesoMaximo;
        this.objetos = new ArrayList<>();
    }

    public void llenarMochilaFraccionaria(List<ObjetoCantidad> objetos){ //O (nlogn)
        ordenarObjetosFraccionaria(objetos);//O (nlogn)
        for(int j = this.pesoMaximo, i = 0; j > 0 && i < objetos.size(); i++){ 

            System.out.println("Peso sobrante: " + j);

            ObjetoCantidad x = objetos.get(i);
            ObjetoCantidad z = new ObjetoCantidad(x.getPeso(), x.getValor(), x.getCantidad());

            double pesoTotal = z.getPeso() * z.getCantidad();

            if (pesoTotal <= j){
                System.out.println("Se agrega el objeto completo");
                j -= pesoTotal; 
                this.objetos.add(z);
            }else{

                double fraccion = (double) j / z.getPeso();
                System.out.println("Se agrega una fracción del objeto: " + fraccion);
                double valorFraccion = z.getValor() * fraccion;
                double cantidadFraccion = z.getCantidad() * fraccion;
                
                z.setCantidad(cantidadFraccion);
                z.setPeso(j);
                z.setValor(valorFraccion);
                j = 0;
                this.objetos.add(z);
            }
        }
    }

    public void llenarMochilaValor(List<ObjetoCantidad> objetos){ //O (nlogn)
        ordenarObjetosValor(objetos);//O (nlogn)
        int j = this.pesoMaximo;
        int i = 0;
        while (j > 0 && i < objetos.size()){
            System.out.println("Peso sobrante: " + j);
            ObjetoCantidad x = objetos.get(i);
            if (x.getPeso() <= j){
                j -= x.getPeso(); 
                this.objetos.add(x);
            }else{
                System.out.println("No se puede agregar el objeto completo, se omite");
                i = objetos.size(); // Salir del bucle si no se puede agregar el objeto completo
            }
            i++;
        } 
    }

    public void llenarMochilaPeso(List<ObjetoCantidad> objetos){ //O (nlogn)
        ordenarObjetosPeso(objetos);//O (nlogn)
        for(int j = this.pesoMaximo, i = 0; j > 0 && i < objetos.size(); i++){ 
            System.out.println("Peso sobrante: " + j);
            ObjetoCantidad x = objetos.get(i);
            if (x.getPeso() <= j){
                j -= x.getPeso(); 
                this.objetos.add(x);
            }else{
                System.out.println("No se puede agregar el objeto completo, se omite");
                i = objetos.size(); // Salir del bucle si no se puede agregar el objeto completo
            }
        }
    }

    public static void ordenarObjetosFraccionaria(List<ObjetoCantidad> objetos){ // O(nlogn)
        objetos.sort((o1, o2) -> {

            double r1 = (double) o1.getValor() / o1.getPeso();
            double r2 = (double) o2.getValor() / o2.getPeso();

            return Double.compare(r2, r1); // descendente
        });
    }

    public static void ordenarObjetosValor(List<ObjetoCantidad> objetos){ // O(n log n)
        objetos.sort((o1, o2) -> 
            Double.compare(o2.getValor(), o1.getValor()) // descendente por valor
        );
    }

    public static void ordenarObjetosPeso(List<ObjetoCantidad> objetos){ // O(n log n)
        objetos.sort((o1, o2) -> 
            Double.compare(o1.getPeso(), o2.getPeso()) // ascendente por peso
        );
    }

    public int getPesoMaximo() {
        return pesoMaximo;
    }

    public void setPesoMaximo(int pesoMaximo) {
        this.pesoMaximo = pesoMaximo;
    }



    public List<ObjetoCantidad> getObjetos() {
        return objetos;
    }



    public void setObjetos(List<ObjetoCantidad> objetos) {
        this.objetos = objetos;
    }

    public static void imprimir(List<ObjetoCantidad> objetos){
        for(int i = 0; i < objetos.size(); i++){
            ObjetoCantidad x = objetos.get(i);
            System.out.println("peso "+x.getPeso()+" valor "+ x.getValor()+ " v/p "+ (double)x.getValor()/(double)x.getPeso());
        }
    }

    public static void main(String[] args) {
        List<ObjetoCantidad> objetos = new ArrayList<>();
        ObjetoCantidad ob1 = new ObjetoCantidad(210, 15, 3);
        objetos.add(ob1);
        ObjetoCantidad ob2 = new ObjetoCantidad(230, 50, 2);
        objetos.add(ob2);
        ObjetoCantidad ob3 = new ObjetoCantidad(150, 20, 4);
        objetos.add(ob3);
        ObjetoCantidad ob4 = new ObjetoCantidad(40, 55, 5);
        objetos.add(ob4);
        ObjetoCantidad ob5 = new ObjetoCantidad(500, 300, 1);
        objetos.add(ob5);
        
        System.out.println("------ Objetos ordenados para fraccionaria ------");
        ordenarObjetosFraccionaria(objetos);
        imprimir(objetos);
        Mochila mochila = new Mochila(520);
        mochila.llenarMochilaFraccionaria(objetos);
        System.out.println("mochila llena");
        imprimir(mochila.objetos);
        System.out.println("\n");

        System.out.println("------ Objetos ordenados para peso ------");
        ordenarObjetosPeso(objetos);
        imprimir(objetos);
        Mochila mochilaPeso = new Mochila(520);
        mochilaPeso.llenarMochilaPeso(objetos);
        System.out.println("mochila llena");
        imprimir(mochilaPeso.objetos);
        System.out.println("\n");

        System.out.println("------ Objetos ordenados para valor ------");
        ordenarObjetosValor(objetos);
        imprimir(objetos);
        Mochila mochilaValor = new Mochila(520);
        mochilaValor.llenarMochilaValor(objetos);
        System.out.println("mochila llena");
        imprimir(mochilaValor.objetos);
        System.out.println("\n");
    }
}