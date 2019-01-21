package blancsv.programmingtest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.TreeSet;

import org.junit.Test;
import org.springframework.web.server.ResponseStatusException;

public class PointServiceTest {

	PointService pointService = new PointService();

	@Test
	public void pointShouldBeAdded() {
		Point p1 = new Point(5.0);
		TreeSet<Point> beforePoints = pointService.getSetOfPoints();
		assertThat(beforePoints.size(), is(equalTo(0)));
		pointService.addNewPoint(p1);
		TreeSet<Point> newPoints = pointService.getSetOfPoints();
		assertThat(newPoints.size(), is(equalTo(1)));
		assertThat(newPoints.first(), is(equalTo(p1)));
		assertThat(newPoints.last(), is(equalTo(p1)));
	}

	@Test
	public void nullPointShouldNotBeAdded() {
		Point p1 = new Point(5.0);
		pointService.addNewPoint(p1);
		pointService.addNewPoint(null);
		TreeSet<Point> newPoints = pointService.getSetOfPoints();
		assertThat(newPoints.size(), is(equalTo(1)));
		assertThat(newPoints.first(), is(equalTo(p1)));
		assertThat(newPoints.last(), is(equalTo(p1)));
	}

	@Test
	public void pointsWithSameOffsetShouldNotBeAddedTwice() {
		Point p1 = new Point(5.0);
		Point p2 = new Point(5.0);
		pointService.addNewPoint(p1);
		assertThat(pointService.getSetOfPoints().size(), is(equalTo(1)));
		pointService.addNewPoint(p2);
		assertThat(pointService.getSetOfPoints().size(), is(equalTo(1)));
	}

	@Test
	public void pointsShouldBeAddedInOrder() {
		Point p1 = new Point(5.0);
		Point p2 = new Point(3.0);
		pointService.addNewPoint(p1);
		pointService.addNewPoint(p2);
		assertThat(pointService.getSetOfPoints().size(), is(equalTo(2)));
		/*
		 * As for the comparator that was used to create the TreeSet in PointService, we
		 * expect the point with the lowest offset to be first in the TreeSet, while the
		 * point with the highest offset will be last.
		 */
		assertThat(pointService.getSetOfPoints().last(), is(equalTo(p1)));
		assertThat(pointService.getSetOfPoints().first(), is(equalTo(p2)));
	}

	@Test
	public void shouldReturnPointsInAscendingOrder() {
		Point p1 = new Point(5.0);
		Point p2 = new Point(3.0);
		Point p3 = new Point(1.0);
		Point p4 = new Point(4.0);
		pointService.addNewPoint(p1);
		pointService.addNewPoint(p2);
		pointService.addNewPoint(p3);
		pointService.addNewPoint(p4);
		TreeSet<Point> pointsInAscendingOrder = pointService.getPointsInOrder("ASC");
		assertThat(pointsInAscendingOrder.last(), is(equalTo(p1)));
		assertThat(pointsInAscendingOrder.first(), is(equalTo(p3)));
	}

	@Test
	public void shouldReturnPointsInDescendingOrder() {
		Point p1 = new Point(5.0);
		Point p2 = new Point(3.0);
		Point p3 = new Point(1.0);
		Point p4 = new Point(4.0);
		pointService.addNewPoint(p1);
		pointService.addNewPoint(p2);
		pointService.addNewPoint(p3);
		pointService.addNewPoint(p4);
		TreeSet<Point> pointsInAscendingOrder = pointService.getPointsInOrder("DESC");
		assertThat(pointsInAscendingOrder.last(), is(equalTo(p3)));
		assertThat(pointsInAscendingOrder.first(), is(equalTo(p1)));
	}
	
	@Test(expected=ResponseStatusException.class)
	public void getPointsShouldThrowError() {
		Point p1 = new Point(5.0);
		Point p2 = new Point(3.0);
		pointService.addNewPoint(p1);
		pointService.addNewPoint(p2);
		pointService.getPointsInOrder("RAND");
	}
	
	@Test
	public void testDeleteAllPoints() {
		Point p1 = new Point(5.0);
		Point p2 = new Point(3.0);
		Point p3 = new Point(1.0);
		Point p4 = new Point(-6.0);
		Point p5 = new Point(4.5);
		pointService.addNewPoint(p1);
		pointService.addNewPoint(p2);
		pointService.addNewPoint(p3);
		pointService.addNewPoint(p4);
		pointService.addNewPoint(p5);
		TreeSet<Point> allPoints = pointService.getSetOfPoints();
		assertThat(allPoints.size(), is(equalTo(5)));
		pointService.deleteAllPoints();
		assertThat(pointService.getSetOfPoints().size(), is(equalTo(0)));
	}

	@Test
	public void shouldReturnEmptySet() {
		Point p1 = new Point(5.0);
		Point p2 = new Point(3.0);
		Point p3 = new Point(1.0);
		Point p4 = new Point(-6.0);
		Point p5 = new Point(4.5);
		pointService.addNewPoint(p1);
		pointService.addNewPoint(p2);
		pointService.addNewPoint(p3);
		pointService.addNewPoint(p4);
		pointService.addNewPoint(p5);
		TreeSet<Point> resultTreeSet = pointService.getNeighbours(2.0, 0);
		assertTrue(resultTreeSet.isEmpty());
	}
	
	@Test(expected=ResponseStatusException.class)
	public void getNeighboursShouldThrowError() {
		Point p1 = new Point(5.0);
		Point p2 = new Point(3.0);
		pointService.addNewPoint(p1);
		pointService.addNewPoint(p2);
		pointService.getNeighbours(12.0, -1);
	}
	
	@Test
	public void getNeighboursShouldReturnAllPoints() {
		Point p1 = new Point(5.0);
		Point p2 = new Point(3.0);
		Point p3 = new Point(1.0);
		Point p4 = new Point(-6.0);
		Point p5 = new Point(4.5);
		pointService.addNewPoint(p1);
		pointService.addNewPoint(p2);
		pointService.addNewPoint(p3);
		pointService.addNewPoint(p4);
		pointService.addNewPoint(p5);
		TreeSet<Point> resultTreeSet = pointService.getNeighbours(2.0, 100);
		ArrayList<Point> resultAsList = new ArrayList<Point>(resultTreeSet);
		Point[] expectedResult = {p3,p2,p5,p1,p4};
		assertArrayEquals(expectedResult, resultAsList.toArray());
	}
	
	@Test
	public void getNeighboursTest() {
		Point p1 = new Point(5.0);
		Point p2 = new Point(3.0);
		Point p3 = new Point(1.0);
		Point p4 = new Point(-6.0);
		Point p5 = new Point(4.5);
		pointService.addNewPoint(p1);
		pointService.addNewPoint(p2);
		pointService.addNewPoint(p3);
		pointService.addNewPoint(p4);
		pointService.addNewPoint(p5);
		TreeSet<Point> resultTreeSet;
		ArrayList<Point> resultAsList;		
		resultTreeSet = pointService.getNeighbours(2.0, 1);
		resultAsList = new ArrayList<Point>(resultTreeSet);
		Point[] expectedResult = {p3};
		assertArrayEquals(expectedResult, resultAsList.toArray());
		resultTreeSet = pointService.getNeighbours(4.0, 3);
		resultAsList = new ArrayList<Point>(resultTreeSet);
		Point[] expectedResult2 = {p5,p2,p1};
		assertArrayEquals(expectedResult2, resultAsList.toArray());
	}
}
