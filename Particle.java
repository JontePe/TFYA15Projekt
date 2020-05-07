import java.awt.Graphics2D;
import java.awt.Color;

public class Particle {

	private double vx; 					//m/s
	private double vy; 					//m/s
	private double t = 0.01; 				//s
	private double r;								//m
	private double m;								//kg
	private double a;								//m^2
	private double dAir = 1.2041; 	//kg/m^3
	private double drag = 0.47;
	private double fNetX = 10;
	private double fNetY = 10;
	private Color color;

	public double f = 0.55;
	public double g = 9.82;

	private double x;

	public double getX() {
		return x;
	}

	public void setX(double x){
		this.x = x;
	}

	private double y;

	public double getY() {
		return y;
	}

	public void setY(double y){
		this.y = y;
	}

	public Particle(double x, double y, double r, double m, Color color) {
		this.x = x;
		this.y = y;
		this.r = r;
		this.m = m;
		this.color = color;
		fNetY -= m*g;
		vx = fNetX/m*t;
		vy = fNetY/m*t;
		a = r*r*Math.PI;
	}

	public void update() {
		//fx
		double airFX = airResistance(vx);
		//System.out.println(airFX);
		double fNetXNew;
		if(fNetX > 0){
			fNetXNew = fNetX - airFX;
		}else{
			fNetXNew = fNetX + airFX;
		}
		double vxNew = (fNetXNew/m)*t;

		//fy
		double airFY = airResistance(vy);
		double fNetYNew = fNetY - airFY;
		double vyNew = vy + (fNetYNew/m)*t;

		x += vx*t;
		y += vy*t;
		vx = vxNew;
		vy = vyNew;
		fNetX = fNetXNew;
		fNetY = fNetYNew;
		//vy += g*t;
		double fN = -m*g;
		checkCollisions(fN);
	}



	public void checkCollisions(double fN) {
		//x
		if(x - r < 0){
			x = r;
			fNetX = -fNetX - (fN * f);
		}else if(x + r > 800){
			x = 800 - r;
			fNetX = -fNetX + (fN * f);
		}else if((x >= 600 && x <= 650) && y > 400 && x - r < 600){
			x = 600 + r;
			fNetX = -fNetX - (fN * f);
		}else if((x >= 600 && x <= 650) && y > 400 && x + r > 650){
			x = 650 - r;
			fNetX = -fNetX + (fN * f);
		}
		//y
		if(y - r < 0){
			y = r;
			fNetY += (fN * f);
		}else if((x < 600 || x > 650) && y + r > 400){
			double oldY = y;
			y = 400 - r;
			vy = -(vy * f);
			vx = 0.98 * vx;
			if(Math.abs(vx) < 0.1) {
				vx = 0;
			}
			if (Math.abs(y - oldY) < 0.1) {
				vy = 0;
			}
		}else if((x >= 600 && x <= 650) && y + r > 450){
			double oldY = y;
			y = 450 - r;
			vy = -(vy * f);
			vx = 0.98 * vx;
			if(Math.abs(vx) < 0.1) {
				vx = 0;
			}
			if (Math.abs(y - oldY) < 0.1) {
				vy = 0;
			}
		}
	}

	public double airResistance(double v){
		double ret = 0.5*drag*a*dAir*v*v;
		System.out.println(ret);
		return ret;
	}

	public void render(Graphics2D g) {
		g.setColor(color);
		g.fillOval((int) Math.round(x - r*500), (int) Math.round(y - r*500), (int) Math.round(2 * r*500), (int) Math.round(2 * r*500));
	}
}
