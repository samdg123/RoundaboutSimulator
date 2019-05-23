import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;


import java.util.Timer;
import java.util.Random;

public class SimulatorModel{

	private static SimulatorModel instance;
	
	int testCase;
	int tickDelay = 50;
	private int genSliderMax;
	Timer frameTimer;
	static Road roadAIn;
	static Road roadAOut;
	static Road roadBIn;
	static Road roadBOut;
	static Road roadCIn;
	static Road roadCOut;
	static Road roadDIn;
	static Road roadDOut;
	
	private int maxCars;
	private int genSpeed;
	private ArrayList<Car.RoadLbl> startingRoads;
	private ArrayList<Car.RoadLbl> endingRoads;
	
	int[][] rbCtrlPs = new int[4][3];
	
	private List<Car> cars = new ArrayList<>();
	
	OutputPanel outputPanel;
		
	public static SimulatorModel simModel() {
		
		if (instance == null) {
			instance = new SimulatorModel();
		}
		
		return instance;
	}
	
	private SimulatorModel() {
		instantiateRoads();
		instantiateControlPoints();
		instantiateStartEndRoads();
				
	}
	
	private void testCase1() {
		addCar(1);
		addCar(1);
	}
	
	private void testCase2() {
		addCar(Car.RoadLbl.d);
		addCar(Car.RoadLbl.c);
	}
	
	private void testCase3() {
		instantiateStartEndRoads();
		genSpeed = (int)(genSliderMax*0.3);
		maxCars = 15;
	}
	
	private void testCase4() {
		instantiateStartEndRoads();
		genSpeed = (int)(genSliderMax*0.8);
		maxCars = 15;
	}

	private void newTestCase() {
		cars.clear();
		System.out.println("cars cleared");
	}
		
	private void instantiateControlPoints() {
		// in format x, y, angle
		
		//Road A
		rbCtrlPs[0] = new int[] {452, 229, 315};
		
		//Road B
		rbCtrlPs[1] = new int[] {545, 237, 45};
		
		//Road C
		rbCtrlPs[2] = new int[] {539, 310, 135};
		
		//Road D
		rbCtrlPs[3] = new int[] {446, 292, 225};
				
	}
	
	private void instantiateStartEndRoads() {
		startingRoads = new ArrayList<Car.RoadLbl>();
		
		startingRoads.add(Car.RoadLbl.a);
		startingRoads.add(Car.RoadLbl.b);
		startingRoads.add(Car.RoadLbl.c);
		startingRoads.add(Car.RoadLbl.d);
		
		endingRoads = new ArrayList<Car.RoadLbl>();
		
		endingRoads.add(Car.RoadLbl.a);
		endingRoads.add(Car.RoadLbl.b);
		endingRoads.add(Car.RoadLbl.c);
		endingRoads.add(Car.RoadLbl.d);
	}
	
	private void instantiateRoads(){
		roadAIn = new Road("a", "in", -50, 250, 375, 250, 0);
		roadAOut = new Road("a", "out", -50, 275, 375, 288, 180);
		roadBIn = new Road("b", "in", 508, -50, 508, 170, 90);
		roadBOut = new Road("b", "out", 480, -50, 480, 170, 270);
		roadCIn = new Road("c", "in", 1050, 286, 628, 286, 180);
		roadCOut = new Road("c", "out", 1050, 237, 628, 244, 0);
		roadDIn = new Road("d", "in", 486, 584, 486, 363, 270);
		roadDOut = new Road("d", "out", 511, 584, 513, 363, 90);
	}

	private void addCar(int testCase){
		cars.add(new Car(testCase));
		
		new Thread(cars.get(cars.size()-1)).start();
	}
	
	//for test case 2
	private void addCar(Car.RoadLbl destination){
		cars.add(new Car(destination));
		
		new Thread(cars.get(cars.size()-1)).start();
	}
	
	public void removeCar(Car car) {
		cars.remove(car);
	}
	
	//stop car generation timer
	public void stopTimer() {
		frameTimer.cancel();
		
		cars.removeAll(cars);
		
		SimulatorController.simCont().clearViewCarList();
	}
	
	//start generation timer for simulation
	public void startTimer() {
		//as some cars may not have been destroyed
		SimulatorController.simCont().clearViewCarList();
		
		Car.resetCarIDs();
		
		newTestCase();
		
		frameTimer = new Timer();
		frameTimer.schedule(new FrameTask(), 0, tickDelay);	
		
		switch(testCase) {
		case 1:
			testCase1();
			break;
		case 2:
			testCase2();
			break;
		case 3:
			testCase3();
			break;
		case 4:
			testCase4();
			break;
		}
		
	}
	
	public List<Car> getCars() {return cars;}
	public int[][] getRBControlPoints() {return rbCtrlPs;}
	public int getTestCase() {return testCase;}
	public void setGenSliderMax(int genSliderMax) {this.genSliderMax = genSliderMax;}
	public void setTestCase(int testCase) {cars.clear(); this.testCase = testCase;}
	public void setMaxCars(int maxCars) {this.maxCars = maxCars;}
	public void setGenSpeed(int genSpeed) {this.genSpeed = genSpeed;}
	public void setStartingRoads(ArrayList<Car.RoadLbl> roads) {this.startingRoads = roads;}
	public void setEndingRoads(ArrayList<Car.RoadLbl> roads) {this.endingRoads = roads;}
	public ArrayList<Car.RoadLbl> getStartingRoads() {return startingRoads;}
	public ArrayList<Car.RoadLbl> getEndingRoads() {return endingRoads;}
	

	//Timer to control generation of cars
	SimulatorModel simModel = this;
	class FrameTask extends TimerTask{

	Timer carGenTimer = new Timer();
	Random random = new Random();

	@Override
	public void run() {
		if (cars.size() < maxCars && random.nextInt(genSliderMax-genSpeed) == 0) {
			switch (testCase) {
			case 3:
				simModel.addCar(3);
				break;
			case 4:
				simModel.addCar(4);
				break;
			case 5:
				simModel.addCar(5);
				break;
			}
		}	
		
	}
} 
}