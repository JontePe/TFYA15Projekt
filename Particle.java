package fysiktest;

import java.awt.Graphics2D;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Particle {

	// F�nster-konstanter
	private final int dimX = PhysicsCanvas.dimX;
	private final int dimY = PhysicsCanvas.dimY - 100;
	private final int holeXMin = dimX - 200;
	private final int holeXMax = dimX - 150;
	private final int holeY = dimY + 50;

	// F�nster-variabler
	private Color color;
	protected List<Integer> trailX; // list koordinater som varit
	protected List<Integer> trailY;

	// Fysik-konstanter
	private final double muX = 0.9; // Friktionskoefficient Saknar dimension (x-led)
	private final double muY = 0.6; // Friktionskoefficient Saknar dimension (y-led)
	private final double drag = 0.47; // Luftens dragskoefficient Saknar dimension
	protected final double g = 9.82; // Tyngdacceleration m/s^2 (y-led)
	protected final double dt = 0.05; // Tidsintervall s
	private final double vG = g * dt; // Gravitationens hastighetsvektor m/s (y-led)
	private final double rhoAir = 1.2041; // Luftens densitet kg/m^3
	public double vBall;

	// Fysik-variabler
	private double r; // Radie m
	protected double m; // Massa kg
	private double A; // Tv�rsnittsarea m^2
	private double vxOld = 0; // Nettohastighetsvektorn i det f�rra tidsintervallet m/s (x-led)
	private double vyOld = 0; // Nettohastighetsvektorn i det f�rra tidsintervallet m/s (y-led)
	private double x; // Position m (x-led)
	private double y; // Position m (y-led)

	/*
	 * Getter & setter f�r x
	 */
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	/*
	 * Getter & setter f�r y
	 */
	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	/*
	 * Getter & setter f�r vxOld
	 */
	public double getVXOld() {
		return vxOld;
	}

	public void setVXOld(double vxOld) {
		this.vxOld = vxOld;
	}

	/*
	 * Getter & setter f�r vyOld
	 */
	public double getVYOld() {
		return vyOld;
	}

	public void setVYOld(double vyOld) {
		this.vyOld = vyOld;
	}

	/*
	 * Konstrukt�r
	 */
	public Particle(double x, double y, double r, double m, Color color) {
		this.x = x;
		this.y = y;
		this.r = r;
		this.m = m;

		this.trailX = new ArrayList<Integer>();
		this.trailY = new ArrayList<Integer>();

		this.color = color;
		A = r * r * Math.PI;
		this.r = r * 400;
	}

	/*
	 * Uppdaterar bollens position
	 */
	public void update() {
		// X-LED
		double dvx = airResistance(vxOld);
		double vxNew = vxOld + dvx;

		// Y-LED
		double dvy = vG + airResistance(vyOld);
		double vyNew = vyOld + dvy;

		// UPPDATERING
		x += vxNew * dt;
		y += vyNew * dt;
		vyOld = vyNew;
		vxOld = vxNew;
		bounceCheck();

		// TRAIL
		trailX.add((int) getY());
		trailY.add((int) getX());
	}

	/*
	 * Ber�knar luftmotst�ndets hastighetsvektor i v:s led
	 */
	private double airResistance(double v) {
		double vAir = ((0.5 * drag * A * rhoAir * v * v) / m) * dt;
		if (v > 0)
			return vAir * (-1);
		else
			return vAir;
	}

	/*
	 * Kollar om bollen ska studsa
	 */
	private void bounceCheck() {
		// X-LED
		if (x - r < 0) // Studs i v�nstra v�ggen
			bounceX(r);
		else if (x + r > dimX) // Studs i h�gra v�ggen
			bounceX(dimX - r);
		else if ((x >= holeXMin && x <= holeXMax) && y > dimY && x - r < holeXMin) // Studs i h�lets v�nstra v�gg
			bounceX(holeXMin + r);
		else if ((x >= holeXMin && x <= holeXMax) && y > dimY && x + r > holeXMax) // Studs i h�lets h�gra v�gg
			bounceX(holeXMax - r);

		// Y-LED
		if (y - r < 0) // Studs i taket
			bounceY(r);
		else if ((x < holeXMin || x > holeXMax) && y + r > dimY) { // Studs i marken
			double yOld = y;
			bounceY(dimY - r);
			stop(yOld);
		} else if ((x >= holeXMin && x <= holeXMax) && y + r > holeY) { // Studs i h�lets botten
			double yOld = y;
			bounceY(holeY - r);
			stop(yOld);
		}
	}

	/*
	 * Utf�r en studs i x-led
	 */
	private void bounceX(double x) {
		this.x = x;
		vxOld *= (-1) * muX;
	}

	/*
	 * Utf�r en studs i y-led
	 */
	private void bounceY(double y) {
		this.y = y;
		vxOld *= muX;
		vyOld *= (-1) * muY;
	}

	/*
	 * Kollar om bollen ska stanna i n�got led, och stannar bollen i s�dant fall
	 */
	private void stop(double yOld) {
		if (Math.abs(vxOld) < 0.1)
			vxOld = 0;
		if (Math.abs(y - yOld) < 0.1)
			vyOld = 0;
	}

	/*
	 * Ritar bollen p� f�nstret
	 */
	public void render(Graphics2D g) {
		g.setColor(color);
		g.fillOval((int) Math.round(x - r), (int) Math.round(y - r), (int) Math.round(2 * r), (int) Math.round(2 * r));
		for (int i = 0; i < trailX.size(); i++) { // Printar alla punkter varje g�ng
			g.fillOval(trailX.get(i), trailY.get(i), 3, 3);
		}

	}

	/*
	 * Ber�knar bollens hastighet efter slaget
	 */
	public double velBall(double mClub, double vEnd) {
		return (2.0 * mClub * vEnd) / (m + mClub);
	}

}