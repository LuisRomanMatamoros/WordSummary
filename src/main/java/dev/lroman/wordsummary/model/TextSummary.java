package dev.lroman.wordsummary.model;

/**
 * 
 * @author Luis Alberto Roman Matamoros
 *
 */
public class TextSummary {
	
	private String texto;
	private Long count;
	
	
	/**
	 * @return the texto
	 */
	public String getTexto() {
		return texto;
	}
	/**
	 * @param texto the texto to set
	 */
	public void setTexto(String texto) {
		this.texto = texto;
	}
	/**
	 * @return the numero
	 */
	public Long getNumero() {
		return count;
	}
	/**
	 * @param numero the numero to set
	 */
	public void setNumero(Long numero) {
		this.count = numero;
	}

}
