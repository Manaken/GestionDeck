package vue.technique;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class Validation extends JFrame {

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
					Validation frame = new Validation("testTitre", "testMessage");
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
	public Validation(String titre, String message) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 437, 180);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		this.setTitle(titre);
		
		JPanel panel = new JPanel();
		panel.setBounds(5, 5, 411, 132);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JTextArea lblMessage = new JTextArea(message);
		lblMessage.setEditable(false);
		lblMessage.setBounds(0, 0, 411, 132);
		panel.add(lblMessage);
	}

}
