package analisis.algoritmos.Devuelta;

import java.util.ArrayList;
import java.util.List;

public class Cajero {
    private List<Billete> billetes;

    public Cajero(List<Billete> billetes) {
        billetes.sort((a, b) -> b.getDenominacion() - a.getDenominacion());
        this.billetes = billetes;
    }

    public List<Billete> calcularDevuelta(int monto) {
        List<Billete> devuelta = new ArrayList<>();

        for (Billete billete : billetes) {
            int cantidadNecesaria = monto / billete.getDenominacion();
            int cantidadDisponible = billete.getCantidad();
            int cantidadAEntregar = Math.min(cantidadNecesaria, cantidadDisponible);

            if (cantidadAEntregar > 0) {
                devuelta.add(new Billete(billete.getDenominacion(), cantidadAEntregar));
                monto -= cantidadAEntregar * billete.getDenominacion();
                billete.setCantidad(billete.getCantidad() - cantidadAEntregar);
            }
        }

        if (monto > 0) {
            System.out.println("No se puede entregar la devuelta completa.");
        }

        return devuelta;
    }

    public List<Billete> getBilletes() {
        return billetes;
    }

    public void setBilletes(List<Billete> billetes) {
        this.billetes = billetes;
    }

    public static void main(String[] args) {
            Billete billete1 = new Billete(100000, 10);
            Billete billete2 = new Billete(50000, 20);
            Billete billete3 = new Billete(20000, 30);
            Billete billete4 = new Billete(10000, 40);
            
            List<Billete> billetes = new ArrayList<>(List.of(billete3, billete1, billete4, billete2));

            Cajero cajero = new Cajero(billetes);
    
            for (Billete billete : cajero.getBilletes()) {
                System.out.println("Denominación: " + billete.getDenominacion() + ", Cantidad: " + billete.getCantidad());
            }
            
            int monto = 300000;
            System.out.println("Monto a retirar: " + monto);
            List<Billete> devuelta = cajero.calcularDevuelta(monto);
            System.out.println("Devuelta:");
            for (Billete billete : devuelta) {
                System.out.println("Denominación: " + billete.getDenominacion() + ", Cantidad: " + billete.getCantidad());
            }
            int total = 0;
            for (Billete billete : cajero.getBilletes()) {
                total += billete.getDenominacion() * billete.getCantidad();
            }
            System.out.println("Total en el cajero: " + total);
    }
}
