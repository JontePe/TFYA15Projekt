package fysiktest;

import java.awt.Graphics2D;
import java.awt.Color;

public class Particle {

	private final int dimX = PhysicsCanvas.dimX;
	private final int dimY = PhysicsCanvas.dimY - 100;
	private final int holeXMin = dimX - 200;
	private final int holeXMax = dimX - 150;
	private final int holeY = dimY + 50;

	private double vx; // m/s
	private double vy; // m/s
	private double t = 0.05; // s
	private double r; // m
	private double m; // kg
	private double a; // m^2
	private double dAir = 1.2041; // kg/m^3
	private double drag = 0.47;
	private double fNetX = 300;
	private double fNetY = 10;
	private Color color;

	public double f = 0.55;
	public double g = 9.82;

	private double x;

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	private double y;

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public Particle(double x, double y, double r, double m, Color color) {
		this.x = x;
		this.y = y;
		this.r = r;
		this.m = m;
		this.color = color;
		fNetY += m * g;
		vx = (fNetX / m) * t;
		vy = (fNetY / m) * t;
		a = r * r * Math.PI;
	}

	public void update() {
		// fx
		// double airFX = airResistance(vx);
		double airFX = 0;
		double fNetXNew;
		if (fNetX > 0) {
			fNetXNew = fNetX - airFX;
		} else {
			fNetXNew = fNetX + airFX;
		}

		double vxNew = (fNetXNew / m) * t;

		// fy
		// double airFY = airResistance(vy);
		double airFY = 0;
		double fNetYNew;
		if (fNetY > 0) {
			fNetYNew = fNetY - airFY + m * g;
		} else {
			fNetYNew = fNetY + airFY + m * g;
		}
		double vyNew = vy + (fNetYNew / m) * t;

		double fN = -m * g;

		x += vx * t;
		y += vy * t;
		vx = vxNew;
		vy = vyNew;
		fNetX = fNetXNew;
		fNetY = fNetYNew;
		checkCollisions(fN);

	}

	public void checkCollisions(double fN) {
		
		System.out.println(vx);
		// x
		if (x - r < 0) {
			x = r;
			vx = -(vx * f);
			fNetX = -fNetX * f;
		} else if (x + r > dimX) {
			x = dimX - r;
			vx = -(vx * f);
			fNetX = -fNetX * f;
		} else if ((x >= holeXMin && x <= holeXMax) && y > dimY && x - r < holeXMin) {
			x = holeXMin + r;
			fNetX = -fNetX * f;
		} else if ((x >= holeXMin && x <= holeXMax) && y > dimY && x + r > holeXMax) {
			x = holeXMax - r;
			fNetX = -fNetX * f;
		}
		// y
		if (y - r <= 0) {
			y = r;
			if (fNetY > 0) {
				fNetY = fNetY - (fN * f);
			} else if (fNetY < 0) {
				fNetY = fNetY + (fN * f);
			}

		} else if ((x < holeXMin || x > holeXMax) && y + r > dimY) {
			double oldY = y;
			y = dimY - r;			
			vy = -(vy * f);
			
			if (fNetY > 0) {
				fNetY = fNetY * f;
			} else if (fNetY < 0) {
				fNetY = fNetY * f;
			}

			if (Math.abs(vx) < 0.1) {
				fNetX = 0;
			}
			if (Math.abs(y - oldY) <= 1) {
				fNetY = 0;
				vy = 0;
			}
		} else if ((x >= holeXMin && x <= holeXMax) && y  > holeY) {
			
			double oldY = y;
			y = holeXMin - r;
			vy = -(vy * f);
			vx = 0.9 * vx;
			

			if (fNetX > 0) {
				fNetX = fNetX * f;
			} else if (fNetX < 0) {
				fNetX = fNetX * f;
			}

			if (Math.abs(fNetX) < 0.1) {
				fNetX = 0;
			}
			if (Math.abs(y - oldY) < 1) {
				fNetY = 0;
				vy = 0;
			}
		}
	}

	public double airResistance(double v) {
		return 0.5 * drag * a * dAir * v * v;
	}

	public void render(Graphics2D g) {
		g.setColor(color);
		g.fillOval((int) Math.round(x - r), (int) Math.round(y - r), (int) Math.round(2 * r), (int) Math.round(2 * r));
	}
}