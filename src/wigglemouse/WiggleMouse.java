package wigglemouse;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Mouse wiggle program. Useful for when you want to be away from your computer
 * however appear to be available. <br>
 * 
 * @author Brian Perel
 *
 */
public class WiggleMouse extends KeyAdapter implements ActionListener {

	private JFrame frame;
	JButton btnStart;
	static Robot robot;
	static int x, y, timeToWait;
	String[] choices = { "1/2 minute", "1 minute", "3 minutes", "5 minutes" };
	
	// need to create a combo box in two lines due to SwingBuilder's design tab problem workaround
	final JComboBox<String> timeOptionsComboBox = new JComboBox<>(new DefaultComboBoxModel<>(choices));

	/**
	 * Launch the application.
	 * @throws InterruptedException	thrown if thread is interrupted while building the apps window and frame
	 */
	public static void main(String[] args) throws InterruptedException {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					WiggleMouse window = new WiggleMouse();
					window.frame.setVisible(true);
					window.frame.setTitle("Mouse move by B. Perel");
					window.frame.setResizable(false);
					window.frame.setLocationRelativeTo(null);
					robot = new Robot();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		timeToWait = 3000;
		moveMouse();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private WiggleMouse() {
		
		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 14));
		frame.setBounds(100, 100, 485, 182);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		btnStart = new JButton("SET TIME");
		btnStart.setFont(new Font("Book Antiqua", Font.ITALIC, 12));
		btnStart.setBounds(198, 78, 99, 23);
		frame.getContentPane().add(btnStart);
		btnStart.addActionListener(this);
		btnStart.addKeyListener(this);

		JLabel lblDisplayMessage = new JLabel("Time to wait before wiggling your mouse (in minutes):");
		lblDisplayMessage.setFont(new Font("Narkisim", Font.PLAIN, 15));
		lblDisplayMessage.setBounds(22, 23, 344, 23);
		frame.getContentPane().add(lblDisplayMessage);

		timeOptionsComboBox.setBounds(351, 23, 93, 22);
		frame.getContentPane().add(timeOptionsComboBox);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == btnStart) {
			switch ((String) timeOptionsComboBox.getSelectedItem()) {
			case "1/2 minute":
				timeToWait = 3000;
				break;

			case "1 minute":
				timeToWait = 60000;
				break;

			case "3 minutes":
				timeToWait = 180000;
				break;

			case "5 minutes":
				timeToWait = 300000;
				break;
			
			default:
				break;
			}
		}
	}
	
	/**
	 * This is responsible for listening to all keyboard keys input
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getSource() == btnStart) {
			switch ((String) timeOptionsComboBox.getSelectedItem()) {
			case "1/2 minute":
				timeToWait = 3000;
				break;

			case "1 minute":
				timeToWait = 60000;
				break;

			case "3 minutes":
				timeToWait = 180000;
				break;

			case "5 minutes":
				timeToWait = 300000;
				break;
			
			default:
				break;
			}
		}
	}

	/**
	 * performs mouse movement action
	 * @throws InterruptedException thrown if thread is interrupted while performing thread sleep operation
	 */
	public static void moveMouse() throws InterruptedException {
		while (true) {
			// get current mouse pointer coordinates and set them. This needs to be done
			// twice in loop so that if user moves mouse the pointer doesn't jump back to original point
			setMouseLocation();
			// time to wait before next mouse move
			Thread.sleep(timeToWait);
			setMouseLocation();

			// move the mouse to specified x,y coordinates with a shift value -- Wiggle
			// Mouse action
			robot.mouseMove(x, y + 1);
			Thread.sleep(50);
			robot.mouseMove(x, y - 1);
			Thread.sleep(50);
			robot.mouseMove(x, y);
		}
	}

	/**
	 * sets the current mouse's position into variables (x, y)
	 */
	public static void setMouseLocation() {
		x = (int) MouseInfo.getPointerInfo().getLocation().getX();
		y = (int) MouseInfo.getPointerInfo().getLocation().getY();
	}
}
