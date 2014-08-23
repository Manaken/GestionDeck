package vue.carte;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import single.Singleton;

import business.Carte;
import javax.swing.JTextArea;
import java.awt.Color;
import javax.swing.UIManager;

public class VisuCarte extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VisuCarte frame = new VisuCarte(Carte.rechercheCarte("Emrakul, the aeons torn"));
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
	public VisuCarte(Carte carte) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 647, 471);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		final JLabel lblImgCartePrinc = new JLabel();
		lblImgCartePrinc.setBounds(33, 11, 223, 310);
		lblImgCartePrinc.setIcon(Singleton.getInstance().getImage(carte, 0));
		contentPane.add(lblImgCartePrinc);
		
		JLabel lblNomcarte = new JLabel(carte.getName());
		lblNomcarte.setBounds(297, 11, 162, 20);
		contentPane.add(lblNomcarte);
		
		JTextArea txtrDescription = new JTextArea();
		txtrDescription.setWrapStyleWord(true);
		txtrDescription.setBackground(UIManager.getColor("Button.background"));
		txtrDescription.setForeground(Color.BLACK);
		txtrDescription.setLineWrap(true);
		txtrDescription.setText(carte.getText());
		txtrDescription.setBounds(297, 92, 300, 112);
		contentPane.add(txtrDescription);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(297, 42, 162, 20);
		contentPane.add(lblNewLabel);
	}
}
