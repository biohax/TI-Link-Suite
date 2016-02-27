import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.Component;

import javax.swing.Box;

import net.miginfocom.swing.MigLayout;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;






public class ConnectionDialog extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -410578299773455674L;
	JComboBox<String> comboBox;

	/**
	 * Create the frame.
	 */
	
	public ConnectionDialog()
	{
		setTitle("TI Connect");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 140);
		getContentPane().setLayout(new MigLayout("", "[][grow][]", "[][]"));
		
		Component verticalStrut = Box.createVerticalStrut(20);
		getContentPane().add(verticalStrut, "cell 1 0");
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(30);
		getContentPane().add(horizontalStrut_1, "cell 0 1");
		
		comboBox = new JComboBox<>();
		

		getContentPane().add(comboBox, "flowx,cell 1 1,growx");
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Main.establishConnection((String) comboBox.getSelectedItem());
				dispose();
			}
		});

		getContentPane().add(btnConnect, "cell 1 1");
		
		Component horizontalStrut = Box.createHorizontalStrut(30);
		getContentPane().add(horizontalStrut, "cell 2 1");
	}
	
	public void setSerialPorts(String[] ports){
		//serialList.setListData(ports.toArray());
		
		for (String port: ports){
			comboBox.addItem(port);
			//TODO usb
		}
	}

	

}
