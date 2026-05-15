package modelo;

public class Menor extends Ser {
    private double factorDesarrollo;

    public Menor(double esperanzaVida, double necesidadVital) {
        super(esperanzaVida, necesidadVital);
        this.factorDesarrollo = 0;
    }

    @Override
    public void alimentar(double cantidadEntregada) {
        if (cantidadEntregada >= necesidadVital) {
            factorDesarrollo += 5.55;
        } else if (cantidadEntregada > 0) {
            factorDesarrollo += 5.55 * (cantidadEntregada / necesidadVital);
        }
    }

    public double getFactorDesarrollo() {
        return factorDesarrollo;
    }

    public void setFactorDesarrollo(double factorDesarrollo) {
        this.factorDesarrollo = factorDesarrollo;
    }
}
