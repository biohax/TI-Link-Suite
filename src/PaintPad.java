/**
 * 
 */

/**
 * @author Karel Dhondt (biohax)
 * 
 *         Description : PaintPad is a basic paint wrapped in a JComponent
 * 
 */
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PaintPad extends JComponent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage image;
	private BufferedImage preview;

	private int tool;
	private double scale = 1;

	private boolean firstPolygon = true;
	private boolean initialize = true;

	private String text;

	private Point startDrag, endDrag;

	final static int TOOL_PEN = 0;
	final static int TOOL_LINE = 1;
	final static int TOOL_RECT = 2;
	final static int TOOL_OVAL = 3;
	final static int TOOL_POLYGON = 4;
	final static int TOOL_ERASER = 5;
	final static int TOOL_TEXT = 6;
	
	private Color color;
	private int size;

	public PaintPad(int x, int y)
	{

		BufferedImage temp = new BufferedImage(x, y, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = temp.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, temp.getWidth(), temp.getHeight());
		g.dispose();
		
		init(temp);
	}

	public PaintPad(BufferedImage image)
	{
		init(image);
	}
	
	public void init(BufferedImage image)
	{
		super.setBackground(Color.green);
		setMinimumSize(new Dimension(image.getWidth(), image.getHeight()));
		setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
		
		setColor(Color.BLACK);
		this.image = image;
		this.preview = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = preview.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		addListeners();
	}
	
	public void setColor(Color c)
	{
		this.color = c;
	}

	private void addListeners()
	{

		this.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{

				if (tool != TOOL_POLYGON || (tool == TOOL_POLYGON && firstPolygon))
				{
					startDrag = new Point((int) (e.getX() / scale), (int) (e.getY() / scale));
					firstPolygon = false;
				}

				if (tool == TOOL_TEXT)
				{
					text = JOptionPane.showInputDialog("Enter text:");
					// graphics2D.drawString(text, (int) (e.getX()/scale), (int)
					// (e.getY()/scale));
					repaint();
				}

			}

			public void mouseReleased(MouseEvent e)
			{

				endDrag = new Point((int) (e.getX() / scale), (int) (e.getY() / scale));
				submitCurrentTool();
				
			}

			// repaint();
			// }
		});

		// this.addMouseMotionListener(new MouseMotionAdapter() {
		// public void mouseDragged(MouseEvent e) {
		// endDrag = new Point((int) (e.getX()/scale), (int) (e.getY()/scale));
		//
		// if (graphics2D != null){
		// switch(tool){
		// case TOOL_PEN:
		// // omg dit is lelijk
		// graphics2D.drawLine(startDrag.x, startDrag.y, endDrag.x, endDrag.y);
		// startDrag=endDrag;
		// break;
		//
		//
		// case TOOL_ERASER:
		//
		// graphics2D.fillRect(startDrag.x, startDrag.y, 2, 2);
		// startDrag=endDrag;
		// break;
		//
		// }
		// repaint();
		//
		// }

		// }
		//
		// public void mouseMoved(MouseEvent e){
		//
		//
		//
		// if(tool==TOOL_POLYGON){
		// endDrag = new Point((int) (e.getX()/scale), (int) (e.getY()/scale));
		// repaint();
		//
		// }
		//
		//
		// }
		//
		//
		// });
		
		
		// mijn logica hierboven mag terug zeker? nee, herschrijf dat, want niets is nog zoals vroeger
		// het enige wat ge moet doen is repaint allen en endDrag updaten
		// zelf tekenen in die dingen moet niet meer, omwille van die dubbele buffers
		// dus, ja, denk gewoon goed na wat ge doet. I guess dat het nu wel zal verder lukken.
		// Ik moet weg. Success er mee :)
		
		// merci :)
		
		//Kak mijn batterijen zijn plat :p
		// ahzo, oké, ja, maja, het gaat werken...
		
		addMouseMotionListener(new MouseMotionAdapter()
		{
			public void mouseDragged(MouseEvent e)
			{
				endDrag = new Point((int) (e.getX()/scale), (int) (e.getY()/scale));
				repaint();
			}
		});
	}

	public void paintComponent(Graphics gg)
	{
		super.paintComponent(gg);
		
		Graphics2D g = preview.createGraphics();
		// stap 1: gebruik de laatste echte buffer, ter constructie van de
		// nieuwe previeuw buffer
		
		scale = ((double) getWidth() / (double) image.getWidth(null));
		g.drawImage(image, 0, 0, null);

		if (startDrag != null && endDrag != null)
		{
			g.setPaint(this.color);
			g.setStroke(new BasicStroke(this.size));
			switch (tool)
			{

			case TOOL_LINE:
				g.drawLine(startDrag.x, startDrag.y, endDrag.x, endDrag.y);
				break;

			case TOOL_RECT:
				g.drawRect(Math.min(startDrag.x, endDrag.x), Math.min(startDrag.y, endDrag.y), Math.abs(startDrag.x - endDrag.x), Math.abs(startDrag.y - endDrag.y));
				break;

			case TOOL_OVAL:
				g.drawOval(Math.min(startDrag.x, endDrag.x), Math.min(startDrag.y, endDrag.y), Math.abs(startDrag.x - endDrag.x), Math.abs(startDrag.y - endDrag.y));
				break;

			case TOOL_POLYGON:
				g.drawLine(startDrag.x, startDrag.y, endDrag.x, endDrag.y);
				break;

			}

		}
		
		g.dispose();
		gg.drawImage(preview, 0, 0, (int) (preview.getWidth() * scale), (int) (preview.getHeight() * scale), null);
		gg.dispose();
	}

	public void clear()
	{
		Graphics2D g = image.createGraphics();
		g.setPaint(Color.white);
		g.fillRect(0, 0, getSize().width, getSize().height);
		g.setPaint(Color.black);
		g.dispose();
	}
	
	private void submitCurrentTool()
	{
		clear();
		Graphics2D g = image.createGraphics();
		g.drawImage(preview, 0, 0, null);
		g.dispose();
		repaint();
	}

	private void cursorchange()
	{
		if (tool == TOOL_ERASER)
		{
			URL imageurl = this.getClass().getResource("/resources/eraser.png");
			Image image = Toolkit.getDefaultToolkit().getImage(imageurl);
			Point hotSpot = new Point(0, 0);
			Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(image, hotSpot, "Eraser");
			setCursor(cursor);
			setColor(Color.WHITE);
		} else
		{
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			setColor(Color.BLACK);

		}
	}

	public void setSize(int size)
	{
		this.size = size;
	}

	public void setTool(int tool)
	{

		// reset points
		startDrag = null;
		endDrag = null;

		this.tool = tool;

		if (tool == TOOL_POLYGON)
		{
			firstPolygon = true;
		}

		setDoubleBuffered(false);
		cursorchange();

	}

	public BufferedImage getImage()
	{
		return preview;

	}

	public JPanel getTools()
	{
		JPanel tools = new JPanel();
		// tools.setLayout(new BoxLayout(tools, BoxLayout.PAGE_AXIS));

		JButton clearButton = new JButton("Clear");
		tools.add(clearButton);
		clearButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				PaintPad.this.clear();
			}
		});

		JButton eraseButton = new JButton("Eraser");
		tools.add(eraseButton);
		eraseButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				PaintPad.this.setTool(PaintPad.TOOL_ERASER);
				;
			}
		});

		JButton rectButton = new JButton("Rectangle");
		tools.add(rectButton);
		rectButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				PaintPad.this.setTool(PaintPad.TOOL_RECT);
			}
		});

		JButton ovalButton = new JButton("Oval");
		tools.add(ovalButton);
		ovalButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				PaintPad.this.setTool(PaintPad.TOOL_OVAL);

			}
		});

		JButton penButton = new JButton("Pencil");
		tools.add(penButton);
		penButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				PaintPad.this.setTool(PaintPad.TOOL_PEN);
			}
		});

		JButton lineButton = new JButton("Line");
		tools.add(lineButton);
		lineButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				PaintPad.this.setTool(PaintPad.TOOL_LINE);
			}
		});

		JButton polygonButton = new JButton("Polygon");
		tools.add(polygonButton);
		polygonButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				PaintPad.this.setTool(PaintPad.TOOL_POLYGON);
			}
		});

		JButton textButton = new JButton("Text");
		tools.add(textButton);
		textButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				PaintPad.this.setTool(PaintPad.TOOL_TEXT);
				;
			}
		});

		final JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 16, 1);
		tools.add(slider);
		slider.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent event)
			{
				PaintPad.this.setSize(slider.getValue());
			}
		});

		return tools;

	}

}