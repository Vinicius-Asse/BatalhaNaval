import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.MatteBorder;

public class BotaoTabuleiro extends JButton{

	TipoBotao tipo;
	private boolean tiroNaAgua = true;

	public BotaoTabuleiro(String s) {
		super(s);
		this.tipo = TipoBotao.AGUA;
		this.setBackground(new Color(176, 224, 230));
	}
	
	
	public void setTipo(TipoBotao t, int i) {
		if(i >= 0) {
			if (t == TipoBotao.AGUA) {
				this.tipo = TipoBotao.AGUA;
				this.setBackground(new Color(50,100,200));
				this.setBorder(new MatteBorder(1, 1, 1, 1, new Color (60,120,200)));
				this.setIcon(null);
			}
		} else {
			if (t == TipoBotao.AGUA) {
				this.tipo = TipoBotao.AGUA;
				this.setBackground(new Color(50,50,50));
				this.setBorder(new MatteBorder(1, 1, 1, 1, Color.DARK_GRAY));
				this.setIcon(null);
			}
		}
		if (t == TipoBotao.SUBMARINO) {
			this.tipo = TipoBotao.SUBMARINO;
			this.setBackground(new Color(255, 255, 155));
			this.setIcon(new ImageIcon("src/Submarino0" + i + ".png"));
			this.setDisabledIcon(new ImageIcon("src/Submarino0" + i + ".png"));
			this.setBorder(null);
		}
		if (t == TipoBotao.PORTAAVIOES) {
			this.tipo = TipoBotao.PORTAAVIOES;
			this.setBackground(new Color(155, 255, 155));
			this.setIcon(new ImageIcon("src/PortaAviao0" + i + ".png"));
			this.setDisabledIcon(new ImageIcon("src/PortaAviao0" + i + ".png"));
			this.setBorder(null);
		}
		if (t == TipoBotao.NAVIO) {
			this.tipo = TipoBotao.NAVIO;
			this.setBackground(new Color(155, 155, 255));
			this.setIcon(new ImageIcon("src/Navio0" + i + ".png"));
			this.setDisabledIcon(new ImageIcon("src/Navio0" + i + ".png"));
			this.setBorder(null);
		}
		if (t == TipoBotao.AVIAO) {
			this.tipo = TipoBotao.AVIAO;
			this.setBackground(new Color(255, 155, 155));
			this.setIcon(new ImageIcon("src/Aviao0" + i + ".png"));
			this.setDisabledIcon(new ImageIcon("src/Aviao0" + i + ".png"));
			this.setBorder(null);
		}
		if (t == TipoBotao.DESTRUIDO) {
			if (this.tipo == TipoBotao.AGUA) {
				this.setIcon(new ImageIcon("src/TiroNaAgua.png"));
				this.setDisabledIcon(new ImageIcon("src/TiroNaAgua.png"));
				this.tiroNaAgua = true;
				this.setBorder(null);
			} else if(this.tipo != TipoBotao.DESTRUIDO) {
				this.setIcon(new ImageIcon("src/Explosao.png"));
				this.setDisabledIcon(new ImageIcon("src/Explosao.png"));
				this.tiroNaAgua = false;
				this.setBorder(null);
			}
			this.tipo = TipoBotao.DESTRUIDO;
		}

	}
	
	public boolean atirar() {
		this.setTipo(TipoBotao.DESTRUIDO, 0);
		if(this.tiroNaAgua) {
			return false;
		}else
			return true;
	}
	public TipoBotao getTipo() {
		return tipo;
	}

	
}
