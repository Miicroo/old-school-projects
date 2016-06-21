import java.awt.geom.Ellipse2D;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.lang.Math.random;
import static java.lang.Math.min;
import static java.lang.Math.max;

public class Bounce implements IBouncingBallsModel {

	private final double areaWidth;
	private final double areaHeight;
	private final double numberOfBalls = 2, minR = 0.4, maxR = 1, minVx = 1,
			maxVx = 4, minVy = 1, maxVy = 4, minMass = 1, maxMass = 4;

	private final List<Ball> balls = new LinkedList<Ball>();

	public Bounce(double width, double height) {
		this.areaWidth = width;
		this.areaHeight = height;
		double x, y, r = max(min(random() * maxR, maxR), minR);
		int tooMany = 0;

		for (int i = 0; i < numberOfBalls; i++) {
			r = max(min(random() * maxR, maxR), minR);
			while (occupied(x = ((x = random() * areaWidth) < r ? r
					: (x + r > areaWidth ? areaWidth - r : x)),
					y = ((y = random() * areaHeight) < r ? r
							: (y + r > areaHeight ? areaHeight - r : y)), r)) {
				tooMany++;
				if (tooMany > 10) {
					System.out.println("Use fewer balls or larger area!");
					System.exit(0);
				}
			}
			balls.add(new Ball(x, y, r, max(min(random() * maxMass, maxMass),
					minMass)));
			tooMany = 0;
		}
	}

	private boolean occupied(double x, double y, double r) {
		boolean isFree = false;

		for (Ball b : balls) {
			if (sqrt(pow(abs(x - b.x), 2) + pow(abs(y - b.y), 2)) < b.r + r) {
				isFree = true;
				break;
			}
		}
		return isFree;
	}

	private void tryForCollision(Ball b, double deltaT) {
		for (Ball other : balls) {
			if (b != other
					&& abs(b.x - other.x) + deltaT * other.vx < other.r + b.r
					&& abs(b.y - other.y) + deltaT * other.vy < other.r + b.r) {
				ResolveCollision(b, other);
				break;
			}
		}
	}

	private void ResolveCollision(Ball b1, Ball b2) {
		double priorSpeed1 = sqrt(pow(b1.vx, 2) + pow(b1.vy, 2)), priorSpeed2 = sqrt(pow(
				b2.vx, 2) + pow(b2.vy, 2)), latterSpeed2, latterSpeed1;
		double priorMomentum = b1.mass * priorSpeed1 + b2.mass * priorSpeed2;
		double priorSpeed = -1 * (priorSpeed2 - priorSpeed1);

		latterSpeed2 = priorMomentum / ((b1.mass / priorSpeed) + b2.mass);
		latterSpeed1 = latterSpeed2 / priorSpeed;
	}

	@Override
	public void tick(double deltaT) {
		for (Ball b : balls) {
			b.tick(deltaT);
		}
	}

	@Override
	public List<Ellipse2D> getBalls() {
		List<Ellipse2D> myBalls = new LinkedList<Ellipse2D>();
		for (Ball b : balls) {
			myBalls.add(new Ellipse2D.Double(b.x - b.r, b.y - b.r, 2 * b.r,
					2 * b.r));
		}
		return myBalls;
	}

	private class Ball {
		private double x, y, vx, vy, r, mass;

		Ball(double x, double y, double r, double mass) {
			this.r = r;
			vx = max(min(random() * maxVx, maxVx), minVx);
			vy = max(min(random() * maxVy, maxVy), minVy);
			this.x = x;
			this.y = y;
			this.mass = mass;
		}

		void tick(double deltaT) {
			if (x < r || x > areaWidth - r) {
				vx *= -1;
			}
			if (y < r || y > areaHeight - r) {
				vy *= -1;
			}
			vy -= deltaT * 9.8 / 2;
			x += vx * deltaT;
			y += vy * deltaT;
			vy -= deltaT * 9.8 / 2;
			tryForCollision(this, deltaT);
		}
	}
}