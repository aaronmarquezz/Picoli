package test.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import modelo.Adulto;
import modelo.Estado;
import modelo.Menor;
import modelo.Ser;

class EstadoJubilarTest {
	
	Estado estado;
	Adulto adulto;
	private int necesidadVital=100;
	Adulto adulto2;
	
	@BeforeEach
	void beforeEach() {
		estado=new Estado();
		necesidadVital = 100;
		adulto=new Adulto(64, 90, necesidadVital, 0);
		adulto2=new Adulto(62, 63, necesidadVital, 0);
	}

	@Test
	void testEnvejecer() {
		//Crear una poblacion base
		Menor menor=new Menor(90, necesidadVital);
		Ser anciano=new Ser(adulto2);
		////////////////////
		estado.getMenores().add(menor);
		estado.getAncianos().add(anciano);
		estado.getParados().add(adulto);
		//////////////////////////////////
		int edadActualMenor = menor.getEdadActual();
		int edadActualAdulto = adulto.getEdadActual();
		int edadActualAnciano = anciano.getEdadActual();
		estado.cerrarPeriodo();
		/////// DEspues de cerrar periodo deben tener un anno mas
		assertEquals(++edadActualMenor, menor.getEdadActual());
		assertEquals(++edadActualAdulto, adulto.getEdadActual());
		assertEquals(++edadActualAnciano, anciano.getEdadActual());
	}

	@Test
	void testJubilar() {
		estado.getParados().add(adulto);
		int antesDeJubilar = estado.getParados().size();
		int cantidadDeAncianos = estado.getAncianos().size();
		estado.cerrarPeriodo();
		assertEquals(--antesDeJubilar, estado.getParados().size());
		assertEquals(++cantidadDeAncianos, estado.getAncianos().size());
		
	}
	
	@Test
	void testEnterrar() {
		estado.getParados().add(adulto2);
		int antesCerrarPeriodo = estado.getParados().size();
		//cerrar periodo para matear al adulto2
		estado.cerrarPeriodo();
		assertFalse(adulto2.isVivo());
		assertEquals(--antesCerrarPeriodo, estado.getParados().size());
	}
	
}
