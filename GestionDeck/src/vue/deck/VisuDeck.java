package vue.deck;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import single.Singleton;

import business.Deck;

public class VisuDeck extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	JLabel lblNomDeck;
	JLabel lblNoir;
	JLabel lblBleu;
	JLabel lblBlanc;
	JLabel lblRouge;
	JLabel lblVert;
	JLabel lblCartePrincip;
	JLabel lblDescription;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VisuDeck frame = new VisuDeck(new Deck());
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VisuDeck(Deck deck) {
		setBounds(100, 100, 568, 639);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		this.lblNomDeck = new JLabel(deck.getNomDeck());
		lblNomDeck.setBounds(101, 11, 270, 28);
		contentPane.add(lblNomDeck);


		if(deck.getCartePrincipale() != null) {
			ImageIcon image = Singleton.getInstance().getImage(deck.getCartePrincipale(), 0);
			this.lblCartePrincip = new JLabel(image);
		} else {

			this.lblCartePrincip = new JLabel();
		}
		lblCartePrincip.setBounds(142, 45, 262, 362);
		contentPane.add(lblCartePrincip);

		this.lblDescription = new JLabel(deck.getDescription());
		lblDescription.setVerticalAlignment(SwingConstants.TOP);
		lblDescription.setBounds(70, 433, 431, 137);
		lblDescription.setAlignmentY(TOP_ALIGNMENT);
		contentPane.add(lblDescription);

		JButton btnRafraichirDeck = new JButton("Roll");
		btnRafraichirDeck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Deck deck = Deck.rechercherDeckRandom();
				VisuDeck visuDeck = new VisuDeck(deck);
				visuDeck.setVisible(true);
				dispose();

			}
		});
		btnRafraichirDeck.setBounds(9, 13, 82, 25);
		contentPane.add(btnRafraichirDeck);


		/*if (deck.getCouleurs() != null && !deck.getCouleurs().isEmpty()) {
			if (deck.getCouleurs().charAt(0) == '1') {
				lblBlanc = new JLabel(new ImageIcon(getClass().getResource("/ManaBlanc.png")));
				lblBlanc.setBounds(449, 11, 24, 28);
				contentPane.add(lblBlanc);
			}


			if (deck.getCouleurs().charAt(1) == '1') {
				lblBleu = new JLabel(new ImageIcon(getClass().getResource("/ManaBleu.png")));
				lblBleu.setBounds(415, 11, 24, 28);
				contentPane.add(lblBleu);
			}
			if (deck.getCouleurs().charAt(2) == '1') {
				lblVert = new JLabel(new ImageIcon(getClass().getResource("/ManaVert.png")));
				lblVert.setBounds(518, 11, 24, 28);
				contentPane.add(lblVert);
			}
			if (deck.getCouleurs().charAt(3) == '1') {
				lblNoir = new JLabel(new ImageIcon(getClass().getResource("/ManaNoir.png")));
				lblNoir.setBounds(381, 11, 24, 28);
				contentPane.add(lblNoir);
			}
			if (deck.getCouleurs().charAt(4) == '1') {
				lblRouge = new JLabel(new ImageIcon(getClass().getResource("/ManaRouge.png")));
				lblRouge.setBounds(483, 11, 24, 28);
				contentPane.add(lblRouge);
			}
		}*/
	}


}
