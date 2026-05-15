package modelo;

import static modelo.TipoPago.anciano;
import static modelo.TipoPago.menor;
import static modelo.TipoPago.parado;
import static modelo.TipoPago.trabajador;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class Estado {
	// atributos sobre desarrollo
	private double capital = 0;
	private double cantidadProducidaPorTrabajador;
	private final double edadJubilacion = 65;
	private final double edadMadurez = 18;
	private final double necesidadVitalBase = 100;
	private int numeroDefunciones = 0;
	private Queue<Double> colaMediaHistorica = new LinkedList<>();

	// poblacion
	private Sector<Menor> menores;
	private Sector<Adulto> trabajadores;
	private Sector<Adulto> parados;
	private Sector<Ser> ancianos;

	// prduccion
	private double totalDemandado = 0;

	public Estado() {
		super();
		menores = new SectorNoPrioritario<Menor>(menor);
		trabajadores = new SectorPrioritario<Adulto>(trabajador);
		ancianos = new SectorNoPrioritario<Ser>(anciano);
		parados = new SectorPrioritarioParados(parado);
	}

	public void abrirPeriodo(double porcentajeIncrementoDemanda) {
		// 1 calcular la cantidad que debe producir el estado segun el incremento (puede
//		// ser una cantidad menor) 
//		double objetivoProduccion = calcularCantidadAProducir(porcentajeIncrementoProduccion);
		totalDemandado *= 1 + porcentajeIncrementoDemanda;
//		// 2 Contratar o despedir a adultos segun sea la necesidad
//		gestionarEmpleos(objetivoProduccion);
//		// 3 decidir los nacimientos en funcion de cuantas defunciones, y otras cosas,
//		// hayan pasado en el periodo anterior
//		gestionarNacimientos();
	}

	// TODO de nada me sirve lo que entra por parámetro no?
	public double calcularCantidadProducir(double porcentajeIncrementoProduccion) {
		double produccionReal;
		produccionReal = trabajadores.size() * cantidadProducidaPorTrabajador;

		return produccionReal;
	}

	// entendemos que objetivoproduccion es el incremento porcentual.
	public double gestionarEmpleos(double objetivoProduccion) {
		double produccionPotencial, produccionReal, produccionFutura;
		produccionPotencial = (trabajadores.size() + parados.size()) * cantidadProducidaPorTrabajador;
		// TODO no se si hacer eso asi o ponerlo como atributo de la clase (funcion de
		// arriba hace set y no return).
		produccionReal = calcularCantidadProducir(objetivoProduccion);
		produccionFutura = produccionReal * (1 + objetivoProduccion / 100);
		// calculo diferencial. si es positiva tengo que despedir, me sobran personas. y
		// al reves.
		double diferencial = produccionReal - produccionFutura;
		// TODO aquí voy a añadir a la cola de medias historicas de diferenciales.
		colaMediaHistorica.add(diferencial);
		// contratar. ¿Cuantos contratamos?
		int personasAfectadas = (int) Math.ceil((diferencial / cantidadProducidaPorTrabajador));
		// el problema es que si personas afectadas es mayor que produccion potencial no
		// dan las personas, así que cogemos el minimo.
		int valorMinimo = (int) Math.min(personasAfectadas, produccionPotencial);
		if (valorMinimo < 0) {
			// vamos a la lista de parados y contratamos de alli.
			for (int i = 0; i < valorMinimo; i++) {
				Adulto parado = parados.getFirst();
				// TODO ese getFirst coge el que tiene que coger?
				trabajadores.add(parado);
				// TODO no entiendo por que tengo que poner una funcion offer a parte.
			}
		} else {
			// vamos a la lista de trabaadores y despedimos. nos sobra gente.
			for (int i = 0; i < valorMinimo; i++) {
				Adulto trabajador = trabajadores.getFirst();
				// TODO ese getFirst coge el que tiene que coger?
				parados.add(trabajador);
				// TODO no entiendo por que tengo que poner una funcion offer a parte.
			}
		}
		return diferencial;
	}

	public void gestionarNacimientos() {
		// TODO ¿donde le vamos actualizando año a año el diferencial?
		// control de que la cola no tenga más de 5 periodos.
		if (colaMediaHistorica.size() > 5) {
			colaMediaHistorica.remove(0);
		}
		// ya aqui en esta funcion tenemos que recorrer la cola y sacar la media.
		// Podriamos hacerlo en una funcion con el siguiente codigo
		double suma = 0;
		for (Double elemento : colaMediaHistorica) {
			suma = suma + elemento;
		}
		int media = (int) Math.ceil(suma / colaMediaHistorica.size());

		int nacimientosPonderados = numeroDefunciones - media;

		for (int i = 0; i < nacimientosPonderados; i++) {
			menores.add(new Menor(80, 100));
		}

	}

	////////////////////////////////////////////////////
	/**
	 * 1º se calcula cuanto ha producido el conjunto de los trabajadores 2º se paga
	 * a todos los seres 3º se envejece a todos los seres 4º se jubila a los adultos
	 * que han llegado a la edad de jubilación y se les quita los ahorros 5º se
	 * eliminan los seres que han muerto y se les quita los ahorros (si son adultos)
	 */
	public void cerrarPeriodo() {
		// 1 Calcular el capital
		double totalProducido = trabajadores.size() * cantidadProducidaPorTrabajador;
		this.capital += totalProducido;
		// 2 pagar a los seres
		pagar(menores, ancianos, trabajadores, parados);
		// Tendria que preguntarme si puedo pagarlo
		ArrayList<Ser> poblacion = new ArrayList<Ser>();
		poblacion.addAll(menores.getMiembros());
		poblacion.addAll(trabajadores.getMiembros());
		poblacion.addAll(parados.getMiembros());
		poblacion.addAll(ancianos.getMiembros());
		envejecer(poblacion);
		jubila(parados.getMiembros(), trabajadores.getMiembros());
		enterrar(menores.getMiembros(), parados.getMiembros(), trabajadores.getMiembros(), ancianos.getMiembros());
	}

	private void pagar(Sector<? extends Ser>... sector) {
		double deficit = 0;
		for (Sector<? extends Ser> poblacion : sector) {
			double presupuestoMaximo = poblacion.getTotalPago();
			deficit = capital - presupuestoMaximo;
			double pagoReal = poblacion.pago(deficit);
			capital -= pagoReal;
			deficit += presupuestoMaximo - pagoReal;
		}
		capital += deficit;
	}

	private boolean hayDeficit(double presupuesto) {
		return capital < presupuesto;
	}

	private double obtenerDeficit(double presupuesto) {
		return capital - presupuesto;
	}

	private double calcularPresupuesto() {
		double presupuestoMenores = menores.size() * menor.getPago();
		double presupuestoAncianos = ancianos.size() * anciano.getPago();
		double prespuestoParados = parados.size() * parado.getPago();
		double presupuestoTrabajadores = trabajadores.size() * trabajador.getPago();
		return prespuestoParados + presupuestoAncianos + presupuestoMenores + presupuestoTrabajadores;
	}

	// Pendiente para el lunes 13 abril robar a los muertos
	private void enterrar(AbstractCollection<? extends Ser>... listas) {
		for (AbstractCollection<? extends Ser> poblacion : listas) {
			Iterator<? extends Ser> iterator = poblacion.iterator();
			while (iterator.hasNext()) {
				Ser ser = iterator.next();
				if (!ser.isVivo()) {
					// TODO de esta forma no tenemos que retornarlo?
					this.numeroDefunciones++;
					iterator.remove();
				}
			}
		}
	}

	private void jubila(AbstractCollection<Adulto>... listas) {
		for (AbstractCollection<Adulto> lista : listas) {
			Iterator<Adulto> iterator = lista.iterator();
			while (iterator.hasNext()) {
				// sustituye al for
//			for (Adulto adulto : lista) {
				Adulto adulto = iterator.next();
				if (isAnciano(adulto)) {
					this.capital += adulto.getAhorros();
					iterator.remove();
					ancianos.getMiembros().add(new Ser(adulto));
				}
			}
		}
	}

	private boolean isAnciano(Adulto adulto) {
		return adulto.getEdadActual() >= edadJubilacion;
	}

	private void envejecer(ArrayList<? extends Ser> lista) {
		for (Ser ser : lista) {
			ser.envejecer();
		}
	}

	public AbstractCollection<Menor> getMenores() {
		return menores.getMiembros();
	}

	public AbstractCollection<Adulto> getTrabajadores() {
		return trabajadores.getMiembros();
	}

	public AbstractCollection<Adulto> getParados() {
		return parados.getMiembros();
	}

	public AbstractCollection<Ser> getAncianos() {
		return ancianos.getMiembros();
	}

	public double getCapital() {
		return capital;
	}

	public void setCapital(double capital) {
		this.capital = capital;
	}
}
