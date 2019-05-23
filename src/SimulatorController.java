import java.util.ArrayList;


public class SimulatorController{
	
	private static SimulatorController instance;
	
	private SimulatorView simView;
	private SimulatorModel simModel;
	private OutputPanel outputPanel;
	
	public static SimulatorController simCont() {
		if (instance == null) {
			instance = new SimulatorController();
		}
		return instance;
	}
	
	private SimulatorController() {
		simView = SimulatorView.simView();
		simModel = SimulatorModel.simModel();
	}
	
	public void selectTestCase(int testCase) {
		simModel.setTestCase(testCase);
	}
	
	public void startSim(){
		simModel.startTimer();
	}
	
	public void stopSim(){
		simModel.stopTimer();
	}
	
	public void clearViewCarList() {
		simView.clearCarList();
	}
	
	public SimulatorView getView() {return simView;}
	public SimulatorModel getModel() {return simModel;}
	public OutputPanel getOutputPanel() {return outputPanel;}
	
	public void setOutputPanel(OutputPanel outputPanel) {this.outputPanel = outputPanel;}
	public void setGenSliderMax(int genSliderMax) {simModel.setGenSliderMax(genSliderMax);}
	public void setMaxCars(int maxCars) {simModel.setMaxCars(maxCars);}
	public void setGenSpeed(int genSpeed) {simModel.setGenSpeed(genSpeed);}
	public void setStartingRoads(ArrayList<Car.RoadLbl> roads) {simModel.setStartingRoads(roads);}
	public void setEndingRoads(ArrayList<Car.RoadLbl> roads) {simModel.setEndingRoads(roads);}
}
