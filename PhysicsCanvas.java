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
import java.awt.Font;

public class PhysicsCanvas extends Canvas implements Runnable {

	public static final int dimX = 1800;
	public static final int dimY = 1000;

	private boolean running;

	private static Particle boll;
	private static Particle klubba;
	final private static int klubbPosX = 200;
	final private static int klubbPosY = 800;
	private static double L = 100; // aka height

	static JButton chargeUp;
	static JButton chargeDown;
	static JButton shoot;
	static JButton restart;
	Color myGreen = new Color(25, 105, 45);
	static int chargeLvl = 1;
	static boolean start = false;

	protected static int i = 0;
	public double vBall;

	Font font = new Font("Comic Sans", Font.BOLD, 20);

	public PhysicsCanvas() {
		Dimension d = new Dimension(dimX, dimY);
		setPreferredSize(d);
		setMinimumSize(d);
		setMaximumSize(d);

		boll = new Particle((klubbPosX + L), (klubbPosY + L), 0.021335, 0.04593, Color.RED);
		klubba = new Particle(klubbPosX, klubbPosY, 0.03, 0.4, Color.BLACK) {
			private double th = Math.PI / 2;
			private double vth = 0; // aka StartVel

			@Override
			public void update() {
				trailX.add((int) getX());
				trailY.add((int) getY());

				if (!(getX() > klubbPosX + 2 * L - L / 50)) {
					vth = vth + (g) * Math.sin(th) * dt;
					th = th + vth * dt;
					setX(klubbPosX + L - L * Math.sin(th));
					setY(klubbPosY - L * Math.cos(th));
				}
				// calc fart

				vBall = (0.45 * chargeLvl) * klubba.velBall(0.4, klubba.endVel(vth, L));
			}
		};

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
		g.fillRect(0, getHeight() - 100, getWidth(), getHeight());
		g.setColor(Color.BLACK);
		g.fillRect(getWidth() - 200, getHeight() - 100, 50, 50);
		g.setFont(font);
		g.drawString("Charge: " + chargeLvl, (dimX / 4 - 50), 50);
		g.setColor(Color.DARK_GRAY);
		g.fillRect(getWidth() - 160, getHeight() - 200, 10, 150);
		g.setColor(Color.MAGENTA);
		g.fillRect(getWidth() - 160, getHeight() - 200, 50, 30);
		g.setColor(Color.YELLOW);
		g.fillOval(dimX - 200, 20, 200, 200);

		boll.render(g);
		klubba.render(g);

		strategy.show();
	}

	private void update() {

		if (start) {
			if (boll.getX() <= klubba.getX() && i == 0) { // bolllkoordinat i yled +/- 5
				i++;
				boll.velUpdate(15, vBall);
			}

			if (i != 0) {

				boll.update();
			}
			klubba.update();
		}
	}

	public static void buttonFunction() {

		int maxCharge = 10;
		int lowCharge = 1;

		chargeUp = new JButton("Charge Up");
		chargeUp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (chargeLvl >= lowCharge && chargeLvl < maxCharge) {
					chargeLvl++;
					System.out.println("Charge: " + chargeLvl);
				}

			}

		});
		chargeDown = new JButton("Charge Down");
		chargeDown.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (chargeLvl > lowCharge && chargeLvl <= maxCharge) {
					chargeLvl--;
					System.out.println("Charge: " + chargeLvl);
				}
			}

		});
		shoot = new JButton("Shoot");
		shoot.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				start = true;
			}

		});

	}

	public static void main(String[] args) {
		JFrame myFrame = new JFrame("GOLFKILLER");
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
