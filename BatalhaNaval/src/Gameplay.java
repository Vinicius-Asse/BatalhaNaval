import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JSeparator;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.GridLayout;

import javax.swing.JButton;

import javax.imageio.ImageIO;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.Cursor;
import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import javax.swing.border.MatteBorder;
import javax.swing.JTextPane;

public class Gameplay extends JFrame {

	private JPanel contentPane;
	private ArrayList<BotaoTabuleiro> listaBotoesJogador = new ArrayList<BotaoTabuleiro>();
	private ArrayList<BotaoTabuleiro> listaBotoesInimigo = new ArrayList<BotaoTabuleiro>();
	private ArrayList<JLabel> listaCoordenada = new ArrayList<JLabel>();
	private TipoBotao tipo;
	private ModoDeJogo modo;
	private JPanel MenuCriacaoMapa, GameOver, MenuGameplay, MenuInicial;
	private JButton BotaoJogar, CarregarArquivo, SalvarArquivo, GerarTabuleiro, ResetaTabuleiro, Jogar, Sair;
	private JButton selecionaTiroAviao, selecionaTiroSubmarino, selecionaTiroNavio;

	private String tabuleiroJogador = "";
	private String saveArquivo = "";

	private int proxTiro = 0, vida = 0, vidaInimigo = 0, tipoTiro = 0;

	private ArrayList<JButton> seleciona = new ArrayList<JButton>();

	Scanner scan = new Scanner("src/save.txt");

	public ArrayList<BotaoTabuleiro> getListaBotoesJogador() {
		return listaBotoesJogador;
	}

	public void setListaBotoesJogador(ArrayList<BotaoTabuleiro> listaBotoesJogador) {
		this.listaBotoesJogador = listaBotoesJogador;
	}

	public ArrayList<BotaoTabuleiro> getListaBotoesInimigo() {
		return listaBotoesInimigo;
	}

	public void setListaBotoesInimigo(ArrayList<BotaoTabuleiro> listaBotoesInimigo) {
		this.listaBotoesInimigo = listaBotoesInimigo;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gameplay frame = new Gameplay();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// Atualizando a Tabela do Jogador
	public void atualizaJogador(int i, TipoBotao tipo) {

		// Lê String do save
		if (i < 0) {
			for (int j = 0; j < this.saveArquivo.length(); j++) {
				if ((int) this.saveArquivo.charAt(j) >= (int) '0' && (int) this.saveArquivo.charAt(j) <= (int) '9') {
					if (this.saveArquivo.charAt(j + 1) == ' ') {
						int index = (int) this.saveArquivo.charAt(j) - (int) '0';

						switch (index) {
						case 1:
							tipo = TipoBotao.AVIAO;
							break;
						case 2:
							tipo = TipoBotao.SUBMARINO;
							break;
						case 3:
							tipo = TipoBotao.NAVIO;
							break;
						case 4:
							tipo = TipoBotao.PORTAAVIOES;
						}
					}
				}

				if (this.saveArquivo.charAt(j) == '\n') {
					if (j > 0 && this.saveArquivo.charAt(j - 1) != '0') {
						i = 10 * ((int) this.saveArquivo.charAt(j - 1) - 49)
								+ ((int) this.saveArquivo.charAt(j - 2) - 65);
						System.out.println("i = " + i);
						atualizaJogador(i, tipo);
					}
					if (this.saveArquivo.charAt(j - 1) == '0') {
						i = 10 * 9 + ((int) this.saveArquivo.charAt(j - 3) - 65);
						System.out.println("i = " + i);
						atualizaJogador(i, tipo);
					}
				}
			}
			return;
		}

		ArrayList<BotaoTabuleiro> coordenada = this.listaBotoesJogador;
		// Checa as posições a serem ocupadas e as ocupa
		for (int j = 0; j < tipo.getValor(); j++) {

			if (coordenada.get(i + j).getTipo().getValor() != 0 || i % 10 > 10 - tipo.getValor()) {
				for (int k = j - 1; k >= 0; k--) {
					coordenada.get(i + k).setTipo(TipoBotao.AGUA, 0);
				}
				return;
			} else {
				coordenada.get(i + j).setTipo(tipo, j + 1);
				if (j == tipo.getValor() - 1)
					ativaBotao(tipo);
			}
		}

		if (tipo == TipoBotao.AVIAO)
			this.tabuleiroJogador += " " + (tipo.getValor() - 1) + " " + (char) ((i % 10) + 65) + ((i / 10) + 1) + "\n";
		else
			this.tabuleiroJogador += " " + (tipo.getValor()) + " " + (char) ((i % 10) + 65) + ((i / 10) + 1) + "\n";
		System.out.println(this.tabuleiroJogador);
	}

	public void ativaBotao(TipoBotao tipo) {
		if (tipo.getValor() == 2 && tipo == TipoBotao.AVIAO) {
			this.seleciona.get(1).setEnabled(false);
			this.tipo = TipoBotao.AGUA;
		} else if (tipo == TipoBotao.SUBMARINO) {
			this.seleciona.get(3).setEnabled(false);
			this.tipo = TipoBotao.AGUA;
		}
		if (tipo.getValor() == 3) {
			this.seleciona.get(2).setEnabled(false);
			this.tipo = TipoBotao.AGUA;
		}
		if (tipo.getValor() == 4) {
			this.seleciona.get(0).setEnabled(false);
			this.tipo = TipoBotao.AGUA;
		}

	}

	public void tiroInimigo() {
		Random r = new Random();

		while (true) {
			int tiro = proxTiro;
			int somaSubtrai = 1;
			if (r.nextInt(2) == 0)
				somaSubtrai = -1;
			else
				somaSubtrai = +1;
			
			//Caso o bloco ja não tenha sido destruido
			if (listaBotoesJogador.get(tiro).getTipo().getValor() != 5) {
				int ale = r.nextInt(5);
				if(ale > 1) {//Tiro comum
					if (listaBotoesJogador.get(tiro).atirar()) {
						vida--;
						if (proxTiro < 99 && proxTiro > 1)
							proxTiro += 1 * somaSubtrai;
						else
							proxTiro = r.nextInt(99);
					}
					else 
						proxTiro = r.nextInt(99);
					System.out.println("Vida do jogador = " + vida + " Vida do Inimigo = " + vidaInimigo);
					return;
				}
				int contadornavio = 0;
				for(int j = 0; j < 100; j++) {
					if(listaBotoesInimigo.get(j).getTipo() == TipoBotao.NAVIO)
						contadornavio++;
				} if(contadornavio > 0) {
					if(ale == 1) {//Tiro cascata
						if (listaBotoesJogador.get(tiro).atirar()) {
							vida--;
							if (proxTiro < 99 && proxTiro > 1)
								proxTiro += 1 * somaSubtrai;
							else
								proxTiro = r.nextInt(99);
						}
						else 
							proxTiro = r.nextInt(99);
						
						if (((tiro%10) + 1) < 10 && listaBotoesJogador.get(tiro + 1).getTipo().getValor() < 5) {
							if(listaBotoesJogador.get(tiro + 1).getTipo().getValor() > 0 && listaBotoesJogador.get(tiro + 1).getTipo().getValor() < 5)
								vida--;
							listaBotoesJogador.get(tiro + 1).atirar();
						}
						if (((tiro%10) + 2) < 10 && listaBotoesJogador.get(tiro + 2).getTipo().getValor() < 5) {
							if(listaBotoesJogador.get(tiro + 2).getTipo().getValor() > 0 && listaBotoesJogador.get(tiro + 2).getTipo().getValor() < 5)
								vida--;
							listaBotoesJogador.get(tiro + 2).atirar();
						}
						
						System.out.println("Vida do jogador = " + vida + " Vida do Inimigo = " + vidaInimigo);
						return;
					}
				}
				int contadoraviao = 0;
				for(int j = 0; j < 100; j++) {
					if(listaBotoesInimigo.get(j).getTipo() == TipoBotao.AVIAO)
						contadoraviao++;
				} if(contadoraviao > 0) {
					if(ale == 0) {//Tiro estrela
						if (listaBotoesJogador.get(tiro).atirar()) {
							vida--;
							if (proxTiro < 99 && proxTiro > 1)
								proxTiro += 1 * somaSubtrai;
							else
								proxTiro = r.nextInt(99);
						}
						else 
							proxTiro = r.nextInt(99);
						
						if (((tiro%10) + 1) < 10 && listaBotoesJogador.get(tiro + 1).getTipo().getValor() < 5) {
							if(listaBotoesJogador.get(tiro + 1).getTipo().getValor() > 0 && listaBotoesJogador.get(tiro + 1).getTipo().getValor() < 5)
								vida--;
							listaBotoesJogador.get(tiro + 1).atirar();
						}
						if (((tiro%10) - 1) >= 0 && listaBotoesJogador.get(tiro - 1).getTipo().getValor() < 5) {
							if(listaBotoesJogador.get(tiro - 1).getTipo().getValor() > 0 && listaBotoesJogador.get(tiro - 1).getTipo().getValor() < 5)
								vida--;
							listaBotoesJogador.get(tiro - 1).atirar();
						}
						if (((tiro) + 10) <= 99 && listaBotoesJogador.get(tiro + 10).getTipo().getValor() < 5) {
							if(listaBotoesJogador.get(tiro + 10).getTipo().getValor() > 0 && listaBotoesJogador.get(tiro + 10).getTipo().getValor() < 5)
								vida--;
							listaBotoesJogador.get(tiro + 10).atirar();
						}
						if (((tiro) - 10) >= 0 && listaBotoesJogador.get(tiro - 10).getTipo().getValor() < 5) {
							if(listaBotoesJogador.get(tiro - 10).getTipo().getValor() > 0 && listaBotoesJogador.get(tiro - 10).getTipo().getValor() < 5)
								vida--;
							listaBotoesJogador.get(tiro - 10).atirar();
						}
						
						System.out.println("Vida do jogador = " + vida + " Vida do Inimigo = " + vidaInimigo);
						return;
					}
				}
				
			} else {//Caso ja tenha sido destruido, procure por outro
				if (proxTiro < 99 && proxTiro > 1)
					proxTiro += 1 * somaSubtrai;
				else
					proxTiro = r.nextInt(99);
			}//Fim da checagem de destruição
		}//Fim do Loop
	}//Fim do método

	public void geraTabuleiroAleatorio(ArrayList<BotaoTabuleiro> tabuleiro) {

		Random r = new Random();

		int randomico = r.nextInt(96);

		for (int i = 0; i < TipoBotao.AVIAO.getValor(); i++) {
			if (tabuleiro.get((randomico) + i).getTipo().getValor() == 0
					&& ((randomico) % 10) <= (10 - TipoBotao.AVIAO.getValor())) {
				if (i == 0) {
					System.out.println("Colocando Aviao na posicao (" + (randomico) + ") || 10 % POSICAO = "
							+ ((randomico) % 10) + " > " + (10 - TipoBotao.AVIAO.getValor()));
					this.tabuleiroJogador += " " + (1) + " " + (char) ((randomico % 10) + 65) + ((randomico / 10) + 1) + "\n";
				}
				tabuleiro.get((randomico) + i).setTipo(TipoBotao.AVIAO, i + 1);
			} else {
				for (int j = 0; j < TipoBotao.AVIAO.getValor(); j++) {
					if (tabuleiro.get(randomico + j).getTipo() == TipoBotao.AVIAO)
						tabuleiro.get((randomico) + j).setTipo(TipoBotao.AGUA, j + 1);
				}
				randomico = r.nextInt(96);
				i = -1;
			}
		}
		randomico = r.nextInt(96);
		for (int i = 0; i < TipoBotao.NAVIO.getValor(); i++) {
			if (tabuleiro.get((randomico) + i).getTipo().getValor() == 0
					&& ((randomico) % 10) <= (10 - TipoBotao.NAVIO.getValor())) {
				if (i == 0) {
					System.out.println("Colocando navio na posicao (" + (randomico) + ") || 10 % POSICAO = "
							+ ((randomico) % 10) + " > " + (10 - TipoBotao.NAVIO.getValor()));
					this.tabuleiroJogador += " " + (3) + " " + (char) ((randomico % 10) + 65) + ((randomico / 10) + 1) + "\n";
				}
				tabuleiro.get((randomico) + i).setTipo(TipoBotao.NAVIO, i + 1);
			} else {
				for (int j = 0; j < TipoBotao.NAVIO.getValor(); j++) {
					if (tabuleiro.get(randomico + j).getTipo() == TipoBotao.NAVIO)
						tabuleiro.get((randomico) + j).setTipo(TipoBotao.AGUA, j + 1);
				}
				randomico = r.nextInt(96);
				i = -1;
			}
		}
		randomico = r.nextInt(96);
		for (int i = 0; i < TipoBotao.PORTAAVIOES.getValor(); i++) {
			if (tabuleiro.get((randomico) + i).getTipo().getValor() == 0
					&& ((randomico) % 10) <= (10 - TipoBotao.PORTAAVIOES.getValor())) {
				if (i == 0) {
					System.out.println("Colocando PA na posicao (" + (randomico) + ") || 10 % POSICAO = "
							+ ((randomico) % 10) + " > " + (10 - TipoBotao.PORTAAVIOES.getValor()));
					this.tabuleiroJogador += " " + (4) + " " + (char) ((randomico % 10) + 65) + ((randomico / 10) + 1) + "\n";
				}
				tabuleiro.get((randomico) + i).setTipo(TipoBotao.PORTAAVIOES, i + 1);
			} else {
				for (int j = 0; j < TipoBotao.PORTAAVIOES.getValor(); j++) {
					if (tabuleiro.get(randomico + j).getTipo() == TipoBotao.PORTAAVIOES)
						tabuleiro.get((randomico) + j).setTipo(TipoBotao.AGUA, j + 1);
				}
				randomico = r.nextInt(96);
				i = -1;
			}
		}
		randomico = r.nextInt(96);
		for (int i = 0; i < TipoBotao.SUBMARINO.getValor(); i++) {
			if (tabuleiro.get((randomico) + i).getTipo().getValor() == 0
					&& ((randomico) % 10) <= (10 - TipoBotao.SUBMARINO.getValor())) {
				if (i == 0) {
					System.out.println("Colocando SUBMARINO na posicao (" + (randomico) + ") || 10 % POSICAO = "
							+ ((randomico) % 10) + " > " + (10 - TipoBotao.SUBMARINO.getValor()));
					this.tabuleiroJogador += " " + (2) + " " + (char) ((randomico % 10) + 65) + ((randomico / 10) + 1) + "\n";
				}
				tabuleiro.get((randomico) + i).setTipo(TipoBotao.SUBMARINO, i + 1);
			} else {
				for (int j = 0; j < TipoBotao.SUBMARINO.getValor(); j++) {
					if (tabuleiro.get(randomico + j).getTipo() == TipoBotao.SUBMARINO)
						tabuleiro.get((randomico) + j).setTipo(TipoBotao.AGUA, j + 1);
				}
				randomico = r.nextInt(96);
				i = -1;
			}
		}
	}

	public void resetaTabuleiro(ArrayList<BotaoTabuleiro> lista, int k) {
		for (int i = 0; i < 100; i++) {
			lista.get(i).setTipo(TipoBotao.AGUA, k);
			this.tabuleiroJogador = "";
			for (int j = 0; j < this.seleciona.size(); j++)
				seleciona.get(j).setEnabled(true);
		}
	}

	/**
	 * Create the frame.
	 * 
	 * @throws IOException
	 */
	public Gameplay() throws IOException {
		setResizable(false);
		setLocationByPlatform(true);

		this.modo = ModoDeJogo.CRIANDOMAPA;
		this.tipo = TipoBotao.AGUA;
		this.proxTiro = new Random().nextInt(99);

		setTitle("Batalha Naval - COMP2");
		setBackground(Color.DARK_GRAY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		//Image img = new Image();
		setIconImage(ImageIO.read(new File("src/Navio01.png")));
		
		contentPane = new JPanel();
		contentPane.setOpaque(false);
		contentPane.setMaximumSize(new Dimension(10, 10));
		contentPane.setRequestFocusEnabled(false);
		contentPane.setForeground(Color.BLACK);
		contentPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		contentPane.setBackground(Color.DARK_GRAY);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel Vitoria = new JPanel();
		Vitoria.setVisible(false);
		
				this.MenuInicial = new JPanel();
				MenuInicial.setDoubleBuffered(false);
				MenuInicial.setRequestFocusEnabled(false);
				MenuInicial.setVerifyInputWhenFocusTarget(false);
				MenuInicial.setBackground(new Color(32, 32, 32, 200));
				MenuInicial.setBounds(-8, -19, 800, 600);
				contentPane.add(MenuInicial);
				MenuInicial.setLayout(null);
				
						JPanel Desfoque = new JPanel();
						Desfoque.setBorder(new MatteBorder(5, 5, 15, 5, (Color) Color.GRAY));
						Desfoque.setBackground(Color.LIGHT_GRAY);
						Desfoque.setBounds(200, 75, 400, 400);
						MenuInicial.add(Desfoque);
						Desfoque.setLayout(null);
								
								JLabel lblNewLabel_1 = new JLabel("Arte: Paula Moraes");
								lblNewLabel_1.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 10));
								lblNewLabel_1.setBounds(15, 354, 145, 23);
								Desfoque.add(lblNewLabel_1);
						
								JLabel lblNewLabel = new JLabel("BATALHA NAVAL");
								lblNewLabel.setBounds(15, 5, 362, 71);
								Desfoque.add(lblNewLabel);
								lblNewLabel.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 48));
								lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
								
										JButton Mascote = new JButton("");
										Mascote.setEnabled(false);
										Mascote.setDisabledIcon(new ImageIcon("src/icone.png"));
										Mascote.setDefaultCapable(false);
										Mascote.setRolloverEnabled(false);
										Mascote.setRequestFocusEnabled(false);
										Mascote.setFocusPainted(false);
										Mascote.setFocusable(false);
										Mascote.setBounds(10, 111, 186, 266);
										Desfoque.add(Mascote);
										Mascote.setVerticalAlignment(SwingConstants.BOTTOM);
										Mascote.setHorizontalAlignment(SwingConstants.LEFT);
										Mascote.setBorder(null);
										Mascote.setIcon(new ImageIcon("src/icone.png"));
										
												this.BotaoJogar = new JButton("INICIAR!");
												BotaoJogar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
												BotaoJogar.setRolloverEnabled(false);
												BotaoJogar.setFocusable(false);
												BotaoJogar.setBounds(214, 321, 153, 31);
												Desfoque.add(BotaoJogar);
												BotaoJogar.addActionListener(new ActionListener() {
													public void actionPerformed(ActionEvent e) {
														MenuInicial.setVisible(false);
														ResetaTabuleiro.setEnabled(true);
														GerarTabuleiro.setEnabled(true);
														CarregarArquivo.setEnabled(true);
														SalvarArquivo.setEnabled(true);
														for (int i = 0; i < listaBotoesJogador.size(); i++) {
															listaBotoesJogador.get(i).setEnabled(true);
															listaBotoesInimigo.get(i).setEnabled(true);
														}

													}
												});
												BotaoJogar.setBackground(new Color(100, 153, 200));
												BotaoJogar.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 16));
												
														JTextPane TextoMenu = new JTextPane();
														TextoMenu.setBounds(204, 111, 173, 200);
														Desfoque.add(TextoMenu);
														TextoMenu.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 14));
														TextoMenu.setText(
																"Atividade Acad\u00EAmica do curso de Ciencia da Computa\u00E7\u00E3o\n\nCriado por:\r\nVinicius e Willian de Assis.\r\n\rUFRRJ.IM - 2017.2 ");
														TextoMenu.setEditable(false);
														
														JSeparator separator = new JSeparator();
														separator.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
														separator.setBackground(Color.GRAY);
														separator.setBounds(71, 67, 254, 33);
														Desfoque.add(separator);
		Vitoria.setBackground(new Color(32, 32, 32, 200));
		Vitoria.setBounds(-3, -14, 800, 600);
		contentPane.add(Vitoria);
		Vitoria.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new MatteBorder(5, 5, 15, 5, (Color) Color.GRAY));
		panel_1.setBackground(Color.LIGHT_GRAY);
		panel_1.setBounds(250, 150, 300, 300);
		Vitoria.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel textoVitoria = new JLabel("Vit\u00F3ria");
		textoVitoria.setBounds(0, 0, 300, 67);
		panel_1.add(textoVitoria);
		textoVitoria.setHorizontalAlignment(SwingConstants.CENTER);
		textoVitoria.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 40));
		
		JTextPane textoVitoria2 = new JTextPane();
		textoVitoria2.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		textoVitoria2.setText("Voc\u00EA ganhou. Parab\u00E9ns! Clique em continuar para jogar novamente");
		textoVitoria2.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 14));
		textoVitoria2.setEditable(false);
		textoVitoria2.setBackground(Color.WHITE);
		textoVitoria2.setBounds(10, 67, 280, 145);
		panel_1.add(textoVitoria2);
		
		JButton Continuar2 = new JButton("Continuar");
		Continuar2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		Continuar2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Vitoria.setVisible(false);
				selecionaTiroNavio.setEnabled(false);
				selecionaTiroAviao.setEnabled(false);
				selecionaTiroSubmarino.setEnabled(false);
				modo = ModoDeJogo.CRIANDOMAPA;
				resetaTabuleiro(listaBotoesJogador, 1);
				resetaTabuleiro(listaBotoesInimigo, -1);
				GameOver.setVisible(false);
				Jogar.setEnabled(false);
				MenuCriacaoMapa.setVisible(true);
				selecionaTiroAviao.setEnabled(true);
				selecionaTiroNavio.setEnabled(true);
				selecionaTiroSubmarino.setEnabled(true);
				MenuGameplay.setVisible(false);
				for (int i = 0; i < 100; i++) {
					listaBotoesJogador.get(i).setEnabled(true);
					listaBotoesInimigo.get(i).setEnabled(true);
				}
			}
		});
		Continuar2.setFont(new Font("Yu Gothic UI", Font.PLAIN, 15));
		Continuar2.setBackground(new Color(102, 153, 0));
		Continuar2.setBounds(10, 217, 280, 63);
		panel_1.add(Continuar2);
		
		JButton Icone2 = new JButton("");
		Icone2.setIcon(new ImageIcon("src/feliz.png"));
		Icone2.setBorder(null);
		Icone2.setDisabledIcon(new ImageIcon("src/feliz.png"));
		Icone2.setEnabled(false);
		Icone2.setBounds(240, 11, 50, 50);
		panel_1.add(Icone2);
		
				this.GameOver = new JPanel();
				GameOver.setVisible(false);
				GameOver.setVerifyInputWhenFocusTarget(false);
				GameOver.setInheritsPopupMenu(true);
				GameOver.setDoubleBuffered(false);
				GameOver.setBounds(-3, -14, 800, 600);
				contentPane.add(GameOver);
				GameOver.setBackground(new Color (32, 32, 32, 200));
				GameOver.setLayout(null);
				
				JPanel panel = new JPanel();
				panel.setLayout(null);
				panel.setBorder(new MatteBorder(5, 5, 15, 5, (Color) Color.GRAY));
				panel.setBackground(Color.LIGHT_GRAY);
				panel.setBounds(250, 150, 300, 300);
				GameOver.add(panel);
				
						JLabel FimDeJogo = new JLabel("FIM DE JOGO!");
						FimDeJogo.setBounds(0, 0, 300, 67);
						panel.add(FimDeJogo);
						FimDeJogo.setHorizontalAlignment(SwingConstants.CENTER);
						FimDeJogo.setFont(new Font("Yu Gothic UI", Font.PLAIN, 16));
						
								JTextPane TextoGameOver = new JTextPane();
								TextoGameOver.setBounds(10, 68, 280, 145);
								panel.add(TextoGameOver);
								TextoGameOver.setEditable(false);
								TextoGameOver.setBackground(new Color(255, 255, 255));
								TextoGameOver.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 14));
								TextoGameOver.setText(
										"Toda a sua frota foi destruida pelo inimigo. Precione continuar para jogar novamente. Boa sorte na proxima!");
								
										JButton ContinuarJogando = new JButton("Continuar");
										ContinuarJogando.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
										ContinuarJogando.setBounds(10, 217, 280, 63);
										panel.add(ContinuarJogando);
										ContinuarJogando.addActionListener(new ActionListener() {
											public void actionPerformed(ActionEvent e) {
												modo = ModoDeJogo.CRIANDOMAPA;
												GameOver.setVisible(false);
												Jogar.setEnabled(false);
												MenuCriacaoMapa.setVisible(true);
												MenuGameplay.setVisible(false);
												for (int i = 0; i < 100; i++) {
													listaBotoesJogador.get(i).setEnabled(true);
													listaBotoesInimigo.get(i).setEnabled(true);
												}
												resetaTabuleiro(listaBotoesJogador, 0);
												resetaTabuleiro(listaBotoesInimigo, -1);
											}
										});
										ContinuarJogando.setFont(new Font("Yu Gothic UI", Font.PLAIN, 15));
										ContinuarJogando.setBackground(new Color(102, 153, 0));
		
				this.MenuGameplay = new JPanel();
				this.MenuGameplay.setBackground(Color.LIGHT_GRAY);
				this.MenuGameplay.setBorder(new MatteBorder(2, 2, 15, 2, (Color) new Color(128, 128, 128)));
				this.MenuGameplay.setBounds(10, 376, 760, 174);
				this.MenuGameplay.setVisible(false);
				contentPane.add(MenuGameplay);
				this.MenuGameplay.setLayout(null);
				
						// BOTAO SAIR
						this.Sair = new JButton("Voltar");
						Sair.setSelectedIcon(null);
						Sair.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						Sair.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								MenuGameplay.setVisible(false);
								modo = ModoDeJogo.CRIANDOMAPA;
								MenuCriacaoMapa.setVisible(true);
								for (int i = 0; i < listaBotoesInimigo.size(); i++) {
									listaBotoesInimigo.get(i).setBackground(new Color(50, 50, 50));
									listaBotoesInimigo.get(i).setIcon(null);
									listaBotoesInimigo.get(i).setBorder(new MatteBorder(1, 1, 1, 1, (Color) Color.DARK_GRAY));
									listaBotoesInimigo.get(i).setEnabled(true);
									listaBotoesJogador.get(i).setEnabled(true);
									listaBotoesJogador.get(i).setIcon(null);

									resetaTabuleiro(listaBotoesInimigo, -1);
									resetaTabuleiro(listaBotoesJogador, 0);
								}
							}
						});
						Sair.setBackground(new Color(255, 100, 100));
						Sair.setBounds(630, 11, 120, 50);
						this.MenuGameplay.add(Sair);
						
								JLabel Texto = new JLabel("Derrube toda a frota Inimiga!");
								Texto.setFont(new Font("Microsoft YaHei Light", Texto.getFont().getStyle(), 25));
								Texto.setHorizontalAlignment(SwingConstants.CENTER);
								Texto.setBounds(10, 104, 740, 59);
								this.MenuGameplay.add(Texto);
								
								this.selecionaTiroAviao = new JButton("");
								selecionaTiroAviao.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent arg0) {
										tipoTiro = 2;
										selecionaTiroAviao.setBackground(new Color(100,200,100));
										selecionaTiroNavio.setBackground(Color.GRAY);
										selecionaTiroSubmarino.setBackground(Color.GRAY);
									}
								});
								selecionaTiroAviao.setBackground(Color.GRAY);
								selecionaTiroAviao.setHorizontalTextPosition(SwingConstants.CENTER);
								selecionaTiroAviao.setIcon(new ImageIcon("src/Aviao01.png"));
								selecionaTiroAviao.setBounds(355, 41, 40, 40);
								MenuGameplay.add(selecionaTiroAviao);
								
								this.selecionaTiroSubmarino = new JButton("");
								selecionaTiroSubmarino.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										tipoTiro = 0;
										selecionaTiroAviao.setBackground(Color.GRAY);
										selecionaTiroNavio.setBackground(Color.GRAY);
										selecionaTiroSubmarino.setBackground(new Color(100,200,100));
									}
								});
								selecionaTiroSubmarino.setIcon(new ImageIcon("src/Submarino01.png"));
								selecionaTiroSubmarino.setHorizontalTextPosition(SwingConstants.CENTER);
								selecionaTiroSubmarino.setBackground(Color.GRAY);
								selecionaTiroSubmarino.setBounds(303, 41, 40, 40);
								MenuGameplay.add(selecionaTiroSubmarino);
								
								this.selecionaTiroNavio = new JButton("");
								selecionaTiroNavio.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										tipoTiro = 1;
										selecionaTiroAviao.setBackground(Color.GRAY);
										selecionaTiroNavio.setBackground(new Color(100,200,100));
										selecionaTiroSubmarino.setBackground(Color.GRAY);
									}
								});
								selecionaTiroNavio.setHorizontalTextPosition(SwingConstants.CENTER);
								selecionaTiroNavio.setIcon(new ImageIcon("src/Navio01.png"));
								selecionaTiroNavio.setBackground(Color.GRAY);
								selecionaTiroNavio.setBounds(408, 41, 40, 40);
								MenuGameplay.add(selecionaTiroNavio);
								
								JLabel textoEscolha = new JLabel("Selecione o tipo de tiro");
								textoEscolha.setHorizontalAlignment(SwingConstants.CENTER);
								textoEscolha.setFont(new Font("Yu Gothic UI Light", Font.BOLD, 12));
								textoEscolha.setBounds(303, 89, 144, 16);
								MenuGameplay.add(textoEscolha);

		this.MenuCriacaoMapa = new JPanel();
		MenuCriacaoMapa.setBorder(new MatteBorder(2, 2, 15, 2, (Color) new Color(128, 128, 128)));
		MenuCriacaoMapa.setBackground(new Color(211, 211, 211));
		MenuCriacaoMapa.setBounds(10, 376, 760, 174);
		contentPane.add(MenuCriacaoMapa);
		MenuCriacaoMapa.setLayout(null);

		// SELECIONANDO A FERRAMENTA DE CRIAÇÃO DE PORTA AVIOES
		JButton SelecionaPortaAviao = new JButton("");
		SelecionaPortaAviao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		SelecionaPortaAviao.setRequestFocusEnabled(false);
		SelecionaPortaAviao.setRolloverEnabled(false);
		SelecionaPortaAviao.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tipo = TipoBotao.PORTAAVIOES;
			}
		});
		seleciona.add(SelecionaPortaAviao);

		this.SalvarArquivo = new JButton("Salvar");
		SalvarArquivo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		SalvarArquivo.setRequestFocusEnabled(false);
		SalvarArquivo.setFont(new Font("Yu Gothic Light", Font.PLAIN, 15));
		SalvarArquivo.setEnabled(false);
		SalvarArquivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					ManipuladorArquivo.escritaArquivo(tabuleiroJogador);
					System.out.println(tabuleiroJogador);
				} catch (IOException e) {
					System.out.println("Arquivo não Encontrado");
				}
			}
		});
		SalvarArquivo.setBounds(496, 11, 120, 50);
		MenuCriacaoMapa.add(SalvarArquivo);
		SalvarArquivo.setBackground(new Color(100, 150, 255));

		this.CarregarArquivo = new JButton("Carregar");
		CarregarArquivo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		CarregarArquivo.setRequestFocusEnabled(false);
		CarregarArquivo.setFont(new Font("Yu Gothic Light", Font.PLAIN, 15));
		CarregarArquivo.setEnabled(false);
		CarregarArquivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String lido = "";
				try {
					lido = ManipuladorArquivo.leituraArquivo();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				saveArquivo = lido;
				resetaTabuleiro(listaBotoesJogador, 0);
				atualizaJogador(-1, tipo);
				
				for(int k = 0; k < seleciona.size(); k++) {
					if(!seleciona.get(k).isEnabled())
						Jogar.setEnabled(true);
					else {
						Jogar.setEnabled(false);
						break;
					}
				}
			}
		});
		CarregarArquivo.setBackground(new Color(150, 100, 255));
		CarregarArquivo.setBounds(496, 72, 120, 50);
		MenuCriacaoMapa.add(CarregarArquivo);
		SelecionaPortaAviao.setBackground(Color.LIGHT_GRAY);
		SelecionaPortaAviao.setBounds(10, 11, 60, 60);
		SelecionaPortaAviao.setIcon(new ImageIcon("src/PortaAviaoIcone.png"));
		MenuCriacaoMapa.add(SelecionaPortaAviao);

		// SELECIONANDO A FERRAMENTA DE CRIAÇÃO DE AVIOES
		JButton SelecionaAviao = new JButton("");
		SelecionaAviao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		SelecionaAviao.setRequestFocusEnabled(false);
		SelecionaAviao.setRolloverEnabled(false);
		SelecionaAviao.setIcon(new ImageIcon("src/AviaoIcone.png"));
		SelecionaAviao.setFont(new Font("Tahoma", Font.PLAIN, 7));
		SelecionaAviao.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tipo = TipoBotao.AVIAO;
			}
		});
		SelecionaAviao.setBackground(Color.LIGHT_GRAY);
		SelecionaAviao.setBounds(80, 11, 60, 60);
		MenuCriacaoMapa.add(SelecionaAviao);
		seleciona.add(SelecionaAviao);

		JLabel EditordeTabuleiro = new JLabel("Editor de Tabuleiro");
		EditordeTabuleiro.setFont(new Font("Microsoft JhengHei Light", EditordeTabuleiro.getFont().getStyle(), 25));
		EditordeTabuleiro.setHorizontalAlignment(SwingConstants.CENTER);
		EditordeTabuleiro.setBounds(10, 113, 740, 50);
		MenuCriacaoMapa.add(EditordeTabuleiro);

		// SELECIONANDO A FERRAMENTA DE CRIAÇÃO DE NAVIOS
		JButton SelecionaNavio = new JButton("");
		SelecionaNavio.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		SelecionaNavio.setRolloverEnabled(false);
		SelecionaNavio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tipo = TipoBotao.NAVIO;
			}
		});
		SelecionaNavio.setBackground(Color.LIGHT_GRAY);
		SelecionaNavio.setBounds(10, 82, 60, 60);
		SelecionaNavio.setIcon(new ImageIcon("src/NavioIcone.png"));
		MenuCriacaoMapa.add(SelecionaNavio);
		seleciona.add(SelecionaNavio);

		// SELECIONANDO A FERRAMENTA DE CRIAÇÃO DE SUBMARINOS
		JButton SelecionaSubmarino = new JButton("");
		SelecionaSubmarino.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		SelecionaSubmarino.setRolloverEnabled(false);
		SelecionaSubmarino.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tipo = TipoBotao.SUBMARINO;
			}
		});
		SelecionaSubmarino.setBackground(Color.LIGHT_GRAY);
		SelecionaSubmarino.setBounds(80, 82, 60, 60);
		SelecionaSubmarino.setIcon(new ImageIcon("src/SubmarinoIcone.png"));
		MenuCriacaoMapa.add(SelecionaSubmarino);
		seleciona.add(SelecionaSubmarino);

		// RESETAR O TABULEIRO
		this.ResetaTabuleiro = new JButton("Resetar");
		ResetaTabuleiro.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		ResetaTabuleiro.setRequestFocusEnabled(false);
		ResetaTabuleiro.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 15));
		ResetaTabuleiro.setEnabled(false);
		ResetaTabuleiro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Jogar.setEnabled(false);
				resetaTabuleiro(listaBotoesJogador, 0);
			}
		});
		ResetaTabuleiro.setBounds(630, 11, 120, 50);
		ResetaTabuleiro.setBackground(new Color(255, 100, 100));
		MenuCriacaoMapa.add(ResetaTabuleiro);

		// COMEÇAR A JOGAR
		this.Jogar = new JButton("JOGAR");
		Jogar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		Jogar.setEnabled(false);
		Jogar.setRequestFocusEnabled(false);
		Jogar.setFont(new Font("Yu Gothic UI", Font.PLAIN, 16));
		Jogar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				modo = ModoDeJogo.JOGANDO;
				MenuCriacaoMapa.setVisible(false);
				MenuGameplay.setVisible(true);
				Sair.setEnabled(true);
				resetaTabuleiro(listaBotoesInimigo, -1);
				geraTabuleiroAleatorio(listaBotoesInimigo);
				vida = vidaInimigo = 0;
				for (int i = 0; i < listaBotoesInimigo.size(); i++) {
					listaBotoesInimigo.get(i).setBackground(new Color(50, 50, 50));
					listaBotoesInimigo.get(i).setIcon(null);
					listaBotoesInimigo.get(i).setBorder(new MatteBorder(1, 1, 1, 1, (Color) Color.DARK_GRAY));
					if (listaBotoesJogador.get(i).getTipo().getValor() != 0)
						vida++;
					if (listaBotoesInimigo.get(i).getTipo().getValor() != 0)
						vidaInimigo++;
					if (i == listaBotoesJogador.size() - 1)
						System.out.println("Vida do jogador = " + vida + " Vida do Inimigo = " + vidaInimigo);
				}
			}
		});

		this.GerarTabuleiro = new JButton("Aleat\u00F3rio");
		GerarTabuleiro.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		GerarTabuleiro.setRequestFocusEnabled(false);
		GerarTabuleiro.setEnabled(false);
		GerarTabuleiro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				resetaTabuleiro(listaBotoesJogador, 0);
				geraTabuleiroAleatorio(listaBotoesJogador);
				Jogar.setEnabled(true);
				tipo = TipoBotao.AGUA;
				for (int i = 0; i < 4; i++) {
					seleciona.get(i).setEnabled(false);
				}
			}
		});
		GerarTabuleiro.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 15));
		GerarTabuleiro.setBackground(new Color(150, 255, 150));
		GerarTabuleiro.setBounds(630, 72, 120, 50);
		MenuCriacaoMapa.add(GerarTabuleiro);
		Jogar.setBackground(new Color(150, 255, 100));
		Jogar.setBounds(280, 11, 200, 50);
		MenuCriacaoMapa.add(Jogar);

		JPanel TabuleiroInimigo = new JPanel();
		TabuleiroInimigo.setBorder(null);
		TabuleiroInimigo.setBackground(Color.WHITE);
		TabuleiroInimigo.setBounds(445, 40, 320, 320);
		contentPane.add(TabuleiroInimigo);
		TabuleiroInimigo.setLayout(new GridLayout(10, 10, 0, 0));

		// Botões do tabuleiro do Inimigo
		for (int i = 0; i < 100; i++) {
			this.listaBotoesInimigo.add(new BotaoTabuleiro(""));
			this.listaBotoesInimigo.get(i).setEnabled(true);

			this.listaBotoesInimigo.get(i).setBorder(new MatteBorder(1, 1, 1, 1, (Color) Color.DARK_GRAY));
			this.listaBotoesInimigo.get(i).setBackground(new Color(50, 50, 50));
			this.listaBotoesInimigo.get(i).setEnabled(false);
			TabuleiroInimigo.add(this.listaBotoesInimigo.get(i));
			this.listaBotoesInimigo.get(i).addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (modo == ModoDeJogo.JOGANDO) {
						if (((BotaoTabuleiro) arg0.getSource()).getTipo().getValor() != 5) {
			
							if (((BotaoTabuleiro) arg0.getSource()).getTipo().getValor() > 0) {
								vidaInimigo--;
								System.out.println(((BotaoTabuleiro) arg0.getSource()).getTipo().getValor());
								if (vidaInimigo <= 0) {
									Sair.setEnabled(false);
									modo = ModoDeJogo.MENU;
									selecionaTiroNavio.setEnabled(false);
									selecionaTiroAviao.setEnabled(false);
									selecionaTiroSubmarino.setEnabled(false);
									for (int i = 0; i < listaBotoesInimigo.size(); i++) {
										listaBotoesInimigo.get(i).setEnabled(false);
										listaBotoesJogador.get(i).setEnabled(false);
									}
									Vitoria.setVisible(true);
								}
							}
							if(tipoTiro == 0) {
								((BotaoTabuleiro) arg0.getSource()).atirar();
							}else {
								for(int i = 0; i < listaBotoesInimigo.size(); i++) {
									if (listaBotoesInimigo.get(i) == ((BotaoTabuleiro) arg0.getSource())){
										if(tipoTiro == 1) {
											listaBotoesInimigo.get(i).atirar();
											if (((i%10) + 1) < 10 && listaBotoesInimigo.get(i + 1).getTipo().getValor() < 5) {
												if(listaBotoesInimigo.get(i + 1).getTipo().getValor() > 0 && listaBotoesInimigo.get(i + 1).getTipo().getValor() < 5)
													vidaInimigo--;
												listaBotoesInimigo.get(i + 1).atirar();
											}
											if (((i%10) + 2) < 10 && listaBotoesInimigo.get(i + 2).getTipo().getValor() < 5) {
												if(listaBotoesInimigo.get(i + 2).getTipo().getValor() > 0 && listaBotoesInimigo.get(i + 2).getTipo().getValor() < 5)
													vidaInimigo--;
												listaBotoesInimigo.get(i + 2).atirar();
											}
										}
										
										if(tipoTiro == 2) {
											
											listaBotoesInimigo.get(i).atirar();
											if (((i%10) + 1) < 10 && listaBotoesInimigo.get(i + 1).getTipo().getValor() < 5) {
												if(listaBotoesInimigo.get(i + 1).getTipo().getValor() > 0 && listaBotoesInimigo.get(i + 1).getTipo().getValor() < 5)
													vidaInimigo--;
												listaBotoesInimigo.get(i + 1).atirar();
											}
											if (((i%10) - 1) >= 0 && listaBotoesInimigo.get(i - 1).getTipo().getValor() < 5) {
												if(listaBotoesInimigo.get(i - 1).getTipo().getValor() > 0 && listaBotoesInimigo.get(i - 1).getTipo().getValor() < 5)
													vidaInimigo--;
												listaBotoesInimigo.get(i - 1).atirar();
											}
											if ((i + 10) < 99 && listaBotoesInimigo.get(i + 10).getTipo().getValor() < 5) {
												if(listaBotoesInimigo.get(i + 10).getTipo().getValor() > 0 && listaBotoesInimigo.get(i + 10).getTipo().getValor() < 5)
													vidaInimigo--;
												listaBotoesInimigo.get(i + 10).atirar();
											}
											if (((i - 10)) > 0 && listaBotoesInimigo.get(i - 10).getTipo().getValor() < 5) {
												if(listaBotoesInimigo.get(i - 10).getTipo().getValor() > 0 && listaBotoesInimigo.get(i - 10).getTipo().getValor() < 5)
													vidaInimigo--;
												listaBotoesInimigo.get(i - 10).atirar();
											}
										}
									}
								}
								if (vidaInimigo <= 0) {
									Sair.setEnabled(false);
									modo = ModoDeJogo.MENU;
									for (int i = 0; i < listaBotoesInimigo.size(); i++) {
										listaBotoesInimigo.get(i).setEnabled(false);
										listaBotoesJogador.get(i).setEnabled(false);
									}
									selecionaTiroNavio.setEnabled(false);
									selecionaTiroAviao.setEnabled(false);
									selecionaTiroSubmarino.setEnabled(false);
									Vitoria.setVisible(true);
								}
							}
							
							tiroInimigo();
							selecionaTiroAviao.setEnabled(false);
							selecionaTiroSubmarino.setEnabled(false);
							selecionaTiroNavio.setEnabled(false);
							selecionaTiroAviao.setBackground(Color.GRAY);
							selecionaTiroNavio.setBackground(Color.GRAY);
							selecionaTiroSubmarino.setBackground(Color.GRAY);
							
							tipoTiro = 0;
							
							if(tipoTiro == 0) selecionaTiroSubmarino.setBackground(new Color(100,200,100));
							if(tipoTiro == 1) selecionaTiroNavio.setBackground(new Color(100,200,100));
							if(tipoTiro == 2) selecionaTiroAviao.setBackground(new Color(100,200,100));
							
							for(int k = 0; k < 100; k++) {
								if(listaBotoesJogador.get(k).getTipo() == TipoBotao.AVIAO)
									{selecionaTiroAviao.setEnabled(true);}
								if(listaBotoesJogador.get(k).getTipo() == TipoBotao.SUBMARINO)
									{selecionaTiroSubmarino.setEnabled(true);}
								if(listaBotoesJogador.get(k).getTipo() == TipoBotao.NAVIO)
									{selecionaTiroNavio.setEnabled(true);}
							}
							if (vida <= 0) {
								modo = ModoDeJogo.MENU;
								Sair.setEnabled(false);
								for (int i = 0; i < listaBotoesInimigo.size(); i++) {
									listaBotoesInimigo.get(i).setEnabled(false);
									listaBotoesJogador.get(i).setEnabled(false);
								}
								selecionaTiroNavio.setEnabled(false);
								selecionaTiroAviao.setEnabled(false);
								selecionaTiroSubmarino.setEnabled(false);
								GameOver.setVisible(true);
							}
						}
					}
				}
			});
		}

		JPanel TabuleiroJogador = new JPanel();
		TabuleiroJogador.setBounds(40, 40, 320, 320);
		TabuleiroJogador.setBackground(Color.WHITE);
		TabuleiroJogador.setBorder(null);
		contentPane.add(TabuleiroJogador);
		TabuleiroJogador.setLayout(new GridLayout(10, 10, 0, 0));

		// Botões do tabuleiro do Jogador
		for (int i = 0; i < 100; i++) {
			this.listaBotoesJogador.add(new BotaoTabuleiro(""));
			// this.listaBotoesJogador.get(i).setIcon(new ImageIcon("src/Agua3.png"));
			this.listaBotoesJogador.get(i).setBorder(new MatteBorder(1, 1, 1, 1, new Color(60, 120, 200)));
			this.listaBotoesJogador.get(i).setBackground(new Color(50, 100, 200));
			this.listaBotoesJogador.get(i).setEnabled(false);
			TabuleiroJogador.add(this.listaBotoesJogador.get(i));

			this.listaBotoesJogador.get(i).addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					for (int i = 0; i < 100; i++) {
						if (listaBotoesJogador.get(i).equals(((BotaoTabuleiro) arg0.getSource()))) {
							if (tipo.getValor() > 0) {
								atualizaJogador(i, tipo);
								
							}
							for(int k = 0; k < seleciona.size(); k++) {
								if(!seleciona.get(k).isEnabled())
									Jogar.setEnabled(true);
								else {
									Jogar.setEnabled(false);
									break;
								}
							}
						}
					}
				}
			});
			this.listaBotoesJogador.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent arg0) {
					// ((BotaoTabuleiro)
					// arg0.getSource()).setBorder(UIManager.getBorder("Button.border"));
				}
			});
			this.listaBotoesJogador.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseExited(MouseEvent arg0) {
					// ((BotaoTabuleiro) arg0.getSource()).setBorder(null);
				}
			});
		}

		JPanel NumerosJogador = new JPanel();
		NumerosJogador.setBorder(new MatteBorder(1, 1, 1, 3, (Color) new Color(128, 128, 128)));
		NumerosJogador.setBackground(new Color(200, 200, 200));
		NumerosJogador.setBounds(10, 40, 30, 320);
		contentPane.add(NumerosJogador);
		NumerosJogador.setLayout(new GridLayout(10, 0, 0, 0));

		for (int i = 0; i < 10; i++) {
			this.listaCoordenada.add(new JLabel("" + (i + 1)));
			this.listaCoordenada.get(i).setHorizontalAlignment(SwingConstants.CENTER);
			NumerosJogador.add(this.listaCoordenada.get(i));
		}

		JPanel NumerosInimigo = new JPanel();
		NumerosInimigo.setBorder(new MatteBorder(1, 1, 1, 3, (Color) new Color(128, 128, 128)));
		NumerosInimigo.setBackground(new Color(200, 200, 200));
		NumerosInimigo.setBounds(417, 40, 30, 320);
		contentPane.add(NumerosInimigo);
		NumerosInimigo.setLayout(new GridLayout(10, 0, 0, 0));

		for (int i = 0; i < 10; i++) {
			this.listaCoordenada.add(new JLabel("" + (i + 1)));
			this.listaCoordenada.get(i + 10).setHorizontalAlignment(SwingConstants.CENTER);
			NumerosInimigo.add(this.listaCoordenada.get(i + 10));
		}

		JPanel LetrasInimigo = new JPanel();
		LetrasInimigo.setBorder(new MatteBorder(1, 1, 3, 1, (Color) new Color(128, 128, 128)));
		LetrasInimigo.setBackground(new Color(200, 200, 200));
		LetrasInimigo.setBounds(445, 11, 320, 30);
		contentPane.add(LetrasInimigo);
		LetrasInimigo.setLayout(new GridLayout(0, 10, 0, 0));

		for (int i = 0; i < 10; i++) {
			this.listaCoordenada.add(new JLabel("" + (char) (i + 65)));
			this.listaCoordenada.get(i + 20).setHorizontalAlignment(SwingConstants.CENTER);
			LetrasInimigo.add(this.listaCoordenada.get(i + 20));
		}

		JPanel LetrasJogador = new JPanel();
		LetrasJogador.setBorder(new MatteBorder(1, 1, 3, 1, (Color) new Color(128, 128, 128)));
		LetrasJogador.setBackground(new Color(200, 200, 200));
		LetrasJogador.setBounds(40, 11, 320, 30);
		contentPane.add(LetrasJogador);
		LetrasJogador.setLayout(new GridLayout(0, 10, 0, 0));

		for (int i = 0; i < 10; i++) {
			this.listaCoordenada.add(new JLabel("" + (char) (i + 65)));
			this.listaCoordenada.get(i + 30).setHorizontalAlignment(SwingConstants.CENTER);
			LetrasJogador.add(this.listaCoordenada.get(i + 30));
		}
	}

}
