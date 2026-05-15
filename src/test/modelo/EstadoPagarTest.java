package test.modelo;

import static modelo.TipoPago.menor;
import static org.junit.Assert.assertEquals;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.PriorityQueue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import modelo.Estado;
import modelo.Menor;
import modelo.Ser;
import modelo.OM.SeresManager;

class EstadoPagarTest {
	Estado estado;
	SeresManager seresManager = new SeresManager();
	int cantidadMenores = 10;
	double cantidadTotalPagoAlSector;
	double factorDesarrollo = 5.55;
	AbstractCollection<Menor> menores;
	
	@BeforeEach
	void before() {
//		ArrayList<Ser> seres;
//		PriorityQueue<Ser> otros;
//		seres.getFirst();
//		otros.poll();
		estado = new Estado();
		menores = estado.getMenores();
		menores.addAll(seresManager.getMenores(cantidadMenores));
	}
	
	@Test
	void testCerrarPeriodo10MenoresCapitalSuficiente() {
		cantidadTotalPagoAlSector=menores.size()*menor.getNecesidadVital();
		estado.setCapital(cantidadTotalPagoAlSector);
		estado.cerrarPeriodo();
		assertEquals(0, estado.getCapital(),.1);
		for (Menor menor : menores) {
			assertEquals(factorDesarrollo, menor.getFactorDesarrollo(),.1);
		}
	}

		@Test
		void testCerrarPeriodo10MenoresCapitalINNSuficiente() {
			// que pasa si el capital es menor?
			double reduccion = .5;
			cantidadTotalPagoAlSector = menores.size() * menor.getNecesidadVital();
			estado.setCapital(cantidadTotalPagoAlSector * reduccion);
			estado.cerrarPeriodo();
			// La prueba
			double factorDesarrollo = this.factorDesarrollo * reduccion;
			for (Menor menor : menores) {
				assertEquals(factorDesarrollo, menor.getFactorDesarrollo(), 0.1);
			}
			assertEquals(0, estado.getCapital(),.1);
		}	
		@Test
		void testCerrarPeriodo10MenoresCapitalSINSupervivencia() {
			double reduccion = .44;
			cantidadTotalPagoAlSector = menores.size() * menor.getNecesidadVital();
			double capital = cantidadTotalPagoAlSector * reduccion;
			int infancia=18;
			//envejecer hasta que dejan de ser menores
			for (int i = 0; i < infancia; i++) {
				estado.setCapital(capital);
				estado.cerrarPeriodo();
			}
			double factorSuperVivencia=45;
			for (Menor menor : menores) {
				assertEquals(factorSuperVivencia,menor.getFactorDesarrollo(),.1);
			}
		}
}
