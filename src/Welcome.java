import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Welcome extends JFrame
{
	private JLabel welcomeL, myLabel;
	private JButton welcomeB;
	private WelcomeButtonHandler wbHandler;
	private ImageIcon bgimg;

	public Welcome(){
      bgimg = new ImageIcon("images/ug.jpg");
      myLabel = new JLabel(bgimg);
      myLabel.setSize(350, 400);

		welcomeL = new JLabel("Welcome to our App", SwingConstants.CENTER);
		welcomeL.setFont(new Font("Serif", Font.BOLD, 16));
		welcomeB = new JButton("Let's begin.");
		Color bg = new Color(0x87ceeb);
		welcomeB.setBackground(bg);
		wbHandler = new WelcomeButtonHandler();
		welcomeB.addActionListener(wbHandler);

		setTitle("Welcome page");

		Container pane = getContentPane();

		pane.setLayout(null);
		welcomeL.setLocation(80,100);
		welcomeL.setSize(150,35);
		welcomeB.setLocation(70,200);
		welcomeB.setSize(200,45);


		myLabel.add(welcomeL);
		myLabel.add(welcomeB);

		pane.add(myLabel);

		String workdr = System.getProperty("user.dir");
		System.out.println("Current working directory : " + workdr);

		setSize(350, 400);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private class WelcomeButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e){
			dispose();
			new Activity();
		}
	}

	public static void main(String[] args){
		new Welcome();
	}
}

