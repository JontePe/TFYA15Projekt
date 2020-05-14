
	package particle;

	import java.awt.Canvas;
	import java.awt.Color;
	import java.awt.Dimension;
	import java.awt.Graphics2D;
	import java.awt.image.BufferStrategy;
	import java.io.IOException;

	import javax.swing.JFrame;


	public class PhysicsCanvas extends Canvas implements Runnable {
		
		public static final int dimX = 1800;
		public static final int dimY = 1000;
		
		private boolean running;
		
		private Particle boll;
		private Particle klubba;
		final private int klubbPosX = 200;
		final private int klubbPosY = 800;
		private double L = 100; //aka height
		
		protected int i = 0;
		public double vBall;

		public PhysicsCanvas() {
			Dimension d = new Dimension(dimX, dimY);
			setPreferredSize(d);
			setMinimumSize(d);
			setMaximumSize(d);
			
			
			boll = new Particle((klubbPosX + L), (klubbPosY + L), 0.021335, 0.04593, Color.BLACK);
			klubba = new Particle(klubbPosX, klubbPosY, 0.03, 0.4, Color.BLUE){
				private double th = Math.PI/2;
				private double vth = 0; //aka StartVel
				@Override
				public void update(){
					trailX.add((int) getX());
					trailY.add((int) getY());
					
					if (!(getX() > klubbPosX+2*L-L/50)) {
						vth = vth + (g)*Math.sin(th)*dt;
						th = th + vth*dt;
						setX(klubbPosX + L - L*Math.sin(th));
						setY(klubbPosY-L*Math.cos(th));
					}
					//calc fart
					
					vBall = klubba.velBall(0.4, klubba.endVel(vth, L));
					System.out.println("ball: " + vBall);
					
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

			System.out.println("UPDATE");
			if (boll.getX() <= klubba.getX() && i == 0) { //bolllkoordinat i yled +/- 5
				i++;
				boll.velUpdate(15, vBall);
				System.out.println("Inne?");
			}
			
			if (i != 0) {
				
				boll.update();
			}
			klubba.update();
		}
		
		
		public static void main(String[] args) {
			JFrame myFrame = new JFrame("My physics canvas");
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

