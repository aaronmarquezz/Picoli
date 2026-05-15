package modelo;

public class SectorPrioritarioParados extends SectorPrioritario<Adulto> {

	public SectorPrioritarioParados(TipoPago tipo) {
		super(tipo);
	}

	@Override
	public double pago(double deficit) {
		double consumido = 0;
		for (Adulto miembro : getMiembros()) {
			double necesidad = miembro.getNecesidad();
			consumido -= necesidad;
			miembro.alimentar(necesidad);
		}
		return consumido;
	}
}
