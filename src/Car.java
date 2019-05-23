import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.ImageIO;

public class Car extends Observable implements Runnable{
	
	Image image;
	private final int distanceRequired = 75;
	private double x = 0;
	private double y = 0;
	private int directionX = 0;
	private int directionY = 0;
	private int colourInt;
	Colour colour;
	Indicate indicate = Indicate.none;
	boolean indicating = false;
	private RoadLbl destination;
	private RoadLbl origin;
	private Status status;
	static int idIncrement = 1;
	private int id;
	private int angle;
	private SimulatorModel simModel = SimulatorModel.simModel();
	OutputPanel outputPanel;
	private int testCase;
	boolean kill = false;
	
	public enum Colour{red, green, blue, yellow}
	public enum RoadLbl{a, b, c, d}
	public enum Status{none, aIn, aOut, bIn, bOut, cIn, cOut, dIn, dOut, rb, waitingRb, cpA, cpB, cpC, cpD}
	public enum Indicate{none, left, right}

	public Car(int testCase) {
		this.testCase = testCase;
		
		construct();
		
		if (testCase == 1) {
			origin = RoadLbl.a;
			destination = RoadLbl.c;

			x = SimulatorModel.roadAIn.getEdgeX();
			y = SimulatorModel.roadAIn.getEdgeY();
			angle = SimulatorModel.roadAIn.getAngle();
		}
				
		setIndicatorDirection();
		notifyNewCar();
		
	}
	
	//constructor for test case 2
	public Car(RoadLbl destination) {
		
		//run construction code that is mutual for all test cases
		construct();
		
		origin = RoadLbl.a;

		x = SimulatorModel.roadAIn.getEdgeX();
		y = SimulatorModel.roadAIn.getEdgeY();
		angle = SimulatorModel.roadAIn.getAngle();
		
		this.destination = destination;
		
		setIndicatorDirection();
		notifyNewCar();
	}
	
	private void construct() {
		status = Status.none;
		
		id = idIncrement;
		idIncrement++;
		
		setObserver(SimulatorView.simView());
		
		setObserver(OutputPanel.outputPanel());
		
		randomiseAttributes();
	}
	
		
	private void randomiseAttributes(){
		Random random = new Random();
		
		//generate random colour attribute
		colourInt = random.nextInt(4)+1;
		switch(colourInt){
		case 1:
			colour = Colour.blue;
			try {
				image = ImageIO.read(new File("src/images/blue_car.png"));
			} catch (IOException e) {
				// TODO Auto-	generated catch block	
				e.printStackTrace();
			}
			break;
		case 2:
			colour = Colour.green;
			try {
				image = ImageIO.read(new File("src/images/green_car.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 3:
			colour = Colour.red;
			try {
				image = ImageIO.read(new File("src/images/red_car.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 4:
			colour = Colour.yellow;
			try {
				image = ImageIO.read(new File("src/images/yellow_car.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		
		//fetches roads that the car can use
		ArrayList<RoadLbl> startingRoads = simModel.getStartingRoads();
		origin = startingRoads.get(random.nextInt(startingRoads.size()));
		
		ArrayList<RoadLbl> endingRoads = simModel.getEndingRoads();
		destination = endingRoads.get(random.nextInt(endingRoads.size()));
						
		
		Road road = null;
		//sets status and required location based on origin
		switch(origin){
		case a:
			status = Status.aIn;
			road = SimulatorModel.roadAIn;
			
			break;
		case b:
			status = Status.bIn;
			road = SimulatorModel.roadBIn;
			
			break;
		case c:
			status = Status.cIn;
			road = SimulatorModel.roadCIn;
			
			break;
		case d:
			status = Status.dIn;
			road = SimulatorModel.roadDIn;
			
			break;
		}
		
		x = road.getEdgeX();
		y = road.getEdgeY();
		angle = road.getAngle();
	}
	
	//Main car method
	@Override
	public void run() {
		
		try {
			performRoadIn();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		while (!checkRoundaboutClear()){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (testCase == 1) {
			return;
		}
		
		joinRoundabout();
		
		performRoundabout();
		
		exitRoundabout();
		
		exitRoad();
		
		simModel.removeCar(this);
		status = Status.none;
		
		notifyCarKilled();		
	}
	
	
	private void performRoadIn() throws InterruptedException {
		int distanceFromTarget = 0;
		int roadEnd;
		
		switch (origin) {
		case a:
			roadEnd = SimulatorModel.roadAIn.getrbX();
			status = Status.aIn;
			directionX = 4;
			distanceFromTarget = (int) (roadEnd - x);
			break;
			
		case b: 
			roadEnd = SimulatorModel.roadBIn.getrbY();
			
			status = Status.bIn;
			directionY = 4;
			distanceFromTarget = (int) (roadEnd - y);
			break;
			
		case c:
			roadEnd = SimulatorModel.roadCIn.getrbX();
			
			status = Status.cIn;
			directionX = -4;
			distanceFromTarget = (int) (x - roadEnd);
			break;
			
		case d:
			roadEnd = SimulatorModel.roadDIn.getrbY();
			
			status = Status.dIn;
			directionY = -4;
			distanceFromTarget = (int) (y - roadEnd);
			break;
		}
		
		//while the car hasn't arrived at the roundabout keep checking if clear then move further
		while (distanceFromTarget > 0) {
			if (roadIsClear()) {
				moveOnRoad();
				distanceFromTarget -= 4;
				
				if (distanceFromTarget < 80) {
					indicating = true;
				}
			}
			
		}
		
		status = Status.waitingRb;
	}
	
	private void moveOnRoad() throws InterruptedException{
		
		x += directionX;
		y += directionY;
		
		notifyCarToView();
		
		Thread.sleep(50);
	}
	
	//check if road is clear to move on and return true or false
	private boolean roadIsClear() {
		CopyOnWriteArrayList<Car> cars = new CopyOnWriteArrayList<Car>(simModel.getCars());
		
		for (Car car : cars){
			if (car != null && (car.status == this.status || car.status == Status.waitingRb)){
				int carX = car.getX();
				int carY = car.getY();
				
				if (directionX > 0){
					if (carX > x && (carX - x) < distanceRequired){
						return false;
					}
					
				} else if (directionX < 0){
					if (carX < x && (x - carX) < distanceRequired){
						return false;
					}
					
				} else if (directionY > 0){
					if (carY > y && (carY - y) < distanceRequired){
						return false;
					}
					
				} else if (directionY < 0){
					if (carY < y && (y - carY) < distanceRequired){
						return false;
					}
				}				
			}
		}		
		return true;
	}
	
	//method for going around the roundabout
	private void performRoundabout() {
		int[][] rbCtrlPs = simModel.getRBControlPoints();
		int lerpX = 0;
		int lerpY = 0;
		int lerpAngle = 0;
		Status lastCP = Status.none;	
		
		switch (destination) {
		case a:
			lastCP = Status.cpD;
			break;
		case b:
			lastCP = Status.cpA;
			break;
		case c:
			lastCP = Status.cpB;
			break;
		case d:
			lastCP = Status.cpC;
			break;
		}
		
		//while not at the last exist of the roundabout
		while (status != lastCP) {
			
			switch (status) {
			case cpA:
				lerpX = rbCtrlPs[1][0];
				lerpY = rbCtrlPs[1][1];
				lerpAngle = rbCtrlPs[1][2];
				status = Status.cpB;
				break;
				
			case cpB:
				lerpX = rbCtrlPs[2][0];
				lerpY = rbCtrlPs[2][1];
				lerpAngle = rbCtrlPs[2][2];
				status = Status.cpC;
				break;
			
			case cpC:
				lerpX = rbCtrlPs[3][0];
				lerpY = rbCtrlPs[3][1];
				lerpAngle = rbCtrlPs[3][2];
				status = Status.cpD;
				break;
			
			case cpD:
				lerpX = rbCtrlPs[0][0];
				lerpY = rbCtrlPs[0][1];
				lerpAngle = rbCtrlPs[0][2];
				status = Status.cpA;
				break;
			}
			
			//move to next control point
			lerp(lerpX, lerpY, lerpAngle);
		}
	}
	
	private boolean checkRoundaboutClear(){
		CopyOnWriteArrayList<Car> cars = new CopyOnWriteArrayList<Car>(simModel.getCars());
		
		//check if there's space to join
		switch (origin) {
		case a:
			for (Car car : cars){
				if (car.indicate != Indicate.left && (car.status == Status.cpB || car.status == Status.cpA || car.status == Status.cpD)){
					return false;
				}
			}
			break;
		case b:
			for (Car car : cars){
				if (car.indicate != Indicate.left && (car.status == Status.cpC || car.status == Status.cpB || car.status == Status.cpA)){
					return false;
				}
			}
			break;
		case c:
			for (Car car : cars){
				if (car.indicate != Indicate.left && (car.status == Status.cpD || car.status == Status.cpC || car.status == Status.cpB)){
					return false;
				}
			}
			break;
		case d:
			for (Car car : cars){
				if (car.indicate != Indicate.left && (car.status == Status.cpA || car.status == Status.cpD || car.status == Status.cpC)){
					return false;
				}
			}
			break;
		}		
		return true;
	}
	
	private void setIndicatorDirection() {
		switch (origin) {
		case a:
			if (destination == RoadLbl.b) {
				indicate = Indicate.left;
			} else if (destination == RoadLbl.d || destination == RoadLbl.a) {
				indicate = Indicate.right;
			}
			break;
			
		case b:
			if (destination == RoadLbl.c) {
				indicate = Indicate.left;
			} else if (destination == RoadLbl.a || destination == RoadLbl.b) {
				indicate = Indicate.right;
			}
			break;
			
		case c:
			if (destination == RoadLbl.d) {
				indicate = Indicate.left;
			} else if (destination == RoadLbl.b || destination == RoadLbl.c) {
				indicate = Indicate.right;
			}
			break;

		case d:
			if (destination == RoadLbl.a) {
				indicate = Indicate.left;
			} else if (destination == RoadLbl.c || destination == RoadLbl.d) {
				indicate = Indicate.right;
			}
			break;
		}
	}
	
	private void joinRoundabout() {
		int[][] rbCtrlPs = simModel.getRBControlPoints();
		int lerpX = 0;
		int lerpY = 0;
		int lerpAngle = 0;
		
		switch (origin) {
		case a: 
			lerpX = rbCtrlPs[0][0];
			lerpY = rbCtrlPs[0][1];
			lerpAngle = rbCtrlPs[0][2];
			status = Status.cpA;
			break;
		case b: 
			lerpX = rbCtrlPs[1][0];
			lerpY = rbCtrlPs[1][1];
			lerpAngle = rbCtrlPs[1][2];
			status = Status.cpB;
			break;
		case c: 
			lerpX = rbCtrlPs[2][0];
			lerpY = rbCtrlPs[2][1];
			lerpAngle = rbCtrlPs[2][2];
			status = Status.cpC;
			
			break;
		case d: 
			lerpX = rbCtrlPs[3][0];
			lerpY = rbCtrlPs[3][1];
			lerpAngle = rbCtrlPs[3][2];
			status = Status.cpD;
			break;
		}
		
		notifyCarEnteredRoundabout();
		
		lerp(lerpX, lerpY, lerpAngle);
		
	}
	
	private void exitRoundabout() {
		indicate = Indicate.left;
		
		int lerpX = 0;
		int lerpY = 0;
		int lerpAngle = 0;
		Status lerpStatus = Status.none;
		
		switch (destination) {
		case a:
			lerpX = SimulatorModel.roadAOut.getrbX();
			lerpY = SimulatorModel.roadAOut.getrbY();
			lerpAngle = SimulatorModel.roadAOut.getAngle();
			lerpStatus = Status.aOut;
			break;
			
		case b:
			lerpX = SimulatorModel.roadBOut.getrbX();
			lerpY = SimulatorModel.roadBOut.getrbY();
			lerpAngle = SimulatorModel.roadBOut.getAngle();
			lerpStatus = Status.bOut;
			break;
			
		case c:
			lerpX = SimulatorModel.roadCOut.getrbX();
			lerpY = SimulatorModel.roadCOut.getrbY();
			lerpAngle = SimulatorModel.roadCOut.getAngle();
			lerpStatus = Status.cOut;
			break;
			
		case d:
			lerpX = SimulatorModel.roadDOut.getrbX();
			lerpY = SimulatorModel.roadDOut.getrbY();
			lerpAngle = SimulatorModel.roadDOut.getAngle();
			lerpStatus = Status.dOut;
			break;
		}
		
		lerp(lerpX, lerpY, lerpAngle);
		
		//set status to final road
		status = lerpStatus;
		indicating = false;
		
		notifyCarLeftRoundabout();
	}
	
	
	private void lerp(int lerpX, int lerpY, int lerpAngle) {
		int moveAngle = 0;
		int slowness = 18;
		
		double moveX = (lerpX - x)/slowness;
		double moveY = (lerpY - y)/slowness;
		
		moveAngle = lerpAngle-angle;
		
		//determine the direction to spin based on current and desired angle
		if (!(lerpAngle-angle < 180 && lerpAngle - angle > -180)){
			if (moveAngle > 180){
				moveAngle = moveAngle-360;
			}
			else if (moveAngle < -180){
				moveAngle = moveAngle+360;
			}
		}
		
		moveAngle /= slowness;
		
		//for every fraction of the movement, move a little
		for (int i = 0; i < slowness; i++) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			x += moveX;
			y += moveY;
			angle += moveAngle;
			
			//update view on position
			notifyCarToView();
		}
		
		//coordinates set to exact goal as they may be off a little
		x = lerpX;
		y = lerpY;
		angle = lerpAngle;
	}
	
	private void exitRoad(){
		
		int edgeX = 0;
		int edgeY = 0;
		directionY = 0;
		directionX = 0;
		
		switch (destination) {
		case a:
			edgeX = SimulatorModel.roadAOut.getEdgeX();
			edgeY = SimulatorModel.roadAOut.getEdgeY();
			directionX = -4;
			status = Status.aOut;
			break;
			
		case b:
			edgeX = SimulatorModel.roadBOut.getEdgeX();
			edgeY = SimulatorModel.roadBOut.getEdgeY();
			directionY = -4;
			status = Status.bOut;
			break;
			
		case c:
			edgeX = SimulatorModel.roadCOut.getEdgeX();
			edgeY = SimulatorModel.roadCOut.getEdgeY();
			directionX = 4;
			status = Status.cOut;
			break;
			
		case d:
			edgeX = SimulatorModel.roadDOut.getEdgeX();
			edgeY = SimulatorModel.roadDOut.getEdgeY();
			directionY = 4;
			status = Status.dOut;
			break;
		}
				
		//while the car has not reached the edge of the screen
		while(x!=edgeX || y!=edgeY){
			try {
				moveOnRoad();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//if x or y are our of the sight of the view exit method
			if (x < -25 || x > 1075 || y < -25 || y > 600){
				return;
			}
		}
	}
		

	public int getID() {return id;}
	public int getX() {return (int) x;}
	public int getY() {return (int) y;}
	public double getAngle() {return angle;}
	public Status getStatus() {return status;}
	public RoadLbl getOrigin() {return origin;}
	public RoadLbl getDestination() {return destination;}
	public Colour getColour() {return colour;}	
	public boolean getIndicating() {return indicating;}	
	public Indicate getIndicator() {return indicate;}
	
	public void setX(int x) {this.x=x;}
	public void setY(int y) {this.y=y;}
	
	public void kill() {kill = true;}
	static public void resetCarIDs() {idIncrement = 1;}
	
	
	
	//observer methods
	private void notifyCarKilled() {
		String text;
		
		text = "Killed car #" + id + 
				" - Colour: " + colour.toString() + 
				", Origin: " + origin.toString() + 
				", Destination: " + destination.toString(); 
				
		setChanged();
		notifyObservers(text);
	}
	
	private void notifyCarEnteredRoundabout() {
		String text;
		
		text = "Car #" + id + 
				" has ENTERED the roundabout from road " + origin.toString(); 
				
		setChanged();
		notifyObservers(text);
	}
	
	private void notifyCarLeftRoundabout() {
		String text;
		
		text = "Car #" + id + 
				" has LEFT the roundabout to road " + destination.toString(); 
				
		setChanged();
		notifyObservers(text);
	}
	
	private void notifyCarToView() {
		if (!kill) {
			setChanged();
			notifyObservers(this);
		}
	}
	
	private void notifyNewCar() {
		String text;
		
		text = "New car #" + id + 
				" - Colour: " + colour.toString() + 
				", Origin: " + origin.toString() + 
				", Destination: " + destination.toString(); 
				
		setChanged();
		notifyObservers(text);
	}
	
		
	private void setObserver(Observer observer) {
		this.addObserver(observer);
	}
}
