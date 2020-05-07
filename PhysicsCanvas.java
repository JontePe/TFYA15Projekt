package fysiktest;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.io.IOException;

import javax.swing.JFrame;

import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PhysicsCanvas extends Canvas implements Runnable {

	private boolean running;
	private Particle p1;
	private Particle p2;
	
	static JButton chargeUp;
	static JButton chargeDown;
	static JButton shoot;
	
	Color myGreen = new Color(25, 105, 45);
	
	static int chargeLvl = 1;

	public PhysicsCanvas() {
		Dimension d = new Dimension(1000, 800);
		setPreferredSize(d);
		setMinimumSize(d);
		setMaximumSize(d);

		p1 = new Particle(50, 250, 20, 10, Color.RED);
		// p2 = new Particle(550, 350, 20, Color.BLUE);
		

	}

	@Override
	public void run() {
		while (running) {
			update();
			render();

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
				running = false;
			}
		}
	}

	public void start() {
		if (!running) {
			Thread t = new Thread(this);
			createBufferStrategy(3);
			running = true;
			t.start();
		}
	}

	private void render() {
		BufferStrategy strategy = getBufferStrategy();
		Graphics2D g = (Graphics2D) strategy.getDrawGraphics();

		g.setColor(Color.CYAN);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(myGreen);
		g.fillRect(0, 650, getWidth(), getHeight());

		p1.render(g);
		// p2.render(g);

		strategy.show();
	}

	private void update() {
		p1.update();
		// p2.update();
		
	}
	
	public static void buttonFunction() {
		
		int maxCharge = 10;
		int lowCharge = 1;
		
		chargeUp = new JButton("Charge Up");
		chargeUp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(chargeLvl >= lowCharge && chargeLvl < maxCharge) {
					chargeLvl++;
					System.out.println("Charge: " + chargeLvl);
				}
					
			}
			
		});
		chargeDown = new JButton("Charge Down");
		chargeDown.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(chargeLvl > lowCharge && chargeLvl <= maxCharge) {
					chargeLvl--;
					System.out.println("Charge: " + chargeLvl);
				}
			}
			
		});
		shoot = new JButton("Shoot");
		shoot.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
			
		});
	}

	public static void main(String[] args) {
		
		JFrame myFrame = new JFrame("My physics canvas");
		buttonFunction();
		chargeDown.setBounds(20, 20, 100, 50);
		chargeUp.setBounds(130, 20, 100, 50);
		shoot.setBounds(240, 20, 100, 50);
		myFrame.add(chargeDown);
		myFrame.add(chargeUp);
		myFrame.add(shoot);
		PhysicsCanvas physics = new PhysicsCanvas();
		myFrame.add(physics);
		myFrame.pack();
		myFrame.setResizable(false);
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.setVisible(true);
		System.out.println("Charge: " + chargeLvl);
		physics.start();
		myFrame.setLocationRelativeTo(null);
		
	}

}