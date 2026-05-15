package modelo.OM;

import java.util.ArrayList;
import java.util.List;
import modelo.Menor;
import modelo.TipoPago;

public class SeresManager {

	public List<Menor> getMenores(int cantidad){
		ArrayList<Menor> menores=new ArrayList<>();
		double esperanzaVida=50;
		for (int i = 0; i < cantidad; i++) {
			menores.add(new Menor(esperanzaVida,TipoPago.menor.getNecesidadVital()));
		}
		return menores;
	}
}
