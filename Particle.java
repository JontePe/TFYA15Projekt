import java.awt.Graphics2D;
import java.awt.Color;

public class Particle {

  //Fönster-konstanter
	private final int dimX = PhysicsCanvas.dimX;
	private final int dimY = PhysicsCanvas.dimY - 100;
	private final int holeXMin = dimX - 200;
	private final int holeXMax = dimX - 150;
	private final int holeY = dimY + 50;

  //Fönster-variabler
  private Color color;

  //Fysik-konstanter
  private final double muX = 0.9;        //Friktionskoefficient                                Saknar dimension (x-led)
  private final double muY = 0.6;        //Friktionskoefficient                                Saknar dimension (y-led)
  private final double drag = 0.47;      //Luftens dragskoefficient                            Saknar dimension
	private final double g = 9.82;         //Tyngdacceleration                                   m/s^2 (y-led)
  private final double dt = 0.1; 				 //Tidsintervall                                       s
  private final double vG = g*dt;        //Gravitationens hastighetsvektor                     m/s (y-led)
  private final double rhoAir = 1.2041;  //Luftens densitet                                    kg/m^3

  //Fysik-variabler
	private double r;								       //Radie                                               m
	private double m;								       //Massa                                               kg
	private double A;								       //Tvärsnittsarea                                      m^2
	private double fHitX = 200;            //Slagets kraft                                       N (x-led)
	private double fHitY = -300;           //Slagets kraft                                       N (Y-led)
  private double vxOld;                  //Nettohastighetsvektorn i det förra tidsintervallet  m/s (x-led)
  private double vyOld;                  //Nettohastighetsvektorn i det förra tidsintervallet  m/s (y-led)
	private double x;                      //Position                                            m (x-led)
  private double y;                      //Position                                            m (y-led)

  /*
  Getter & setter för x
  */
	public double getX(){
		return x;
	}
	public void setX(double x){
		this.x = x;
	}

  /*
  Getter & setter för y
  */
	public double getY(){
		return y;
	}
	public void setY(double y){
		this.y = y;
	}

  /*
  Getter & setter för vxOld
  */
	public double getVXOld(){
		return vxOld;
	}
	public void setVXOld(double vxOld){
		this.vxOld = vxOld;
	}

  /*
  Getter & setter för vyOld
  */
	public double getVYOld(){
		return vyOld;
	}
	public void setVYOld(double vyOld){
		this.vyOld = vyOld;
	}

  /*
  Konstruktör
  */
	public Particle(double x, double y, double r, double m, Color color){
		this.x = x;
		this.y = y;
		this.r = r;
		this.m = m;
    vxOld = fHitX/m*dt;
    vyOld = fHitY/m*dt;
		this.color = color;
		A = r*r*Math.PI;
    this.r = r*400;
	}

  /*
  Uppdaterar bollens position
  */
	public void update(){
    //X-LED
    double dvx = airResistance(vxOld);
    System.out.println("dvx = " + dvx);
    double vxNew = vxOld + dvx;

    //Y-LED
    double dvy = vG + airResistance(vyOld);
    System.out.println("dvy = " + dvy);
    double vyNew = vyOld + dvy;

    //UPPDATERING
    x += vxNew*dt;
		y += vyNew*dt;
    vyOld = vyNew;
    vxOld = vxNew;
    bounceCheck();
	}

  /*
  Beräknar luftmotståndets hastighetsvektor i v:s led
  */
  private double airResistance(double v){
    double vAir = ((0.5*drag*A*rhoAir*v*v)/m)*dt;
    if(v > 0)
		  return vAir*(-1);
    else return vAir;
	}

  /*
  Kollar om bollen ska studsa
  */
	private void bounceCheck(){
		//X-LED
		if(x - r < 0)                                                               //Studs i vänstra väggen
      bounceX(r);
		else if(x + r > dimX)                                                       //Studs i högra väggen
      bounceX(dimX - r);
		else if((x >= holeXMin && x <= holeXMax) && y > dimY && x - r < holeXMin)   //Studs i hålets vänstra vägg
      bounceX(holeXMin + r);
		else if((x >= holeXMin && x <= holeXMax) && y > dimY && x + r > holeXMax)   //Studs i hålets högra vägg
      bounceX(holeXMax - r);

		//Y-LED
		if(y - r < 0)                                                               //Studs i taket
			bounceY(r);
		else if((x < holeXMin || x > holeXMax) && y + r > dimY){                    //Studs i marken
      double yOld = y;
			bounceY(dimY - r);
      stop(yOld);
		}else if((x >= holeXMin && x <= holeXMax) && y + r > holeY){                //Studs i hålets botten
      double yOld = y;
      bounceY(holeY - r);
      stop(yOld);
		}
	}

  /*
  Utför en studs i x-led
  */
  private void bounceX(double x){
    this.x = x;
    vxOld *= (-1)*muX;
  }

  /*
  Utför en studs i y-led
  */
  private void bounceY(double y){
    this.y = y;
    vxOld *= muX;
    vyOld *= (-1)*muY;
  }

  /*
  Kollar om bollen ska stanna i något led, och stannar bollen i sådant fall
  */
  private void stop(double yOld){
    if(Math.abs(vxOld) < 0.1)
      vxOld = 0;
    if(Math.abs(y - yOld) < 0.25)
      vyOld = 0;
  }

  /*
  Ritar bollen på fönstret
  */
	public void render(Graphics2D g) {
		g.setColor(color);
		g.fillOval((int) Math.round(x - r), (int) Math.round(y - r), (int) Math.round(2 * r), (int) Math.round(2 * r));
	}
}
