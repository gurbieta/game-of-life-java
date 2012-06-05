package gameoflife;

import static org.junit.Assert.assertEquals;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

public class GameOfLifeSteps {

	private Game g;

	@Given("a matrix $matrix")
	public void aMatrix(String matrix) {
		this.g = new Game(matrix);
	}

	@When("next generation")
	public void nextGen() {
		g.nextGen();
	}

	@Then("the matrix should look like $matrix")
	public void newMatrix(String matrix) {
		assertEquals(matrix, g.render());
	}

}
