package blancsv.programmingtest;

import java.io.IOException;
import java.util.TreeSet;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping(path = "/", produces ="application/json")
public class PointRestController {
	
	private PointService testService;

	public PointRestController(PointService testService) {
		this.testService = testService;
	}
	
	@PostMapping(path = "/point",consumes = "application/json")
	public TreeSet<Point> addPointController(@Valid @RequestBody Point newPoint) throws JsonParseException, JsonMappingException, IOException {
		return this.testService.addNewPoint(newPoint);
	}
	
	
	@GetMapping("/points")
	public TreeSet<Point> getAllPointsController(@RequestParam(value = "order", required = true, defaultValue="ASC") String order){
		return this.testService.getPointsInOrder(order);
	}
	
	
	@DeleteMapping("/points")
	public void deleteAllPointsController() {
		this.testService.deleteAllPoints();
	}
	
	@GetMapping("/neighbours/{offset}")
	public TreeSet<Point> getNearestNeighboursController(@PathVariable Double offset, @RequestParam(required=false, defaultValue = "1") int k) {
		return this.testService.getNeighbours(offset, k);
	}
}
