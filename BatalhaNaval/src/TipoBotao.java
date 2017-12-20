
public enum TipoBotao {
	AGUA(0), AVIAO(2), SUBMARINO(2), NAVIO(3), PORTAAVIOES(4), DESTRUIDO(5);
	
	private final int valor;
	
	private TipoBotao(int valor) {
		this.valor = valor;
	}
	
	public int getValor() {
		return this.valor;
	}
}
