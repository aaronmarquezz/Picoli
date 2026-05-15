package modelo;

//Comparable es la comparacion del objeto con otro objeto
public class Adulto extends Ser implements Comparable<Adulto> {
	public int getPeriodosEnEstado() {
		return periodosEnEstado;
	}

	public void setPeriodosEnEstado(int periodosEnEstado) {
		this.periodosEnEstado = periodosEnEstado;
	}

	private double ahorros;
	//antiguedad
	private int periodosEnEstado=0;

	// Constructor para transición desde etapa Menor
	public Adulto(Menor menor, boolean empleado, double necesidadVital) {
		super(menor.getEdadActual(), menor.getEsperanzaVida(), necesidadVital);
		this.ahorros = 0;
	}

	// Constructor general / directo
	public Adulto(int edadActual, double esperanzaVida, double necesidadVital, double ahorrosIniciales) {
		super(edadActual, esperanzaVida, necesidadVital);
		this.ahorros = ahorrosIniciales;
	}

	@Override
	public void alimentar(double cantidadEntregada) {
		this.ahorros -= necesidadVital - cantidadEntregada;
	}
	
	public double getNecesidad(){
	    return necesidadVital - Math.min(ahorros, necesidadVital);
	}

	public double entregarAhorros() {
        double ahorrosEntregados = ahorros;
        this.ahorros = 0;
        return ahorrosEntregados;
    }

	public double getAhorros() {
        return ahorros;
    }

	@Override
	public int compareTo(Adulto o) {
		return periodosEnEstado-o.periodosEnEstado;
	}
}
