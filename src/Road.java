public class Road {
	
	String inout;							
	String location;
	
	//Coordinates where road comes off screen
	private final int endXCoordinate;
	private final int endYCoordinate;
	
	//Coordinates where road meets the roundabout
	private final int rbXCoordinate;
	private final int rbYCoordinate;
	
	private final int angle;
	
	public Road(String inout, String location, int endX, int endY, int rbX, int rbY, int carAngle){
		this.inout = inout;
		this.location = location;
		endXCoordinate = endX;
		endYCoordinate = endY;
		rbXCoordinate = rbX;
		rbYCoordinate = rbY;
		angle = carAngle;
	}
	
	public int getEdgeX() {return endXCoordinate;}
	public int getEdgeY() {return endYCoordinate;}
	public int getrbX() {return rbXCoordinate;}
	public int getrbY() {return rbYCoordinate;}
	public int getAngle() {return angle;}
	
}
