/**
 * @author Karel Dhondt (biohax)
 * 
 * 
 *         Description : Class that handels the GUI
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import net.biohax.ticonnect.TIConnection;
import net.biohax.ticonnect.titypes.TIPicture;
import net.biohax.ticonnect.titypes.TIProgram;
import net.biohax.ticonnect.titypes.TIString;
import net.biohax.ticonnect.titypes.TIVariable;


public class UserInterface extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2971277610635748955L;
	private JPanel contentPane;
	private static TIVariable currentVar;
	JTextPane textPane;
	PaintPad paint;
	JComboBox<Integer> number;
	
	Integer[] numberArray = {0,1,2,3,4,5,6,7,8,9};


	TIConnection tic;

	/**
	 * Create the frame.
	 */
	public UserInterface(final TIConnection tic)
	{
		this.tic = tic;

		setTitle("TI Link Suite");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setPreferredSize(new Dimension(700, 500));


		contentPane = new JPanel();
		getContentPane().add(contentPane);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				newVar();
			}
		});
		mnFile.add(mntmNew);

		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				open();
			}
		});
		mnFile.add(mntmOpen);

		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				save(currentVar);
			}
		});
		mnFile.add(mntmSave);

		JMenuItem mntmSend = new JMenuItem("Send");
		mntmSend.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				send(currentVar);
			}
		});
		mnFile.add(mntmSend);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);

		JMenu mnList = new JMenu("List");
		mnList.addMenuListener(new MenuListener()
		{
			@Override
			public void menuSelected(MenuEvent arg0)
			{
				final JFrame select = new JFrame("Variables");
				DefaultListModel<TIVariable> defaultListModel = new DefaultListModel<TIVariable>();

				List<TIVariable> list = tic.dirList();

				for (TIVariable v : list)
				{
					defaultListModel.addElement(v);
				}

				System.out.println(list.size());

				final JList<TIVariable> jlist = new JList<TIVariable>(defaultListModel);
				jlist.addMouseListener(new MouseAdapter()
				{
					public void mouseClicked(MouseEvent evt)
					{
						if (evt.getClickCount() == 2)
						{
							setInterface(tic.get((TIVariable) jlist.getSelectedValue()));
							select.dispose();
						}
					}
				});

				select.getContentPane().add(new JScrollPane(jlist), BorderLayout.CENTER);
				select.setAlwaysOnTop(true);
				select.pack();
				select.setLocationRelativeTo(null);
				select.setVisible(true);
			}

			@Override
			public void menuDeselected(MenuEvent arg0)
			{
			}

			@Override
			public void menuCanceled(MenuEvent arg0)
			{
			}
		});

		menuBar.add(mnList);

		JMenu mnOther = new JMenu("Other");
		menuBar.add(mnOther);

		JMenuItem mntmShowScreen = new JMenuItem("Show screen");
		mntmShowScreen.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					TIPicture pic = tic.getScreenShot();
					setInterface(pic);
				} catch (Exception e2)
				{
					e2.printStackTrace();
				}

			}
		});
		mnOther.add(mntmShowScreen);

		JMenuItem mntmKeyboard = new JMenuItem("Keyboard");
		mntmKeyboard.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				// new KeyBoard();
				// tic.sendKeyStroke(11);;
			}
		});
		mnOther.add(mntmKeyboard);

		setInterface(new TIString("karel", 1));
		pack();
		setVisible(true);
	}

	public void setInterface(TIVariable a)
	{

		currentVar = a;

		contentPane.removeAll();
		contentPane.setLayout(new BorderLayout());

		if (a instanceof TIString)
		{
			textPane = new JTextPane();
			number = new JComboBox<Integer>(numberArray);
			
			number.setSelectedItem(((TIString) a).getNumber());
			textPane.setText(((TIString) a).getString());
			
			contentPane.add(new JScrollPane(textPane), BorderLayout.CENTER);
			contentPane.add(number, BorderLayout.EAST);

		} else if (a instanceof TIPicture)
		{

			BufferedImage karel = ((TIPicture) currentVar).getImage();

			paint = new PaintPad(karel);
			
			number.setSelectedItem(((TIPicture) a).getNumber());

			contentPane.add(new JScrollPane(paint.getTools()), BorderLayout.NORTH);
			contentPane.add(paint, BorderLayout.CENTER);
			contentPane.add(number, BorderLayout.EAST);

		} else if(a instanceof TIProgram){
				JTextPane name = new JTextPane();
				JTextPane code = new JTextPane();
				//number = new JComboBox<Integer>(numberArray);
				
				name.setText(new String(((TIProgram) a).getVarname()));
				
				contentPane.add(new JScrollPane(name), BorderLayout.NORTH);
				//contentPane.add(new JScrollPane(code), BorderLayout.NORTH);
				contentPane.add(number, BorderLayout.EAST);

			}
		
		else{
			JOptionPane.showMessageDialog(this, "No valid file!");
		}

		contentPane.validate();
		contentPane.repaint();

	}

	private void open()
	{

		JFileChooser fileChooser = new JFileChooser();

		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			File file = fileChooser.getSelectedFile();
			setInterface(TIVariable.getVar(file));
		}
	}

	private void save(TIVariable var)
	{

		if (var != null)
		{
			JFileChooser fileChooser = new JFileChooser();
			if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
			{
				File file = fileChooser.getSelectedFile();
				updateVar();
				var.toFile(file);
			}
		} else
		{
			JOptionPane.showMessageDialog(this, "No file opened!");
		}
	}

	private void send(TIVariable var)
	{

		if (var != null)
		{
			updateVar();

			try
			{
				tic.send(var);
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else
		{
			JOptionPane.showMessageDialog(this, "No file opened!");
		}
	}

	private void newVar()
	{
		/*String[] list = { "A", "B", "C" };
		JComboBox jcb = new JComboBox(list);
		JOptionPane.showMessageDialog(this, jcb, "Create new variable", JOptionPane.QUESTION_MESSAGE);
		System.out.println(jcb.getSelectedItem());
		*/
		
		// TODO

	}

	private void updateVar()
	{
		Object[] obj = new Object[1];
		if (currentVar instanceof TIString)
		{
			((TIString) currentVar).setNumber(number.getSelectedIndex()); // update number
			obj[0] = textPane.getText(); //update text
		} else if (currentVar instanceof TIPicture)
		{
			((TIPicture) currentVar).setNumber(number.getSelectedIndex()); // update number
			obj[0] = paint.getImage(); //update image
			
			//DEBUG
			JFrame frame = new JFrame("test");
			JPanel mainPanel = new JPanel(new BorderLayout());
			JLabel lblimage = new JLabel(new ImageIcon(paint.getImage()));
			mainPanel.add(lblimage);
			frame.getContentPane().add(mainPanel);
			frame.setVisible(true);

		}


		currentVar.update(obj);
	}

}
