package analisis.algoritmos.MochilaEjercicio;

public class ObjetoCantidad extends ObjetoMochila{
    private double cantidad;

    public ObjetoCantidad(double peso, double valor, double cantidad){
        super(peso, valor);
        this.cantidad = cantidad;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }


}
