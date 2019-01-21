package blancsv.programmingtest;

import java.util.Comparator;
import java.util.TreeSet;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PointService {

	/*
	 * The comparator used to classify the points in the TreeSet structure will
	 * order them in ascending order (lowest offset is first, highest is last).
	 * Every time we add an element to the TreeSet, it will be inserted using this
	 * comparator.
	 */
	private Comparator<Point> offsetComparator = (p1, p2) -> {
		return Double.compare(p1.getOffset(), p2.getOffset());
	};

	private TreeSet<Point> setOfPoints = new TreeSet<Point>(offsetComparator);

	public TreeSet<Point> getSetOfPoints() {
		return setOfPoints;
	}

	public TreeSet<Point> addNewPoint(Point newPoint) {
		if (newPoint != null) {
			setOfPoints.add(newPoint);
		}
		return setOfPoints;
	}

	public TreeSet<Point> getPointsInOrder(String order) {
		switch (order) {
		case "DESC":
			return (TreeSet<Point>) setOfPoints.descendingSet();
		case "ASC":
			return setOfPoints;
		default:
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order must be either ASC or DESC");
		}
	}

	public void deleteAllPoints() {
		this.setOfPoints = new TreeSet<Point>(offsetComparator);
	}

	public TreeSet<Point> getNeighbours(Double givenPoint, int k) {
		if (k < 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Integer k must be a non-negative value.");
		}

		/*
		 * Create a new comparator to order the points by their distance with respect to
		 * the givenPoint in ascending order (the farthest will be the last). It allows
		 * multiple points with the same distance to be added in the closestNeighbours
		 * TreeSet.
		 */
		Comparator<Point> distanceComparator = (p1, p2) -> {
			int result = Double.compare(p1.getDistanceFromPoint(givenPoint), p2.getDistanceFromPoint(givenPoint));
			return result != 0 ? result : 1;
		};
		TreeSet<Point> closestNeighbours = new TreeSet<Point>(distanceComparator);
		if (k == 0) {
			return closestNeighbours;
		}

		if (k >= setOfPoints.size()) {
			closestNeighbours.addAll(setOfPoints);
			return closestNeighbours;
		}
		Double worstNeighbourDistance, currentPointDistance;

		/*
		 * As the elements of setOfPoints are sorted in ascending order, we are sure
		 * that if 2 points are equidistant from the givenPoint, we add the smallest
		 * first.
		 */
		for (Point currentPoint : setOfPoints) {
			if (closestNeighbours.size() < k) {
				closestNeighbours.add(currentPoint);
			}
			/*
			 * If the closestNeighbours set has size k, we check whether the currentPoint is
			 * closer to the givenPoint than the last of the closestNeighbours. If it is, we
			 * replace this worst with the currentPoint.
			 */
			else {
				worstNeighbourDistance = closestNeighbours.last().getDistanceFromPoint(givenPoint);
				currentPointDistance = currentPoint.getDistanceFromPoint(givenPoint);
				if (currentPointDistance < worstNeighbourDistance) {
					closestNeighbours.pollLast();
					closestNeighbours.add(currentPoint);
				}
				/*
				 * If the distance between the currentPoint and the givenPoint is higher than
				 * the last of the closestNeighbours, this means that all the next points will
				 * too have a larger distance. We can therefore exit and return the
				 * closestNeighbours.
				 */
				else {
					break;
				}
			}
		}
		return closestNeighbours;
	}

}
