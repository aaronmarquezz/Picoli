package modelo;

import java.util.Comparator;
import java.util.PriorityQueue;

public class SectorPrioritario<T extends Adulto> extends Sector<T> {

	public SectorPrioritario(TipoPago tipo) {
		super(tipo.getNecesidadVital(),tipo.getPago(), tipo.getReduccionMaxima(), new PriorityQueue<T>(new Comparator<Adulto>() {
			@Override
			public int compare(Adulto o1, Adulto o2) {
				return o1.getPeriodosEnEstado() - o2.getPeriodosEnEstado();
			}
		}));
	}

	@Override
	public T getFirst() {
		return ((PriorityQueue<T>)getMiembros()).poll();
	}

}
