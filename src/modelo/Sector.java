package modelo;

import java.util.AbstractCollection;

public abstract class Sector<T extends Ser>  {
	private AbstractCollection<T> miembros;
	private final double necesidadVital;
	private final double pago;
	private final double reduccionMaxima;

	//usando un tipo complejo (mas control, mas acoplamiento)
	public Sector(TipoPago tipo,AbstractCollection<T> miembros) {
		this(tipo.getNecesidadVital(),tipo.getPago(),tipo.getReduccionMaxima(), miembros);
	}
	
	//Con tipos primitivos (menos control pero menos acoplamiento)
	public Sector(double necesidadVital, double pago, double reduccionMaxima,AbstractCollection<T> miembros) {
		super();
		//Componente
//		miembros = new ArrayList<>();
		//se susituye con ingrediente
		this.miembros=miembros;
		this.necesidadVital = necesidadVital;
		this.pago = pago;
		this.reduccionMaxima = reduccionMaxima;
	}
	
	public abstract T getFirst();
	//funcion que sabe como añadir
	public void add(T elemento) {
		miembros.add(elemento);
	}
	
	public double pago(double deficit) {
		double pagoSector = this.pago;
		double pagoTotalPorSector = miembros.size() * pagoSector;;
		if (deficit < 0) {
			double presupuestoCorregidoMaximo = pagoTotalPorSector * reduccionMaxima;
			double presupuestoSectorReal = pagoTotalPorSector + deficit;
			pagoTotalPorSector = Math.max(presupuestoCorregidoMaximo, presupuestoSectorReal);
			deficit += pagoTotalPorSector - pagoTotalPorSector;
			pagoSector = pagoTotalPorSector / miembros.size();
		}
		for (Ser poblador : miembros) {
			poblador.alimentar(pagoSector);
		}
		return pagoTotalPorSector;
	}
	public int size() {
		return miembros.size();
	}

	public AbstractCollection<T> getMiembros() {
		return miembros;
	}
	
	public double getTotalPago() {
		return miembros.size()*necesidadVital;
	}
}
