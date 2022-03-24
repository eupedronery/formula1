package SO;

import java.util.concurrent.Semaphore;

import view.efiumview;

public class fUM extends Thread {

	private int escu;
	private Semaphore semaforoLargada;
	private Semaphore semaforoEscuderia;
	public static int carrosForaDaPista = 0;

	public fUM(int id, Semaphore semaforoLargada, Semaphore semaforoEscuderia) {
		this.escu = id;
		this.semaforoLargada = semaforoLargada;
		this.semaforoEscuderia = semaforoEscuderia;
	}

	@Override
	public void run() {

		for (int i = 1; i < 3; i++) {
			try {
				semaforoLargada.acquire();
				CarroAndando(i);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				semaforoLargada.release();
				System.out.println("O carro " + i + " da escuderia " + escu + " saiu da pista");
				carrosForaDaPista++;
			}
		}
		if (carrosForaDaPista == 14) {
			OrdenaGrid();
		}
	}

	private void CarroAndando(int carro) {

		System.out.println("O carro " + carro + " da escuderia " + escu + " entrou na pista");
		for (int i = 1; i < 4; i++) {
			int tempoVolta = (int) ((Math.random() * 180) + 60);
			try {
				sleep(tempoVolta * 30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println("Escuderia: " + escu + " Carro: " + carro + " Volta: " + i + " Tempo: "
					+ tempoVolta + " segundos");
			try {
				semaforoEscuderia.acquire();
				if (tempoVolta < efiumview.valorVoltas[(2 * escu) - carro]
						|| efiumview.valorVoltas[(2 * escu) - carro] == 0) {
					efiumview.valorVoltas[(2 * escu - 2 + carro) - 1] = tempoVolta;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				semaforoEscuderia.release();
			}

		}

	}

	public void OrdenaGrid() {
		int aux;
		String auxiliar;
		for (int i = 0; i < 13; i++) {
			for (int j = i + 1; j < 14; j++) {
				if (efiumview.valorVoltas[i] > efiumview.valorVoltas[j]) {
					aux = efiumview.valorVoltas[i];
					efiumview.valorVoltas[i] = efiumview.valorVoltas[j];
					efiumview.valorVoltas[j] = aux;
					auxiliar = efiumview.textoVoltas[i];
					efiumview.textoVoltas[i] = efiumview.textoVoltas[j];
					efiumview.textoVoltas[j] = auxiliar;
				}
			}
		}
		for (int i = 0; i < 14; i++) {
			System.out.println(
					"Posição " + (i + 1) + ": " + efiumview.textoVoltas[i] + efiumview.valorVoltas[i] + " segundos");
		}
	}

}