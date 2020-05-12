import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.io.IOException;

import javax.swing.JFrame;


public class PhysicsCanvas extends Canvas implements Runnable {

	public static final int dimX = 800;
	public static final int dimY = 600;

	private boolean running;
	private Particle boll;
	private Particle klubba;
	private boolean hit = false;

	public PhysicsCanvas() {
		Dimension d = new Dimension(dimX, dimY);
		setPreferredSize(d);
		setMinimumSize(d);
		setMaximumSize(d);

		boll = new Particle(400, 400, 0.021335, 0.046, Color.RED);										//0.04593
		klubba = new Particle(300, 300, 0.03, 0.4, Color.BLUE){
				private double L = 100; //aka height
				private double th = Math.PI/2;
				private double vth = 2; //aka StartVel

				@Override
				public void update(){
					trailX.add((int)getY());
					trailY.add((int)getX());

					if (!(300-80 < getY() && getY() < 300-60)) {
						vth = vth + g*Math.sin(th)*dt;
						th = th + vth*dt;
						setX(300+L-L*Math.sin(th));
						setY(300-L*Math.cos(th));
					}

					//calc fart
					vBall = boll.velBall(m, endVel(vth, L));
				}

				/*
			  Beräknar klubbans hastighet vid slaget
			  */
				public double endVel(double startVel, double h){
			    double allEnergy = Math.pow(startVel, 2)/2 + g*h;
			    double v = Math.sqrt(allEnergy*2);
			    return v;
				}
			};

	}

	@Override
	public void run() {
		while(running) {
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
		if(!running) {
			Thread t = new Thread(this);
			createBufferStrategy(3);
			running = true;
			t.start();
		}
	}

	private void render() {
		BufferStrategy strategy = getBufferStrategy();
		Graphics2D g = (Graphics2D) strategy.getDrawGraphics();

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());

		boll.render(g);
		klubba.render(g);

		strategy.show();
	}


	private void update() {
		if(395 < klubba.getY() && klubba.getY() < 405 && !hit){
			hit = true;
			bollHit(40, klubba.vBall);
		}
		if(hit)
			boll.update();
		klubba.update();
	}

	private void bollHit(double loft, double v){
		double loftInRad = loft * 0.01753;
		boll.setVXOld(v * Math.cos(loftInRad));
		boll.setVYOld((-1)*v * Math.sin(loftInRad));
	}

	public static void main(String[] args) {
		JFrame myFrame = new JFrame("GOLFKILLER");
		PhysicsCanvas physics = new PhysicsCanvas();
		myFrame.add(physics);
		myFrame.pack();
		myFrame.setResizable(false);
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.setVisible(true);
		physics.start();
		myFrame.setLocationRelativeTo(null);
	}
}
