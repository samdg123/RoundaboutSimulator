import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;



public class SimulatorView extends JPanel implements Observer{

	private static SimulatorView instance;
	
	BufferedImage backgroundImage;
	BufferedImage indicator;
	BufferedImage blueCar;
	BufferedImage greenCar;
	BufferedImage redCar;
	BufferedImage yellowCar;
	
	CopyOnWriteArrayList<Car> cars = new CopyOnWriteArrayList<>();
	
	public static SimulatorView simView() {
		if (instance == null) {
			instance = new SimulatorView();
		}
		return instance;
	}
	
	private SimulatorView(){
		
		try {
			backgroundImage = ImageIO.read(new File("src/images/RoundaboutImage.png"));
			indicator = ImageIO.read(new File("src/images/indicator.png"));
			blueCar = ImageIO.read(new File("src/images/blue_car.png"));
			greenCar = ImageIO.read(new File("src/images/green_car.png"));
			redCar = ImageIO.read(new File("src/images/red_car.png"));
			yellowCar = ImageIO.read(new File("src/images/yellow_car.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setSize(1000, 535);
		
	}
	
	@Override
	public void paint(Graphics g) {
		BufferedImage image = null;
		
		this.setSize(1000, 535);
		g.drawImage(backgroundImage, 0, 0, null);
				
		for(Car car : cars) {
			
			switch (car.getColour()) {
			case blue:
				image = blueCar;
				break;
			case red:
				image = redCar;
				break;
			case green:
				image = greenCar;
				break;
			case yellow:
				image = yellowCar;
				break;
			}
			
			g.drawImage(rotateImage(addIndicator(image, car), car.getAngle()), car.getX()-25, car.getY()-25, null);
		}
	}
	
	private BufferedImage rotateImage(BufferedImage image, double intRotation) {
		BufferedImage bufferedImage = image;
		double rotation = Math.toRadians(intRotation);

		    AffineTransform tx = new AffineTransform();
		    tx.rotate(rotation, 25, 25);

		    AffineTransformOp op = new AffineTransformOp(tx,
		        AffineTransformOp.TYPE_BILINEAR);
		    bufferedImage = op.filter(bufferedImage, null);
		    
		    return bufferedImage;
	}
	

	@Override
	public void update(Observable observable, Object car) {
		
		//if observable car is not on view's car list, then add it
		if (car.getClass() == Car.class) {
			if (!cars.contains(car)) {
				
				cars.add((Car)car);
			} 
			
			//if it is, but status is 'none' then remove from list as it's been killed	
			if (((Car) car).getStatus() == Car.Status.none) {
				cars.remove(car);
			}
		} 
		
		repaint();
	}
	
	private BufferedImage addIndicator(BufferedImage carImage, Car car) {
		if (!car.getIndicating() || car.indicate == Car.Indicate.none) {
			return carImage;
		}
		
		BufferedImage newCar = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = newCar.createGraphics();
			g2d.drawImage(carImage, 0, 0, null);
			
			if (car.indicate == Car.Indicate.left) {
				g2d.drawImage(indicator, 25, 10, null);
			} else {
				g2d.drawImage(indicator, 25, 30, null);
			}
			
			g2d.dispose();
		
		return newCar;
	}
	
	//Stops the cars in the list from notifying the view then removes them from the view's list
	public void clearCarList() {
		for (Car car : cars) {
			car.kill();
		}
		
		cars.removeAll(cars);
		System.out.println(cars.size() + " items in cars now");
	}
}
