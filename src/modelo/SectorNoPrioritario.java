package modelo;

import java.util.ArrayList;

public class SectorNoPrioritario<T extends Ser> extends Sector<T> {

	public SectorNoPrioritario(TipoPago tipo) {
			super(tipo.getNecesidadVital(),tipo.getPago(), tipo.getReduccionMaxima(),new ArrayList<T>());
	}

	@Override
	public T getFirst() {
		return ((ArrayList<T>)getMiembros()).getFirst();
	}

}
