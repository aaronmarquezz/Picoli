package modelo;

public enum TipoPago {

	menor(1,1,.45), parado(1,1,1), trabajador(1,2,1), anciano(.5,1,.3);

	private static final double necesidadVitalBase = 100;
	// Lo que necesitan
	private double factorNecesidadVital;
	// lo que cobran
	private double coeficiente;
	// lo minimo que pueden cobrar
	private double reduccionMaxima;

	private TipoPago(double factorNecesidadVital, double coeficiente, double reduccionMaxima) {
		this.factorNecesidadVital = factorNecesidadVital;
		this.coeficiente = coeficiente;
		this.reduccionMaxima = reduccionMaxima;
	}

	public double getNecesidadVital() {
		return necesidadVitalBase*factorNecesidadVital;
	}
	public double getPago() {
		return getNecesidadVital()*coeficiente;
	}
	public double getReduccionMaxima() {
		return reduccionMaxima;
	}
}
