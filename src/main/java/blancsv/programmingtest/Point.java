package blancsv.programmingtest;

import javax.validation.constraints.NotNull;

public class Point {
	@NotNull(message="You must pass an offset to create a point")
	private Double offset;

	public Point() {};
	
	public Point(Double offset) {
		this.offset = offset;
	}
	
	public Double getOffset() {
		return offset;
	}
	public void setOffset(Double offset) {
		this.offset = offset;
	}
	
	public Double getDistanceFromPoint(Double offsetToCompare) {
		return Math.abs(this.offset-offsetToCompare);
	}	

}
